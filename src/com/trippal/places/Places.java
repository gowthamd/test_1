package com.trippal.places;

public class Places {
	

	Integer rank;

	TimeSlot openedTime;
	TimeSlot bestTime;
	Location location;
	
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public TimeSlot getOpenedTime() {
		return openedTime;
	}
	public void setOpenedTime(TimeSlot openedTime) {
		this.openedTime = openedTime;
	}
	public TimeSlot getBestTime() {
		return bestTime;
	}
	public void setBestTime(TimeSlot bestTime) {
		this.bestTime = bestTime;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

}
