package com.crowdar.zapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZapiModel {

    protected  ObjectMapper mapper = new ObjectMapper();
    public String toString() {
        String str = "";
        try {
            str =  mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

     public <T extends ZapiModel> T getInstance(String json){

        Object o = null;
        try {
            o =  mapper.readValue(json,this.getClass());
         } catch (IOException e) {
             e.printStackTrace();
         }
         return (T)o;
     }
}
