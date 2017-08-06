package com.trippal.places.apis.planner.modifyroute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trippal.places.planner.Place;

@XmlRootElement
public class UpdateTimeToSpentRequest {

	@XmlElement(name="selected-places")
	List<Place> selectedPlaces;
	
	@XmlElement(name="destination")
	String destination;
	
	@XmlElement(name="all-places")
	List<Place> allPlaceList;
	
	@XmlElement(name="removed-place-ids")
	List<String> removedPlacesList;

	public List<Place> getSelectedPlaces() {
		return selectedPlaces;
	}

	public String getDestination() {
		return destination;
	}

	public List<Place> getAllPlaceList() {
		return allPlaceList;
	}

	public List<String> getRemovedPlacesList() {
		return removedPlacesList;
	}
		
}
