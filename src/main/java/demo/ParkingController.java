package demo;

import java.util.ArrayList;
import java.util.List;

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
			
			if(UserLogin.isLoggedIn(userDomain,userEmail)) {
				
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
		
		
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public List<ParkingBoundary> getAllParking(@PathVariable("userDomain") String userDomain,
				@PathVariable("userEmail") String userEmail) {
			
			if(UserLogin.isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				
				return getAllParkingsFromDB();
				
			} 
			
			//User is not logged in.
			return null;
							
		}

		private List<ParkingBoundary> getAllParkingsFromDB() {
			List<ParkingBoundary> list=new ArrayList<>();
			list.add(new ParkingBoundary("5","5","city","street","parkingDomain","9999"));
			list.add(new ParkingBoundary("5","5","city","street","parkingDomain","123"));
			list.add(new ParkingBoundary("5","5","city","street","parkingDomain","56856"));

			return list;
		}

}