package com.trippal;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.trippal.utils.TPUtil;

@Path("/autocomplete")
public class DestinationAPIRest {
	/**
	 * get autocomplete place result for given str
	 * 
	 * @param placeStr
	 * @return
	 * @throws Exception
	 */

	@GET
	@Path("/places")
	public String getAutoCompletePlaces(@QueryParam(value = "str") String placeStr) throws Exception {
		JsonObject jsonObject = TPUtil.getAutoCompletePlaces(placeStr);
		return jsonObject.toString();
	}

}
