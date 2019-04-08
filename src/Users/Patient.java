package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class Patient {

	static Scanner sc = new Scanner(System.in);
	
	public void patientMenu(Connection conn, int pid) throws InterruptedException, SQLException {
		// TODO Auto-generated method stub
		try {
			System.out.println("----------------------------Welcome Patient----------------------------");
			System.out.println("1. View profile");
			System.out.println("2. View Test Results");
			System.out.println("3. View Bills");
			System.out.println("4. View Medical Records");		
			System.out.println("5. Logout");
			System.out.println("Enter your choice :-> ");
			
			int patient_choice=sc.nextInt();
			
			switch(patient_choice)
			{
				case 1: 
					viewProfile(conn,pid);
					break;
			
				
				default:
					System.out.println("Enter Valid choice");
					patientMenu(conn,pid);
					break;
			}
		}
		catch(Exception e) {
			System.out.print(e);
		}
		
	}
	
	public void viewProfile(Connection conn, int pid)
	{
		try {
		PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER,PROCESSING_TREATMENT_PLAN,COMPLETING_TREATMENT FROM PATIENT WHERE PID=?");
		
		  stmt.setInt(1, pid);
		  ResultSet rs = stmt.executeQuery();
		  if(rs.next()) {
				System.out.println("PID : " + rs.getInt("PID"));
				System.out.println("NAME : " + rs.getString("NAME"));
				System.out.println("SSN : " + rs.getString("SSN"));
				System.out.println("DOB : " + rs.getDate("DOB"));
				System.out.println("PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("AGE : " + rs.getInt("AGE"));
				System.out.println("GENDER : " + rs.getString("GENDER"));
				System.out.println("PROCESSING TREATMENT PLAN : " + rs.getInt("PROCESSING_TREATMENT_PLAN"));
				System.out.println("COMPLETING TREATMENT : " + rs.getString("COMPLETING_TREATMENT"));
				System.out.println("\n");
			}
		
			System.out.println("Press 0 to go back");
			int choice = sc.nextInt();
			if (choice == 0) {
				patientMenu(conn, pid);
			}
			else
			{
				validChoice(0);
				patientMenu(conn, pid);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void validChoice(int choice)
	{
		System.out.println("Enter Valid Choice");
		int c = sc.nextInt();
		if(c!=choice)
		{
			validChoice(choice);
		}
		else
		{
			return;
		}
	
	}
	
}
