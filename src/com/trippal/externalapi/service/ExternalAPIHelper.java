package com.trippal.externalapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.trippal.constants.TPConstants;
import com.trippal.externalapi.domain.ExternalPlaceObj;
import com.trippal.places.apis.distance.service.domain.exceptions.APIQuotaExceededException;
import com.trippal.utils.RestClient;

public class ExternalAPIHelper {

	private String apiKey;
	private RestClient restClient;

	public ExternalAPIHelper(String apiKey) {
		this.apiKey = apiKey;
		this.restClient = new RestClient();
	}

	private static List<ExternalPlaceObj> convertToPlacesArray(JsonObject nearByJson, boolean sortByRating)
			throws Exception {
		JsonArray placesArray = (JsonArray) nearByJson.get("results");
		List<ExternalPlaceObj> placeList = new ArrayList<ExternalPlaceObj>();

		long startTime = System.currentTimeMillis();
		// placesArray = sortByRating(placesArray);
		System.out.println("sorting : " + (System.currentTimeMillis() - startTime));
		startTime = System.currentTimeMillis();
		for (int i = 0; i < placesArray.size(); i++) {
			JsonObject place = placesArray.getJsonObject(i);
			placeList.add(convertGooglePlaceToPlace(place));
		}
		System.out.println("convert to google place : " + (System.currentTimeMillis() - startTime));
		
		placeList = placeList.parallelStream().filter(place -> !place.getTypes().toString().contains("travel_agency"))
				.collect(Collectors.toList());
		Collections.sort(placeList);
		return placeList;
	}

	private static ExternalPlaceObj convertGooglePlaceToPlace(JsonObject place) throws Exception {
		ExternalPlaceObj tpPlaceObj = new ExternalPlaceObj();
		JsonObject geometry = place.getJsonObject("geometry");
		tpPlaceObj.setGeometry(geometry.getJsonObject("location"));
		tpPlaceObj.setViewport(geometry.getJsonObject("viewport"));
		tpPlaceObj.setOpeningHours(place.getJsonObject("opening_hours"));
		tpPlaceObj.setName(place.get("name").toString());
		tpPlaceObj.setGoogleId(place.getString("place_id"));
		if (place.getJsonArray("photos") != null) {
			tpPlaceObj.setPhotoRef(place.getJsonArray("photos").getJsonObject(0).getString("photo_reference"));
		}
		Double rating = 0.0;
		if (place.get("rating") != null) {
			rating = Double.parseDouble(place.get("rating").toString());
		}
		tpPlaceObj.setRating(rating);
		tpPlaceObj.setTypes(place.get("types"));
		return tpPlaceObj;
	}

	public List<ExternalPlaceObj> getTouristPlacesByName(String destination) throws Exception {
		String uri = TPConstants.GOOGLE_TEXT_SEARCH_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", apiKey);
		queryParams.put("query", "tourist+places+in+" + destination);
		JsonObject googleResponse = restClient.get(uri, queryParams);

		if (googleResponse.getString("status").trim().equals(TPConstants.QUOTA_EXCEEDED)) {
			throw new APIQuotaExceededException("API Quota Exceeded For Project");
		}
		List<ExternalPlaceObj> placeList = convertToPlacesArray(googleResponse, false);
		return placeList;
	}

}
