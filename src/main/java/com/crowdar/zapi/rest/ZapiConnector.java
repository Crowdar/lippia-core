package com.crowdar.zapi.rest;

import java.net.URISyntaxException;
import org.json.JSONException;

import com.crowdar.zapi.model.TestCycle;
import com.crowdar.zapi.model.ZapiUser;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ZapiConnector {

	public String createTestCycle(ZapiUser zapiUser, TestCycle testCycle)
			throws URISyntaxException, JSONException, JsonProcessingException;

}
