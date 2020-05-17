package acs.logic;

import java.util.List;

import acs.boundaries.ActionBoundary;

public interface EnhancedActionService extends ActionService {

	List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page);
	
}
