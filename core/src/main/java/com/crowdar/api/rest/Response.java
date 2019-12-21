package com.crowdar.api.rest;

public class Response {

    private int statusCode;
    private String message;
    private Object response;
    private Headers headers;


    public Response(int statusCode, String message, Object response, Headers headers) {
        this.statusCode = statusCode;
        this.message = message;
        this.response = response;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getResponse() {
        return response;
    }

    public Headers getHeader() {
        return this.headers;
    }

}
