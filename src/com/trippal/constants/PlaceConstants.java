package com.trippal.constants;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalTime;

public enum PlaceConstants {
	
	WILD_LIFE("zoo"),
	HERITAGE("art_gallery", "museum", "premise"),
	THEME_PARKS("amusement_park"),
	WELLNESS("hair_care", "health", "spa"),
	TEMPLE("church", "hindu_temple", "mosque"),
	OTHERS("bowling_alley", "casino", "night_club", "cafe", "synagogue",
			"aquarium", "park", "natural_feature");
	
	private final List<String> values;
	
	PlaceConstants(String... values){
		this.values = Arrays.asList(values);
	}
	
	public List<String> getValues() {
        return values;
    }
	
	public static PlaceConstants find(String name) {
	    for (PlaceConstants placeType : PlaceConstants.values()) {
	        if (placeType.getValues().contains(name)) {
	            return placeType;
	        }
	    }
	    return OTHERS;
	}
	
	public static LocalTime getTimeToSpent(PlaceConstants placeType){
		LocalTime timeToSpent;
		switch(placeType){
			case WILD_LIFE : timeToSpent = new LocalTime(3,0);
			break;
			case HERITAGE : timeToSpent = new LocalTime(1,0);
			break;
			case THEME_PARKS : timeToSpent = new LocalTime(6,0);
			break;
			case WELLNESS : timeToSpent = new LocalTime(1,0);
			break;
			case TEMPLE : timeToSpent = new LocalTime(1,0);
			break;
			default: timeToSpent = new LocalTime(2,0);
			break;
		}
		return timeToSpent;
	}
	
}