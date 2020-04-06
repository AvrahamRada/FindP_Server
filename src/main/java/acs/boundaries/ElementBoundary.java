package acs.boundaries;

import java.util.Date;

import javax.management.RuntimeErrorException;

import acs.util.CreatedBy;
import acs.util.ElementAttributes;
import acs.util.ElementId;
import acs.util.Location;

public class ElementBoundary {

	private ElementId elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimeStamp;
	private CreatedBy createdBy;
	private Location location;
	private ElementAttributes elementAttributes;

	public ElementBoundary() {
		// TODO Auto-generated constructor stub
	}

	public ElementBoundary(ElementId elementId, String type, String name, Boolean active, Date createdTimeStamp,
			CreatedBy createdBy, Location location, ElementAttributes elementAttributes) {
		
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimeStamp = createdTimeStamp;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttributes = elementAttributes;
	}

	public ElementId getElementId() {
		return elementId;
	}

	public void setElementId(ElementId elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ElementAttributes getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(ElementAttributes elementAttributes) {
		this.elementAttributes = elementAttributes;
	}

	public void validation() {

		elementId.validation();
		createdBy.validation();
		location.validation();
		elementAttributes.validation();

		if (type == null) {
			throw new RuntimeException("type was not instantiate");
		}

		if (name == null) {
			throw new RuntimeException("name was not instantiate");
		}

		if (createdTimeStamp == null) {
			throw new RuntimeException("createdTimeStamp was not instantiate");
		}
	}
}