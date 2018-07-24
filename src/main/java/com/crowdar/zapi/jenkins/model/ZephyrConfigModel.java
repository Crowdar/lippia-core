package com.crowdar.zapi.jenkins.model;

import java.util.List;

import com.crowdar.zapi.jenkins.utils.rest.RestClient;
import com.crowdar.zapi.model.ZapiTestCaseResultModel;

public class ZephyrConfigModel {

	private List<ZapiTestCaseResultModel> testcases;
	private Long zephyrProjectId;
	private Long versionId;
	private String cycleId;
	private String cyclePrefix;
	private long testIssueTypeId;
	private String cycleName;
	private String cycleDuration;
	private RestClient restClient;
	private boolean zfjClud;
	private String cycleIdZfjCloud;
	private int builNumber;


	public long getTestIssueTypeId() {
		return testIssueTypeId;
	}

	public void setTestIssueTypeId(long testIssueTypeId) {
		this.testIssueTypeId = testIssueTypeId;
	}

	public String getCyclePrefix() {
		return cyclePrefix;
	}

	public void setCyclePrefix(String cyclePrefix) {
		this.cyclePrefix = cyclePrefix;
	}

	public List<ZapiTestCaseResultModel> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<ZapiTestCaseResultModel> testcases) {
		this.testcases = testcases;
	}

	public Long getZephyrProjectId() {
		return zephyrProjectId;
	}

	public void setZephyrProjectId(Long zephyrProjectId) {
		this.zephyrProjectId = zephyrProjectId;
	}

	public String getCycleId() {
		return cycleId;
	}

	public void setCycleId(String cycleId) {
		this.cycleId = cycleId;
	}

	public String getCycleName() {
		return cycleName;
	}

	public String getCycleDuration() {
		return cycleDuration;
	}

	public void setCycleDuration(String cycleDuration) {
		this.cycleDuration = cycleDuration;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public RestClient getRestClient() {
		return restClient;
	}

	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public boolean isZfjClud() {
		return zfjClud;
	}

	public void setZfjClud(boolean zfjClud) {
		this.zfjClud = zfjClud;
	}

	public String getCycleIdZfjCloud() {
		return cycleIdZfjCloud;
	}

	public void setCycleIdZfjCloud(String cycleIdZfjCloud) {
		this.cycleIdZfjCloud = cycleIdZfjCloud;
	}

	public int getBuilNumber() {
		return builNumber;
	}

	public void setBuilNumber(int builNumber) {
		this.builNumber = builNumber;
	}

	@Override
	public String toString() {
		return String.format("projectid : %s  --- versionID : %s--- cycleid : %s",zephyrProjectId,versionId,cycleId);
	}
}