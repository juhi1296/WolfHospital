package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {

	static Scanner sc = new Scanner(System.in);
	
	public static void doctorMenu(Connection conn, int person_id) {
		// TODO Auto-generated method stub
		try {
		System.out.println("----------------------------Welcome Doctor----------------------------");
		System.out.println("1. View profile");
		System.out.println("2. Recommend Tests to Patients"); // adding a record in test
		System.out.println("3. Update Test Results");// adding results in test
		System.out.println("4. View Patients"); 
		System.out.println("5. View Test Results");
		System.out.println("6. Access Medical Records of a patient");
		System.out.println("7. Update Medical Records of a patient");
		System.out.println("8. Logout");
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();
		
		switch(doctor_choice) {
		case 1: 
			viewProfile(conn,person_id);
			break;
		
		case 2:
			recommendTest(conn,person_id);
			break;
			
		case 3:
			updateTestResult(conn,person_id);
			
		case 4:
			viewPatient(conn,person_id);
			break;
			
		case 5:
			viewTestResults(conn,person_id);
			break;
			
		case 6: getMedicalRecord(conn,person_id);
				break;
				
		case 7: 
			updateMedicalRecord(conn,person_id);
			
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

  private static void updateMedicalRecord(Connection conn, int person_id) {
		System.out.println("Enter Record ID :=> ");
		int rec_id = sc.nextInt();
		
		try {
			
		String sel_query = "SELECT * FROM medical_records WHERE RID = ? ";
		PreparedStatement sel_stmt = conn.prepareStatement(sel_query);
			sel_stmt.setInt(1, rec_id);
			ResultSet rs = sel_stmt.executeQuery();
			
			rs.next();
			
			String start_date = rs.getString("start_date");
			String end_date = rs.getString("end_date");
			String prescription = rs.getString("prescription");
			String diagnosis_details = rs.getString("diagnosis_details");
			String responsible_doctor = rs.getString("responsible_doctor");
			
			System.out.println("Record ID:" + rs.getInt("RID"));
			System.out.println("1. Start Date:" + start_date);
			System.out.println("2. End Date:" + end_date);
			System.out.println("3. Prescription:" + prescription);
			System.out.println("4. Diagnosis Details:" + diagnosis_details);
			System.out.println("5. Responsible Doctor:" + responsible_doctor);
			System.out.println("Enter your choice :=> ");
			int choice = sc.nextInt();
			switch(choice) {
			case 1: System.out.println("Enter start date :=> ");
					start_date = sc.next();
					break;
			case 2:	System.out.println("Enter end date :=> ");
					end_date = sc.next();
					break;
			case 3:	System.out.println("Enter prescription :=> ");
					prescription = sc.next();
					break;
			case 4: System.out.println("Enter diagnosis details :=> ");
					diagnosis_details = sc.next();
					break;
			case 5: System.out.println("Enter responsible doctor :=> ");
					responsible_doctor = sc.next();
					break;
			default:
				System.out.println("Please enter a valid choice!!");
				updateMedicalRecord(conn,person_id);
			}
			
			String update_query = "UPDATE medical_records set start_date = ?,end_date = ?,prescription = ?,diagnosis_details = ?,responsible_doctor = ? WHERE RID = ? ";
			PreparedStatement stmt = conn.prepareStatement(update_query);
			stmt.setString(1, start_date);
			stmt.setString(2, end_date);
			stmt.setString(3, prescription);
			stmt.setString(4, diagnosis_details);
			stmt.setString(5, responsible_doctor);
			stmt.setInt(6, rec_id);
			
			stmt.executeUpdate();
			System.out.println("Successfully updated the record!!");
			doctorMenu(conn,person_id);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

private static void getMedicalRecord(Connection conn, int person_id) {
		System.out.println("Enter Patient ID :=> ");
		int pid = sc.nextInt();
		
		System.out.println("1. Get active medical record");
		System.out.println("2. Get previous all medical records");
		System.out.println("Enter your choice =>");
		int ch = sc.nextInt();
		try {
		ResultSet rs = null;
		switch(ch) {
		case 1: String select_record = "SELECT * FROM medical_records WHERE PID = ? AND END_DATE IS NULL" ;
			PreparedStatement sel_stmt;
			sel_stmt = conn.prepareStatement(select_record);
			sel_stmt.setInt(1, pid);
			rs = sel_stmt.executeQuery();
			break;
		case 2:
			String select_all_record = "SELECT * FROM medical_records WHERE PID = ?" ;
			PreparedStatement sel_all_stmt;
			sel_all_stmt = conn.prepareStatement(select_all_record);
			sel_all_stmt.setInt(1, pid);
			rs = sel_all_stmt.executeQuery();
			break;
		
		default: System.out.println("Invalid Choice!!!");
				 getMedicalRecord(conn,person_id);
				 break;
		}
		 
		while(rs.next()) {
			System.out.println("Record ID:" + rs.getInt("RID"));
			System.out.println("Start Date:" + rs.getString("start_date"));
			System.out.println("End Date:" + rs.getString("end_date"));
			System.out.println("Prescription:" + rs.getString("prescription"));
			System.out.println("Diagnosis Details:" + rs.getString("diagnosis_details"));
			System.out.println("Responsible Doctor:" + rs.getString("responsible_doctor"));
			System.out.println("\n");
		}
		
		doctorMenu(conn, person_id);
		
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

private static void viewTestResults(Connection conn, int person_id) {
	  try {
	  System.out.println("Enter Test Id :=> ");
	  int test_id = sc.nextInt();
	  
	  String Sel_Test = "SELECT * FROM TEST WHERE TID = ?";
	  
		PreparedStatement sel_stmt = conn.prepareStatement(Sel_Test);
		sel_stmt.setInt(1, test_id);
		ResultSet rs = sel_stmt.executeQuery();
		
		if(rs.next()) {
			  System.out.println("Test Details :");
			  System.out.println("TID:" + rs.getInt("TID"));
			  System.out.println("name:" + rs.getString("name"));
			  System.out.println("result:" + rs.getString("result"));
			  System.out.println("Test Date:" + rs.getString("DATE_PERFORMED"));
		
	} 
		}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

private static void updateTestResult(Connection conn, int person_id) {
	  try {
	  System.out.println("Enter the Test ID :-> ");
	  int t_id = sc.nextInt();
	  
	  String select_test = "SELECT * FROM TEST WHERE TID = ?" ;
	  PreparedStatement sel_stmt = conn.prepareStatement(select_test) ;
	  sel_stmt.setInt(1, t_id);
	  ResultSet rs = sel_stmt.executeQuery();
	  if(rs.next()) {
	  int Test_id = rs.getInt("TID");
	  String name = rs.getString("name");
	  String result = rs.getString("result");
	  String date = rs.getString("DATE_PERFORMED");
	  
	  System.out.println("Test Details :");
	  System.out.println("1. TID:" + Test_id);
	  System.out.println("2. name:" + name);
	  System.out.println("3. result:" + result);
	  System.out.println("4. Test Date:" + date);
	  System.out.println("Enter/Update Test results :=> ");
	  result = sc.next();
	  
	  String update_query = "UPDATE TEST SET RESULT = ? WHERE TID = ? ";
	  PreparedStatement update_stmt = conn.prepareStatement(update_query);
	  update_stmt.setString(1, result);
	  update_stmt.setInt(2, t_id);
	  
	  if(update_stmt.execute())
		  System.out.println("Successfully updated the results");
	  
	  doctorMenu(conn, person_id);
	  }
	  } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	}

private static void recommendTest(Connection conn, int person_id) {
	  try {
		  
	  System.out.println("Enter the patient ID :-> ");
	  int pid=sc.nextInt();
	  
	  System.out.println("Enter the name of the test :-> ");
	  String t_name = sc.next();
	  
	  System.out.println("Enter the ID of the doctor performing test :-> ");
	  int r_did = sc.nextInt();
	  
	  String select_record = "SELECT RID FROM medical_records WHERE PID = ? AND END_DATE IS NULL;" ;
	  PreparedStatement sel_stmt = conn.prepareStatement(select_record);
	  sel_stmt.setInt(1, pid);
	  ResultSet rs = sel_stmt.executeQuery();
	  
	  
	  String insert_query_test = "INSERT INTO TEST(recommended_SID,performed_SID,name) VALUES(?,?,?);";
	  PreparedStatement stmt1 = conn.prepareStatement(insert_query_test);
	  stmt1.setInt(1, person_id);
	  stmt1.setInt(2, r_did);
	  stmt1.setString(3, t_name);
	  stmt1.execute();
	  
	  String get_max_TID = "SELECT MAX(TID) AS TID FROM TEST ;" ;
	  PreparedStatement stmt2 = conn.prepareStatement(get_max_TID);
	  ResultSet rs1 = stmt2.executeQuery();
	  
	  String insert_query_record_has_test = "INSERT INTO RECORD_HAS_TEST VALUES(?,?)";
	  PreparedStatement insert_stmt_record_has_test = conn.prepareStatement(insert_query_record_has_test);
	  if(rs.next())
	  insert_stmt_record_has_test.setInt(1, rs.getInt("RID"));
	  if(rs1.next())
	  insert_stmt_record_has_test.setInt(2, rs1.getInt("TID"));
	  insert_stmt_record_has_test.execute();
	  System.out.println("Successfully added test");
	  doctorMenu(conn, person_id);
	  
	  } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
  
  public static void viewPatient(Connection conn, int person_id)
  {
	  try {
		  
		  System.out.println("1. View all patients.");
		  System.out.println("2. View a particular patient.");
		  System.out.println("Enter your choice :-> ");

		  int ch=sc.nextInt();
		  
		  if(ch==1)
		  {
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
					doctorMenu(conn, person_id);
				}
				else
				{
					validChoice(0);
					doctorMenu(conn, person_id);
				}
			  
		  }
		  else if(ch==2)
		  {
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
					doctorMenu(conn, person_id);
				}
				else
				{
					validChoice(0);
					doctorMenu(conn, person_id);
				}
			  
		  }
		  else
		  {
			  System.out.println("Enter Valid choice");
				viewPatient(conn,person_id);	
		  }
		  
		  
		 
		  
	  }
	  catch(Exception e){
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