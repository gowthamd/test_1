package com.trippal.places.apis.distance.service.domain;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Row {

    @Override
	public String toString() {
		return "[" + elements + "]";
	}

	@SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
