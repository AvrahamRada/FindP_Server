package acs.data;

import java.util.Date;
import java.util.Map;

import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.Location;

public class ElementEntity {
	
	private ElementId elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String,Object> elementAttributes;
	
	public ElementEntity() {
		// TODO Auto-generated constructor stub
	}

	public ElementEntity(ElementId elementId, String type, String name, Boolean active, Date createdTimestamp,
			CreatedBy createdBy, Location location,Map<String,Object> elementAttributes) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
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

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
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

	public Map<String,Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(Map<String,Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	
	
}
