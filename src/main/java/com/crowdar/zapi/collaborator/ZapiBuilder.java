package com.crowdar.zapi.collaborator;

import com.crowdar.core.PropertyManager;
import com.crowdar.zapi.jenkins.reporter.ZfjConstants;
import com.crowdar.zapi.jenkins.reporter.ZfjReporter;
import com.crowdar.zapi.model.ZapiTestCycle;
import com.crowdar.zapi.model.ZapiUser;

public class ZapiBuilder {

	public static ZapiUser getZapiUser() {
		ZapiUser zapiUser = new ZapiUser();
		zapiUser.setAccessKey(PropertyManager.getProperty("zapi.user.accessKey"));
		zapiUser.setSecretKey(PropertyManager.getProperty("zapi.user.secretKey"));
		zapiUser.setUserName(PropertyManager.getProperty("zapi.user.userName"));
        zapiUser.setPassword(PropertyManager.getProperty("zapi.user.password"));
		return zapiUser;
	}

	/*public static ZapiTestCycle getCycleObject() {
		ZapiTestCycle zapiTestCycle = new ZapiTestCycle();
		zapiTestCycle.setProjectId(Long.parseLong(PropertyManager.getProperty("zapi.testCycle.projectId")));
		zapiTestCycle.setVersionId(Long.parseLong(PropertyManager.getProperty("zapi.testCycle.versionId")));
		zapiTestCycle.setName(PropertyManager.getProperty("zapi.testCycle.name"));
		zapiTestCycle.setDescription(PropertyManager.getProperty("zapi.testCycle.description"));
		zapiTestCycle.setStartDate(System.currentTimeMillis());
		return zapiTestCycle;
	}*/

	public static ZfjReporter buildZapiReporterWithNewCycleForEachBuild(){

	    ZapiUser user = getZapiUser();
	    String zapiServerName = PropertyManager.getProperty("zapi.server.address");
        String zapiCloudServerName = PropertyManager.getProperty("zapi.cloud.server.address");
	    String zapiProjectKey = PropertyManager.getProperty("zapi.project.key");
        String zapiVersionKey = PropertyManager.getProperty("zapi.version.key");
        String zapiDurationCycle = PropertyManager.getProperty("zapi.cycle.duration");
        String zapiCyclePrefix = PropertyManager.getProperty("zapi.cycle.prefix");

		return  new ZfjReporter(zapiServerName,zapiCloudServerName,
                                zapiProjectKey,zapiVersionKey,
                                ZfjConstants.NEW_CYCLE_KEY,
                                zapiDurationCycle,zapiCyclePrefix,user);

	}

}
