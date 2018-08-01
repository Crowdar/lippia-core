package com.crowdar.zapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gherkin.formatter.model.Result;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ZapiTestCase extends ZapiModel {

    @JsonProperty("project")
    private ZapiProject project;
    @JsonProperty("issuetype")
    private ZapiIssueType issueType;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("description")
    private String description;

    @JsonProperty("customfield_10064")
    private SelectList customfield_10064;
    @JsonProperty("customfield_10065")
    private SelectList customfield_10065;
    @JsonProperty("labels")
    private List<String> labels;

    @JsonIgnore
    private String jiraTicket;

    @JsonIgnore
    private ZapiExecution execution;

    @JsonIgnore
    Result result;




    public ZapiTestCase() {
        customfield_10064 = new SelectList("SI");
        customfield_10065 = new SelectList("SI");
        labels = new ArrayList<String>();
        labels.add("Automation");

    }

    public ZapiTestCase(String summary) {
        this.summary = summary;
    }

    public ZapiProject getProject() {
        return project;
    }

    public void setProject(ZapiProject project) {
        this.project = project;
    }

    public ZapiIssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(ZapiIssueType issueType) {
        this.issueType = issueType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJiraTicket() {
        return jiraTicket;
    }

    public void setJiraTicket(String jiraTicket) {
        this.jiraTicket = jiraTicket;
    }

    public ZapiExecution getExecution() {
        if(execution == null){
            execution = new ZapiExecution();
        }
        return execution;
    }

    public void setExecution(ZapiExecution execution) {
        this.execution = execution;
    }

    public SelectList getCustomfield_10064() {
        return customfield_10064;
    }

    public void setCustomfield_10064(SelectList customfield_10064) {
        this.customfield_10064 = customfield_10064;
    }

    public SelectList getCustomfield_10065() {
        return customfield_10065;
    }

    public void setCustomfield_10065(SelectList customfield_10065) {
        this.customfield_10065 = customfield_10065;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}


class SelectList{


    private String value;

    public SelectList(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}