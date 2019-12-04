package com.crowdar.api.rest;

import static com.crowdar.api.rest.APIManager.BASE_URL;
import static com.crowdar.api.rest.APIManager.setLastResponse;

import java.util.Map;

import com.crowdar.core.JsonUtils;

public class MethodsService {

    public static <T> Response get(Request req, Class<T> classModel) {
        Response resp = new RestClient().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response get(Request req, Class<T> classModel, Map<String, String> jsonParameters) {
    	Response resp = new RestClient().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }

    public static <T> Response post(Request req, Class<T> classModel) {
        Response resp = new RestClient().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response post(Request req, Class<T> classModel, Map<String, String> jsonParameters) {
        Response resp = new RestClient().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response put(Request req, Class<T> classModel) {
    	Response resp = new RestClient().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }
    
    public static <T> Response put(Request req, Class<T> classModel, Map<String, String> jsonParameters) {
    	Response resp = new RestClient().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }
    public static <T> Response patch(Request req, Class<T> classModel) {
        Response resp = new RestClient().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    
    public static <T> Response patch(Request req, Class<T> classModel, Map<String, String> jsonParameters) {
        Response resp = new RestClient().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(Request req, Class<T> classModel) {
        Response resp = new RestClient().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }
    public static <T> Response delete(Request req, Class<T> classModel, Map<String, String> jsonParameters) {
    	Response resp = new RestClient().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
    	setLastResponse(resp);
    	return resp;
    }
    

    protected static Request getRequest(String jsonFileName, Map<String, String> replacementParameters) {
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
        String endpoint = req.getEndpoint();
        if(url == null || url.isEmpty() && endpoint == null || endpoint.isEmpty() ){
            url = BASE_URL;
        }else if(!(endpoint == null) || !(endpoint.isEmpty())){
            url = BASE_URL+endpoint;
        }
        return url;
    }

}
