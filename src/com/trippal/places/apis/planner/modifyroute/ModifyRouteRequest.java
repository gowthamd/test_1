package com.trippal.places.apis.planner.modifyroute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trippal.places.planner.Place;

@XmlRootElement
public class ModifyRouteRequest {
	
	@XmlElement(name="added-places")
	List<Place> addedPlaces;
	@XmlElement(name="removed-places")
	List<Place> removedPlaces;
	@XmlElement(name="retained-places")
	List<Place> retainedPlaces;
	public List<Place> getAddedPlaces() {
		return addedPlaces;
	}
	public List<Place> getRemovedPlaces() {
		return removedPlaces;
	}
	public List<Place> getRetainedPlaces() {
		return retainedPlaces;
	}
	

}
