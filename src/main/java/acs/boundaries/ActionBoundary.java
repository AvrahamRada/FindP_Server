package acs.boundaries;

import java.util.Date;
import java.util.Map;

import acs.action.ActionAttributes;
import acs.action.ActionId;
import acs.action.InvokedBy;
import acs.util.Element;

public class ActionBoundary {
	
	private ActionId actionId;
	private String type;
	private Element element;
	private Date createdTimestamp;
	private InvokedBy invokedBy;
	private Map<String,Object> actionAttributes;
	
	public ActionBoundary() {
		// TODO Auto-generated constructor stub
	}
	
	public ActionBoundary(ActionId actionId, String type, Element element, Date createdTimestamp, InvokedBy invokedBy,
			Map<String,Object> actionAttributes) {
		super();
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public ActionId getActionId() {
		return actionId;
	}

	public void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String,Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String,Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}	
}
