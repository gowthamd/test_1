package com.trippal.places.apis.planner.modifyroute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trippal.places.planner.Place;

@XmlRootElement
public class UpdateTimeToSpentRequest {

	@XmlElement(name="selected-places")
	List<Place> selectedPlaces;

	public List<Place> getSelectedPlaces() {
		return selectedPlaces;
	}

	public void setSelectedPlaces(List<Place> selectedPlaces) {
		this.selectedPlaces = selectedPlaces;
	}	
	
}
