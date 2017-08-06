package com.trippal.places.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayPlanner {
	public Route planItenary(Place startPlace, List<Place> places, Comparator<Place> comparator) throws Exception {

		// sort place based on ranking
		Collections.sort(places, comparator);

		// taking top 10 rating places from the places list and using for
		// calculating ideal route
		List<Place> placesForAlgo = getPlacesForRouteAlgo(places);

		DayPlannerIter1 iter1 = new DayPlannerIter1(startPlace, placesForAlgo);
		List<Route> validRoutes = iter1.getValidRoutes();

		DayPlannerIter2 iter2 = new DayPlannerIter2();
		List<Route> maxWeightRoutes = iter2.identifySuitablePathBasedOnWeightage(validRoutes);
		return maxWeightRoutes.get(0);

	}

	/**
	 * take top ten places from the place list and add rank for the selected
	 * places
	 * 
	 * @param places
	 * @return
	 */
	private List<Place> getPlacesForRouteAlgo(List<Place> places) {
		List<Place> placesForAlgo = new ArrayList<>();
		int rank = 1;
		for (Place place : places) {
			if (rank > 10) {
				break;
			}
			place.setRank(rank++);
			placesForAlgo.add(place);
		}
		return placesForAlgo;
	}

}
