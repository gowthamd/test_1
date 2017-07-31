package com.trippal.places.apis.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.trippal.constants.TPConstants;
import com.trippal.utils.RestClient;
import com.trippal.utils.TPUtil;

@Path("/autocomplete")
public class AutoCompletePlaceAPIRest {
	/**
	 * get autocomplete place result for given str
	 * 
	 * @param placeStr
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/places")
	public String getAutoCompletePlaces(@QueryParam(value = "str") String placeStr,
			@QueryParam(value = "region") Integer regionType) throws Exception {
		JsonObject jsonObject = getAutoCompletePlacess(placeStr, regionType);
		return jsonObject.toString();
	}

	private JsonObject getAutoCompletePlacess(String searchStr, Integer regionType) throws Exception {
		String uri = TPConstants.GOOGLE_AUTOCOMPLETE_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("input", searchStr);
		queryParams.put("key", TPUtil.getGoogleAPIKey());
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

	/**
	 * convert google json response format to our json format
	 * 
	 * @param result
	 * @return
	 */
	private JsonObject convertTo(JsonObject result, String regionType) {
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

	private boolean isTypeMatching(JsonObject jsonObject, String regionType) {
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

}
