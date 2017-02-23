package com.trippal;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.trippal.utils.TPUtil;

@Path("/nearbysearch")
public class NearByPlacesAPIRest {
	
	@GET
	@Path("/places")
	public String getNearByPlaces(@QueryParam(value = "place-id") String placeId,
			@QueryParam(value = "radius") Integer radius) throws Exception {
		JsonObject jsonObject = TPUtil.getNearbyPlaces(placeId, radius);
		return jsonObject.toString();
	}

}
