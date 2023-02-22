package io.lippia.api.lowcode;

import com.crowdar.api.rest.APIManager;
import io.lippia.api.extractor.json.JsonStringValueExtractor;
import io.lippia.api.extractor.xml.XmlStringValueExtractor;
import org.apache.commons.lang.NotImplementedException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static com.crowdar.core.JsonUtils.isJSONValid;

public class ResponseManager {

    private ResponseManager() {}

    public static String getPath(String path) {
        String response = (String) APIManager.getLastResponse().getResponse();
        try {
            return isJSONValid(response) ? JsonStringValueExtractor.handle(response, path) : XmlStringValueExtractor.handle(response, path);
        } catch (ParserConfigurationException | SAXException | IOException ignored) {
            throw new NotImplementedException();
        }
    }

}