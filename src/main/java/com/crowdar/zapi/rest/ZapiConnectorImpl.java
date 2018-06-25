package com.crowdar.zapi.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.crowdar.zapi.model.TestCycle;
import com.crowdar.zapi.model.ZapiUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

public class ZapiConnectorImpl implements ZapiConnector {
	private static final String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	private static final String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";

	@Override
	public String createTestCycle(ZapiUser zapiUser, TestCycle testCycle)
			throws URISyntaxException, JSONException, JsonProcessingException {

		StringEntity cycleJSON = this.convetToJson(testCycle);
		URI uri = new URI(createCycleUri);
		String jwt = this.generateJWT(uri, zapiUser);

		HttpResponse response = null;
		HttpClient restClient = HttpClientBuilder.create().build();

		HttpPost createCycleReq = new HttpPost(uri);
		createCycleReq.addHeader("Content-Type", "application/json");
		createCycleReq.addHeader("Authorization", jwt);
		createCycleReq.addHeader("zapiAccessKey", zapiUser.getAccessKey());
		createCycleReq.setEntity(cycleJSON);
		System.out.println(cycleJSON.toString());

		try {
			response = restClient.execute(createCycleReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		String cycleId = "-1";
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject cycleObj = new JSONObject(string);
				cycleId = cycleObj.getString("id");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return cycleId;
	}

	private StringEntity convetToJson(Object entity) {

		ObjectMapper mapper = new ObjectMapper();
		String cycleObjectMapper;
		StringEntity cycleJSON = null;
		try {
			cycleObjectMapper = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
			cycleJSON = new StringEntity(cycleObjectMapper);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return cycleJSON;

	}

	private String generateJWT(URI uri, ZapiUser zapiUser) {
		ZFJCloudRestClient cloudRestClient = ZFJCloudRestClient
				.restBuilder(zephyrBaseUrl, zapiUser.getAccessKey(), zapiUser.getSecretKey(), zapiUser.getUserName())
				.build();
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = cloudRestClient.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);
		return jwt;
	}

}
