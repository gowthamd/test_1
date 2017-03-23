package com.trippal.places.apis.distance.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trippal.constants.TPConstants;
import com.trippal.places.apis.distance.service.domain.DistanceMatrix;
import com.trippal.places.apis.distance.service.domain.DistanceResponse;
import com.trippal.places.apis.distance.service.domain.Element;
import com.trippal.places.apis.distance.service.domain.Row;
import com.trippal.utils.RestClient;
import com.trippal.utils.TPUtil;

@Path("/distance")
public class DistanceFinderAPI {

	@GET
	@Path("/find")
	public String calculateDistance(@QueryParam(value = "origin") String origin,
			@QueryParam(value = "destination") String destination, @QueryParam(value = "unit") String unit) {

		String distanceResponse = null;
		RestClient client = new RestClient();
		Map<String, Object> queryParams = buildRequestData(origin, destination, unit);

		try {
			String googleResponse = client.getAsString(TPConstants.GOOGLE_MAPS_DISANCE_CALC_API, queryParams);
			Gson gson = new GsonBuilder().create();
			DistanceMatrix distanceMatrix = gson.fromJson(googleResponse, DistanceMatrix.class);

			if (distanceMatrix != null && distanceMatrix.getRows() != null && !distanceMatrix.getRows().isEmpty()) {
				Row row = distanceMatrix.getRows().get(0);
				if(row != null && row.getElements() != null && !row.getElements().isEmpty()){
					Element element = row.getElements().get(0);
					DistanceResponse response = new DistanceResponse();
					if(element.getDistance() != null && element.getDistance().getText() != null){
						response.setDistance(element.getDistance().getText());
					}
					if(element.getDuration() != null && element.getDuration().getText() != null){
						response.setDuration(element.getDuration().getText());
					}
					distanceResponse = gson.toJson(response); 
				}
			}
		} catch (Exception e) {

		}

		return distanceResponse;
	}

	public Map<String, Object> buildRequestData(String origin, String destination, String unit) {
		Map<String, Object> queryParams = new HashMap<String, Object>();

		queryParams.put("origins", origin);
		queryParams.put("destinations", destination);
		if (TPConstants.KILOMETERS.equalsIgnoreCase(unit)) {
			queryParams.put("units", "metric");
		} else {
			queryParams.put("units", "imperial");
		}
		queryParams.put("key", TPUtil.getGoogleAPIKey());

		return queryParams;
	}

}
