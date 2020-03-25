package demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElementController {
		//Retreive Specific Parking
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary getParking(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
				@PathVariable("elementDomain") String elementDomain,@PathVariable("elementId") String elementId) {
			
			if(UserLogin.isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				System.out.println("elementDomain = " + elementDomain);
				System.out.println("elementId = " + elementId);
		
				return new ElementBoundary(new ElementId(userDomain, "id"),"type","name",
						true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId(userDomain,userEmail))
						,new ElementAttributes(true));
				
			} 
			
			//User is not logged in.
			return null;
							
		}
		
		
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[] getAllParking(@PathVariable("userDomain") String userDomain,
				@PathVariable("userEmail") String userEmail) {
			
			if(UserLogin.isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				
				return getAllParkingsFromDB().toArray(new ElementBoundary[0]);
				
			} 
			
			//User is not logged in.
			return null;
							
		}

		private List<ElementBoundary> getAllParkingsFromDB() {
			List<ElementBoundary> list=new ArrayList<>();
			list.add(new ElementBoundary(new ElementId("userDomain", "1"),"type","name",
					true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail"))
					,new ElementAttributes(true)));
			list.add(new ElementBoundary(new ElementId("userDomain", "2"),"type","name",
					true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail"))
					,new ElementAttributes(true)));
			list.add(new ElementBoundary(new ElementId("userDomain", "3"),"type","name",
					true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail"))
					,new ElementAttributes(true)));

			return list;
		}

}