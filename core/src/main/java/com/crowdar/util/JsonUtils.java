package com.crowdar.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> List<T> deserialize(String json, Class<T> type) {
		try {
			TypeFactory typeFactory = mapper.getTypeFactory();
			return mapper.readValue(json, typeFactory.constructCollectionType(List.class, type));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T deserializeWithoutList(String json, Class<T> type) {
		try {
			TypeFactory typeFactory = mapper.getTypeFactory();
			return mapper.readValue(json, typeFactory.constructType(type));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String serialize(Object json) {
		String jsonResult = null;
		try {
			jsonResult = mapper.writeValueAsString(Lists.newArrayList(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	public static String serializeWithoutList(Object json) {
		String jsonResult = null;
		try {
			jsonResult = mapper.writeValueAsString(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	public static String getJsonAsString(String jsonPath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(jsonPath)));
	}
	
	public static String getJsonFromFile(String jsonPath) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;

		try {
			FileInputStream fis = new FileInputStream(Paths.get(jsonPath).toFile());
			JsonNode rootNode = mapper.readTree(fis);
			json = rootNode.toString();
		} catch (IOException var5) {
			System.out.println("JSON was not found. " + var5.getMessage());
		}
		return json;
}

	public static boolean isJSONValid(Object json) {
		String jsonString = serializeWithoutList(json);

		return isJSONValid(jsonString);
	}
	
	public static boolean isJSONValid(String jsonString) {
		return (jsonString.startsWith("{") && jsonString.endsWith("}"))
				|| jsonString.startsWith("[") && jsonString.endsWith("]");
	}
}
