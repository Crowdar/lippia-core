package io.lippia.api.service;

import com.crowdar.api.rest.APIManager;
import com.crowdar.api.rest.Request;
import io.lippia.api.configuration.EndpointConfiguration;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CallerService {
	public static Object call(EndpointConfiguration config)
			throws IllegalArgumentException, SecurityException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
		String methodName = config.getHttConfiguration().getMethod();
		config.getMethodService().getClazz().getMethod(methodName.toLowerCase(), Request.class).invoke("", getRequest(config));
		return APIManager.getLastResponse().getResponse();
	}
	
	public static Request getRequest(EndpointConfiguration config) throws IOException {
		Request request = new Request();
		
		request.setBody(config.getBody());

		request.setUrl(getCompleteUrl(config));
		
		if(config.getUrlParameters() != null) {
			request.setUrlParameters(config.getUrlParameters());
		}
		
		if(config.getHeaders() != null) {
			request.setHeaders(config.getHeaders());		
		}
				
        return request;
    }
	
	public static String getCompleteUrl(EndpointConfiguration config) {
       
        String url = "";	
        
        if (StringUtils.isEmpty(config.getHttConfiguration().getUrl())) {
        	return APIManager.BASE_URL;
        }
		
		if(config.getHttConfiguration().getProtocol() != null) {
			url += config.getHttConfiguration().getProtocol() + "://";
		}
		
		url += config.getHttConfiguration().getUrl();
		
		if(config.getHttConfiguration().getPort() != null) {
			url += ":" + config.getHttConfiguration().getPort();
		}
		
		if(config.getHttConfiguration().getEndpoint() != null) {
			url += config.getHttConfiguration().getEndpoint();
		}
		
         return url;
    }
}
