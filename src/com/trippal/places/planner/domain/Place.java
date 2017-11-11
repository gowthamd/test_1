package com.trippal.places.planner.domain;

import javax.xml.bind.annotation.XmlElement;

import org.joda.time.LocalTime;

/**
 * 
 * Object to hold place information needed for Trip-Pal
 *
 */
public class Place {
	@XmlElement(name="rank")
	Integer rank;
	@XmlElement(name="rating")
	Double rating;
	@XmlElement(name="location")
	Location location;
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="googleId")
	private String googlePlaceId;
	@XmlElement(name="time-to-spent")
	private LocalTime timeToSpent;
	private LocalTime[][] timeSlots = new LocalTime[7][2];
	private boolean isAlwaysOpen = false;
	private String photoRef;

	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public void setOpeningHour(int weekday, int time){
		if(weekday <= 6 && weekday >=0 && time >= 0){
			LocalTime localTime = new LocalTime(time/100, time%100);
			timeSlots[weekday][0] = localTime;
		}
	}
	public LocalTime getOpeningHour(int weekday){
		if(weekday <= 6 && weekday >=0){
			return timeSlots[weekday][0];
		}
		return null;
	}
	
	public void setClosingHour(int weekday, int time){
		if(weekday <= 6 && weekday >=0 && time >= 0){
			LocalTime localTime = new LocalTime(time/100, time%100);
			timeSlots[weekday][1] = localTime;
		}
	}
	public LocalTime getClosingHour(int weekday){
		if(weekday <= 6 && weekday >=0){
			return timeSlots[weekday][1];
		}
		return null;
	}
	
	public boolean isAlwaysOpen(){
		return this.isAlwaysOpen;
	}
	
	public void setAlwaysOpen(boolean isAlwaysOpen){
		this.isAlwaysOpen = isAlwaysOpen;
	}
	
	public void setDefaultTime() {
		for(int i=0;i<7;i++){
			timeSlots[i][0] = new LocalTime(9,0);
			timeSlots[i][1] = new LocalTime(18,0);
		}
		
	}
	public LocalTime getTimeToSpent() {
		return timeToSpent;
	}
	public void setTimeToSpent(LocalTime timeToSpent) {
		this.timeToSpent = timeToSpent;
	}
	public void setPhotoRef(String photoRef) {
		this.photoRef = photoRef;
	}
	
	public String getPhotoRef(){
		return this.photoRef;
	}
	
	
}
