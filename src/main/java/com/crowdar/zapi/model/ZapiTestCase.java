package com.crowdar.zapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


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



    public ZapiTestCase() {

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
}
