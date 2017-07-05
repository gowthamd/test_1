package com.trippal.places;

import java.util.List;
import java.util.Map;

public class DayPlanner {

	public List<Input> planItenary(List<Places> places) {

		// sort place based on ranking
		int placeCount = places.size();
	/*	Collections.sort(places, new Comparator<Places>() {
			@Override
			public int compare(Places o1, Places o2) {
				return o1.getRank() - o2.getRank();
			}
		});*/
		
		DayPlannerIter1 iter1 = new DayPlannerIter1(placeCount, places);
		iter1.calculateDistanceAndKM();
		Map<Integer, List<Input>> totalDstToPathsMap = iter1.calculatePath();
		
		
		DayPlannerIter2 iter2 = new DayPlannerIter2();
		Integer identifiedPathTotalDst = iter2.identifySuitablePathBasedOnWeightage(totalDstToPathsMap);
		List<Input> identifiedPath = totalDstToPathsMap.get(identifiedPathTotalDst);
		return identifiedPath;
	}

}
