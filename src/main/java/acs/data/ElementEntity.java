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
	private Map<String, Object> elementAttributes;

	public ElementEntity() {

		this.location = new Location();

	}

	public ElementEntity(ElementId elementId, String type, String name, Boolean active, Date createdTimestamp,
			CreatedBy createdBy, Location location, Map<String, Object> elementAttributes) {
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
		if (type != null) {
			this.type = type;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		if (active != null) {
			this.active = active;
		}
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

		if (location != null) {
			this.location.setLat(location.getLat());
			this.location.setLng(location.getLng());
		}
	}

	public Map<String, Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(Map<String, Object> elementAttributes) {

		if (elementAttributes != null) {
			for (Map.Entry<String, Object> entry : elementAttributes.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

//				if (key == null || value == null) {
//					elementAttributes.remove(key);
//				}
			}
			this.elementAttributes = elementAttributes;
		}
	}

}
