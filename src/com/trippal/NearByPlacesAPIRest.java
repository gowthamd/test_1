package com.trippal;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.trippal.utils.TPUtil;

@Path("/nearbysearch")
public class NearByPlacesAPIRest {
	
	@GET
	@Path("/rating")
	public String getNearByPlacesByRating(@QueryParam(value = "place-id") String placeId,
			@QueryParam(value = "radius") Integer radius) throws Exception {
		JsonObject jsonObject = TPUtil.getNearbyPlacesByRating(placeId, radius);
		return jsonObject.toString();
	}
	
	@GET
	@Path("/prominence")
	public String getNearByPlacesByProminence(@QueryParam(value = "place-id") String placeId,
			@QueryParam(value = "radius") Integer radius) throws Exception {
		JsonObject jsonObject = TPUtil.getNearbyPlacesByRating(placeId, radius);
		return jsonObject.toString();
	}

}