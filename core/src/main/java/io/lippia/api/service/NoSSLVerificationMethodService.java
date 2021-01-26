package io.lippia.api.service;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.crowdar.api.rest.MethodsService;
import com.crowdar.api.rest.Request;
import com.crowdar.api.rest.Response;
import com.crowdar.api.rest.RestClient;

public class NoSSLVerificationMethodService extends MethodsService{
	
	public static Response get(Request request) {
		return MethodsService.get(request, String.class, getNoSslVerificationRestClient());
	}
	
	public static Response post(Request request) {
		return post(request, String.class, getNoSslVerificationRestClient() );
	}
	
	public static Response put(Request request) {
		return put(request, String.class, getNoSslVerificationRestClient());
	}
	
	public static Response patch(Request request) {
		return patch(request, String.class, getNoSslVerificationRestClient());
	}
	
	public static Response delete(Request request) {
		return delete(request, String.class, getNoSslVerificationRestClient());
	}

	
   private static RestClient getNoSslVerificationRestClient() {

	   TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
		
		@Override
	       public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
	           return true;
	       }
	   };
   
	   SSLContext sslContext;
	   try {
		   sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	   } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e);
	   }
	   SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
	   CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	   HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	   requestFactory.setHttpClient(httpClient);
	 
	   return RestClient.getRestClient(new RestTemplate(requestFactory));
    }
   
}
