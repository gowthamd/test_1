package com.trippal.places.planner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.trippal.places.apis.distance.service.domain.exceptions.APIQuotaExceededException;
import com.trippal.places.planner.domain.Place;
import com.trippal.places.planner.domain.Route;

public class DayPlanner {
	public Route planItenary(Place startPlace, List<Place> places,Place endPlace, Comparator<Place> comparator) throws Exception {

		// sort place based on ranking
		Collections.sort(places, comparator);

		// taking top 10 rating places from the places list and using for
		// calculating ideal route
		List<Place> placesForAlgo = getPlacesForRouteAlgo(places);
		try{
			DayPlannerIter1 iter1 = new DayPlannerIter1(startPlace, endPlace, placesForAlgo);
			List<Route> validRoutes = iter1.getValidRoutes();
	
			DayPlannerIter2 iter2 = new DayPlannerIter2();
			List<Route> maxWeightRoutes = iter2.identifySuitablePathBasedOnWeightage(validRoutes);
			Optional<Route> selected = maxWeightRoutes.parallelStream().min(Comparator.comparing(Route::getTotalTime));
			return selected.orElse(maxWeightRoutes.get(0));

		}catch(APIQuotaExceededException ex){
			throw ex;
		}

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
			if (rank > 8) {
				break;
			}
			place.setRank(rank++);
			placesForAlgo.add(place);
		}
		return placesForAlgo;
	}

}
