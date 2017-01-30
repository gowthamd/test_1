package com.trippal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.trippal.constants.TPConstants;

public class TPUtil {
	
	public static void main(String args[]) throws Exception {
		JsonObject jsonObject = TPUtil.getAutoCompletePlaces("bangla",14);
		System.out.println(jsonObject.toString());
	}

	public static JsonObject getAutoCompletePlaces(String searchStr, Integer regionType) throws Exception {
		String uri = TPConstants.GOOGLE_AUTOCOMPLETE_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("input", searchStr);
		queryParams.put("key", getGoogleAPIKey());
		RestClient restClient = new RestClient();

		JsonObjectBuilder finalResBuilder = Json.createObjectBuilder();
		JsonArrayBuilder regionArrayBuilder = Json.createArrayBuilder();
		List<String> regionStr = getRegionList(regionType);
		for (String type : regionStr) {
			if ("cities".equals(type)) {
				queryParams.put("type", "(cities)");
			} else {
				queryParams.put("type", "(regions)");
			}
			JsonObject googleResponse = restClient.get(uri, queryParams);
			regionArrayBuilder.add(convertTo(googleResponse, type));
		}

		finalResBuilder.add("destinations", regionArrayBuilder.build());
		return finalResBuilder.build();
	}

	private static List<String> getRegionList(Integer regionType) {
		List<String> regionList = new ArrayList<String>();
		if (regionType == 2) {
			regionList.add("cities");
		}
		if (regionType == 4) {
			regionList.add("states");
		}
		if (regionType == 8) {
			regionList.add("countries");
		}
		if (regionType == 14) {
			regionList.add("cities");
			regionList.add("states");
			regionList.add("countries");
		}
		if (regionType == 10) {
			regionList.add("cities");
			regionList.add("countries");
		}
		if (regionType == 6) {
			regionList.add("cities");
			regionList.add("states");
		}
		if (regionType == 12) {
			regionList.add("states");
			regionList.add("countries");
		}
		return regionList;
	}

	/**
	 * convert google json response format to our json format
	 * 
	 * @param result
	 * @return
	 */
	private static JsonObject convertTo(JsonObject result, String regionType) {
		JsonArray array = (JsonArray) result.get("predictions");
		JsonObjectBuilder destinationResBuilder = Json.createObjectBuilder();
		JsonArrayBuilder destinationArrayBuilder = Json.createArrayBuilder();
		for (int i = 0; i < array.size(); i++) {
			boolean isSameType = isTypeMatching(array.getJsonObject(i), regionType);
			if (isSameType) {
				JsonObjectBuilder objResBuilder = Json.createObjectBuilder();
				objResBuilder.add("destination", array.getJsonObject(i).getJsonString("description"));
				objResBuilder.add("id", array.getJsonObject(i).getJsonString("place_id"));
				destinationArrayBuilder.add(objResBuilder.build());
			}
		}
		destinationResBuilder.add(regionType, destinationArrayBuilder.build());
		return destinationResBuilder.build();
	}

	private static boolean isTypeMatching(JsonObject jsonObject, String regionType) {
		JsonArray regionArr = jsonObject.getJsonArray("types");
		if ("countries".equals(regionType)) {
			for (int i = 0; i < regionArr.size(); i++) {
				String regionName = regionArr.getString(i);
				if ("country".equalsIgnoreCase(regionName)) {
					return true;
				}
			}

		} else if ("states".equals(regionType)) {
			for (int i = 0; i < regionArr.size(); i++) {
				String regionName = regionArr.getString(i);
				if ("administrative_area_level_1".equalsIgnoreCase(regionName)) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
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
