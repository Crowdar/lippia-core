package io.lippia.api.service;

import com.crowdar.api.rest.MethodsService;
import com.crowdar.api.rest.Request;
import com.crowdar.api.rest.Response;

public class SimplifyMethodService {
	
	public static Response get(Request request) {
		return MethodsService.get(request, String.class);
	}
	
	public static Response post(Request request) {
		return MethodsService.post(request, String.class);
	}
	
	public static Response put(Request request) {
		return MethodsService.put(request, String.class);
	}
	
	public static Response patch(Request request) {
		return MethodsService.patch(request, String.class);
	}
	
	public static Response delete(Request request) {
		return MethodsService.delete(request, String.class);
	}

}
