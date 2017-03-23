package com.trippal.auth;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class JWTAuthFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authHeaderVal = requestContext.getHeaderString("Authorization");
		if(authHeaderVal == null) {
			// commenting this code until UI changes are done
			//requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			return;
		}
		if(authHeaderVal.startsWith("Basic")) {
			// logging in using username and password
			return;
		}
		if (null != authHeaderVal && authHeaderVal.startsWith("Bearer")) {
			try {
				String token = authHeaderVal.substring("Bearer".length()).trim();
				AuthHelper authHelper = new AuthHelper();
				authHelper.verifyToken(token);
			} catch (Exception ex) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} else {
			System.out.println(authHeaderVal);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}
}