package com.trippal.places.apis.planner;

import javax.xml.bind.annotation.XmlElement;

public class Location {

	@XmlElement(name="lat")
	String latitude;
	@XmlElement(name="lng")
	String longtitude;
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
}
