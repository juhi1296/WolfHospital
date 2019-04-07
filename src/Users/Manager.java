package Users;

import java.sql.Connection;
import java.util.Scanner;
import java.sql.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Manager {

	static Scanner sc = new Scanner(System.in);
	
	public static void managerMenu(Connection conn, String person_id) {
		// TODO Auto-generated method stub
		try {
			System.out.println("----------------------------Welcome Manager----------------------------");
			System.out.println("1. View Patients");
			System.out.println("2. View Staff");
			System.out.println("3. Add Patient");
			System.out.println("4. Add Staff");
			System.out.println("5. Update Patient");
			System.out.println("6. Update Staff");
			System.out.println("7. Remove Patient");
			System.out.println("8. Remove Staff");
			System.out.println("9. Get Ward Usage");
			System.out.println("10. View Patient Statistics");
			System.out.println("11. Update Ward Information");
			System.out.println("12. Delete Ward Information");
			System.out.println("13. Doctor responsible all patients");
			System.out.println("14. Logout");
			System.out.println("Enter your choice :-> ");
			
			int manager_choice = sc.nextInt();
			
			switch(manager_choice) {
			case 1: 
				viewPatients(conn,person_id);
				break;
			
			case 2:
				viewStaff(conn,person_id);
				break;
			
			case 3:
				addPatient(conn,person_id);
				break;
				
			default:
				System.out.println("Enter Valid choice");
				managerMenu(conn,person_id);
				break;	
			}
			
			
		}
		catch(Exception e) {
			System.out.print(e);
		}
		
	}
	
	public static void viewPatients(Connection conn, String person_id) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER,PROCESSING_TREATMENT_PLAN,STATUS FROM PATIENT");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println("PID : " + rs.getInt("PID"));
				System.out.println("NAME : " + rs.getString("NAME"));
				System.out.println("SSN : " + rs.getString("SSN"));
				System.out.println("DOB : " + rs.getDate("DOB"));
				System.out.println("PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("AGE : " + rs.getInt("AGE"));
				System.out.println("GENDER : " + rs.getString("GENDER"));
				System.out.println("PROCESSING TREATMENT PLAN : " + rs.getInt("PROCESSING_TREATMENT_PLAN"));
				System.out.println("STATUS : " + rs.getString("STATUS"));
				System.out.println("\n");
				
			}
			
			System.out.println("Press 0 to go back");
			int choice = sc.nextInt();
			if (choice == 0) {
				managerMenu(conn, person_id);
			}
			else
			{
				validChoice(0);
				managerMenu(conn, person_id);
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void viewStaff(Connection conn, String person_id) {
		try {
			System.out.println("Enter Role :-> ");
			String role = sc.next();
			PreparedStatement stmt = conn.prepareStatement("SELECT SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF WHERE JOB_TITLE=?");
			
			stmt.setString(1, role);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println("SID : " + rs.getInt("SID"));
				System.out.println("NAME : " + rs.getString("NAME"));
				System.out.println("AGE : " + rs.getInt("AGE"));
				System.out.println("GENDER : " + rs.getString("GENDER"));
				System.out.println("JOB_TITLE : " + rs.getString("JOB_TITLE"));
				System.out.println("PROFESSIONAL TITLE : " + rs.getString("PROFESSIONAL_TITLE"));
				System.out.println("PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("DEPARTMENT : " + rs.getString("DEPARTMENT"));
				System.out.println("\n");
				
			}
			
			System.out.println("Press 0 to go back");
			int choice = sc.nextInt();
			if (choice == 0) {
				managerMenu(conn, person_id);
			}
			else
			{
				validChoice(0);
				managerMenu(conn, person_id);
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void addPatient(Connection conn, String person_id) throws ParseException, SQLException, InterruptedException {
		try {
			System.out.println("Enter Patient's ID :--> ");
			int pid = sc.nextInt();
			System.out.println("Enter Patient's Name :--> ");
			String name = sc.next();
			System.out.println("Enter Patient's SSN :--> ");
			String ssn = sc.next();
			System.out.println("Enter Patient's DOB :--> ");
			String dob = sc.next();
			System.out.println("Enter Patient's Phone Number :--> ");
			String phone_number = sc.next();
			System.out.println("Enter Patient's Address :--> ");
			sc.nextLine();
			String address = sc.nextLine();
			System.out.println("Enter Patient's Age :--> ");
			int age = sc.nextInt();
			System.out.println("Enter Patient's Gender :--> ");
			String gender = sc.next();
			System.out.println("Enter Patient's Processing Treatment Plan :--> ");
			int processing_treatment_plan = sc.nextInt();
			System.out.println("Enter Patient's Status :--> ");
			String status = sc.next();
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient (pid,name,ssn,dob,phone_number,address,age,gender,processing_treatment_plan,status) values(?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, pid);
			stmt.setString(2, name);
			stmt.setString(3, ssn);
			stmt.setString(4, dob);
			stmt.setString(5, phone_number);
			stmt.setString(6, address);
			stmt.setInt(7, age);
			stmt.setString(8, gender);
			stmt.setInt(9, processing_treatment_plan);
			stmt.setString(10, status);
			stmt.executeUpdate();
			
			System.out.println("Patient added successfully");
			managerMenu(conn, person_id);
		}catch(Exception e) {
			System.out.println(e);
			managerMenu(conn, person_id);
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
