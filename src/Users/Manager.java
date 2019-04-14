package Users;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.sql.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Manager {

	static Scanner sc = new Scanner(System.in);
	
	public static void managerMenu(Connection conn, int person_id) {
		try {
			System.out.println("----------------------------Welcome Manager----------------------------");
			System.out.println("1. View Patients"); 
			System.out.println("2. View Staff");
			System.out.println("3. Add Staff"); 
			System.out.println("4. Update Patient"); 
			System.out.println("5. Update Staff"); 
			System.out.println("6. Remove Staff");
			System.out.println("7. Get Ward Usage"); 
			System.out.println("8. View Patient Statistics"); 
			System.out.println("9. Add Ward Information");
			System.out.println("10. Update Ward Information");
			System.out.println("11. Delete Ward Information");
			System.out.println("12. Add New Bed to ward");
			System.out.println("13. Delete Bed Information");
			System.out.println("14. Doctor responsible for patients");
			System.out.println("15. Logout");
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
				addStaff(conn,person_id);
				break;
				
			case 4:
				editPatient(conn,person_id);
				break;
				
			case 5:
				editStaff(conn,person_id);
				break;
				
			case 6:
				deleteStaff(conn,person_id);
				break;
				
			case 7:
				wardUsage(conn,person_id);
				break;
				
			case 8:
				patientsPerMonth(conn,person_id);
				break;
				
			case 9:
				addWard(conn,person_id);
				break;
				
			case 10: 
				updateWard(conn,person_id);
				break;
				
			case 11:
				deleteWard(conn,person_id);
				break;
				
			case 12:
				addBed(conn,person_id);
				break;
				
			case 13:
				deleteBed(conn,person_id);
				break;
				
			case 14:
				responsibleDoctor(conn,person_id);
				break;
				
			case 15:
				System.out.println("Loggin out..");
				TimeUnit.SECONDS.sleep(3);
				System.exit(0);
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
			System.out.println("Press 0 to go back");
			System.out.println("Enter your choice :-> ");
		

			int ch=sc.nextInt();
			
			if (ch == 0) {
				managerMenu(conn, person_id);
			}
			
			else if(ch == 1) {
				PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER FROM PATIENT");
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
					
					System.out.println("\n");
					
				}
				
				System.out.println("Press 0 to go back \n");
				int choice = sc.nextInt();
				
				if(choice == 0) {
					viewPatients(conn,person_id);
				}else {
					validChoice(0);
					managerMenu(conn, person_id);
				}
			}
			else if(ch == 2) {
				System.out.println("Enter the patient ID :-> ");
				  int pid=sc.nextInt();
				  
				  PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER FROM PATIENT WHERE PID=?");
				
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
						
						System.out.println("\n");
					}else {
						System.out.println("Enter a Valid PID \n");
						viewPatients(conn,person_id);	
					}
				
					System.out.println("Press 0 to go back");
					int choice = sc.nextInt();
					if (choice == 0) {
						viewPatients(conn, person_id);
					}
					else
					{
						validChoice(0);
						managerMenu(conn, person_id);
					}
			}else {
				System.out.println("Enter a Valid Choice \n");
				viewPatients(conn,person_id);
			}
			
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void viewStaff(Connection conn, int person_id) {
		try {
			String role = "";
			System.out.println("Select an option for Role :-> ");
			System.out.println("1: Doctor, 2: Nurse, 3: Operator");
			System.out.println("Press 0 to go back");
			
			int choice = sc.nextInt();
			if (choice == 0) {
				managerMenu(conn, person_id);
			}
			
			PreparedStatement stmt = null;
			switch(choice) {
			case 1: 
				 role = "Doctor";
				 stmt = conn.prepareStatement("SELECT staff.SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF,DOCTOR WHERE DOCTOR.SID = staff.SID");
				 break;
			case 2:
				 role = "Nurse";
				 stmt = conn.prepareStatement("SELECT staff.SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF,NURSE WHERE NURSE.SID = staff.SID");
				 break;
			case 3:
				 role = "Operator";
				 stmt = conn.prepareStatement("SELECT staff.SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF,OPERATOR WHERE OPERATOR.SID = staff.SID");
				 break;
			default:
				System.out.println("Enter a Valid Choice ");
				viewStaff(conn,person_id);
				break;
			}
			
			
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
			int ch = sc.nextInt();
			if (ch == 0) {
				viewStaff(conn, person_id);
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
	
	public static int addPatient(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		int pid1 = 0;
		try {
			
			System.out.println("Enter Patient's Name :--> ");
			String name = sc.next();
			System.out.println("Enter Patient's SSN (###-##-####):--> ");
			String ssn = sc.next();
			System.out.println("Enter Patient's DOB (YYYY-MM-DD) :--> ");
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
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient (name,ssn,dob,phone_number,address,age,gender) values(?,?,?,?,?,?,?)");
			stmt.setString(1, name);
			stmt.setString(2, ssn);
			stmt.setString(3, dob);
			stmt.setString(4, phone_number);
			stmt.setString(5, address);
			stmt.setInt(6, age);
			stmt.setString(7, gender);
			
			stmt.executeUpdate();
			
			String get_max_PID = "SELECT MAX(PID) AS PID FROM PATIENT ;" ;
			PreparedStatement stmt2 = conn.prepareStatement(get_max_PID);
			ResultSet rs1 = stmt2.executeQuery();
			  
			rs1.next();
			
			PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO registrations (sid,pid) values(?,?)");
			stmt1.setInt(1, person_id);
			stmt1.setInt(2, rs1.getInt("PID"));
			stmt1.executeUpdate();
			
			pid1 = rs1.getInt("PID");
			
		}catch(Exception e) {
			
			System.out.println(e);
			System.out.println("Something went wrong, please try again \n");
			addPatient(conn,person_id);
		}
		
		return pid1 ;
	}
	
	public static void addStaff(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		try {
			
			conn.setAutoCommit(false);
			String r = null ;
			String role = null;
			System.out.println("Select an option for Role :-> ");
			System.out.println("1: Doctor, 2: Nurse, 3: Operator");
			System.out.println("Press 0 to go back");
			
			int choice = sc.nextInt();
			if (choice == 0) {
				managerMenu(conn, person_id);
			}
			
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
			
			String get_max_SID = "SELECT MAX(SID) AS SID FROM STAFF ;" ;
			PreparedStatement stmt2 = conn.prepareStatement(get_max_SID);
			ResultSet rs1 = stmt2.executeQuery();
			  
			rs1.next();
			int sid = rs1.getInt("SID");
			
			PreparedStatement stmt1 = null;
			switch(choice) {
			case 1: 
				 role = "Doctor";
				 stmt1 = conn.prepareStatement("INSERT INTO doctor (SID) VALUES (?)");
				 r = "D";
				 break;
			case 2:
				 role = "Nurse";
				 stmt1 = conn.prepareStatement("INSERT INTO nurse (SID) VALUES (?);");
				 r = "N";
				 break;
			case 3:
				 role = "Operator";
				 stmt1 = conn.prepareStatement("INSERT INTO operator (SID) VALUES (?);");
				 r = "O";
				 break;
			default:
				System.out.println("Enter a Valid Choice ");
				viewStaff(conn,person_id);
				break;
			}
			
			stmt1.setInt(1,sid);
			stmt1.execute();
			
			PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO USER_LOGIN values (?,'root',?,?)");
			stmt3.setString(1, name);
			stmt3.setInt(2,sid);
			stmt3.setString(3, r);
			stmt3.execute();
			
			conn.commit();
			conn.setAutoCommit(true);
			System.out.println("Staff added successfully");
			
			managerMenu(conn, person_id);
			
		}catch(Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			System.out.println(e);
			System.out.println("Something went wrong, please try again \n");
			managerMenu(conn, person_id);
			
		}
	}
	
	public static void editPatient(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Edit Patient's profile--------------------");
			System.out.println("Enter the patient ID :-> ");
			int pid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT PID,NAME,SSN,DOB,PHONE_NUMBER,ADDRESS,AGE,GENDER FROM PATIENT WHERE PID=?");
			stmt.setInt(1, pid);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("PID : " + rs.getInt("PID"));
				System.out.println("1. NAME : " + rs.getString("NAME"));
				System.out.println("2. SSN : " + rs.getString("SSN"));
				System.out.println("3. DOB : " + rs.getDate("DOB"));
				System.out.println("4. PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("5. ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("6. AGE : " + rs.getInt("AGE"));
				System.out.println("7. GENDER : " + rs.getString("GENDER"));
				
				System.out.println("\n");
			}
			
			System.out.println("Enter your selection to edit(Press 0 to go back): -->");
			int choice = sc.nextInt();
			
			switch(choice) {
				case 0: 
					managerMenu(conn,person_id);
					break;
				
				case 1: //Edit name
					System.out.println("Enter new Name: --> ");
					String name = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET NAME = ? WHERE PID=?");
					stmt.setString(1, name);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's name edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 2: //Edit SSN
					System.out.println("Enter new SSN: --> ");
					String ssn = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET SSN = ? WHERE PID=?");
					stmt.setString(1, ssn);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's SSN edited successfully");
					managerMenu(conn, person_id);
					break;	
					
				case 3: //Edit DOB
					System.out.println("Enter new DOB: --> ");
					String dob = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET DOB = ? WHERE PID=?");
					stmt.setString(1, dob);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's DOB edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 4: //Edit Phone Number
					System.out.println("Enter new Phone number: --> ");
					String phone_number = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET PHONE_NUMBER = ? WHERE PID=?");
					stmt.setString(1, phone_number);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's Phone Number edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 5: //Edit Address
					System.out.println("Enter new Address: --> ");
					sc.nextLine();
					String address = sc.nextLine();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET ADDRESS = ? WHERE PID=?");
					stmt.setString(1, address);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's Address edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 6: //Edit Age
					System.out.println("Enter new age: --> ");
					int age = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET AGE = ? WHERE PID=?");
					stmt.setInt(1, age);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's name edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 7: //Edit Gender
					System.out.println("Enter new Gender: --> ");
					String gender = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE PATIENT SET GENDER = ? WHERE PID=?");
					stmt.setString(1, gender);
					stmt.setInt(2, pid);
					stmt.executeUpdate();
					System.out.println("Patient's gender edited successfully");
					managerMenu(conn, person_id);
					break;
						
				default:
					System.out.println("Enter a Valid Choice ");
					editPatient(conn,person_id);
					break;
			}
			
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void editStaff(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Edit Staff's profile--------------------");
			System.out.println("Enter the staff ID :-> ");
			int sid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT SID,NAME,AGE,GENDER,JOB_TITLE,PROFESSIONAL_TITLE,PHONE_NUMBER,ADDRESS,DEPARTMENT FROM STAFF WHERE SID=?");
			stmt.setInt(1, sid);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("SID : " + rs.getInt("SID"));
				System.out.println("NAME : " + rs.getString("NAME"));
				System.out.println("JOB TITLE : " + rs.getString("JOB_TITLE"));
				System.out.println("1. AGE : " + rs.getInt("AGE"));
				System.out.println("2. GENDER : " + rs.getString("GENDER"));
				
				System.out.println("3. PROFESSIONAL TITLE : " + rs.getString("PROFESSIONAL_TITLE"));
				System.out.println("4. PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("5. ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("6. DEPARTMENT : " + rs.getString("DEPARTMENT"));
				System.out.println("\n");
			}
			
			System.out.println("Enter your selection to edit(Press 0 to go back): -->");
			int choice = sc.nextInt();
			
			switch(choice) {
				case 0: 
					managerMenu(conn,person_id);
					break;
				
					
				case 1: //Edit Age
					System.out.println("Enter new Age: --> ");
					int age = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET AGE = ? WHERE SID=?");
					stmt.setInt(1, age);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Staff's age edited successfully");
					managerMenu(conn, person_id);
					break;	
					
				case 2: //Edit Gender
					System.out.println("Enter new Gender: --> ");
					String gender = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET GENDER = ? WHERE SID=?");
					stmt.setString(1, gender);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Staff's Gender edited successfully");
					managerMenu(conn, person_id);
					break;
					
					
				case 3: //Edit Professional Title
					System.out.println("Enter new Professional Title: --> ");
					String professional_title = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET PROFESSIONAL_TITLE = ? WHERE SID=?");
					stmt.setString(1, professional_title);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Patient's Professional Title edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 4: //Edit Phone Number
					System.out.println("Enter new phone number: --> ");
					String phone_number = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET PHONE_NUMBER = ? WHERE SID=?");
					stmt.setString(1, phone_number);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Staff's Phone Number edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 5: //Edit Address
					System.out.println("Enter new Address: --> ");
					sc.nextLine();
					String address = sc.nextLine();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET ADDRESS = ? WHERE SID=?");
					stmt.setString(1, address);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Staff's address edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 6: //Edit Department
					System.out.println("Enter new Department: --> ");
					String department = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STAFF SET DEPARTMENT = ? WHERE SID=?");
					stmt.setString(1, department);
					stmt.setInt(2, sid);
					stmt.executeUpdate();
					System.out.println("Staff's Department edited successfully");
					managerMenu(conn, person_id);
					break;
				
				default:
					System.out.println("Enter a Valid Choice ");
					editStaff(conn,person_id);
					break;
			}
			
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void deletePatient(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Delete Patient's profile--------------------");
			System.out.println("Enter the patient ID :-> ");
			int pid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM PATIENT WHERE PID=?");
			stmt.setInt(1, pid);
			stmt.executeUpdate();
			System.out.println("Patient " + " "+pid+" " + "deleted succefully");
			managerMenu(conn,person_id);
			
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	public static void deleteStaff(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Delete Staff's profile--------------------");
			System.out.println("Enter the staff ID :-> ");
			int sid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM STAFF WHERE SID=?");
			stmt.setInt(1, sid);
			stmt.executeUpdate();
			System.out.println("Staff " + " "+sid+" " + "deleted succefully");
			managerMenu(conn,person_id);
			
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	public static void wardUsage(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Ward Usage Percentage--------------------");
			System.out.println("Enter the Ward ID :-> ");
			int wid = sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT ? AS WID, COUNT(*) * 100/ (SELECT COUNT(*) FROM BED WHERE WID = ?) AS PERCENTAGE_UTILIZED FROM BED WHERE AVAILABILITY = 1 AND WID = ?");
			stmt.setInt(1, wid);
			stmt.setInt(2, wid);
			stmt.setInt(3, wid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println("WID : " + rs.getInt("WID"));
				System.out.println("PERCENTAGE UTILIZATION : " + rs.getDouble("PERCENTAGE_UTILIZED"));
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
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	public static void patientsPerMonth(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Patients Per Month--------------------");
			System.out.println("Enter Year and Month in the form YYYY-MM :-> ");
			String start_date = sc.next();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS NUMBER_OF_PATIENTS FROM MEDICAL_RECORDS WHERE START_DATE LIKE ? ");
			stmt.setString(1, start_date + "%");
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				System.out.println("Number of Patients : " + rs.getInt("NUMBER_OF_PATIENTS"));
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
		}catch(Exception ex) {
			System.out.println("Exception" + ex);
		}
	}
	
	public static void addBed(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		try {
			
			System.out.println("Enter Ward ID :--> ");
			int wid = sc.nextInt();
			System.out.println("Enter Bed ID :--> ");
			int bid = sc.nextInt();
			System.out.println("Enter availability status :--> ");
			int availability = sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO bed (wid,Bed_ID,availability) values(?,?,?)");
			stmt.setInt(1, wid);
			stmt.setInt(2, bid);
			stmt.setInt(3, availability);
			stmt.executeUpdate();
			
			System.out.println("Bed added successfully");
			managerMenu(conn, person_id);
		}catch(Exception e) {
			System.out.println(e);
			managerMenu(conn, person_id);
		}
	}
	
	/*public static void updateBed(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Edit Bed's Information--------------------");
			System.out.println("Enter the Ward ID :-> ");
			int wid = sc.nextInt();
			System.out.println("Enter the Bed ID :-> ");
			int bid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT WID,BID,AVAILABILITY FROM WARD WHERE Bed_ID=? AND WID = ?");
			stmt.setInt(1, bid);
			stmt.setInt(2, wid);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				
				System.out.println("1. WID : " + rs.getInt("WID"));
				System.out.println("BID : " + rs.getInt("BID"));
				System.out.println("AVAILABILITY : " + rs.getInt("AVAILABILITY"));
				System.out.println("\n");
			}
			
			System.out.println("Enter your selection to edit(Press 0 to go back): -->");
			int choice = sc.nextInt();
			
			switch(choice) {
				case 0: 
					managerMenu(conn,person_id);
					break;
				
				case 1: //Edit Ward 
					System.out.println("Enter new Ward ID: --> ");
					int wid = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE BED SET WID = ? WHERE BID=?");
					stmt.setInt(1, wid);
					stmt.setInt(2, bid);
					stmt.executeUpdate();
					System.out.println("Ward edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 2: //Edit Ward Availability
					System.out.println("Change Availability: --> ");
					int availability = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE BED SET AVAILABILITY = ? WHERE BID=?");
					stmt.setInt(1, availability);
					stmt.setInt(2, bid);
					stmt.executeUpdate();
					System.out.println("Bed availability edited successfully");
					managerMenu(conn, person_id);
					break;	
				
				default:
					break;
			}
			
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}*/
	
	public static void deleteBed(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Delete Bed--------------------");
			System.out.println("Enter the Ward ID :-> ");
			int wid = sc.nextInt();
			System.out.println("Enter the bed ID :-> ");
			int bid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM BED WHERE Bed_ID=? AND WID=?");
			stmt.setInt(1, bid);
			stmt.setInt(2, wid);
			stmt.executeUpdate();
			System.out.println("Bed " + " "+bid+" " + "deleted succefully");
			managerMenu(conn,person_id);
			
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	public static void addWard(Connection conn, int person_id) throws ParseException, SQLException, InterruptedException {
		try {
			
			
			System.out.println("Enter ID for Responsible Nurse :--> ");
			int sid = sc.nextInt();
			System.out.println("Enter charges for ward :--> ");
			int charges = sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO ward (sid,charges) values(?,?)");
			stmt.setInt(1, sid);
			stmt.setInt(2, charges);
			stmt.executeUpdate();
			
			System.out.println("Ward added successfully");
			managerMenu(conn, person_id);
		}catch(Exception e) {
			System.out.println(e);
			managerMenu(conn, person_id);
		}
	}
	
	public static void updateWard(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Edit Ward's Information--------------------");
			System.out.println("Enter the Ward ID :-> ");
			int wid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT WID,SID,CHARGES FROM WARD WHERE WID=?");
			stmt.setInt(1, wid);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("WID : " + rs.getInt("WID"));
				System.out.println("1. SID : " + rs.getInt("SID"));
				System.out.println("2. CHARGES : " + rs.getInt("CHARGES"));
				System.out.println("\n");
			}
			
			System.out.println("Enter your selection to edit(Press 0 to go back): -->");
			int choice = sc.nextInt();
			
			switch(choice) {
				case 0: 
					managerMenu(conn,person_id);
					break;
				
				case 1: //Edit Responsible Nurse
					System.out.println("Enter new Nurse ID: --> ");
					int sid = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE WARD SET SID = ? WHERE WID=?");
					stmt.setInt(1, sid);
					stmt.setInt(2, wid);
					stmt.executeUpdate();
					System.out.println("Responsible Nurse edited successfully");
					managerMenu(conn, person_id);
					break;
					
				case 2: //Edit Ward Charges
					System.out.println("Enter new Ward Charges: --> ");
					int charges = sc.nextInt();
					stmt = conn.prepareStatement(
					"UPDATE WARD SET CHARGES = ? WHERE WID=?");
					stmt.setInt(1, charges);
					stmt.setInt(2, wid);
					stmt.executeUpdate();
					System.out.println("Ward charges edited successfully");
					managerMenu(conn, person_id);
					break;	
				
				default:
					System.out.println("Enter Valid Choice");
					updateWard(conn,person_id);
			}
			
		}
		catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	public static void deleteWard(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Delete WARD--------------------");
			System.out.println("Enter the ward ID :-> ");
			int wid=sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM WARD WHERE WID=?");
			stmt.setInt(1, wid);
			stmt.executeUpdate();
			System.out.println("Ward " + " "+wid+" " + "deleted succefully");
			managerMenu(conn,person_id);
			
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
		}
	}
	
	private static void responsibleDoctor(Connection conn, int person_id) {
		try {
			System.out.println("----------------------Patients for whom the Doctor is responsible--------------------");
			System.out.println("Enter the Doctor ID :-> ");
			int sid = sc.nextInt();
			
			PreparedStatement stmt = conn.prepareStatement("SELECT STAFF.NAME Doctor_Name, PATIENT.* FROM PATIENT,TREATS,DOCTOR,STAFF WHERE STAFF.SID = DOCTOR.SID AND DOCTOR.SID = TREATS.SID AND TREATS.PID = PATIENT.PID AND STAFF.SID = ?");
			
			stmt.setInt(1, sid);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println("Responsible Doctor : " + rs.getString("Doctor_Name"));
				System.out.println("PID : " + rs.getInt("PID"));
				System.out.println("Patient : " + rs.getString("NAME"));
				System.out.println("SSN : " + rs.getString("SSN"));
				System.out.println("DOB : " + rs.getString("DOB"));
				System.out.println("PHONE NUMBER : " + rs.getString("PHONE_NUMBER"));
				System.out.println("ADDRESS : " + rs.getString("ADDRESS"));
				System.out.println("AGE : " + rs.getInt("AGE"));
				System.out.println("GENDER : " + rs.getString("GENDER"));
				
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
		}catch(Exception ex) {
			System.out.println("Something went wrong, please try again" + ex);
			managerMenu(conn,person_id);
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
