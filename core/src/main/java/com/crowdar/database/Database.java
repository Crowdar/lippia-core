package com.crowdar.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import com.crowdar.core.PropertyManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    private static final Semaphore semaphore = new Semaphore(0);
    private static boolean databaseConnected = false;

    public static void getConnection() throws IOException {
        if (!databaseConnected) {
            FileInputStream serviceAccount = new FileInputStream(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "firebase.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(PropertyManager.getProperty("db.url"))
                    .build();


            FirebaseApp.initializeApp(options);

            databaseConnected = true;
        } else {
            FirebaseDatabase.getInstance().goOnline();
        }
    }

    private static Query loadJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Query>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String executeQueryEqualTo(String json) {
        Query query = loadJson(json);
        final String[] data = {null};
        try {
            getConnection();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(query.getReference());
            ref.orderByChild(query.getKeyToOrderBy()).equalTo(query.getValueEqualTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        data[0] = children.child(query.getKeyChildFinal()).getValue().toString();
                    }
                    semaphore.release();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("error " + databaseError);
                    data[0] = databaseError.toString();
                    semaphore.release();
                }
            });

            semaphore.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FirebaseDatabase.getInstance().goOffline();
        }

        return data[0];
    }

    public static boolean executeUpdateEqualTo(String json) {
        final boolean[] updateCorrect = {false};
        Query query = loadJson(json);
        try {
            getConnection();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(query.getReference());
            ref.orderByChild(query.getKeyToOrderBy()).equalTo(query.getValueEqualTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        Object valueToSet;
                        if (query.getValueToSet().equals("true") || query.getValueToSet().equals("false")) {
                            valueToSet = Boolean.valueOf(query.getValueToSet());
                        } else {
                            valueToSet = query.getValueToSet();
                        }
                        ApiFuture<Void> apiFuture = children.getRef().child(query.getKeyChildFinal()).setValueAsync(valueToSet);
                        ApiFutures.addCallback(apiFuture, new ApiFutureCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                System.out.println("Operation completed with result: " + result);
                                updateCorrect[0] = true;
                                semaphore.release();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                System.out.println("Operation failed with error: " + t);
                                updateCorrect[0] = false;
                                semaphore.release();
                            }
                        });

                    }
                    updateCorrect[0] = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("error " + databaseError);
                    updateCorrect[0] = false;
                }
            });

            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            FirebaseDatabase.getInstance().goOffline();
            return updateCorrect[0];
        }
    }
}
