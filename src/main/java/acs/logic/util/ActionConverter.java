package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.action.ActionId;
import acs.action.InvokedBy;
import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
import acs.util.Element;
import acs.util.ElementId;
import acs.util.UserId;
@Component
public class ActionConverter extends Converter{
	
	public ActionBoundary fromEntity(ActionEntity entity) {
		ActionBoundary rv = new ActionBoundary();
		rv.setActionAttributes(entity.getActionAttributes());
		rv.setActionId(convertToActionId(entity.getActionId()));
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setElement(convertToElementId(entity.getElement()));
		rv.setInvokedBy(new InvokedBy(convertToUserId(entity.getInvokedBy())));
		rv.setType(entity.getType());
		return rv;
	}

	public ActionEntity toEntity(ActionBoundary boundary) {
		ActionEntity rv = new ActionEntity();
		rv.setActionAttributes(boundary.getActionAttributes());
		rv.setActionId(convertToEntityId(boundary.getActionId().getDomain(), boundary.getActionId().getId()));
		rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
		rv.setElement(convertToEntityId(boundary.getElement().getElementId().getDomain(),boundary.getElement().getElementId().getId()));
		rv.setInvokedBy(convertToEntityId(boundary.getInvokedBy().getUserId().getDomain(),boundary.getInvokedBy().getUserId().getEmail()));
		rv.setType(boundary.getType());
		return rv;
	}
	
	public ActionId convertToActionId(String actionId) {
		String actionIdSplit[] = actionId.split(DELIMITER);
		return new ActionId(actionIdSplit[0], actionIdSplit[1]);
	}
	
	public Element convertToElementId(String elementId) {
		String elementIdSplit[] = elementId.split(DELIMITER);
		return new Element(new ElementId(elementIdSplit[0], elementIdSplit[1]));
	}
		
}
