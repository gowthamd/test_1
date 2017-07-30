package com.trippal.places.apis.planner;



public class Input {
	int time;//in minutes fromPlace to toPlace
	int distances;//in kms fromPlace to toPlace
	
	Place fromPlace;
	Place toPlace;
	public Place getFromPlace() {
		return fromPlace;
	}
	public void setFromPlace(Place fromPlace) {
		this.fromPlace = fromPlace;
	}
	public Place getToPlace() {
		return toPlace;
	}
	public void setToPlace(Place toPlace) {
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
