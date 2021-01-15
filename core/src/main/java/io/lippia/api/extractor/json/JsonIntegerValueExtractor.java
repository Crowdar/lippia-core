package io.lippia.api.extractor.json;

import org.json.JSONObject;

public class JsonIntegerValueExtractor {
	
	public static int handle(String jsonString, String jsonPath) {
		String[] jsonPaths = jsonPath.split("\\.");
		int response = 0;
		int count  = 0;
		for(String once : jsonPaths) {
			count++;
			JSONObject jsonObject = new JSONObject(jsonString);
			if(count == jsonPaths.length) return jsonObject.getInt(once);
			jsonString = jsonObject.getJSONObject(once).toString();
		}
		
		return response;
	}
	
}
