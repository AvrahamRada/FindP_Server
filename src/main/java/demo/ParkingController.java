package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingController {
		//Retreive Specific Parking
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{parkingDomain}/{parkingId}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ParkingBoundary getParking(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
				@PathVariable("parkingDomain") String parkingDomain,@PathVariable("parkingId") String parkingId) {
			
			if(isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				System.out.println("elementDomain = " + parkingDomain);
				System.out.println("elementId = " + parkingId);
		
				return new ParkingBoundary("lat","lon","City","Street",parkingDomain, parkingId);
				
			} 
			
			//User is not logged in.
			return null;
							
		}
		
		public boolean isLoggedIn(String userDomian, String userEmail) {
			
			//In the future, we need to ad some real logic of checking if the user is logged in
			return true;
		}

}