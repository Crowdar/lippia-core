package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {

    private HttpHeaders headers;
    private RestTemplate restTemplate;

    public RestClient() {
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public HttpHeaders setHeaders(String jsonHeaders) {
        this.headers = new HttpHeaders();
        try {
            HashMap<String, String> result =
                    new ObjectMapper().readValue(jsonHeaders, HashMap.class);
            this.headers.setAll(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headers;
    }

    public HTTPResponse get(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.GET);
    }

    public HTTPResponse post(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.POST);
    }

    public HTTPResponse patch(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.PATCH);
    }

    public HTTPResponse delete(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.DELETE);
    }

    private HTTPResponse createHTTPMethod(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers, HttpMethod httpMethod) {
        URI uri = this.getURIWithURLQueryParameters(url, urlParameters);
        HttpEntity<String> request = this.createRequest(body, this.setHeaders(headers));
        ResponseEntity<Object> response = null;
        try {
            response = (ResponseEntity<Object>) this.restTemplate.exchange(uri, httpMethod,
                    request, type);
        } catch (HttpClientErrorException var11) {
            response = ((ResponseEntity.BodyBuilder) ResponseEntity.status(var11.getRawStatusCode()).headers(var11.getResponseHeaders())).body(JsonUtils.deserialize("[" + var11.getResponseBodyAsString() + "]", type).iterator().next());
        }
        HTTPHeaders responseHeaders = new HTTPHeaders(this.getHeaders(response.getHeaders()));
        return this.createResponse(response.getStatusCode().value(), "OK", response.getBody(), responseHeaders);
    }

    private HttpEntity<String> createRequest(String body, HttpHeaders headers) {
        if (body.isEmpty())
            return new HttpEntity<>(headers);
        return new HttpEntity<>(body, headers);
    }

    private HTTPResponse createResponse(int statusCode, String message, Object response, HTTPHeaders headers) {
        return new HTTPResponse(statusCode, message, response, headers);
    }

    private Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private URI getURIWithURLQueryParameters(String url, HashMap<String, String> urlParameters) {
        if (urlParameters.isEmpty())
            return this.getUriFromUrl(url);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().encode().toUri();
    }

    private URI getUriFromUrl(String url) {
        URI uri;
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
