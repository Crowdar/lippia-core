package io.lippia.api.lowcode.assertions;

import com.crowdar.core.annotations.Beta;

import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.config.XmlPathConfig;

@Beta
public class XmlPathAnalyzer {
    private XmlPathAnalyzer() {
    }

    public static String read(String xmlString, String xmlPath) {
        XmlPath xmlPathInstance = new XmlPath(xmlString)
                .using(XmlPathConfig.xmlPathConfig());

        return xmlPathInstance.get(xmlPath);
    }

}