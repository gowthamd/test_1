package com.trippal.places;

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
	public Time getTime(int weekday) {
		return timeMap.get(weekday);
	}
	
	public void setTime(int weekday, int open, int close) {
		timeMap.put(weekday, new Time(open, close));
	}
	
	public void setDefaultTime(){
		timeMap.put(6, new Time(0,2359));
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
