package com.trippal.places;

import java.util.List;

public class DayPlanner {

	public Route planItenary(Place startPlace, List<Place> places) throws Exception {

		// sort place based on ranking
		/*LIST ALREADY SORTED
		Collections.sort(places, new Comparator<Place>() {
			@Override
			public int compare(Place o1, Place o2) {
				return o1.getRank() - o2.getRank();
			}
		});*/

		DayPlannerIter1 iter1 = new DayPlannerIter1(startPlace, places);
		List<Route> validRoutes = iter1.getValidRoutes();

		DayPlannerIter2 iter2 = new DayPlannerIter2();
		List<Route> maxWeightRoutes = iter2.identifySuitablePathBasedOnWeightage(validRoutes);
		return maxWeightRoutes.get(0);
	}

}
