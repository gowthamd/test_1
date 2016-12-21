package com.trippal.utils;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.trippal.constants.TPConstants;

public class TPUtil {

	public static JsonObject getAutoCompletePlaces(String searchStr) throws Exception {
		String uri = TPConstants.GOOGLE_AUTOCOMPLETE_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("input", searchStr);
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("type", "(cities)");
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return convertTo(googleResponse);
	}

	/**
	 * convert google json response format to our json format
	 * 
	 * @param result
	 * @return
	 */
	private static JsonObject convertTo(JsonObject result) {
		JsonArray array = (JsonArray) result.get("predictions");
		JsonObjectBuilder finalResBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrayResBuilder = Json.createArrayBuilder();
		for (int i = 0; i < array.size(); i++) {
			JsonObjectBuilder objResBuilder = Json.createObjectBuilder();
			objResBuilder.add("destination", array.getJsonObject(i).getJsonString("description"));
			objResBuilder.add("id", array.getJsonObject(i).getJsonString("place_id"));
			arrayResBuilder.add(objResBuilder.build());
		}
		finalResBuilder.add("destinations", arrayResBuilder.build());

		return finalResBuilder.build();
	}

	/**
	 * gets google api key from properties file(googleapi.properties)
	 * 
	 * @return
	 */
	public static String getGoogleAPIKey() throws Exception {
		/*
		 * Properties prop = new Properties(); try {
		 * prop.load(Thread.currentThread().getContextClassLoader().
		 * getResourceAsStream("googleapi.properties")); } catch (IOException e)
		 * { throw new Exception("failed to load properties file"); } return
		 * prop.getProperty(TPConstants.GOOGLE_API_KEY);
		 */
		return "AIzaSyDhozxmXh6oh3CgHX481fyNYiPTFFPwwzs";
	}
}
