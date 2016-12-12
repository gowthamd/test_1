package com.trippal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/entry-point")
public class API 
{
	@GET
    @Path("test")
    public String test() {
        return "Test";
    }
}