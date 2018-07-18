package com.crowdar.zapi.jenkins.model;

import com.crowdar.zapi.model.ZapiTestCase;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestCaseResultModel {

	@JsonProperty("passed")
	private Boolean passed;

	@JsonProperty("testCase")
	private ZapiTestCase testCase;

	@JsonProperty("TestCaseName")
	private String testCaseName;

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public ZapiTestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(ZapiTestCase testCase) {
		this.testCase = testCase;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

}
