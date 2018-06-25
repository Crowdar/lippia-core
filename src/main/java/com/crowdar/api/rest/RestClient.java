package com.crowdar.api.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestClient {

    private Map<String, String> urlParameters;
    private HttpHeaders headers;
    private RestTemplate restTemplate;

    public RestClient() {
        this.headers = new HttpHeaders();
        this.urlParameters = new HashMap<String, String>();
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public void setHeader(String field, String value) {
        this.headers.set(field, value);
    }

    public void addURLQueryParameter(String field, String value) {
        this.urlParameters.put(field, value);
    }

    public HTTPResponse get(String url, Class<?> type) {
        URI uri = this.getURIWithURLQueryParameters(url);
        HttpEntity<String> entity = new HttpEntity<String>(this.headers);
        initWithKapschProxy();
        @SuppressWarnings("unchecked")
        ResponseEntity<Object> response = (ResponseEntity<Object>) this.restTemplate.exchange(uri, HttpMethod.GET,
                entity, type);
        HTTPHeaders responseHeaders = new HTTPHeaders(this.getHeaders(response.getHeaders()));
        return this.createResponse(response.getStatusCode().value(), "OK", response.getBody(), responseHeaders);
    }

    private HTTPResponse createResponse(int statusCode, String message, Object response, HTTPHeaders headers) {
        return new HTTPResponse(statusCode, message, response, headers);
    }

    private Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private URI getURIWithURLQueryParameters(String url) {
        if (this.urlParameters.isEmpty())
            return this.getUriFromUrl(url);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : this.urlParameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().encode().toUri();
    }

    private URI getUriFromUrl(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
            return uri;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public void initWithKapschProxy() {
        this.restTemplate = new RestTemplate();

        final int proxyPortNum = 8080;
        final String proxyHost = "148.198.148.50";
        final String proxyUser = "KAPSCH\\spoleti";
        final String proxyPassword = "Kapsch2018";
        
        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxyHost, proxyPortNum), new UsernamePasswordCredentials(proxyUser, proxyPassword));

        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyHost, proxyPortNum));
        clientBuilder.setDefaultCredentialsProvider(credsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        final CloseableHttpClient client = clientBuilder.build();

        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);

        restTemplate.setRequestFactory(factory);
    }

}
