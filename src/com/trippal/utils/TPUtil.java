package com.trippal.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.trippal.constants.TPConstants;

import com.trippaldal.dal.places.GooglePlacesDao;
import com.trippaldal.dal.places.GooglePlacesDaoImpl;

public class TPUtil {
	
	private static String apiKey = null;
	
	static{
		GooglePlacesDao placesDao = new GooglePlacesDaoImpl();
		apiKey = placesDao.getAPIKey();
	}
	
	public static void main(String args[]) throws Exception {
		JsonObject jsonObject = TPUtil.getAutoCompletePlaces("bangla",14);
		System.out.println(jsonObject.toString());
		String place_id = jsonObject.getJsonArray("destinations").getJsonObject(0).getJsonArray("cities").getJsonObject(0).getString("id");
		//String place_id = "ChIJbU60yXAWrjsR4E9-UejD3_g"; //Bangalore
		//String place_id = "ChIJv8a-SlENCDsRkkGEpcqC1Qs";//Kochi
		JsonObject location = TPUtil.getPlaceDetailsById(place_id);
		JsonObject nearByPlaces = TPUtil.getNearbyPlacesByRating(place_id, 50000);
		System.out.println(nearByPlaces.toString());
		JsonObject prominentPlace = TPUtil.getNearbyPlacesByProminence(place_id, 20000);
		System.out.println(prominentPlace.toString());
		JsonObject touristPlaces = TPUtil.getNearbyTouristPlaces("bangalore");
		System.out.println(touristPlaces.toString());		
	}

	public static JsonObject getNearbyPlacesByRating(String placeId, int radius) throws Exception {
		String uri = TPConstants.GOOGLE_NEARBY_PLACES_API;
		Map<String, Object> queryParams = getNearByPlacesQuery(placeId, radius);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return convertToPlacesArray(googleResponse, true, "nearbyplaces");
	}
	
	public static JsonObject getNearbyPlacesByProminence(String placeId, int radius) throws Exception {
		String uri = TPConstants.GOOGLE_NEARBY_PLACES_API;
		Map<String, Object> queryParams = getNearByPlacesQuery(placeId, radius);
		queryParams.put("rankby", "prominence");
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);		
		return convertToPlacesArray(googleResponse,false,"nearbyplaces");
	}

	public static JsonObject getNearbyTouristPlaces(String destination) throws Exception {
		String uri = TPConstants.GOOGLE_TEXT_SEARCH_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("query", "tourist+places+in+"+destination);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return convertToPlacesArray(googleResponse,false,"touristplaces");
	}
	
	private static Map<String, Object> getNearByPlacesQuery(String placeId, int radius) throws Exception{
		JsonObject location = getPlaceDetailsById(placeId);
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("location", getLocationParam(location));
		queryParams.put("radius", radius>50000?50000:radius);
		return queryParams;
	}
	
	private static String getLocationParam(JsonObject location){
		String param = location.get("lat").toString()+','+location.get("lng").toString();
		return param;
	}

	private static JsonObject getPlaceDetailsById(String place_id) throws Exception {
		String uri = TPConstants.GOOGLE_PLACE_DETAILS_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("placeid", place_id);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return getLocationCordinates(googleResponse);
	}
	
	private static JsonObject getLocationCordinates(JsonObject placeDetails){
		JsonObject placeDetail = placeDetails.getJsonObject("result").getJsonObject("geometry").getJsonObject("location");
		return placeDetail;
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
	

	
	private static JsonObject convertToPlacesArray(JsonObject nearByJson, boolean sortByRating, String searchTitle){
		JsonArray placesArray = (JsonArray)nearByJson.get("results");
		JsonObjectBuilder destinationResBuilder = Json.createObjectBuilder();
		JsonArrayBuilder destinationArrayBuilder = Json.createArrayBuilder();
		
		if(sortByRating){
			sortByRating(placesArray, destinationArrayBuilder);
		}else{
			for(int i=1; i<placesArray.size(); i++){
				JsonObject place = placesArray.getJsonObject(i);
				destinationArrayBuilder.add(convertGooglePlaceToTPPlace(place));
			}
		}
		destinationResBuilder.add(searchTitle, destinationArrayBuilder.build());
		
		return destinationResBuilder.build();
	}
	
	private static JsonObject convertGooglePlaceToTPPlace(JsonObject place){
		TPPlaceObj tpPlaceObj = new TPPlaceObj();
		JsonObject location = place.getJsonObject("geometry").getJsonObject("location");
		tpPlaceObj.setGeometry(location);
		//tpPlaceObj.setViewport(place.getJsonObject("viewport"));
		tpPlaceObj.setName(place.get("name"));
		//tpPlaceObj.setOpeningHours(place.getJsonObject("opening_hours"));
		tpPlaceObj.setTypes(place.get("types"));
		tpPlaceObj.setRating(place.get("rating"));
		JsonObjectBuilder tpPlaceObjBuilder = Json.createObjectBuilder();
		tpPlaceObjBuilder.add(tpPlaceObj.getName().toString(), tpPlaceObj.getJsonObject());
		return tpPlaceObjBuilder.build();
		
	}
	
	private static void sortByRating(JsonArray placesArray, JsonArrayBuilder destinationArrayBuilder){
		Map<Double,List<JsonObject>> ratingMap = new HashMap<Double,List<JsonObject>>();
		for(int i=1; i<placesArray.size(); i++){
			JsonObject place = placesArray.getJsonObject(i);
			Double rating = Double.parseDouble(place.get("rating") != null?place.get("rating").toString():"0");
			List<JsonObject> places = ratingMap.get(rating);
			if(places == null){
				places = new ArrayList<JsonObject>();
				ratingMap.put(rating, places);
			}
			places.add(place);			
		}
		List<Double> keyList = new ArrayList<Double>(ratingMap.keySet());
		Collections.sort(keyList, new Comparator<Double>() {

			@Override
			public int compare(Double rating1, Double rating2) {
				return rating2.compareTo(rating1);
			}
		});
		Iterator<Double> keyIter = keyList.iterator();
		while(keyIter.hasNext()){
			List<JsonObject> places = ratingMap.get(keyIter.next());
			for(JsonObject place : places){
				destinationArrayBuilder.add(place);
			}
		}
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
	public static String getGoogleAPIKey(){

		return apiKey;
	}
}
