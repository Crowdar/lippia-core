package com.crowdar.examples.testng;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.crowdar.zapi.collaborator.ZapiBuilder;
import com.crowdar.zapi.jenkins.reporter.ZfjReporter;
import com.crowdar.zapi.model.ZapiTestCase;
import org.json.JSONException;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class newtTestCycleTest {

	@Test
	public void testPerformReport() throws JSONException, URISyntaxException, JsonProcessingException {

		//ZapiUser zapiUser = ZapiBuilder.getZapiUser();
		//ZapiTestCycle zapiTestCycle = ZapiBuilder.getCycleObject();

		//ZapiConnector connector = new ZapiConnectorImpl();
		//connector.getExecutionByStatuses(zapiUser, "ZEP");
		//ZfjReporter reporter = new ZfjReporter("https://crowdar.atlassian.net","ZEP","ZephyrTestProject","Cycle1","1 days","");
		//ZfjReporter reporter = new ZfjReporter("https://crowdar.atlassian.net","ZEP","-1",ZfjConstants.NEW_CYCLE_KEY,ZfjConstants.CYCLE_DURATION_1_DAY,"");
		ZfjReporter reporter = ZapiBuilder.buildZapiReporterWithNewCycleForEachBuild();
		Map<ZapiTestCase,Boolean> scenarios = new HashMap<ZapiTestCase, Boolean>();


		scenarios.put(new ZapiTestCase("Test 1 ------------"),true);
		scenarios.put(new ZapiTestCase("Test 2 ------------"),true);
		scenarios.put(new ZapiTestCase("Test 3 ------------"),true);
		scenarios.put(new ZapiTestCase("Test 4 ------------"),true);
		scenarios.put(new ZapiTestCase("Test 5 ------------"),false);
		reporter.perform(1,scenarios);



	}
}
