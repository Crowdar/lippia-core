package com.crowdar.api.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {

    private HttpHeaders headers;
    private RestTemplate restTemplate;

    public RestClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    public static RestClient getRestclientInstance() {
        return new RestClient();
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

    public Response get(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.GET);
    }

    public Response post(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.POST);
    }

    public Response put(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.PUT);
    }

    public Response patch(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.PATCH);
    }

    public Response delete(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers) {
        return createHTTPMethod(url, type, body, urlParameters, headers, HttpMethod.DELETE);
    }

    private Response createHTTPMethod(String url, Class<?> type, String body, HashMap<String, String> urlParameters, String headers, HttpMethod httpMethod) {
        URI uri = this.getURIWithURLQueryParameters(url, urlParameters);
        HttpEntity<String> request = this.createRequest(body, this.setHeaders(headers));
        try {
            ResponseEntity<Object> response = (ResponseEntity<Object>) restTemplate.exchange(uri, httpMethod, request, type);
            return this.createResponse(response.getStatusCode().value(), "OK", response.getBody(), createHeaders(response.getHeaders()));

        } catch (HttpClientErrorException e1) {
            System.out.println(e1.getResponseBodyAsString());
            return this.createResponse(e1.getStatusCode().value(), e1.getResponseBodyAsString(), e1.getLocalizedMessage(), createHeaders(e1.getResponseHeaders()));
        } catch (HttpServerErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            return this.createResponse(e.getStatusCode().value(), e.getResponseBodyAsString(), e.getLocalizedMessage(), createHeaders(e.getResponseHeaders()));
        }
    }

    private Headers createHeaders(HttpHeaders headers) {
        return new Headers(this.getHeaders(headers));
    }

    private HttpEntity<String> createRequest(String body, HttpHeaders headers) {
        if (body.isEmpty())
            return new HttpEntity<>(headers);
        return new HttpEntity<>(body, headers);
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

}
