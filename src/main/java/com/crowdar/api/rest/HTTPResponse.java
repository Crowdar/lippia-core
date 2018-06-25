package com.crowdar.api.rest;

public class HTTPResponse {

	private int statusCode;
	private String message;
	private Object response;
	private HTTPHeaders headers;


	public HTTPResponse(int statusCode, String message, Object response, HTTPHeaders headers) {
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

	public HTTPHeaders getHeader() {
		return this.headers;
	}

}
