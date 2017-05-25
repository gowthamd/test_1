package com.trippal.places;

public class Input {
	int time;//in minutes fromPlace to toPlace
	int distances;//in kms fromPlace to toPlace
	
	Places fromPlace;
	Places toPlace;
	public Places getFromPlace() {
		return fromPlace;
	}
	public void setFromPlace(Places fromPlace) {
		this.fromPlace = fromPlace;
	}
	public Places getToPlace() {
		return toPlace;
	}
	public void setToPlace(Places toPlace) {
		this.toPlace = toPlace;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getDistances() {
		return distances;
	}
	public void setDistances(int distances) {
		this.distances = distances;
	}

}
