package com.trippal.places.apis.planner.comparators;

import java.util.Comparator;

import com.trippal.places.planner.Place;

public class RatingComparator implements Comparator<Place> {

	@Override
	public int compare(Place o1, Place o2) {
		return (int) ((o2.getRating() * 10) - (o1.getRating() * 10));
	}

}
