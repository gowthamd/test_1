package com.trippal.utils;

import java.util.concurrent.CountDownLatch;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.trippal.places.Place;
import com.trippal.places.TimeSlot;

public class PlaceDetailsPopulator implements Runnable{
	
	private Place place;
	private CountDownLatch cl;

	public PlaceDetailsPopulator(CountDownLatch cl, Place place){
		this.place = place;
		this.cl = cl;
	}

	@Override
	public void run() {
		try {
			JsonObject placeObj = TPUtil.getPlaceDetailsById(place.getGoogleId());
			TimeSlot timeSlot = getOpenedTime(placeObj.getJsonObject("opening_hours"));
			if(timeSlot == null){
				place.setDefaultTime();
			}else{
				if(timeSlot.isAlwaysOpen()){
					place.setAlwaysOpen(true);					
				}else{
					for(int i=0;i<7;i++){
						place.setOpeningHour(i, timeSlot.getOpeningHour(i));
						place.setClosingHour(i, timeSlot.getClosingHour(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cl.countDown();
		
	}
	

	
	private static TimeSlot getOpenedTime(JsonObject jsonObject) {
		TimeSlot timeSlot = new TimeSlot();
		if(null == jsonObject || jsonObject.getJsonArray("periods") == null){
			return null;
		}
		JsonArray timings = jsonObject.getJsonArray("periods");
		for(int i=0;i<timings.size();i++){
			JsonObject time = timings.getJsonObject(i);
			JsonObject openTime = time.getJsonObject("open");
			JsonObject closeTime = time.getJsonObject("close");
			if(null == closeTime){
				timeSlot.setAlwaysOpen(true);
				continue;
			}
			timeSlot.setTime(openTime.getInt("day"), Integer.parseInt(openTime.getString("time")), 
					Integer.parseInt(time.getJsonObject("close").getString("time")));
		}		
		
		return timeSlot;
	}
	
	

}
