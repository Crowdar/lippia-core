package com.crowdar.zapi.cloud.rest;

import com.crowdar.zapi.cloud.rest.client.JwtGenerator;
import com.crowdar.zapi.model.ZapiTestCase;
import com.crowdar.zapi.model.ZapiTestCycle;
import com.crowdar.zapi.model.ZapiUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class
ZapiConnectorImpl implements ZapiConnector {
	private static final String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	private static final String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";
	private static final String getTest =  zephyrBaseUrl + "/public/rest/api/1.0/config/generalinformation";
	private static final String getCycleStatuses = zephyrBaseUrl + "/public/rest/api/1.0/execution/statuses";

	@Override
	public String createTestCycle(ZapiUser zapiUser, ZapiTestCycle zapiTestCycle)
			throws URISyntaxException, JSONException {

		StringEntity cycleJSON = this.convetToJson(zapiTestCycle);
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


	@Override
	public List<ZapiTestCase> getZapiTestByProject(ZapiUser zapiUser, String projectid) throws URISyntaxException, JSONException, JsonProcessingException {


		URI uri = new URI(getTest);
		String jwt = this.generateJWT(uri, zapiUser);




		HttpResponse response = null;
		HttpClient restClient = HttpClientBuilder.create().build();

		HttpGet getTests = new HttpGet(uri);
		getTests.addHeader("Content-Type", "application/json");
		getTests.addHeader("Authorization", jwt);
		getTests.addHeader("zapiAccessKey", zapiUser.getAccessKey());
		try {
			response = restClient.execute(getTests);
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








		return null ;
	}

	@Override
	public void getExecutionByStatuses(ZapiUser zapiUser, String projectid) throws URISyntaxException, JSONException, JsonProcessingException {
		HttpClient restClient = HttpClientBuilder.create().build();
		HttpResponse response = null;
		HttpGet getExecutionsByStatuses = new HttpGet(getCycleStatuses);
		String jwt = generateJWT(getCycleStatuses,zapiUser,HttpMethod.GET);
		getExecutionsByStatuses.addHeader("Content-Type", "application/json");
		getExecutionsByStatuses.addHeader("Authorization", jwt);
		getExecutionsByStatuses.addHeader("zapiAccessKey", zapiUser.getAccessKey());

		try {
			response = restClient.execute(getExecutionsByStatuses);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);

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
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);
		return jwt;
	}

	private String generateJWT(String uri, ZapiUser zapiUser, HttpMethod method) throws URISyntaxException {

		ZFJCloudRestClient cloudRestClient = ZFJCloudRestClient
				.restBuilder(zephyrBaseUrl, zapiUser.getAccessKey(), zapiUser.getSecretKey(), zapiUser.getUserName())
				.build();
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = cloudRestClient.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT(method.name(), new URI(uri), expirationInSec);
		System.out.println(uri.toString());
		System.out.println(jwt);
		return jwt;
	}

}
