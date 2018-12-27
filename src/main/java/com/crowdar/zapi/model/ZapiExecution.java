package com.crowdar.zapi.model;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZapiExecution {

    private String duration;
    private Boolean isFailed;
    private List<ZapiStepExecution> zapiStepExecutionList;



    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getFailed() {

        return isFailed;
    }

    public void setFailed(Boolean failed) {
        isFailed = failed;
    }

    public void addStepExecution( ZapiStepExecution zapiStepExecution){
        if(zapiStepExecutionList == null){
            zapiStepExecutionList = new ArrayList<ZapiStepExecution>();
        }
        zapiStepExecutionList.add(zapiStepExecution);
    }




}
