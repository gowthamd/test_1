package com.trippal.utils;

import java.io.StringReader;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {
	Client client;

	public RestClient() {
		client = ClientBuilder.newClient();
	}

	public JsonObject get(String uri, Map<String, Object> queryParams) throws Exception {
		uri = buildQueryParam(uri, queryParams);
		WebTarget target = client.target(uri);
		Response response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
		return jsonFromString(response.readEntity(String.class));
	}
	
	public JsonArray getAsArray(String uri, Map<String, Object> queryParams) throws Exception {
		uri = buildQueryParam(uri, queryParams);
		WebTarget target = client.target(uri);
		Response response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
		return jsonArrayFromString(response.readEntity(String.class));
	}
	

	/**
	 * build query params
	 * 
	 * @param target
	 * @param queryParams
	 */
	private String buildQueryParam(String uri, Map<String, Object> queryParams) {
		if (queryParams == null || queryParams.isEmpty()) {
			return uri;
		}
		int paramCount = 0;
		for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
			paramCount++;
			if (paramCount == 1) {
				uri += "?" + entry.getKey() + "=" + entry.getValue();
			} else {
				uri += "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		return uri;
	}

	/**
	 * 
	 * converts jsonStr to jsonObj
	 * 
	 * @param jsonObjectStr
	 * @return
	 */
	private JsonObject jsonFromString(String jsonObjectStr) {
		JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();
		return object;
	}
	
	/**
	 * 
	 * converts jsonStr to jsonObj
	 * 
	 * @param jsonObjectStr
	 * @return
	 */
	private JsonArray jsonArrayFromString(String jsonObjectStr) {
		JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
		JsonArray object = jsonReader.readArray();
		jsonReader.close();
		return object;
	}

}
