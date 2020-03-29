package demo.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import demo.boundaries.ElementBoundary;
import demo.boundaries.UserBoundary;
import demo.element.CreatedBy;
import demo.element.ElementAttributes;
import demo.element.ElementId;
import demo.element.Location;
import demo.element.NewUserDetails;
import demo.element.UserId;

public class Database {
	
	
	public static  void deleteAllActions() {

		// TODO Auto-generated method stub
		
	}
	
	public static  void deleteAllElements() {

		// TODO Auto-generated method stub
		
	}
	
	public static String generateUniqueId() {
		// TODO Auto-generated method stub - need to be completed in future.
		return "1";
	}
	
	public static void saveElement(ElementBoundary input) {
		// TODO Auto-generated method stub - need to be completed in future.
	}
	
	public static boolean checkElement(String elementDomain, String elementId) {
		// TODO Auto-generated method stub - need to be completed in future.
		return true;
	}
	
	public static void updateElement(ElementBoundary input) {
		// TODO Auto-generated method stub - need to be completed in future.
	}
	
	public static List<ElementBoundary> getAllElements() {
		List<ElementBoundary> list=new ArrayList<>();
		
		list.add(new ElementBoundary(new ElementId("userDomain", "1"),"type","name",
				true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail")),new Location(40.730610,-73.935242)
				,new ElementAttributes(true)));
		list.add(new ElementBoundary(new ElementId("userDomain", "2"),"type","name",
				true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail")),new Location(40.730610,-73.935242)
				,new ElementAttributes(true)));
		list.add(new ElementBoundary(new ElementId("userDomain", "3"),"type","name",
				true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId("userDomain","userEmail")),new Location(40.730610,-73.935242)
				,new ElementAttributes(true)));

		return list;
	}
	
	public static List<UserBoundary> getAllUsers(){
		List<UserBoundary> list=new ArrayList<>();
		list.add(new UserBoundary(new UserId("Hod","1@1.com"),"role", "userName","avatar"));
		list.add(new UserBoundary(new UserId("Sarel","2@2.com"),"role", "userName","avatar"));
		list.add(new UserBoundary(new UserId("Mor","3@3.com"),"role", "userName","avatar"));

		
		return list;
	}
	
	public static UserBoundary createUser(NewUserDetails input) {
		return new UserBoundary(new UserId("2020b.lior_trachtman",input.getEmail()), input.getRole()
				, input.getUserName(), input.getAvatar());
	}
	
	
	
	public static void updateUserDetails(UserBoundary userBoundary) {
		
	}

	public static void deleteAllUsers() {
		
		// TODO Auto-generated method stub
		
	}

}
