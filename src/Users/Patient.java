package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class Patient {

	static Scanner sc = new Scanner(System.in);
	
	public void patientMenu(Connection conn, String person_id) throws InterruptedException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("----------------------------Welcome Patient----------------------------");
		System.out.println("1. View profile");
		System.out.println("2. View Test Results");
		System.out.println("3. View Bills");
		System.out.println("4. View Medical Records");		
		System.out.println("5. Logout");
		System.out.println("Enter your choice :-> ");
	}
	
}
