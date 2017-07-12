package com.trippal.places.apis.distance.service.domain;

import org.joda.time.LocalTime;

public class DistanceResponse {
	
	private String[][] distanceMatrix;
	private String distance;
	private String duration;

	private LocalTime[][] durationMatrix;

	public String[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public void setDistanceMatrix(String[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	public LocalTime[][] getDurationMatrix() {
		return durationMatrix;
	}

	public void setDurationMatrix(LocalTime[][] durationMatrix) {
		this.durationMatrix = durationMatrix;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance() {
		return distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
}
