package com.trippal.places.apis.planner.modifyroute;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.trippal.utils.TPUtil;

@Path("/modifyroute")
public class ModifyRouteAPI {
	
	@PUT
	@Path("/modify")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	public String getModifiedRoute(ModifyRouteRequest modifyRouteRequest) throws Exception {
		JsonObject jsonObject = TPUtil.getModifiedRoute(modifyRouteRequest);
		return jsonObject.toString();
	}
	
	@PUT
	@Path("/nextday")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	public String getNextDaySuggestedRoute(AddDayToRouteRequest addDayToRouteRequest) throws Exception {
		JsonObject jsonObject = TPUtil.getNextdayRoute(addDayToRouteRequest);
		return jsonObject.toString();
	}

}
