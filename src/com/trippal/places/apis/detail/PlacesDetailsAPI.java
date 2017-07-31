package com.trippal.places.apis.detail;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.trippal.constants.TPConstants;
import com.trippal.utils.RestClient;
import com.trippal.utils.TPUtil;


@Path("/place")
public class PlacesDetailsAPI {
	
	/**
	 * get place details result for given placeid
	 * 
	 * @param placeStr
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/details")
	public String getPlaceDetail(@QueryParam(value = "id") String placeId) throws Exception {
		JsonObject jsonObject = getPlaceDetails(placeId);
		return jsonObject.toString();
	}
	
	private JsonObject getPlaceDetails(String place_id) throws Exception {
		String uri = TPConstants.GOOGLE_PLACE_DETAILS_API;
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("key", TPUtil.getGoogleAPIKey());
		queryParams.put("placeid", place_id);
		RestClient restClient = new RestClient();
		JsonObject googleResponse = restClient.get(uri, queryParams);
		JsonObjectBuilder finalResBuilder = Json.createObjectBuilder();
		copyGoogleRes(googleResponse,finalResBuilder);
		return finalResBuilder.build();
	}

	private void copyGoogleRes(JsonObject googleResponse, JsonObjectBuilder finalResBuilder) {
		finalResBuilder.add("location", googleResponse.getJsonObject("result").getJsonObject("geometry").getJsonObject("location"));
		finalResBuilder.add("id", googleResponse.getJsonObject("result").get("place_id"));
		finalResBuilder.add("name", googleResponse.getJsonObject("result").get("name"));
	}
}
