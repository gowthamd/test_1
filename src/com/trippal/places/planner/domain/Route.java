package com.trippal.places.planner.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

public class Route {
	
	Place startPlace;
	
	Place endPlace;
	
	private LocalTime totalTravelTime = new LocalTime(0, 0);

	/**
	 *  list of place to covered by this route
	 *  places are in order of visit
	 */
	private List<Place> route;
	
	private List<LocalTime> timeTakenToReachNextPlace = new ArrayList<LocalTime>();;
	
	public Route(Place startPlace, Place endPlace) {
		this.startPlace = startPlace;
		this.endPlace = endPlace;
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
		totalTravelTime = totalTravelTime.plusHours(timeTaken.getHourOfDay()).plusMinutes(timeTaken.getMinuteOfHour());;
	}
	
	public String getTimeTaken(int position) {
		return timeTakenToReachNextPlace.get(position).toString();
	}

	public LocalTime getTotalTime(){
		return totalTravelTime;
	}

}
