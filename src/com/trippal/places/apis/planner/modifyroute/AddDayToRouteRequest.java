package com.trippal.places.apis.planner.modifyroute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trippal.places.planner.Place;

@XmlRootElement
public class AddDayToRouteRequest {
	
	@XmlElement(name="selected-place-ids")
	List<String> selectedPlacesList;
	
	@XmlElement(name="destination")
	String destination;
	
	@XmlElement(name="all-places")
	List<Place> allPlaceList;

	public List<String> getSelectedPlacesList() {
		return selectedPlacesList;
	}

	public String getDestination() {
		return destination;
	}

	public List<Place> getAllPlaceList() {
		return allPlaceList;
	}

}
