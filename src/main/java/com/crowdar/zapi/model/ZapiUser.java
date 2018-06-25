package com.crowdar.zapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZapiUser {

	@JsonProperty("accessKey")
	private String accessKey;
	@JsonProperty("secretKey")
	private String secretKey;
	@JsonProperty("userName")
	private String userName;

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getUserName() {
		return userName;
	}

}
