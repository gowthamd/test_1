package com.trippal.places.planner.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.trippal.places.apis.distance.service.DistanceFinderAPI;
import com.trippal.places.apis.distance.service.domain.DistanceResponse;
import com.trippal.places.apis.distance.service.domain.exceptions.APIQuotaExceededException;
import com.trippal.places.planner.domain.Place;
import com.trippal.places.planner.domain.Route;

public class DayPlannerIter1 {

	/**
	 * 
	 * NxN matrix contains time taken from ith position to jth position
	 * 
	 */
	LocalTime[][] timeMatrix;
	Place startPlace;
	Place endPlace;
	List<Place> places;
	// have all possible combination routes made from places list
	List<Route> routes = new ArrayList<Route>();
	DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
	DistanceResponse distanceMatrix;

	/**
	 * 
	 * @param startPlace
	 * @param places
	 *            {p1,p2,p3,p4,p5,p6,p7}
	 * @throws Exception
	 */
	public DayPlannerIter1(Place startPlace, Place endPlace, List<Place> places) throws Exception {
		this.startPlace = startPlace;
		this.endPlace = endPlace;
		this.places = places;
		timeMatrix = new LocalTime[places.size()][places.size()];
		try {
			populateDistanceMatrix(places);
		} catch (APIQuotaExceededException ex) {
			throw ex;
		}
		generateValidRoutes(places, new ArrayList<Integer>(), new Route(startPlace, endPlace), routes, true);
	}

	private void populateDistanceMatrix(List<Place> places) throws Exception {
		String placesUri = startPlace.getLocation().getLatitude() + "," + startPlace.getLocation().getLongtitude()
				+ "%7C";
		for (Place place : places) {
			placesUri += place.getLocation().getLatitude() + "," + place.getLocation().getLongtitude() + "%7C";
		}
		placesUri += endPlace.getLocation().getLatitude()+","+endPlace.getLocation().getLongtitude()+"%7C";
		DistanceFinderAPI finderAPI = new DistanceFinderAPI();
		try {
			distanceMatrix = finderAPI.calculateDistanceMatrix(placesUri, placesUri, "kms");
		} catch (APIQuotaExceededException ex) {
			throw ex;
		}
		timeMatrix = distanceMatrix.getDurationMatrix();
	}

	/**
	 * 
	 * generates all possible valid routes from given places list
	 * 
	 * @param places
	 * @param visitedPositions
	 * @param route
	 * @param routes
	 * @param firstPass
	 */
	public void generateValidRoutes(List<Place> places, List<Integer> visitedPositions, Route route, List<Route> routes,
			boolean firstPass) {
		if (visitedPositions.size() == places.size()) {
			return;
		}
		for (int i = 0; i < places.size(); i++) {
			List<Place> prevPlace = new ArrayList<>();
			if (firstPass) {
				visitedPositions = new ArrayList<Integer>();
			} else if (visitedPositions.size() != 0) {
				for (int j = 0; j <= visitedPositions.size() - 1; j++) {
					prevPlace.add(route.getRoute().get(j));
				}
			}
			if (!visitedPositions.contains(i)) {
				route = new Route(startPlace, endPlace);
				route.setRoute(prevPlace);
				route.addPlace(places.get(i));
				if (isValidRoute(route)) {
					routes.add(route);
					visitedPositions.add(i);
					generateValidRoutes(places, visitedPositions, route, routes, false);
					visitedPositions.remove(visitedPositions.size() - 1);
				}
			}
		}
	}

	public List<Route> getValidRoutes() {
		return routes;
	}

	private boolean isValidRoute(Route route) {
		// startTIme 9.00 am and endTime is 6.00pm
		LocalTime startTime = formatter.parseLocalTime("9:00");
		// for all route from position will be from startPosition
		int fromPosition = 0;
		for (Place place : route.getRoute()) {
			int toPosition = place.getRank();
			LocalTime travelTime = timeMatrix[fromPosition][toPosition];
			fromPosition = toPosition;
			// if timeMatrix doesn't have travel time then setting travel time
			// to one hour
			if (travelTime == null) {
				travelTime = formatter.parseLocalTime("1:00");
			}
			route.updateTimeTaken(travelTime);

			startTime = startTime.plusHours(travelTime.getHourOfDay()).plusMinutes(travelTime.getMinuteOfHour());
			// if this route goes beyond 6.00 pm then not a valid route
			if (startTime.getHourOfDay() > 18) {
				return false;
			}
			startTime = startTime.plusHours(place.getTimeToSpent().getHourOfDay())
					.plusMinutes(place.getTimeToSpent().getMinuteOfHour());
			// if this route goes beyond 6.00 pm then not a valid route
			if (startTime.getHourOfDay() > 18) {
				return false;
			}
		}
		LocalTime travelTime = timeMatrix[fromPosition][places.size()+1];
		route.updateTimeTaken(travelTime);
		startTime = startTime.plusHours(travelTime.getHourOfDay()).plusMinutes(travelTime.getMinuteOfHour());
		// if this route goes beyond 8.00 pm then not a valid route
		if (startTime.getHourOfDay() > 20) {
			return false;
		}
		return true;
	}
}
