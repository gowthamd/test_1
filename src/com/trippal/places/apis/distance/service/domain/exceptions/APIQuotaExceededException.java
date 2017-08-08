package com.trippal.places.apis.distance.service.domain.exceptions;

public class APIQuotaExceededException extends Exception {

	public APIQuotaExceededException(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String getMessage() {
		return "Google API key Quota Exceeded for the Project";
	}

}
