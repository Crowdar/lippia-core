package com.crowdar.zapi.model;

public class ZapiStepExecution {
    private String name;
    private String status;
    private Boolean isFailed;
    private String exception;
    private String errorMessage;

    public ZapiStepExecution(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFailed() {
        return isFailed;
    }

    public void setFailed(Boolean failed) {
        isFailed = failed;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
