package com.trippal.places.planner.domain;

import java.util.HashMap;
import java.util.Map;

public class TimeSlot {

	Map<Integer, Time> timeMap = new HashMap<Integer, Time>();
	boolean isAlwaysOpen = false;
	int hoursToSpend;
	public int getHoursToSpend() {
		return hoursToSpend;
	}
	public void setHoursToSpend(int hoursToSpend) {
		this.hoursToSpend = hoursToSpend;
	}
	public int getOpeningHour(int weekday) {
		Time time = timeMap.get(weekday);
		if(null == time){
			return -1;
		}
		return time.getOpenTime();
	}
	public int getClosingHour(int weekday) {
		Time time = timeMap.get(weekday);
		if(null == time){
			return -1;
		}
		return time.getCloseTime();
	}
	
	public void setTime(int weekday, int open, int close) {
		timeMap.put(weekday, new Time(open, close));
	}

	class Time{
		int openTime;
		int closeTime;
		public Time(int openTime, int closeTime){
			this.openTime = openTime;
			this.closeTime = closeTime;
		}
		public int getOpenTime() {
			return openTime;
		}
		public int getCloseTime() {
			return closeTime;
		}
	}

	public void setAlwaysOpen(boolean isAlwaysOpen) {
		this.isAlwaysOpen = isAlwaysOpen;
	}
	public boolean isAlwaysOpen(){
		return this.isAlwaysOpen;
	}
}
