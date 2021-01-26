package io.lippia.api.service;

import com.crowdar.api.rest.MethodsService;
import com.crowdar.api.rest.Request;
import com.crowdar.api.rest.Response;

public class SimplifyMethodService extends MethodsService{
	
	public static Response get(Request request) {
		return get(request, String.class);
	}
	
	public static Response post(Request request) {
		return post(request, String.class);
	}
	
	public static Response put(Request request) {
		return put(request, String.class);
	}
	
	public static Response patch(Request request) {
		return patch(request, String.class);
	}
	
	public static Response delete(Request request) {
		return delete(request, String.class);
	}

}
