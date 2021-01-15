package io.lippia.api.extractor.ddbb;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DatabaseIntegerValueExtractor {
	
	public static int handle(List<Map<String, Object>> result, String varPath) throws ParserConfigurationException, SAXException, IOException {
		
		String[] varPaths = varPath.split("\\.");
				
		String index = varPaths[0].split("\\[")[0].replace("]", "");

		return (Integer) result.get(Integer.parseInt(index)).get(varPaths[1]);
	}
	
}
