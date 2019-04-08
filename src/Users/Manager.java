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
	
	public static void managerMenu(Connection conn, int person_id) {
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
				
			case 4:
				addStaff(conn,person_id);
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
	
	public static void viewPatients(Connection conn, int person_id) {
		try {
			
			System.out.println("1. View all patients.");
			System.out.println("2. View a particular patient.");
			System.out.println("Enter your choice :-> ");

			int ch=sc.nextInt();
			
			if(ch == 1) {
				PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER,PROCESSING_TREATMENT_PLAN,COMPLETING_TREATMENT FROM PATIENT");
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
					System.out.println("COMPLETING TREATMENT : " + rs.getString("COMPLETING_TREATMENT"));
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
			}
			else if(ch == 2) {
				System.out.println("Enter the patient ID :-> ");
				  int pid=sc.nextInt();
				  
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
						managerMenu(conn, person_id);
					}
					else
					{
						validChoice(0);
						managerMenu(conn, person_id);
					}
			}
			
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void viewStaff(Connection conn, int person_id) {
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
	
	public static void addPatient(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		try {
			
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
			System.out.println("Enter Patient's Completing Treatment :--> ");
			String completing_treatment = sc.next();
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient (name,ssn,dob,phone_number,address,age,gender,processing_treatment_plan,completing_treatment) values(?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, name);
			stmt.setString(2, ssn);
			stmt.setString(3, dob);
			stmt.setString(4, phone_number);
			stmt.setString(5, address);
			stmt.setInt(6, age);
			stmt.setString(7, gender);
			stmt.setInt(8, processing_treatment_plan);
			stmt.setString(9, completing_treatment);
			stmt.executeUpdate();
			
			System.out.println("Patient added successfully");
			managerMenu(conn, person_id);
		}catch(Exception e) {
			System.out.println(e);
			managerMenu(conn, person_id);
		}
	}
	
	public static void addStaff(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		try {
			
			System.out.println("Enter Staff's Name :--> ");
			String name = sc.next();
			System.out.println("Enter Staff's Age :--> ");
			int age = sc.nextInt();
			System.out.println("Enter Staff's Gender :--> ");
			String gender = sc.next();
			System.out.println("Enter Staff's Job Title :--> ");
			String job_title = sc.next();
			System.out.println("Enter Staff's Professional Title :--> ");
			String professional_title = sc.next();
			System.out.println("Enter Staff's Phone Number :--> ");
			String phone_number = sc.next();
			System.out.println("Enter Staff's Address :--> ");
			sc.nextLine();
			String address = sc.nextLine();
			System.out.println("Enter Staff's Department :--> ");
			String department = sc.next();
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO staff (name,age,gender,job_title,professional_title,phone_number,address,department) values(?,?,?,?,?,?,?,?)");
			stmt.setString(1, name);
			stmt.setInt(2, age);
			stmt.setString(3, gender);
			stmt.setString(4, job_title);
			stmt.setString(5, professional_title);
			stmt.setString(6, phone_number);
			stmt.setString(7, address);
			stmt.setString(8, department);
			
			stmt.executeUpdate();
			
			System.out.println("Staff added successfully");
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
