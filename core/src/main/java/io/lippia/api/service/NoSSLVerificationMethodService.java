package io.lippia.api.service;


import com.crowdar.api.rest.MethodsService;
import com.crowdar.api.rest.Request;
import com.crowdar.api.rest.Response;
import com.crowdar.api.rest.RestClient;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class NoSSLVerificationMethodService extends MethodsService {

    public static Response get(Request request) {
        return MethodsService.get(request, getRestClient());
    }


    public static Response post(Request request) {
        return MethodsService.post(request, getRestClient());
    }

    public static Response put(Request request) {
        return MethodsService.put(request, getRestClient());
    }


    public static Response patch(Request request) {
        return MethodsService.patch(request, getRestClient());
    }


    public static Response delete(Request request) {
        return MethodsService.delete(request, getRestClient());
    }

    public static RestClient getRestClient() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Set up a TrustManager that trusts everything
        try {
            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                        }
                    }

            }, new SecureRandom());
        } catch (KeyManagementException e) {
            throw new RuntimeException(e.getMessage());
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return RestClient.getRestClient(new RestTemplate(requestFactory));
    }

}
