package com.trippal.auth;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.jetty.util.security.Password;

import com.trippal.constants.TPConstants;
import com.trippal.utils.RestClient;
import com.trippal.utils.TPUtil;

@Path("/auth")
public class UserAuthenticationRest {
	@GET
	@Path("/login")
	public String login(@Context HttpServletRequest req) throws Exception {
		String authorization = req.getHeader("Authorization");
		if (authorization == null) {
			System.out.println("authorization is null.cannot login -");
			return "User authentication failed";
		}

		String credentials = authorization.substring("Basic".length()).trim();
		byte[] decoded = DatatypeConverter.parseBase64Binary(credentials);
		String decodedString = new String(decoded);
		String[] actualCredentials = decodedString.split(":");
		String userName = actualCredentials[0];
		String userPassword = actualCredentials[1];

		if (userName == null) {
			System.out.println("user name is null.cannot login -");
			return "User Name is empty";
		}

		boolean hasUserAccount = false;
		if (userPassword != null) {
			boolean isValidUser = validateUserCredentials(userName, userPassword);
			if (!isValidUser) {
				return "User login failed";
			}
			hasUserAccount = isValidUser;
		}

		if (userName.startsWith("trippalguest") || hasUserAccount) {
			AuthHelper authHelper = new AuthHelper();
			return authHelper.createJWTToken(userName);
		}
		return "Not a valid user";
	}

	private boolean validateUserCredentials(String userName, String password) throws Exception {
		RestClient restClient = new RestClient();
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("apiKey", getmLabAPIKey());
		JsonArray userResponse = restClient.getAsArray(TPConstants.MLAB_GET_ALL_USER, queryParams);
		for (int i = 0; i < userResponse.size(); i++) {
			JsonObject userObj = userResponse.getJsonObject(i);
			String dbUserName = userObj.getString("username");
			String dbUserPassword = userObj.getString("password");
			if (dbUserName.equals(userName)) {
				return true;
			}
		}
		return false;
	}

	private String getmLabAPIKey() {
		return "nyyAF4eC7rzEsIvNFdZVnuPNGuMmyIBz";
	}

}
