package com.trippal.utils;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.trippal.constants.TPConstants;

public class TPUtil {
	public static void main(String args[]) throws Exception {
		JsonObject jsonObj = getAutoCompletePlaces("ban");
		System.out.println(jsonObj);
	}

	public static JsonObject getAutoCompletePlaces(String searchStr) throws Exception {
		String uri = TPConstants.GOOGLE_AUTOCOMPLETE_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("input", searchStr);
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("type", "geocode");
		RestClient restClient = new RestClient();
		return restClient.get(uri, queryParams);
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
