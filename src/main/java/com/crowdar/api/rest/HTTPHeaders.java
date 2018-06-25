package com.crowdar.api.rest;

import java.util.List;
import java.util.Map;

public class HTTPHeaders {

	private Map<String, List<String>> headers;
	
	public HTTPHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public List<String> getHeaderValues(Object header) {
		return this.headers.get(header);
	}

	public Map<String, List<String>> getAll() {
		return this.headers;
	}

}
