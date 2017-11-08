package com.trippal.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class TPPlaceObj implements Comparable<TPPlaceObj>{
	
	private JsonObject geometry;
	private JsonObject viewport;
	private String googleId;
	private String name;
	private JsonObject openingHours;
	private Double rating;
	private JsonValue types;
	public JsonObject getGeometry() {
		return geometry;
	}
	public void setGeometry(JsonObject geometry) {
		this.geometry = geometry;
	}
	public JsonObject getViewport() {
		return viewport;
	}
	public void setViewport(JsonObject viewport) {
		this.viewport = viewport;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JsonObject getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(JsonObject openingHours) {
		this.openingHours = openingHours;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public JsonValue getTypes() {
		return types;
	}
	public void setTypes(JsonValue jsonValue) {
		this.types = jsonValue;
	}
	public JsonObject getJsonObject() {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("location", this.getGeometry());
		jsonBuilder.add("name", this.getName());
		if(this.getRating() != null){
			jsonBuilder.add("rating", this.getRating());
		}
		jsonBuilder.add("types", this.getTypes());
		return jsonBuilder.build();
	}
	@Override
	public int compareTo(TPPlaceObj tpPlaceObj) {		
		return tpPlaceObj.getRating().compareTo(this.getRating());
	}
	
}