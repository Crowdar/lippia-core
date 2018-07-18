package com.crowdar.zapi.jenkins.reporter;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowdar.zapi.jenkins.utils.rest.*;
import com.crowdar.zapi.model.ZapiIssueType;
import com.crowdar.zapi.model.ZapiProject;
import com.crowdar.zapi.model.ZapiTestCase;

import com.crowdar.zapi.model.ZapiUser;
import org.apache.commons.lang.StringUtils;


import com.crowdar.zapi.jenkins.model.TestCaseResultModel;
import com.crowdar.zapi.jenkins.model.ZephyrConfigModel;
import org.apache.log4j.Logger;

import static com.crowdar.zapi.jenkins.reporter.ZfjConstants.*;

public class ZfjReporter {

	public static Logger logger = Logger.getLogger(ZfjReporter.class);

	private String serverAddress;
	private String serverCloudAddress;
	private String projectKey;
	private String versionKey;
	private String cycleName;
	private String cyclePrefix;
	private String cycleDuration;
	private ZapiUser user;

	
    private static final String PluginName = new String("[ZapiTestResultReporter]");
    private final String pInfo = String.format("%s [INFO]", PluginName);


	public ZfjReporter(String serverAddress,
                       String serverCloudAddress,
                       String projectKey,
                       String versionKey,
                       String cycleName,
                       String cycleDuration,
                       String cyclePrefix,ZapiUser user) {


    	this.serverAddress = serverAddress;
    	this.serverCloudAddress = serverCloudAddress;
        this.projectKey = projectKey;
        this.versionKey = versionKey;
        this.cycleName = cycleName;
        this.cyclePrefix = cyclePrefix;
        this.cycleDuration = cycleDuration;
        this.user = user;
    }



    public boolean perform(int buildNumber, Map<String, Boolean> zephyrTestCaseMap) {

  
		if (!validateBuildConfig()) {
			logger.error("Cannot Proceed. Please verify the job configuration");
			return false;
		}

		System.out.println("----------------------------MapTestResults : ---------------------------------");
		System.out.println(zephyrTestCaseMap.toString());
		System.out.println("----------------------------MapTestResults  ---------------------------------");


		ZephyrConfigModel zephyrConfig = initializeZephyrData();

		System.out.println("----------------------------ZapiConfig Initialized: ---------------------------------");
		System.out.println(zephyrTestCaseMap.toString());
		System.out.println("----------------------------ZapiConfig Initialized ---------------------------------");
		zephyrConfig.setBuilNumber(buildNumber);

        	boolean prepareZephyrTests = prepareZephyrTests(zephyrConfig,zephyrTestCaseMap);
        	
        	if(!prepareZephyrTests) {
    			logger.error("Error parsing surefire reports.");
    			logger.error("Please ensure \"Publish JUnit test result report is added\" as a post build action");
    			return false;
        	}
        	
			TestCaseUtil.processTestCaseDetails(zephyrConfig);

            zephyrConfig.getRestClient().destroy();
        logger.info(pInfo+" Done.");
        return true;
    }



	private boolean prepareZephyrTests(	ZephyrConfigModel zephyrConfig, Map<String, Boolean> zephyrTestCaseMap) {

		boolean status = true;
		//Map<String, Boolean> zephyrTestCaseMap = new HashMap<String, Boolean>();

		//zephyrTestCaseMap.put("Test 1", true);
        //zephyrTestCaseMap.put("Test 2", true);
        //zephyrTestCaseMap.put("Test 3", true);
        //zephyrTestCaseMap.put("Test 4", false);
        //zephyrTestCaseMap.put("Test 5", true);
        //zephyrTestCaseMap.put("Test 6", false);

		
		logger.info("Total Test Cases : " + zephyrTestCaseMap.size());

		//here is where I am creating the zephyr testcases result
		List<TestCaseResultModel> testcases = new ArrayList<TestCaseResultModel>();

		
		Set<String> keySet = zephyrTestCaseMap.keySet();
		
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String testCaseName = iterator.next();
			Boolean isPassed = zephyrTestCaseMap.get(testCaseName);

			ZapiIssueType isssueType = new ZapiIssueType();
			isssueType.setId(zephyrConfig.getTestIssueTypeId());
			
			ZapiProject project = new ZapiProject();
			project.setId(zephyrConfig.getZephyrProjectId());

			ZapiTestCase fields = new ZapiTestCase();
			fields.setProject(project);
			fields.setSummary(testCaseName);
			fields.setDescription("Creating the Test via Crowdar Automation");
			fields.setIssueType(isssueType);
			TestCaseResultModel caseWithStatus = new TestCaseResultModel();
			caseWithStatus.setPassed(isPassed);
			caseWithStatus.setTestCase(fields);
			caseWithStatus.setTestCaseName(testCaseName);
			testcases.add(caseWithStatus);
		}
		
		zephyrConfig.setTestcases(testcases);
		
		return status;
	}

	private boolean validateBuildConfig() {
		boolean valid = true;
		if (StringUtils.isBlank(serverAddress)
				|| StringUtils.isBlank(projectKey)
				|| StringUtils.isBlank(versionKey)
				|| StringUtils.isBlank(cycleName)
				|| ADD_ZEPHYR_GLOBAL_CONFIG.equals(serverAddress.trim())
				|| ADD_ZEPHYR_GLOBAL_CONFIG.equals(projectKey.trim())
				|| ADD_ZEPHYR_GLOBAL_CONFIG.equals(versionKey.trim())
				|| ADD_ZEPHYR_GLOBAL_CONFIG.equals(cycleName.trim()))	{

			logger.error("Cannot Proceed validation build config is not correct");
			valid = false;
		}
		return valid;
	}

	private void determineTestIssueTypeId(ZephyrConfigModel zephyrConfig) {
		long testIssueTypeId = ServerInfo.findTestIssueTypeId(zephyrConfig.getRestClient());
		zephyrConfig.setTestIssueTypeId(testIssueTypeId);
	}

	private void determineCyclePrefix(ZephyrConfigModel zephyrConfig) {
		if (StringUtils.isNotBlank(cyclePrefix)) {
			zephyrConfig.setCyclePrefix(cyclePrefix+ "_");
		} else {
			zephyrConfig.setCyclePrefix("Automation_");
		}
	}

	private ZephyrConfigModel initializeZephyrData() {
		ZephyrConfigModel zephyrConfig = new ZephyrConfigModel();
		
		String hostName = StringUtils.removeEnd(serverAddress, "/");
		prepareRestClient(zephyrConfig, hostName);
	
		zephyrConfig.setCycleDuration(cycleDuration);
		determineProjectID(zephyrConfig);
		determineVersionID(zephyrConfig);
		determineCycleID(zephyrConfig);
		determineCyclePrefix(zephyrConfig);
        determineTestIssueTypeId(zephyrConfig);

		
		return zephyrConfig;
	}

	private void prepareRestClient(ZephyrConfigModel zephyrConfig, String url) {
		RestClient restClient = null;
		//restClient = new RestClient(serverAddress, "dario.vallejos","Guille141210",
		//"https://prod-api.zephyr4jiracloud.com/connect","amlyYTpjNDljMTFmOC1iZTRlLTRkYmQtODUyZS1hNzQyMGJjNWZhMDIgZGFyaW8udmFsbGVqb3MgZGFyaW8udmFsbGVqb3M","Mrh8XzOkW9JLuhrFdIcJ4m2NDSXX9sgYUMNG9OqhMM8");

        restClient = new RestClient(serverAddress,
                                    user.getUserName(),
                                    user.getPassword(),
                                    serverCloudAddress,user.getAccessKey(),user.getSecretKey());

        zephyrConfig.setZfjClud(true);
		zephyrConfig.setRestClient(restClient);
	}

	private void determineCycleID(ZephyrConfigModel zephyrConfig) {
        String cycleId = "";
		if(zephyrConfig.isZfjClud()) {
            zephyrConfig.setCycleName(this.cycleName);
			if (this.cycleName.equalsIgnoreCase(NEW_CYCLE_KEY)) {
				zephyrConfig.setCycleId(NEW_CYCLE_KEY_IDENTIFIER.toString());
				zephyrConfig.setCycleIdZfjCloud(NEW_CYCLE_KEY_IDENTIFIER+"");
				return;
			}
			cycleId = Cycle.getCycleByName(zephyrConfig);
			zephyrConfig.setCycleId(cycleId);
			zephyrConfig.setCycleIdZfjCloud(cycleId.toString());
			return;
		}
		if (this.cycleName.equalsIgnoreCase(NEW_CYCLE_KEY)) {
			zephyrConfig.setCycleId(NEW_CYCLE_KEY_IDENTIFIER.toString());
			return;
		}

		try {
			cycleId = this.cycleName;
		} catch (NumberFormatException e1) {

			logger.error("Cycle Key appears to be the name of the cycle");
			e1.printStackTrace();
		}
		zephyrConfig.setCycleName(this.cycleName);
		zephyrConfig.setCycleId(cycleId);
	}

	private void determineVersionID(ZephyrConfigModel zephyrData) {

		long versionId = 0;
		try {
			versionId = Long.parseLong(versionKey);
		} catch (NumberFormatException e1) {
			versionId = Version.getVersionIdByNameProjectId(this.versionKey,zephyrData.getZephyrProjectId(),zephyrData.getRestClient());
			logger.error("Version Key appears to be Name of the Version");
			e1.printStackTrace();
		}
		zephyrData.setVersionId(versionId);
	}

	private void determineProjectID(ZephyrConfigModel zephyrData) {
		long projectId = 0;
		try {
			projectId = Long.parseLong(projectKey);
		} catch (NumberFormatException e1) {
			logger.error("Project Key appears to be Name of the project");
			try {
				Long projectIdByName = Project.getProjectIdByCode(projectKey, zephyrData.getRestClient());
				projectId = projectIdByName;
			} catch (Exception e) {
				e.printStackTrace();
			}
			//e1.printStackTrace();
		}
		
		zephyrData.setZephyrProjectId(projectId);
	}


	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getVersionKey() {
		return versionKey;
	}

	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getCyclePrefix() {
		return cyclePrefix;
	}

	public void setCyclePrefix(String cyclePrefix) {
		this.cyclePrefix = cyclePrefix;
	}

	public String getCycleDuration() {
		return cycleDuration;
	}

	public void setCycleDuration(String cycleDuration) {
		this.cycleDuration = cycleDuration;
	}
    
}

