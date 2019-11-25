package com.crowdar.api.rest;

import static com.crowdar.api.rest.APIManager.BASE_URL;
import static com.crowdar.api.rest.APIManager.setLastResponse;

import java.util.Map;

import com.crowdar.core.JsonUtils;

public class MethodsService {

    public static <T> Response get(String jsonName, Class<T> classModel) {
        return get(jsonName, classModel, null);
    }

    public static <T> Response get(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = RestClient.getRestclientInstance().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }

    public static <T> Response post(String jsonName, Class<T> classModel) {
        return post(jsonName, classModel, null);
    }
    
    public static <T> Response post(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        Response resp = RestClient.getRestclientInstance().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response put(String jsonName, Class<T> classModel) {
        return put(jsonName, classModel, null);
    }
    
    public static <T> Response put(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = RestClient.getRestclientInstance().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }

    public static <T> Response patch(String jsonName, Class<T> classModel) {
        return patch(jsonName, classModel, null);
    }
    
    public static <T> Response patch(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        Response resp = RestClient.getRestclientInstance().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel) {
        return delete(jsonName, classModel, null);
    }

    public static <T> Response delete(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = RestClient.getRestclientInstance().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }
    

    private static Request getRequest(String jsonFileName, Map<String, String> replacementParameters) {
        String jsonRequest = JsonUtils.getJSONFromFile(jsonFileName);
        
        if(replacementParameters != null) {
	        for (String key : replacementParameters.keySet()) {
	        	jsonRequest=jsonRequest.replace("{{"+key+"}}", replacementParameters.get(key));
			} 
        }
        
        return JsonUtils.deserialize(jsonRequest, Request.class);
    }
    
    private static String getURL(Request req){
        String url = req.getUrl();
        if(url == null || url.isEmpty()){
            url = BASE_URL;
        }
        return url;
    }

}
