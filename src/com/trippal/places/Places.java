package com.trippal.places;

public class Places {
	

	Integer rank;
	TimeSlot openedTime;
	TimeSlot bestTime;
	Location location;
	private String googleId;

	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public TimeSlot getOpenedTime() {
		return openedTime;
	}
	public int getOpenedTime(int weekday){
		if(null == openedTime.getTime(weekday)){
			return 0;
		}
		return openedTime.getTime(weekday).getOpenTime();
	}
	public int getClosedTime(int weekday){
		if(null == openedTime.getTime(weekday)){
			return 2359;
		}
		return openedTime.getTime(weekday).getCloseTime();
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
	public void setGoogleId(String googleId) {
		this.googleId = googleId;		
	}
	public String getGoogleId() {
		return googleId;
	}
	

}
