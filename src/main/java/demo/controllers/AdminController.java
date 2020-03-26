package demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	
	/*--------------------- GET APIS ------------------- */
	
	
	/*--------------------- POST APIS ------------------- */

	
	
	/*--------------------- DELETE APIS ------------------- */
	
	@RequestMapping(path = "/acs/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteMessage () {
		
		deleteAllActions();
	}

	private void deleteAllActions() {

		// TODO Auto-generated method stub
		
	}


}
