package com.crowdar.zapi.cloud.rest;

import java.net.URISyntaxException;
import java.util.List;

import com.crowdar.zapi.model.ZapiTestCase;
import com.crowdar.zapi.model.ZapiTestCycle;
import org.json.JSONException;

import com.crowdar.zapi.model.ZapiUser;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ZapiConnector {

	public String createTestCycle(ZapiUser zapiUser, ZapiTestCycle zapiTestCycle)
			throws URISyntaxException, JSONException, JsonProcessingException;


	public List<ZapiTestCase> getZapiTestByProject(ZapiUser zapiUser, String projectid)
			throws URISyntaxException, JSONException, JsonProcessingException;


	public void getExecutionByStatuses(ZapiUser zapiUser,String projectid)
			throws URISyntaxException, JSONException, JsonProcessingException;
}
