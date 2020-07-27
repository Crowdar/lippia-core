package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {

    private HttpHeaders headers;
    private static RestTemplate restTemplate;
    private static RestClient restClient;

    public static RestClient getRestClient() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    public RestClient() {
        setRestTemplate(new RestTemplate(new HttpComponentsClientHttpRequestFactory()));
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));

        messageConverters.add(mappingJackson2HttpMessageConverter);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    private static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    private static void setRestTemplate(RestTemplate newRestTemplate) {
        restTemplate = newRestTemplate;
    }

    private void setRequestHeaders(Map<String, String> headers) {
        this.headers = new HttpHeaders();
        this.headers.setAll(headers);
    }

    private HttpHeaders getRequestHeaders() {
        return headers;
    }

    public Response get(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.GET);
    }

    public Response post(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.POST);
    }

    public Response put(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.PUT);
    }

    public Response patch(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.PATCH);
    }

    public Response delete(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.DELETE);
    }

    private Response createHTTPMethod(String url, Class<?> type, String body, Map<String, String> urlParameters, Map<String, String> headers, HttpMethod httpMethod) {
        URI uri = this.getURIWithURLQueryParameters(url, urlParameters);
        setRequestHeaders(headers);
        HttpEntity<String> request = this.createRequest(body, getRequestHeaders());
        try {
            ResponseEntity response = getRestTemplate().exchange(uri, httpMethod, request, type);
            Logger.getRootLogger().info(">>>Response: " + response.toString());
            return this.createResponse(response.getStatusCode().value(), "OK", response.getBody(), createResponseHeaders(response.getHeaders()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            Logger.getRootLogger().info(">>>Error Response: " + e.toString());
            Object responseBody = JsonUtils.deserialize(e.getResponseBodyAsString(), type);
            if(responseBody == null){
                Assert.fail("Cannot cast error response in: " + type.getName() + ". Actual response: " + e.getStatusCode().value() + ", " + e.getResponseBodyAsString());
            }
            return this.createResponse(e.getStatusCode().value(), e.getLocalizedMessage(), responseBody, createResponseHeaders(e.getResponseHeaders()));
        }
    }

    private Headers createResponseHeaders(HttpHeaders headers) {
        return new Headers(this.getHeaders(headers));
    }

    private HttpEntity<String> createRequest(String body, HttpHeaders headers) {
        if (body.isEmpty()) {
            return new HttpEntity<>(headers);
        } else {
            return new HttpEntity<>(body, headers);
        }
    }

    private Response createResponse(int statusCode, String message, Object response, Headers headers) {
        return new Response(statusCode, message, response, headers);
    }

    private Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private URI getURIWithURLQueryParameters(String url, Map<String, String> urlParameters) {
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
}
