package com.trippal.places;

/**
 * 
 * Object to hold place information needed for Trip-Pal
 *
 */
public class Place {
	Integer rank;
	String rating;
	Location location;
	private String name;
	private String googlePlaceId;

	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGooglePlaceId() {
		return googlePlaceId;
	}
	public void setGooglePlaceId(String googlePlaceId) {
		this.googlePlaceId = googlePlaceId;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setGoogleId(String googleId) {
		this.googlePlaceId = googleId;		
	}
	public String getGoogleId() {
		return googlePlaceId;
	}
}
