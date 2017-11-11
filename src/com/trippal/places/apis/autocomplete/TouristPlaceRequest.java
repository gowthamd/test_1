package com.trippal.places.apis.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trippal.places.planner.domain.Place;

public class TouristPlaceRequest {
	@SerializedName("startPlace")
    @Expose
	private Place startPlace;
	
	@SerializedName("endPlace")
    @Expose
	private Place endPlace;

	public Place getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(Place startPlace) {
		this.startPlace = startPlace;
	}

	public Place getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(Place endPlace) {
		this.endPlace = endPlace;
	}

}
