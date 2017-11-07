package com.trippal.places.planner;

import java.util.ArrayList;
import java.util.List;

public class DayPlannerIter2 {

	int[] weightageMatrix;

	public DayPlannerIter2() {
		weightageMatrix = new int[10];
		weightageMatrix[0] = 4000;
		weightageMatrix[1] = 3920;
		weightageMatrix[2] = 3760;
		weightageMatrix[3] = 3520;
		weightageMatrix[4] = 3300;
		weightageMatrix[5] = 2900;
		weightageMatrix[6] = 2420;
		weightageMatrix[7] = 1860;
		weightageMatrix[8] = 1220;
		weightageMatrix[9] = 500;
	}

	public List<Route> identifySuitablePathBasedOnWeightage(List<Route> routes) {
		int maxWeight = 0;
		List<Route> maxWeightRoutes = new ArrayList<>();

		for (Route route : routes) {
			int curWeight = 0;
			for (Place place : route.getRoute()) {
				int position = place.getRank();
				curWeight += weightageMatrix[position-1];
			}
			if (curWeight > maxWeight) {
				maxWeight = curWeight;
				maxWeightRoutes.clear();
				maxWeightRoutes.add(route);
			} else if (curWeight == maxWeight) {
				maxWeightRoutes.add(route);
			}
		}

		return maxWeightRoutes;
	}

}
