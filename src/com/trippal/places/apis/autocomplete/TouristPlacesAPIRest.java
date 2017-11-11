package com.trippal.places.apis.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.trippal.constants.PlaceConstants;
import com.trippal.externalapi.domain.ExternalPlaceObj;
import com.trippal.externalapi.service.ExternalAPIHelper;
import com.trippal.places.apis.distance.service.domain.exceptions.APIQuotaExceededException;
import com.trippal.places.apis.planner.comparators.RatingComparator;
import com.trippal.places.planner.domain.Location;
import com.trippal.places.planner.domain.Place;
import com.trippal.places.planner.domain.Route;
import com.trippal.places.planner.service.DayPlanner;
import com.trippal.utils.PlaceDetailsPopulator;
import com.trippal.utils.TPUtil;

@Path("/nearbysearch")
public class TouristPlacesAPIRest {

	@POST
	@Produces("application/json")
	@Path("/tourist-places")
	public String getSuggestedTouristPlaces(@QueryParam(value = "destination") String destination,
			TouristPlaceRequest req) throws Exception {
		ExternalAPIHelper helper = new ExternalAPIHelper(TPUtil.getGoogleAPIKey());
		JsonObject jsonObject = getSuggestedTouristPlace(destination, req, helper);
		return jsonObject.toString();
	}

	public JsonObject getSuggestedTouristPlace(String destination, TouristPlaceRequest req, ExternalAPIHelper helper)
			throws Exception {
		try {
			List<ExternalPlaceObj> externalAPIPlaceList = helper.getTouristPlacesByName(destination);
			List<Place> placeList = new ArrayList<Place>();
			externalAPIPlaceList.stream().forEach(externalPlaceObj -> {
				placeList.add(convertTo(externalPlaceObj));
			});
			populateOpeningHours(placeList);
			return getSuggestedRoute(placeList,req);
		} catch (APIQuotaExceededException ex) {
			JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
			responseBuilder.add("error", "Google API Quota for this Project Exceeded!!!");
			return responseBuilder.build();
		}
	}

	public static JsonObject getSuggestedRoute(List<Place> placeList,TouristPlaceRequest req) throws Exception {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		DayPlanner planner = new DayPlanner();
		Route route = null;
		try {

			route = planner.planItenary(req.getStartPlace(), placeList, req.getEndPlace(), new RatingComparator());
		} catch (APIQuotaExceededException ex) {
			objectBuilder.add("error", "Google API Quota Exceeded");
			return objectBuilder.build();
		}
		JsonObjectBuilder inputObjectBuilder = Json.createObjectBuilder();
		int i = 0;
		inputObjectBuilder.add("name", req.getStartPlace().getName());
		inputObjectBuilder.add("latitute", req.getStartPlace().getLocation().getLatitude());
		inputObjectBuilder.add("longitude", req.getStartPlace().getLocation().getLongtitude());
		inputObjectBuilder.add("timeTakenToNextPlace", route.getTimeTaken(i++));
		arrayBuilder.add(inputObjectBuilder);
		
		for (Place place : route.getRoute()) {
			inputObjectBuilder.add("googleId", place.getGoogleId());
			inputObjectBuilder.add("name", place.getName());
			inputObjectBuilder.add("rating", place.getRating());
			inputObjectBuilder.add("latitute", place.getLocation().getLatitude());
			inputObjectBuilder.add("longitude", place.getLocation().getLongtitude());
			inputObjectBuilder.add("timeTakenToNextPlace", route.getTimeTaken(i++));
			inputObjectBuilder.add("timeToSpent", place.getTimeToSpent().toString());
			arrayBuilder.add(inputObjectBuilder);
		}
		
		inputObjectBuilder.add("name", req.getEndPlace().getName());
		inputObjectBuilder.add("latitute", req.getEndPlace().getLocation().getLatitude());
		inputObjectBuilder.add("longitude", req.getEndPlace().getLocation().getLongtitude());
		arrayBuilder.add(inputObjectBuilder);
		objectBuilder.add("result", arrayBuilder.build());
		return objectBuilder.build();
	}

	private Place convertTo(ExternalPlaceObj input) {
		Place place = new Place();
		Location location = new Location();
		location.setLatitude(input.getGeometry().get("lat").toString());
		location.setLongtitude(input.getGeometry().get("lng").toString());
		place.setLocation(location);
		place.setName(input.getName().toString());
		place.setRating(input.getRating());
		place.setGoogleId(input.getGoogleId());
		place.setPhotoRef(input.getPhotoRef());
		JsonArray typeArray = (JsonArray) input.getTypes();
		for (int i = 0; i < typeArray.size(); i++) {
			if (typeArray.getString(i).trim().equals("point_of_interest")) {
				continue;
			}
			place.setTimeToSpent(PlaceConstants.getTimeToSpent(PlaceConstants.find(typeArray.getString(i).trim())));
			break;
		}
		return place;
	}

	private static void populateOpeningHours(List<Place> placeList) {
		ExecutorService es = Executors.newCachedThreadPool();
		CountDownLatch cl = new CountDownLatch(placeList.size());
		for (Place placeObj : placeList) {
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

}
