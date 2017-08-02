package com.trippal.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.trippal.constants.TPConstants;
import com.trippaldal.dal.places.GooglePlacesDao;
import com.trippaldal.dal.places.GooglePlacesDaoImpl;
import com.trippal.places.apis.distance.service.DistanceFinderAPI;
import com.trippal.places.apis.planner.modifyroute.AddDayToRouteRequest;
import com.trippal.places.apis.planner.modifyroute.ModifyRouteRequest;
import com.trippal.places.planner.DayPlanner;
import com.trippal.places.planner.Location;
import com.trippal.places.planner.Place;
import com.trippal.places.planner.Route;

public class TPUtil {
	
	private static String apiKey = null;
	
	public static void main(String args[]) throws Exception {
		/*JsonObject jsonObject = TPUtil.getAutoCompletePlaces("bangla",14);
		System.out.println(jsonObject.toString());
		String place_id = jsonObject.getJsonArray("destinations").getJsonObject(0).getJsonArray("cities").getJsonObject(0).getString("id");
		//String place_id = "ChIJbU60yXAWrjsR4E9-UejD3_g"; //Bangalore
		//JsonObject location = TPUtil.getPlaceDetailsById(place_id);
		JsonObject nearByPlaces = TPUtil.getNearbyPlacesByRating(place_id, 50000);
		System.out.println(nearByPlaces.toString());
		JsonObject prominentPlace = TPUtil.getNearbyPlacesByProminence(place_id, 20000);
		System.out.println(prominentPlace.toString());*/
		JsonObject touristPlaces = TPUtil.getNearbyTouristPlaces("manali");
		System.out.println(touristPlaces.toString());/*
		JsonObject suggestedTouristPlaces = TPUtil.getSuggestedTouristPlaces("bangalore");
		System.out.println(suggestedTouristPlaces.toString());
		DistanceFinderAPI finderAPI = new DistanceFinderAPI();
		System.out.println(finderAPI.calculateDistance("ChIJHdPykcEVrjsRIr4v35kLEY4", "ChIJL2fQ53MWrjsRuN9D6aalLMY", "kms"));*/
	}

	public static JsonObject getNearbyPlacesByRating(String placeId, int radius) throws Exception {
		String uri = TPConstants.GOOGLE_NEARBY_PLACES_API;
		Map<String, Object> queryParams = getNearByPlacesQuery(placeId, radius);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return convertToPlacesJsonArray(googleResponse, true, "nearbyplaces");
	}
	
	public static JsonObject getNearbyPlacesByProminence(String placeId, int radius) throws Exception {
		String uri = TPConstants.GOOGLE_NEARBY_PLACES_API;
		Map<String, Object> queryParams = getNearByPlacesQuery(placeId, radius);
		queryParams.put("rankby", "prominence");
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);		
		return convertToPlacesJsonArray(googleResponse,false,"nearbyplaces");
	}

	public static JsonObject getSuggestedTouristPlaces(String destination) throws Exception {
		Map<String, JsonObject> idToJson = new HashMap<String, JsonObject>();
		List<Place> placeList = getTouristPlacesByName(destination, idToJson);
		return getSuggestedRoute(placeList);
		
	}
	


	public static JsonObject getNextdayRoute(AddDayToRouteRequest addDayToRouteRequest) throws Exception {
		List<Place> placeList = addDayToRouteRequest.getAllPlaceList();
		if(null == placeList || placeList.size() == 0){
			placeList = getTouristPlacesByName(addDayToRouteRequest.getDestination(), new HashMap<String, JsonObject>());
		}
		Iterator<Place> placeIter = placeList.iterator();
		List<String> selectedList = addDayToRouteRequest.getSelectedPlacesList();
		while(placeIter.hasNext()){
			if(selectedList.contains(placeIter.next().getGoogleId())){
				placeIter.remove();
			}
		}
		return getSuggestedRoute(placeList);
	}
	
	public static JsonObject getModifiedRoute(ModifyRouteRequest modifyRequest) throws Exception{
		List<Place> placeList = modifyRequest.getRetainedPlaces();
		if(modifyRequest.getAddedPlaces().size() == 0){
			return getSuggestedRoute(placeList);
		}
		placeList.addAll(modifyRequest.getAddedPlaces());
		return getSuggestedRoute(placeList);		
	}
	
	private static JsonObject getSuggestedRoute(List<Place> placeList) throws Exception{
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		DayPlanner planner = new DayPlanner();
		Route route = planner.planItenary(new Place(),placeList);

		JsonObjectBuilder inputObjectBuilder = Json.createObjectBuilder();
		int i=0;
		
		for(Place place : route.getRoute()){
			inputObjectBuilder.add("googleId", place.getGoogleId());
			inputObjectBuilder.add("name", place.getName());
			inputObjectBuilder.add("rating", place.getRating());
			inputObjectBuilder.add("latitute", place.getLocation().getLatitude());
			inputObjectBuilder.add("longitude", place.getLocation().getLongtitude());
			inputObjectBuilder.add("TimeTakenToNextPlace", route.getTimeTaken(i++));
			arrayBuilder.add(inputObjectBuilder);
		}
		objectBuilder.add("result", arrayBuilder.build());
		return objectBuilder.build();
	}
	
	public static JsonObject getNearbyTouristPlaces(String destination) throws Exception{
		Map<String, JsonObject> idToJson = new HashMap<String, JsonObject>();
		List<Place> placeList = getTouristPlacesByName(destination, idToJson);
		return convertTo(placeList, idToJson);
	}
	
	private static List<Place> getTouristPlacesByName(String destination, Map<String, JsonObject> idToJson) throws Exception{
		String uri = TPConstants.GOOGLE_TEXT_SEARCH_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("query", "tourist+places+in+"+destination);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		List<TPPlaceObj> placeList = convertToPlacesArray(googleResponse,idToJson,false,"touristplaces");
		List<Place> newList = new ArrayList<Place>();
		for(TPPlaceObj input : placeList){
			Place place = convertTo(input);
			newList.add(place);
		}
		long start = System.currentTimeMillis();
		populateOpeningHours(newList);
		long finish = System.currentTimeMillis();
		System.out.printf("Start : %d \nEnd : %d\nDiff: %d\n ",start,finish,finish-start);
		return newList;
	}

	private static void populateOpeningHours(List<Place> placeList) {
		ExecutorService es = Executors.newCachedThreadPool();
		CountDownLatch cl = new CountDownLatch(placeList.size());
		for(Place placeObj : placeList){
			PlaceDetailsPopulator detailPopulator = new PlaceDetailsPopulator(cl, placeObj);
			es.submit(detailPopulator);
		}
		try {
			cl.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static JsonObject convertTo(List<Place> placeList, Map<String, JsonObject> idToJson){
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for(Place place : placeList){
			arrayBuilder.add(convertTo(place, idToJson));
		}
		objectBuilder.add("tourist-places", arrayBuilder.build());
		return objectBuilder.build();
	}

	private static JsonObject convertTo(Place place, Map<String, JsonObject> idToJson) {
		JsonObjectBuilder placesObjectBuilder = Json.createObjectBuilder();
		JsonObject placeJsonObj = idToJson.get(place.getGoogleId());
		placesObjectBuilder.add("geometry", placeJsonObj.get("geometry"));
		placesObjectBuilder.add("name", placeJsonObj.get("name"));
		placesObjectBuilder.add("rating", placeJsonObj.get("rating"));
		placesObjectBuilder.add("googleId", place.getGoogleId());
		JsonObjectBuilder openingHours = Json.createObjectBuilder();
		if(null != place.getOpeningHour(6)){
			openingHours.add("open", place.getOpeningHour(6).toString());
			openingHours.add("close", place.getClosingHour(6).toString());
			placesObjectBuilder.add("opening_hours", openingHours);
		}
		return placesObjectBuilder.build();
	}

	private static Map<String, Object> getNearByPlacesQuery(String placeId, int radius) throws Exception{
		JsonObject location = getLocationCordinates(placeId);
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

	public static JsonObject getPlaceDetailsById(String place_id) throws Exception {
		String uri = TPConstants.GOOGLE_PLACE_DETAILS_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", getGoogleAPIKey());
		queryParams.put("placeid", place_id);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		return googleResponse.getJsonObject("result");
	}
	
	private static JsonObject getLocationCordinates(String place_id) throws Exception{
		JsonObject placeDetails = getPlaceDetailsById(place_id);
		JsonObject location = placeDetails.getJsonObject("geometry").getJsonObject("location");
		return location;
	}
	
	private static Place convertTo(TPPlaceObj input) {
		Place place = new Place();
		Location location = new Location();
		location.setLatitude(input.getGeometry().get("lat").toString());
		location.setLongtitude(input.getGeometry().get("lng").toString());
		place.setLocation(location);
		place.setName(input.getName().toString());
		place.setRating(input.getRating());
		place.setGoogleId(input.getGoogleId());
		return place;
	}
	

	
	private static JsonObject convertToPlacesJsonArray(JsonObject nearByJson, boolean sortByRating, String searchTitle){
		JsonArray placesArray = (JsonArray)nearByJson.get("results");
		JsonObjectBuilder destinationResBuilder = Json.createObjectBuilder();
		JsonArrayBuilder destinationArrayBuilder = Json.createArrayBuilder();
		
		if(sortByRating){
			placesArray = sortByRating(placesArray);
		}
		for(int i=1; i<placesArray.size(); i++){
			JsonObject place = placesArray.getJsonObject(i);
			destinationArrayBuilder.add(convertGooglePlaceToTPPlace(place));
		}
		destinationResBuilder.add(searchTitle, destinationArrayBuilder.build());
		
		return destinationResBuilder.build();
	}
	
	private static List<TPPlaceObj> convertToPlacesArray(JsonObject nearByJson, Map<String, JsonObject> idToJson, boolean sortByRating, String searchTitle) throws Exception{
		JsonArray placesArray = (JsonArray)nearByJson.get("results");
		List<TPPlaceObj> placeList = new ArrayList<TPPlaceObj>();
		
		long startTime = System.currentTimeMillis();
		//placesArray = sortByRating(placesArray);
		System.out.println("sorting : "+(System.currentTimeMillis()-startTime));
		startTime = System.currentTimeMillis();
		for(int i=0;i<placesArray.size();i++){	
			JsonObject place = placesArray.getJsonObject(i);
			idToJson.put(place.getString("place_id"), place);
			placeList.add(convertGooglePlaceToPlace(place));
		}
		System.out.println("convert to google place : "+(System.currentTimeMillis()-startTime));
		//destinationResBuilder.add(searchTitle, destinationArrayBuilder.build());
		Collections.sort(placeList);
		return placeList;
	}
	
	private static TPPlaceObj convertGooglePlaceToPlace(JsonObject place) throws Exception {
		//JsonObject details = getPlaceDetailsById(place.getString("place_id"));
		TPPlaceObj tpPlaceObj = new TPPlaceObj();
		JsonObject geometry = place.getJsonObject("geometry");
		tpPlaceObj.setGeometry(geometry.getJsonObject("location"));
		tpPlaceObj.setViewport(geometry.getJsonObject("viewport"));
		tpPlaceObj.setOpeningHours(place.getJsonObject("opening_hours"));
		tpPlaceObj.setName(place.get("name"));
		tpPlaceObj.setGoogleId(place.getString("place_id"));
		Double rating = 0.0;
		if(place.get("rating") != null){
			rating = Double.parseDouble(place.get("rating").toString());
		}
		tpPlaceObj.setRating(rating);
		tpPlaceObj.setTypes(place.get("types"));
		return tpPlaceObj;
		
	}

	private static JsonObject convertGooglePlaceToTPPlace(JsonObject place){
		TPPlaceObj tpPlaceObj = new TPPlaceObj();
		JsonObject location = place.getJsonObject("geometry").getJsonObject("location");
		tpPlaceObj.setGeometry(location);
		//tpPlaceObj.setViewport(place.getJsonObject("viewport"));
		tpPlaceObj.setName(place.get("name"));
		//tpPlaceObj.setOpeningHours(place.getJsonObject("opening_hours"));
		tpPlaceObj.setTypes(place.get("types"));
		Double rating = 0.0;
		if(place.get("rating") != null){
			rating = Double.parseDouble(place.get("rating").toString());
		}
		tpPlaceObj.setRating(rating);
		JsonObjectBuilder tpPlaceObjBuilder = Json.createObjectBuilder();
		tpPlaceObjBuilder.add(tpPlaceObj.getName().toString(), tpPlaceObj.getJsonObject());
		return tpPlaceObjBuilder.build();
		
	}
	
	private static JsonArray sortByRating(JsonArray placesArray){
		Map<Double,List<JsonObject>> ratingMap = new HashMap<Double,List<JsonObject>>();
		JsonArrayBuilder destinationArrayBuilder = Json.createArrayBuilder();
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
		return destinationArrayBuilder.build();
	}
 
	/**
	 * gets google api key from properties file(googleapi.properties)
	 * 
	 * @return
	 */
	public static String getGoogleAPIKey(){

		if(apiKey == null){
			GooglePlacesDao placesDao = new GooglePlacesDaoImpl();
			apiKey = placesDao.getAPIKey();
		}

		return apiKey;
	}
}
