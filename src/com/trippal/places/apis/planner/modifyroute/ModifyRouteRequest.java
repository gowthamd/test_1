package com.trippal.places.apis.planner.modifyroute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trippal.places.planner.Place;

@XmlRootElement
public class ModifyRouteRequest {
	
	@XmlElement(name="added-places")
	List<Place> addedPlaces;
	@XmlElement(name="removed-place-ids")
	List<String> removedPlaceIds;
	@XmlElement(name="retained-places")
	List<Place> retainedPlaces;
	@XmlElement(name="re-run-algo")
	Boolean reRunAlgo;
	@XmlElement(name="all-places")
	List<Place> allPlaceList;
	@XmlElement(name="destination")
	String destination;
	
	public List<Place> getAddedPlaces() {
		return addedPlaces;
	}
	public List<String> getRemovedPlaceIds() {
		return removedPlaceIds;
	}
	public List<Place> getRetainedPlaces() {
		return retainedPlaces;
	}
	public boolean needRerunAlgo() {
		return Boolean.TRUE.equals(reRunAlgo);
	}
	public List<Place> getAllPlaceList() {
		return allPlaceList;
	}
	public String getDestination() {
		return destination;
	}

}
