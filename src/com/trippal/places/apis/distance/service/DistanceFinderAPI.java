package com.trippal.places.apis.distance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.joda.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trippal.constants.TPConstants;
import com.trippal.places.apis.distance.service.domain.DistanceMatrix;
import com.trippal.places.apis.distance.service.domain.DistanceResponse;
import com.trippal.places.apis.distance.service.domain.Element;
import com.trippal.places.apis.distance.service.domain.Row;
import com.trippal.places.apis.distance.service.domain.exceptions.APIQuotaExceededException;
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
			System.out.println(e);
		}

		return distanceResponse;
	}

	
	public DistanceResponse calculateDistanceMatrix(@QueryParam(value = "origin") String origin,
			@QueryParam(value = "destination") String destination, @QueryParam(value = "unit") String unit) 
	throws APIQuotaExceededException{

		RestClient client = new RestClient();
		Map<String, Object> queryParams = buildRequestData(origin, destination, unit);
		DistanceResponse response = new DistanceResponse();

		try {
			String googleResponse = client.getAsString(TPConstants.GOOGLE_MAPS_DISANCE_CALC_API, queryParams);
			Gson gson = new GsonBuilder().create();
			DistanceMatrix distanceMatrix = gson.fromJson(googleResponse, DistanceMatrix.class);
			if(distanceMatrix.getStatus().trim().equals(TPConstants.QUOTA_EXCEEDED)){
				throw new APIQuotaExceededException("Google API Quota Exceeded!!!");
			}

			if (distanceMatrix != null && distanceMatrix.getRows() != null && !distanceMatrix.getRows().isEmpty()) {
				int size = distanceMatrix.getRows().size();
				String[][] distance = new String[size][size];
				LocalTime[][] duration = new LocalTime[size][size];
				for(int i=0;i<size;i++){
					Row row = distanceMatrix.getRows().get(i);
					if(row != null && row.getElements() != null && !row.getElements().isEmpty()){
						List<Element> elements = row.getElements();
						for(int j=0;j<size;j++){
							Element element = elements.get(j);
							
							if(element.getDistance() != null && element.getDistance().getText() != null){
								distance[i][j] = element.getDistance().getText();
							}
							if(element.getDuration() != null && element.getDuration().getText() != null){
								String timeTaken = element.getDuration().getText();
								String times[] = timeTaken.split(" ");
								LocalTime time = null;
								if(times.length == 2){
									time = new LocalTime(0, Integer.parseInt(times[0]));
								}else{
									time = new LocalTime(Integer.parseInt(times[0]), Integer.parseInt(times[2]));
								}
								duration[i][j] = time;
							}
						}
					}
				}
				response.setDistanceMatrix(distance);
				response.setDurationMatrix(duration);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return response;
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
