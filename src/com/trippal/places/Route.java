package com.trippal.places;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

public class Route {
	
	Place startPlace;

	/**
	 *  list of place to convered by this route
	 *  places are in order of visit
	 */
	private List<Place> route;
	
	private List<LocalTime> timeTakenToReachNextPlace = new ArrayList<LocalTime>();;
	
	public Route(Place startPlace) {
		this.startPlace = startPlace;
		route = new ArrayList<Place>();
	}
	
	public List<Place> getRoute() {
		return route;
	}

	public void setRoute(List<Place> route) {
		this.route = route;
	}
	
	public void addPlace(Place place) {
		route.add(place);
	}
	
	public void updateTimeTaken(LocalTime timeTaken) {
		timeTakenToReachNextPlace.add(timeTaken);
	}
	
	public String getTimeTaken(int position) {
		return timeTakenToReachNextPlace.get(position).toString();
	}

}
