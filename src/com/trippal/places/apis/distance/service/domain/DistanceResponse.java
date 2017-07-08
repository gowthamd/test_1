package com.trippal.places.apis.distance.service.domain;

import org.joda.time.LocalTime;

public class DistanceResponse {
	
	private String[][] distance;

	private LocalTime[][] duration;

	public String[][] getDistance() {
		return distance;
	}

	public void setDistance(String[][] distance) {
		this.distance = distance;
	}

	public LocalTime[][] getDuration() {
		return duration;
	}

	public void setDuration(LocalTime[][] duration) {
		this.duration = duration;
	}
	
}
