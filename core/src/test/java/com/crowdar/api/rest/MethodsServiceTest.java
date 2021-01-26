package com.crowdar.api.rest;

import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.crowdar.core.PropertyManager;

@PrepareForTest({RestClient.class, PropertyManager.class})
@PowerMockIgnore({"javax.net.ssl.*","org.apache.log4j.*","org.slf4j.*","org.apache.xerces.*","org.w3c.*", "javax.xml.*", "org.xml.*", "org.apache.*", "org.w3c.dom.*", "org.apache.cxf.*"})
public class MethodsServiceTest extends PowerMockTestCase {

	@Mock
    private RestClient restClient;
	private Response response;
	private Request request;
	
	@BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        request = new Request();
		request.setBody("");

		PowerMockito.mockStatic(RestClient.class);
        when(RestClient.class, "getRestClient").thenReturn(restClient);
		
        PowerMockito.mockStatic(PropertyManager.class);
		when(PropertyManager.class, "getProperty", Mockito.anyString())
			.thenReturn("http://test.test");
        
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		response = new Response(200, "OK", new Object(), new Headers(map));
		
		Mockito.when(
				restClient.get(
						"http://test.test", 
						Response.class, 
						request.getBody().toString(), 
						request.getUrlParameters(), 
						request.getHeaders()
				)
		).thenReturn(response);
		
		Mockito.when(
				restClient.post(
						"http://test.test", 
						Response.class, 
						request.getBody().toString(), 
						request.getUrlParameters(), 
						request.getHeaders()
				)
		).thenReturn(response);
		
		Mockito.when(
				restClient.patch(
						"http://test.test", 
						Response.class, 
						request.getBody().toString(), 
						request.getUrlParameters(), 
						request.getHeaders()
				)
		).thenReturn(response);
		
		Mockito.when(
				restClient.delete(
						"http://test.test", 
						Response.class, 
						request.getBody().toString(), 
						request.getUrlParameters(), 
						request.getHeaders()
				)
		).thenReturn(response);
		
		Mockito.when(
				restClient.put(
						"http://test.test", 
						Response.class, 
						request.getBody().toString(), 
						request.getUrlParameters(), 
						request.getHeaders()
				)
		).thenReturn(response);
		
	}
	
	@Test
	public void whenCallSomeApiWithRestClientAndGetMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.get(request, Response.class, restClient);
		Mockito.verify(restClient, Mockito.times(1)).get(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}

	@Test
	public void whenCallSomeApiWithGetMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.get(request, Response.class);
		Mockito.verify(restClient, Mockito.times(1)).get(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}
    
    
    @Test
	public void whenCallSomeApiWithPostMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.post(request, Response.class);
		Mockito.verify(restClient, Mockito.times(1)).post(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}

    @Test
    public void whenCallSomeApiWithRestClientAndPostMethod() throws Exception{
    	Response responseFromStaticMethod = MethodsService.post(request, Response.class, restClient);
    	Mockito.verify(restClient, Mockito.times(1)).post(
    			"http://test.test", 
    			Response.class, 
    			request.getBody().toString(), 
    			request.getUrlParameters(), 
    			request.getHeaders());
    	Assert.assertTrue(response.equals(responseFromStaticMethod));
    	Assert.assertTrue(APIManager.getLastResponse().equals(response));
    }
    
    @Test
	public void whenCallSomeApiWithPatchMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.patch(request, Response.class);
		Mockito.verify(restClient, Mockito.times(1)).patch(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}

    @Test
    public void whenCallSomeApiWithRestClientAndPatchMethod() throws Exception{
    	Response responseFromStaticMethod = MethodsService.patch(request, Response.class, restClient);
    	Mockito.verify(restClient, Mockito.times(1)).patch(
    			"http://test.test", 
    			Response.class, 
    			request.getBody().toString(), 
    			request.getUrlParameters(), 
    			request.getHeaders());
    	Assert.assertTrue(response.equals(responseFromStaticMethod));
    	Assert.assertTrue(APIManager.getLastResponse().equals(response));
    }
    
    @Test
	public void whenCallSomeApiWithDeleteMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.delete(request, Response.class);
		Mockito.verify(restClient, Mockito.times(1)).delete(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}

    @Test
    public void whenCallSomeApiWithRestClientAndDeleteMethod() throws Exception{
    	Response responseFromStaticMethod = MethodsService.delete(request, Response.class, restClient);
    	Mockito.verify(restClient, Mockito.times(1)).delete(
    			"http://test.test", 
    			Response.class, 
    			request.getBody().toString(), 
    			request.getUrlParameters(), 
    			request.getHeaders());
    	Assert.assertTrue(response.equals(responseFromStaticMethod));
    	Assert.assertTrue(APIManager.getLastResponse().equals(response));
    }
    
    @Test
	public void whenCallSomeApiWithPutMethod() throws Exception{
		Response responseFromStaticMethod = MethodsService.put(request, Response.class);
		Mockito.verify(restClient, Mockito.times(1)).put(
				"http://test.test", 
				Response.class, 
				request.getBody().toString(), 
				request.getUrlParameters(), 
				request.getHeaders());
		Assert.assertTrue(response.equals(responseFromStaticMethod));
		Assert.assertTrue(APIManager.getLastResponse().equals(response));
	}

    @Test
    public void whenCallSomeApiWithRestClientAndPutMethod() throws Exception{
    	Response responseFromStaticMethod = MethodsService.put(request, Response.class, restClient);
    	Mockito.verify(restClient, Mockito.times(1)).put(
    			"http://test.test", 
    			Response.class, 
    			request.getBody().toString(), 
    			request.getUrlParameters(), 
    			request.getHeaders());
    	Assert.assertTrue(response.equals(responseFromStaticMethod));
    	Assert.assertTrue(APIManager.getLastResponse().equals(response));
    }

}
