package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Patient {

	static Scanner sc = new Scanner(System.in);
	
	public static void  patientMenu(Connection conn, int pid) throws InterruptedException, SQLException {
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
			
				case 2: 
					viewTestResult(conn,pid);
					break;
					
				case 3:
					viewBill(conn, pid);
					break;
				
				case 4:
					viewMedicalRecord(conn,pid);
					break;
				case 5:
					System.out.println("Logging out..");
					TimeUnit.SECONDS.sleep(3);
					System.exit(0);
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
	
	public static void viewProfile(Connection conn, int pid)
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
	
	public static void viewTestResult(Connection conn,int pid)
	{
		try {
			System.out.println("------------Choose an option----------------");
			System.out.println("1. View all your test results");
			System.out.println("2. View Test performed on a particular Date");
			System.out.println("3. View test result by Name of the test");
			System.out.println("4. View test recommended by a doctor. ");
			System.out.println("5. View test performed by a doctor.");
			
			int ch=sc.nextInt();
			
			
			if(ch==1)
			{
				try {
					PreparedStatement stmt = conn.prepareStatement("SELECT t.TID, t.recommended_SID,t.performed_SID, t.name, t.result,t.test_date FROM TEST t,MEDICAL_RECORDS m,RECORD_HAS_TEST r WHERE \n" + 
							"r.RID=m.RID and r.TID=t.TID and m.PID=?");
					stmt.setInt(1, pid);
					ResultSet rs = stmt.executeQuery();
					
					if(!rs.next())
					{
						System.out.println("No test results available.");
					}
					else
					{
					   do {
						    System.out.println("*******************************************");
							System.out.println("TID : " + rs.getInt("TID"));
							System.out.println("TEST RECOMMENDED BY : " + rs.getString("RECOMMENDED_SID"));
							System.out.println("TEST PERFORMED BY : " + rs.getString("PERFORMED_SID"));
							System.out.println("TEST NAME : " + rs.getString("NAME"));
							System.out.println("TEST RESULT : " + rs.getString("RESULT"));
							System.out.println("TEST DATE : " + rs.getDate("TEST_DATE"));
							System.out.println("*******************************************");
						}
					   while(rs.next());
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
			
			
			else if(ch==2)
			{
				
			    Date date = null;
				try
				{
					
					String expectedPattern = "yyyy-MM-dd";
				    SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
			        formatter.setLenient(false);
					System.out.println("Please enter a particular date in yyyy-mm-dd format.");
					String d=sc.next();
					date = formatter.parse(d);
					

				}
				catch(ParseException e)
				{
					System.out.println("Please enter date in yyyy-mm-dd format.");
					viewTestResult(conn, pid);
				}
				
				
				try {
	
					PreparedStatement stmt = conn.prepareStatement("SELECT t.TID, t.recommended_SID,t.performed_SID, t.name, t.result,t.test_date FROM TEST t,MEDICAL_RECORDS m,RECORD_HAS_TEST r WHERE r.RID=m.RID and r.TID=t.TID and m.PID=? and t.test_date=?");
					stmt.setInt(1, pid);
					stmt.setDate(2, new java.sql.Date(date.getTime()));
					ResultSet rs = stmt.executeQuery();
					
					if(!rs.next())
					{
						System.out.println("No test results available.");
					}
					else
					{
					  do{
						    System.out.println("*******************************************");
							System.out.println("TID : " + rs.getInt("TID"));
							System.out.println("TEST RECOMMENDED BY : " + rs.getString("RECOMMENDED_SID"));
							System.out.println("TEST PERFORMED BY : " + rs.getString("PERFORMED_SID"));
							System.out.println("TEST NAME : " + rs.getString("NAME"));
							System.out.println("TEST RESULT : " + rs.getString("RESULT"));
							System.out.println("TEST DATE : " + rs.getDate("TEST_DATE"));
							System.out.println("*******************************************");
						}
					  while(rs.next()) ;
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
			else if(ch==3)
			{
				try {
					System.out.println("Please enter name of the test");
					String test=sc.next();
					
					PreparedStatement stmt = conn.prepareStatement("SELECT t.TID, t.recommended_SID,t.performed_SID, t.name, t.result,t.test_date FROM TEST t,MEDICAL_RECORDS m,RECORD_HAS_TEST r WHERE \n" + 
							"r.RID=m.RID and r.TID=t.TID and m.PID=? and t.NAME like ?");
					stmt.setInt(1, pid);
					stmt.setString(2,'%'+test+'%');
					ResultSet rs = stmt.executeQuery();
					
					if(!rs.next())
					{
						System.out.println("No test results available.");
					}
					else
					{ 
					  do {
						   System.out.println("*******************************************");
							System.out.println("TID : " + rs.getInt("TID"));
							System.out.println("TEST RECOMMENDED BY : " + rs.getString("RECOMMENDED_SID"));
							System.out.println("TEST PERFORMED BY : " + rs.getString("PERFORMED_SID"));
							System.out.println("TEST NAME : " + rs.getString("NAME"));
							System.out.println("TEST RESULT : " + rs.getString("RESULT"));
							System.out.println("TEST DATE : " + rs.getDate("TEST_DATE"));
							System.out.println("*******************************************");
					  }
					  while(rs.next());
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
			else if(ch==4)
			{
				try {
					System.out.println("Please enter ID of the recommending doctor");
					int rec_doc=sc.nextInt();
					
					PreparedStatement stmt = conn.prepareStatement("SELECT t.TID, t.recommended_SID,t.performed_SID, t.name, t.result,t.test_date FROM TEST t,MEDICAL_RECORDS m,RECORD_HAS_TEST r WHERE \n" + 
							"r.RID=m.RID and r.TID=t.TID and m.PID=? and t.RECOMMENDED_SID=?");
					stmt.setInt(1, pid);
					stmt.setInt(2,rec_doc);
					ResultSet rs = stmt.executeQuery();
					
					if(!rs.next())
					{
						System.out.println("No test results available.");
					}
					else
					{  
					  do {
						    System.out.println("*******************************************");
							System.out.println("TID : " + rs.getInt("TID"));
							System.out.println("TEST RECOMMENDED BY : " + rs.getString("RECOMMENDED_SID"));
							System.out.println("TEST PERFORMED BY : " + rs.getString("PERFORMED_SID"));
							System.out.println("TEST NAME : " + rs.getString("NAME"));
							System.out.println("TEST RESULT : " + rs.getString("RESULT"));
							System.out.println("TEST DATE : " + rs.getDate("TEST_DATE"));
							System.out.println("*******************************************");
						
					  }while(rs.next());
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
			else if(ch==5)
			{
				try {
					System.out.println("Please enter ID of the doctor who performed the test");
					int per_doc=sc.nextInt();
					
					PreparedStatement stmt = conn.prepareStatement("SELECT t.TID, t.recommended_SID,t.performed_SID, t.name, t.result,t.test_date FROM TEST t,MEDICAL_RECORDS m,RECORD_HAS_TEST r WHERE \n" + 
							"r.RID=m.RID and r.TID=t.TID and m.PID=? and t.PERFORMED_SID=?");
					stmt.setInt(1, pid);
					stmt.setInt(2,per_doc);
					ResultSet rs = stmt.executeQuery();
					

					if(!rs.next())
					{
						System.out.println("No test results available.");
					}
					else
					{  
					 
					 do {
						 System.out.println("*******************************************");
							System.out.println("TID : " + rs.getInt("TID"));
							System.out.println("TEST RECOMMENDED BY : " + rs.getString("RECOMMENDED_SID"));
							System.out.println("TEST PERFORMED BY : " + rs.getString("PERFORMED_SID"));
							System.out.println("TEST NAME : " + rs.getString("NAME"));
							System.out.println("TEST RESULT : " + rs.getString("RESULT"));
							System.out.println("TEST DATE : " + rs.getDate("TEST_DATE"));
							System.out.println("*******************************************");
					  } while(rs.next());
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
			else
			{
				System.out.println("Enter Valid choice");
				patientMenu(conn,pid);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public static void viewBill(Connection conn, int pid)
	{
		try
		{
			PreparedStatement stmt=conn.prepareStatement("Select B.BILL_ID, B.PID,P.NAME,B.CARD_NUMBER,B.SSN_PAYER,B.BILLING_ADDRESS, B.REGISTRATION_FEE,B.ACCOMODATION_FEE,B.MEDICATION_PRESCRIBED,B.VISIT_DATE FROM\n" + 
					"BILLING_ACCOUNT B, PATIENT P WHERE B.PID=P.PID and B.PID=? and B.ACCOMODATION_FEE IS NOT NULL");
		
			stmt.setInt(1, pid);
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next())
			{
				System.out.println("Bill is not yet ready");
			}
			else
			{
				do
				{
					System.out.println("*******************************************");
					System.out.println("BILL ID : " + rs.getInt("BILL_ID"));
					System.out.println("PATIENT ID : " + rs.getInt("PID"));
					System.out.println("PATIENT NAME : " + rs.getString("NAME"));
					System.out.println("CARD NUMBER : " + rs.getString("CARD_NUMBER"));
					System.out.println("SS OF THE PAYER : " + rs.getString("SSN_PAYER"));
					System.out.println("BILLING ADDRESS : " + rs.getString("BILLING_ADDRESS"));
					System.out.println("REGISTRATION FEE : " + rs.getDouble("REGISTRATION_FEE"));
					System.out.println("ACCOMODATION FEE : " + rs.getDouble("ACCOMODATION_FEE"));
					System.out.println("MEDICATIION PRESCRIBED : " + rs.getString("MEDICATION_PRESCRIBED"));
					System.out.println("VISIT DATE : " + rs.getDate("VISIT_DATE"));
					System.out.println("*******************************************");
				}
				while(rs.next());
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
	
	public static void viewMedicalRecord(Connection conn,int pid)
	{
		try {
			PreparedStatement stmt=conn.prepareStatement("Select RID,START_DATE,END_DATE,PRESCRIPTION,DIAGNOSIS_DETAILS,RESPONSIBLE_DOCTOR FROM MEDICAL_RECORDS WHERE PID=?");
		
			stmt.setInt(1, pid);
			
			ResultSet rs=stmt.executeQuery();
			
			if(!rs.next())
			{
				System.out.println("No medical record exists");
			}
			else
			{
				do
				{
					System.out.println("*******************************************");
					System.out.println("MEDICAL RECORD ID : " + rs.getInt("RID"));
					System.out.println("START DATE : " + rs.getDate("START_DATE"));
					System.out.println("END DATE : " + rs.getDate("END_DATE"));
					System.out.println("PRESCRIPTION : " + rs.getString("PRESCRIPTION"));
					System.out.println("DIAGNOSIS DETAILS : " + rs.getString("DIAGNOSIS_DETAILS"));
					System.out.println("RESPONSIBLE DOCTOR : " + rs.getString("RESPONSIBLE_DOCTOR"));
					System.out.println("*******************************************");
				}
				while(rs.next());
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
