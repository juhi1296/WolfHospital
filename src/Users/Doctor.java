package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Doctor {

	static Scanner sc = new Scanner(System.in);
	
	public static void doctorMenu(Connection conn, int person_id) {
		// TODO Auto-generated method stub
		// If a doctor logs in successfully, he/she can perform following functions:
		try {
		System.out.println("----------------------------Welcome Doctor----------------------------");
		System.out.println("1. View profile"); //Doctor can view all his profile information
		System.out.println("2. Recommend Tests to Patients"); // adding a record in test
		System.out.println("3. Update Test Results");// adding results in test
		System.out.println("4. View Patients"); //Doctor can view patient information
		System.out.println("5. View Test Results"); //Doctor can view test results of a patient
		System.out.println("6. Access Medical Records of a patient"); //Doctor can view medical records of a patient
		System.out.println("7. Update Medical Records of a patient"); //Doctor can update medical record of a patient
		System.out.println("8. Logout"); //logs out of the system
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();
		
		switch(doctor_choice) {
		case 1: 
			viewProfile(conn,person_id); //if doctor chooses option 1, jump to viewProfile function
			break;
		
		case 2:
			recommendTest(conn,person_id); //if doctor chooses option 2, jump to recommendTest function
			break;
			
		case 3:
			updateTestResult(conn,person_id); //if doctor chooses option 3, jump to updateTestResult function
			
		case 4:
			viewPatient(conn,person_id); //if doctor chooses option 4, jump to viewPatient function
			break;
			
		case 5:
			viewTestResults(conn,person_id); //if doctor chooses option 5, jump to viewTestResults function
			break;
			
		case 6: getMedicalRecord(conn,person_id); //if doctor chooses option 6, jump to getMedicalRecord function
				break;
				
		case 7: 
			updateMedicalRecord(conn,person_id); //if doctor chooses option 7, jump to updateMedicalRecord function
			
			
		case 8:
			System.out.println("Logging out.."); //exit system
			TimeUnit.SECONDS.sleep(3);
			System.exit(0);
			break;	
		
		default:
			System.out.println("Enter Valid choice"); //Error message if doctor chooses any option other than 1-7
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
			    //Extract all the information of the doctor from staff table based on staff staff ID(SID)
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
	  	
	
	private static void recommendTest(Connection conn, int person_id) {
		  try {
			  //to recommend test, doctor is asked Patient ID who he wants to recommend the test to, name of the test, ID of the doctor performing the test
		  System.out.println("Enter the patient ID :-> ");
		  int pid=sc.nextInt();
		  
		  System.out.println("Enter the name of the test :-> ");
		  sc.nextLine();
		  String t_name = sc.nextLine();
		  
		  System.out.println("Enter the ID of the doctor performing test :-> ");
		  int r_did = sc.nextInt();
		  
		  String select_record = "SELECT RID FROM MEDICAL_RECORDS WHERE PID = ? AND END_DATE IS NULL;" ;
		  PreparedStatement sel_stmt = conn.prepareStatement(select_record);
		  sel_stmt.setInt(1, pid);
		  ResultSet rs = sel_stmt.executeQuery();
		  
		  //Only if end_date of patient's medical record is null, he can be recommended tests
		  if(rs.next())
		  {
			  String insert_query_test = "INSERT INTO TEST(recommended_SID,performed_SID,name,test_date) VALUES(?,?,?,curdate());";
			  PreparedStatement stmt1 = conn.prepareStatement(insert_query_test);
			  stmt1.setInt(1, person_id);
			  stmt1.setInt(2, r_did);
			  stmt1.setString(3, t_name);
			  stmt1.execute();
			  String get_max_TID = "SELECT MAX(TID) AS TID FROM TEST ;" ; //Get the ID of the recently inserted test
			  PreparedStatement stmt2 = conn.prepareStatement(get_max_TID);
			  ResultSet rs1 = stmt2.executeQuery();
			  if(rs1.next())
			  {
				  String insert_query_record_has_test = "INSERT INTO RECORD_HAS_TEST VALUES(?,?)";
			      PreparedStatement insert_stmt_record_has_test = conn.prepareStatement(insert_query_record_has_test);
			      insert_stmt_record_has_test.setInt(1, rs.getInt("RID"));
			      insert_stmt_record_has_test.setInt(2, rs1.getInt("TID"));
			      insert_stmt_record_has_test.execute();
			      System.out.println("Successfully added test");
			      doctorMenu(conn, person_id);
			  }
		  }
		  else
		  {
			  //if end_date in patient's medical record is not null that means he has completed that treatment
			  System.out.println("Patient already checked out from the hospital!"); 
			  doctorMenu(conn, person_id);

			  
		  }
		  
		  
		  } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		 
		}	
	
	private static void updateTestResult(Connection conn, int person_id) {
		  try {
			  //To update test results in the test table, doctor is asked the particular test id(TID)
		  System.out.println("Enter the Test ID :-> ");
		  int t_id = sc.nextInt();
		  //Get and display all the information of the provided TestID
		  String select_test = "SELECT * FROM TEST WHERE TID = ?" ;
		  PreparedStatement sel_stmt = conn.prepareStatement(select_test) ;
		  sel_stmt.setInt(1, t_id);
		  ResultSet rs = sel_stmt.executeQuery();
		  if(rs.next()) {
		  int Test_id = rs.getInt("TID");
		  String name = rs.getString("name");
		  String result = rs.getString("result");
		  Date date = rs.getDate("test_date");
		  
		  System.out.println("Test Details :");
		  System.out.println("1. TID:" + Test_id);
		  System.out.println("2. name:" + name);
		  System.out.println("3. result:" + result);
		  System.out.println("4. Test Date:" + date);
		  System.out.println("Enter/Update Test results :=> ");
		  sc.nextLine();
		  result = sc.nextLine();
		  
		  //Update the test result
		  String update_query = "UPDATE TEST SET RESULT = ? WHERE TID = ? ";
		  PreparedStatement update_stmt = conn.prepareStatement(update_query);
		  update_stmt.setString(1, result);
		  update_stmt.setInt(2, t_id);
		  
		  if(update_stmt.executeUpdate()>0)
			  System.out.println("Successfully updated the results");
		  
		  doctorMenu(conn, person_id);
		  }
		  else
		  {
			  //if the update operation is not successfully, display error
			  System.out.println("No test exists with given Test ID !");
			  doctorMenu(conn,person_id);
		  }
		  } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		}


	public static void viewPatient(Connection conn, int person_id)
	  {
		  try {
			  //A doctor can view information of all the patients or of a particular patient
			  System.out.println("1. View all patients.");
			  System.out.println("2. View a particular patient.");
			  System.out.println("Enter your choice :-> ");

			  int ch=sc.nextInt();
			  
			  if(ch==1)
			  {
				  //If the doctor wishes to see information of all the patients, Select query is performed on Patients table without any constraint
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
						System.out.println("********************************************************");
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
				  //If the doctor wishes to see the information of a particular patient, he should enter the patient ID
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
					}
				  else
				  {
					  //if no record is found with provided ID, display the error message
					  System.out.println("No patient exists with provided patient ID !");
					  doctorMenu(conn,person_id);
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
		
	private static void viewTestResults(Connection conn, int person_id) {
		  try {
			  //If the doctor wishes to see the test results, he is asked to provide the Test ID for which he wants information
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
				  System.out.println("Test Date:" + rs.getDate("Test_date"));
			
			} 
			else {
				//Display error message if no tests are found with provided testID
				System.out.println("No test exists with provided test ID");
				doctorMenu(conn,person_id);
			}
			
			
			System.out.println("\n\n\nPress 0 to go back");
			int choice = sc.nextInt();
			if (choice == 0) {
				doctorMenu(conn, person_id);
			}
			else
			{
				validChoice(0);
				doctorMenu(conn, person_id);
			}
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}

	
	private static void getMedicalRecord(Connection conn, int person_id) {
		System.out.println("Enter Patient ID :=> ");
		int pid = sc.nextInt();
		//Doctor can see active medical records or all medical records of a particular patient
		System.out.println("1. Get active medical record");
		System.out.println("2. Get previous all medical records");
		System.out.println("Enter your choice =>");
		int ch = sc.nextInt();
		try {
		ResultSet rs = null;
		switch(ch) {
		//For case 1, records for a particular PID with null end date are extracted from medical_record table
		case 1: String select_record = "SELECT * FROM MEDICAL_RECORDS WHERE PID = ? AND END_DATE IS NULL" ;
			PreparedStatement sel_stmt;
			sel_stmt = conn.prepareStatement(select_record);
			sel_stmt.setInt(1, pid);
			rs = sel_stmt.executeQuery();
			break;
		//For case 2, all records for a particular PID are extracted from medical_record table
		case 2:
			String select_all_record = "SELECT * FROM MEDICAL_RECORDS WHERE PID = ?" ;
			PreparedStatement sel_all_stmt;
			sel_all_stmt = conn.prepareStatement(select_all_record);
			sel_all_stmt.setInt(1, pid);
			rs = sel_all_stmt.executeQuery();
			break;
		
		default: System.out.println("Invalid Choice!!!");
				 getMedicalRecord(conn,person_id);
				 break;
		}
		 if(!rs.next())
		 {
			 //if no record exists with provided patient ID, display error message
			 System.out.println("No medical records exist for provided Patient ID");
		 }
		 else
		 {
		  do{
			System.out.println("Record ID:" + rs.getInt("RID"));
			System.out.println("Start Date:" + rs.getString("start_date"));
			System.out.println("End Date:" + rs.getString("end_date"));
			System.out.println("Prescription:" + rs.getString("prescription"));
			System.out.println("Diagnosis Details:" + rs.getString("diagnosis_details"));
			System.out.println("Responsible Doctor:" + rs.getInt("responsible_doctor"));
			System.out.println("\n");
		  }
		  while(rs.next());
		 }
		 
		  System.out.println("\n\n\nPress 0 to go back");
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
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	
  private static void updateMedicalRecord(Connection conn, int person_id) {
		System.out.println("Enter Record ID :=> ");
		int rec_id = sc.nextInt();
		
		try {
			//To update medical record, recordID(RID) is requested from doctor
			//Based on the RID provided, record is extracted from medical_records table
		String sel_query = "SELECT * FROM MEDICAL_RECORDS WHERE RID = ? ";
		PreparedStatement sel_stmt = conn.prepareStatement(sel_query);
			sel_stmt.setInt(1, rec_id);
			ResultSet rs = sel_stmt.executeQuery();
			
			if(rs.next())
			{
				//if a record exists with provided ID, all the information is displayed
			 String start_date = rs.getString("start_date");
			 String end_date = rs.getString("end_date");
			 String prescription = rs.getString("prescription");
			 String diagnosis_details = rs.getString("diagnosis_details");
			 int responsible_doctor = rs.getInt("responsible_doctor");
			
			 System.out.println("Record ID:" + rs.getInt("RID"));
			 System.out.println("1. Start Date:" + start_date);
			 System.out.println("2. End Date:" + end_date);
		     System.out.println("3. Prescription:" + prescription);
			 System.out.println("4. Diagnosis Details:" + diagnosis_details);
			 System.out.println("5. Responsible Doctor:" + responsible_doctor);
			 System.out.println("Enter your choice :=> ");
			 int choice = sc.nextInt();
			 //User is given choice to update any one of the 5 column values and further options are provided as per the selection choice
			 switch(choice) {
			 case 1: System.out.println("Enter start date :=> ");
					start_date = sc.next();
					break;
			 case 2:System.out.println("Enter end date :=> ");
			  		end_date = sc.next();
					break;
			 case 3:System.out.println("Enter prescription :=> ");
					prescription = sc.next();
					break;
			 case 4: System.out.println("Enter diagnosis details :=> ");
					diagnosis_details = sc.next();
					break;
			 case 5: System.out.println("Enter responsible doctor :=> ");
					responsible_doctor = sc.nextInt();
					break;
			 default:
				System.out.println("Please enter a valid choice!!");
				updateMedicalRecord(conn,person_id);
			 }
			
			 //Update the record, with the new value provided for the selected column, for all other columns old value will be set
			String update_query = "UPDATE MEDICAL_RECORDS set start_date = ?,end_date = ?,prescription = ?,diagnosis_details = ?,responsible_doctor = ? WHERE RID = ? ";
			PreparedStatement stmt = conn.prepareStatement(update_query);
			stmt.setString(1, start_date);
			stmt.setString(2, end_date);
			stmt.setString(3, prescription);
			stmt.setString(4, diagnosis_details);
			stmt.setInt(5, responsible_doctor);
			stmt.setInt(6, rec_id);
			
			stmt.executeUpdate();
			System.out.println("Successfully updated the record!!");
			doctorMenu(conn,person_id);
			}
			else
			{
				//Display error message, if no record exists for the given recordID(RID)
				System.out.println("No medical record exists");
				doctorMenu(conn,person_id);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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