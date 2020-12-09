package io.lippia.api.extractor.json;

import java.util.List;

import org.json.JSONObject;

public class JsonArrayValueExtractor {
	
	public static List<Object> handle(String jsonString, String jsonPath) {
		String[] jsonPaths = jsonPath.split("\\.");
		int count  = 0;
		for(String once : jsonPaths) {
			count++;
			JSONObject jsonObject = new JSONObject(jsonString);
			if(count == jsonPaths.length) return jsonObject.getJSONArray(once).toList();
			jsonString = jsonObject.getJSONObject(once).toString();
		}
		
		return null;
	}
	
}
