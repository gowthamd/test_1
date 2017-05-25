package com.trippal.places;

import java.sql.Time;

public class TimeSlot {

	Time openTime;
	Time closeTime;
	int hoursToSpend;
	public int getHoursToSpend() {
		return hoursToSpend;
	}
	public void setHoursToSpend(int hoursToSpend) {
		this.hoursToSpend = hoursToSpend;
	}
	public Time getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Time openTime) {
		this.openTime = openTime;
	}
	public Time getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Time closeTime) {
		this.closeTime = closeTime;
	}
}
