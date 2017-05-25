package com.trippal.places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayPlannerIter1 {

	Input[][] inputMatrix;
	int size;
	List<Places> places;

	/**
	 * 
	 * @param size
	 * @param places {startPlace,p1,p2,p3,p4,p5,p6,p7}
	 */
	public DayPlannerIter1(int size, List<Places> places) {
		this.size = size;
		this.places = places;
		inputMatrix = new Input[size][size];
	}

	/**
	 * populate the size*size matrix with dst and time
	 */
	public void calculateDistanceAndKM() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				inputMatrix[i][j] = createInput(i, j);
			}
		}
	}

	private Input createInput(int i, int j) {
		Places place1 = places.get(i);
		Places place2 = places.get(j);
		int timeTakenToTravel = calculateTime(place1, place2);
		int dstToTravel = calculateDst(place1, place2);
		Input obj = new Input();
		obj.setTime(timeTakenToTravel);
		obj.setDistances(dstToTravel);
		obj.setFromPlace(place1);
		obj.setToPlace(place2);
		return obj;
	}

	/**
	 * returns Distance to travel from place1 to place2 in kms
	 * 
	 * @param place1
	 * @param place2
	 * @return
	 */
	private int calculateDst(Places place1, Places place2) {
		// TODO as of now returning 60 ,need to factor-in the actual dst by
		// calling distance API
		return 60;
	}

	/**
	 * returns Distance to travel from place1 to place2 in minutes
	 * 
	 * @param place1
	 * @param place2
	 * @return
	 */
	private int calculateTime(Places place1, Places place2) {
		// TODO as of now returning 60 ,need to factor-in the actual time by
		// calling distance API
		return 60;
	}

	/**
	 * calculate paths from the matrix calculated
	 * 
	 * @return
	 */
	public Map<Integer, List<Input>> calculatePath() {

		Map<Integer, List<Input>> totalDstToPathsMap = new HashMap<Integer, List<Input>>();
		List<Input[]> routeList = CalculatePossiblePathCombos.generate(inputMatrix);
		
		for(Input[] route : routeList) {
			
			//if path combo is valid then add to map
			if(idValidCombo(route)) {
				int totalKms = getTotalKmsFromCombo(route);
				totalDstToPathsMap.put(totalKms, Arrays.asList(route));
			}			
		}
		return totalDstToPathsMap;
	}

	/**
	 * get total kms in covering the route
	 * @param combo
	 * @return
	 */
	private int getTotalKmsFromCombo(Input[] route) {
		int totalKms = 0;
		for(Input iter : route) {
			totalKms += iter.getDistances();
		}
		return totalKms;
	}

	/**
	 * check whether this route is a valid route
	 * @param route
	 * @return
	 */
	private boolean idValidCombo(Input[] route) {
		Places fromPalce = null;
		Places toPalce = null;
		List<Places> coveredPlaces = new ArrayList<>();
		for (Input iter :route) {
			fromPalce = iter.getFromPlace();
			toPalce = iter.getToPlace(); 
			
			//adding from place for the first time only
			if(coveredPlaces.isEmpty()) {
				coveredPlaces.add(fromPalce);
			}
			
			//this places is already added so not a valid combo path
			if(coveredPlaces.contains(toPalce)) {
				return false;
			}
			
			coveredPlaces.add(toPalce);
			
			
		}
		return true;
	}
}
