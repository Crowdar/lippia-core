package com.crowdar.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;

import java.util.List;

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

	public static String serialize(Object json) {
		String jsonResult = null;
		try {
			jsonResult = mapper.writeValueAsString(Lists.newArrayList(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
}
