package com.crowdar.api.rest;

import static com.crowdar.api.rest.APIManager.BASE_URL;
import static com.crowdar.api.rest.APIManager.setLastResponse;

import java.util.Map;

import com.crowdar.core.JsonUtils;

public class MethodsService {

    public static <T> Response get(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        Response resp = new RestClient().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response get(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = new RestClient().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }

    public static <T> Response post(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        Response resp = new RestClient().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response post(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        Response resp = new RestClient().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response put(String jsonName, Class<T> classModel) {
    	Request req = getRequest(jsonName, null);
    	Response resp = new RestClient().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }
    
    public static <T> Response put(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = new RestClient().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }

    public static <T> Response patch(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        Response resp = new RestClient().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response patch(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        Response resp = new RestClient().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        Response resp = new RestClient().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
    	Request req = getRequest(jsonName, jsonParameters);
    	Response resp = new RestClient().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
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
