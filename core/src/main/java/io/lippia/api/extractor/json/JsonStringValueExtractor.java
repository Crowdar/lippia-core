package io.lippia.api.extractor.json;

import org.json.JSONObject;

public class JsonStringValueExtractor {
	
	public static String handle(String jsonString, String jsonPath) {
		String[] jsonPaths = jsonPath.split("\\.");
			
		for (int i = 0; i < jsonPaths.length; i++) {
			String[] once = jsonPaths[i].split("\\[");
			JSONObject jsonObject;
			
			jsonObject= new JSONObject(jsonString);
			
			if(once.length > 1) {
				jsonString = jsonObject.getJSONArray(once[0]).get(Integer.parseInt(once[1].replace("]",""))).toString();
				if(i+1 == jsonPaths.length) {
					return jsonString;
				}
				continue;
			}		
			
			if(i+1 == jsonPaths.length) {
				return jsonObject.getString(once[0]);
			}
			
			jsonString = jsonObject.getJSONObject(once[1]).toString();
		}
		return jsonString;
	}
	
}
