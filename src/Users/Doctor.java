package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Doctor {

	static Scanner sc = new Scanner(System.in);
	
	public static void doctorMenu(Connection conn, int person_id) {
		// TODO Auto-generated method stub
		try {
		System.out.println("----------------------------Welcome Doctor----------------------------");
		System.out.println("1. View profile");
		System.out.println("2. Recommend Tests to Patients");
		System.out.println("3. Perform Tests");
		System.out.println("4. Update Medical Records");
		System.out.println("5. View Patients");
		System.out.println("6. View Test Results");
		System.out.println("7. Enter Medical Records");
		System.out.println("8. Generate medical history for a Patient");
		System.out.println("9. Logout");
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();
		
		switch(doctor_choice) {
		case 1: 
			viewProfile(conn,person_id);
			break;
		
	/*	case 4:
			updateMedRecord(conn,person_id);
			break;	
			
		case 5:
			viewPatient(conn,person_id);
			break;
		
		case 6:
			viewTestResult(conn,person_id);
			break;
			
		case 7:
			addMedRecord(conn,person_id);
			break;	
			
		case 8:
			generateMedHistory(conn,person_id);
			break;
			
		case 8:
			generateMedHistory(conn,person_id);
			break;
			*/
		default:
			System.out.println("Enter Valid choice");
			doctorMenu(conn,person_id);
			break;	
		}
		
	}
	catch(Exception e) {
		System.out.print(e);
	}
		
  }

  public static void viewProfile(Connection conn, int person_id)
  {
	  try {
			PreparedStatement stmt = conn.prepareStatement("SELECT SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF WHERE SID=?");
			
			stmt.setInt(1, person_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
			System.out.println("SID : " + rs.getInt("SID"));
			System.out.println("NAME : " + rs.getString("NAME"));
			System.out.println("AGE : " + rs.getString("AGE"));
			System.out.println("GENDER : " + rs.getString("GENDER"));
			System.out.println("JOB TITLE : " + rs.getString("JOB_TITLE"));
			System.out.println("PROFESSIONAL TITLE : " + rs.getString("PROFESSIONAL_TITLE"));
			System.out.println("PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
			System.out.println("ADDRESS : " + rs.getString("ADDRESS"));
			System.out.println("DEPARTMENT : " + rs.getString("DEPARTMENT"));
			System.out.println("\n");
			}
		
			System.out.println("Press 0 to go back");
			int choice = sc.nextInt();
			if (choice == 0) {
				doctorMenu(conn, person_id);
			}
			else
			{
				validChoice(0);
				doctorMenu(conn, person_id);
			}
			
		}catch(Exception e) {
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