package com.crowdar.zapi.collaborator;

import com.crowdar.core.PropertyManager;
import com.crowdar.zapi.model.TestCycle;
import com.crowdar.zapi.model.ZapiUser;

public class ZapiRequestBuilder {

	public static ZapiUser getZapiUser() {
		ZapiUser zapiUser = new ZapiUser();
		zapiUser.setAccessKey(PropertyManager.getProperty("zapi.user.accessKey"));
		zapiUser.setSecretKey(PropertyManager.getProperty("zapi.user.secretKey"));
		zapiUser.setUserName(PropertyManager.getProperty("zapi.user.userName"));
		return zapiUser;
	}

	public static TestCycle getCycleObject() {
		TestCycle testCycle = new TestCycle();
		testCycle.setProjectId(Long.parseLong(PropertyManager.getProperty("zapi.testCycle.projectId")));
		testCycle.setVersionId(Long.parseLong(PropertyManager.getProperty("zapi.testCycle.versionId")));
		testCycle.setName(PropertyManager.getProperty("zapi.testCycle.name"));
		testCycle.setDescription(PropertyManager.getProperty("zapi.testCycle.description"));
		testCycle.setStartDate(System.currentTimeMillis());
		return testCycle;
	}

}
