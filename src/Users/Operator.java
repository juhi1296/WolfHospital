package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class Operator {

	static Scanner sc = new Scanner(System.in);
	public void operatorMenu(Connection conn, int person_id) {
		
		// TODO Auto-generated method stub
		int pid = 0;
		try {
		System.out.println("----------------------------Welcome Operator----------------------------");
		//System.out.println("1. Check In Patient");
		// displays a menu for the user to perform operations
		System.out.println("1. Register new patient");
		System.out.println("2. Get patient ID of registered patients");
		System.out.println("3. Assign Bed");
		System.out.println("4 Assign Doctor to Patient");
		System.out.println("5. Create Billing Account and Medical Record for the visit");
		
		System.out.println("6. Check Out Patient");
		System.out.println("7. Get Ward and Bed assigned to a patient");
		
		//System.out.println("8. remove doc assigned");
		
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();	//takes input from the user
		
		switch(doctor_choice) {
		
		case 1: pid = registerPatient(conn,person_id);		// Registers a new patient in our database
				System.out.println("Patient with PID " + pid + " successfully added");
				operatorMenu(conn, person_id);	// displays operator menu after the successfully registering patient
				break;
		
		case 2: pid = getRegisteredPatient(conn,person_id);		// Gets patient ID 
				System.out.println("PID of patient: " + pid);
				operatorMenu(conn, person_id);	//displays operator menu
				break;
				
		case 3:	
				System.out.println("Enter patient ID");	// this takes patient id from the user
				pid = sc.nextInt();
				if(!assignBed(conn,pid,person_id))	//if bed is not assigned
				{
				System.out.println("Bed not assigned!!");
				operatorMenu(conn, person_id);	//displays operator menu
				}
				operatorMenu(conn, person_id);	//displays operator menu
				break;
		
		case 4:
				assignDoc(conn, person_id);		//assigns doctor to a patient
				break;
			
		case 5: 	
				System.out.println("Enter patient ID");	//display patient id
				pid = sc.nextInt();
				genMedAndBillAcc(conn,person_id,pid);	//generates medical record and billing account for this particular visit
				operatorMenu(conn, person_id);	//displays operator menu
				break;
				
	//	case 1:
		//	checkin(conn,person_id);
			//break;
		
		case 6:
			checkout(conn,person_id);		//checks out patient
			break;
		
		case 7: 
				System.out.println("Enter patient ID");		//gets patient id from the user
				pid = sc.nextInt();
				getBedWardAssigned(conn,pid,person_id);		//get ward and bed id from patient id
				operatorMenu(conn, person_id);		//displays operator menu
				break;
			
			
		default:
			System.out.println("Enter a valid choice: ");		//if valid choice is not entered by the user
			operatorMenu(conn, person_id);		//displays operator menu
		}
		
		}catch(Exception ex) {
			System.out.println("Exception" + ex);
		}
	}
	private void getBedWardAssigned(Connection conn, int pid, int person_id) {		//function to get bed and ward assigned to the patient
		try {
			String sel_query = "select * from PATIENT_IS_ASSIGNED_BED where pid = ?;" ;		//store SQL query in string
			PreparedStatement stmt = conn.prepareStatement(sel_query);		
			stmt.setInt(1, pid);				// place pid from the user in place of first '?'
			ResultSet rs = stmt.executeQuery();			//execute sql query
			
			if(rs.next()) {								// if there is an output
			System.out.println(" Patient PID " + pid + " is assigned Ward " + rs.getInt("WID") + " Bed " + rs.getInt("Bed_ID"));			
			return;
			}
			else {
			System.out.println("Record not found !!");	// if there is no output
			return;
			}
			
		} catch (SQLException e) {
			System.out.println("Something went wrong. Please try again!!");
			operatorMenu(conn, person_id);	//displays operator menu
			e.printStackTrace();
		}
		
		
	}
	private void genMedAndBillAcc(Connection conn, int person_id, int pid) throws SQLException { //function to generate medical and billing account
		try {
		
		conn.setAutoCommit(false);			//transaction begins
		
		PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO MEDICAL_RECORDS(PID,start_date) values(?,curdate());");	//store sql query 
		stmt1.setInt(1, pid);		//place pid in place of first '?'
		stmt1.execute();			//execute sql query
		
		System.out.println("Generating Billing Account ...");
		System.out.println("Enter Payment Method :--> ");
		String payment_method = sc.next();				//takes payment method input from the user
		System.out.println("Enter Card Number :--> ");
		String card_number = sc.next();					//takes card number from the user
		System.out.println("Enter SSN for Payer :--> ");
		String SSN_payer = sc.next();					//takes SSN of payer from the user
		System.out.println("Enter Billing Address :--> ");
		String billing_address = sc.next();				//takes billing address from the user
		
		
					
		PreparedStatement stm2 = conn.prepareStatement("INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,visit_date) \r\n" + 
				"values (?,?,?,?,?,100,curdate())");	//store sql query
		stm2.setInt(1, pid);							//place pid in place of first '?'
		stm2.setString(2,payment_method );				//place payment method in place of second '?'
		stm2.setString(3, card_number);					//place card number in place of third '?'
		stm2.setString(4, SSN_payer);					//place ssn of payer in place of fourth '?'
		stm2.setString(5, billing_address);				//place billing address in place of fifth '?'	
		stm2.execute();									//execute sql query
		System.out.println("Patient " + pid + " successfully chekedin!!");
		conn.commit();									//transaction complete
		conn.setAutoCommit(true);						//transaction ends
		operatorMenu(conn, person_id);					//displays operator menu
	}			
	catch(Exception ex) {
			System.out.println(ex);
			conn.rollback();							//if transaction fails, rollback
			conn.setAutoCommit(true);					//transaction ends
			operatorMenu(conn, person_id);				//displays operator menu
	}
		
	}
	private int getRegisteredPatient(Connection conn, int person_id) {			//function to get pid of registered patient
		try {
		System.out.println("Enter Your Name : ");			
		String name = sc.next();								//takes name from the user
		System.out.println("Enter Your DOB (YYYY-MM-DD) : ");	
		String dob = sc.next();									//takes dob from the user
		PreparedStatement stmt = conn.prepareStatement("SELECT PID FROM PATIENT WHERE NAME = ? AND DOB = ?");
		stmt.setString(1, name);								//puts name in place of first '?'
		stmt.setString(2, dob);									//puts dob in place of second '?'
		ResultSet rs = stmt.executeQuery();						//execute sql query and get result
		rs.next();
		return rs.getInt("PID");							//return pid of patient
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	private void checkout(Connection conn, int person_id) throws SQLException {			//function to checkout patient
		try {
			conn.setAutoCommit(false);						//transaction begins
			System.out.println("Enter Patient ID for Check out : ");
			int pid = sc.nextInt();					//gets pid from user
			System.out.println("Enter End Date : ");
			String end_d = sc.next();				//gets end date from user
			
			releaseBed(conn,person_id,pid,end_d);		//calls function to release bed based of the pid as the argument
			
			generateBill(conn,person_id,pid);			// calls function to generate bill for the patient
			
			removeDocandupdateMedRec(conn,pid);			//calls function to update medical record
			
			PreparedStatement stmt = conn.prepareStatement("delete from PATIENT_IS_ASSIGNED_BED where PID = ?");
			stmt.setInt(1, pid);						//puts pid in place of first '?'
			stmt.execute();								//execute sql quey
					
			PreparedStatement stmt1 = conn.prepareStatement("update MEDICAL_RECORDS set end_date = ? where end_date is null and PID = ? ");
			stmt1.setString(1, end_d);			//puts end date in plca eof first '?'
			stmt1.setInt(2, pid);				//puts pid in place of second '?'
			stmt1.execute();					//execute sql query
			
			System.out.println("Patient " + pid + " successfully checked out!!");
			conn.commit();						//transaction complete
			conn.setAutoCommit(true);			//transaction ends
			operatorMenu(conn, person_id);		//displays operator menu
		}catch(Exception ex) {
			conn.rollback();					//transaction failed, rollback
			conn.setAutoCommit(true);			//transaction ends
			System.out.println(ex);
			System.out.println("Something went wrong, please try again \n");
			operatorMenu(conn, person_id);		//displays operator menu
		}
		
	}
	
	private void generateBill(Connection conn, int person_id, int pid) {			// function to generate bill for patient
		try {
			
			PreparedStatement stmt = conn.prepareStatement("select datediff(end_date,start_date) * (select charges from WARD where WID = (select WID from PATIENT_IS_ASSIGNED_BED where PID = ?)) as accomodation_charges \r\n" + 
					"from PATIENT_IS_ASSIGNED_BED where PID = ?;");
			stmt.setInt(1, pid);			//puts pid in place of first '?"
			stmt.setInt(2, pid);			//puts pid in place of second '?'
			ResultSet rs = stmt.executeQuery();		// execute query
			
			rs.next();
			int accomodation_charges = rs.getInt("accomodation_charges");
			
			PreparedStatement stmt1 = conn.prepareStatement("select max(Bill_ID) as Bill_ID from BILLING_ACCOUNT where PID = ?");
			stmt1.setInt(1, pid);
			ResultSet rs1 = stmt1.executeQuery();		//execute query
			
			rs1.next();
			int bill_id = rs1.getInt("Bill_ID");
			
			PreparedStatement stmt2 = conn.prepareStatement("update BILLING_ACCOUNT set accomodation_fee= ?  where PID = ? and Bill_id = ?");
			stmt2.setInt(1, accomodation_charges);
			stmt2.setInt(2, pid);
			stmt2.setInt(3, bill_id);
			stmt2.execute();			//execute query
			System.out.println("Accomodation fee successfully updated \n");
						
		}catch(Exception ex) {
			ex.getStackTrace();
		}
	}
//	private void checkin(Connection conn, int person_id) throws SQLException {
//		int pid = 0;
//		try {
//			conn.setAutoCommit(false);
//			System.out.println("Do you want to register new Patient ?(Y/N) :");
//			String choice = sc.next();
//			
//			if(choice.equals("Y") || choice.equals("y")) {
//				pid = registerPatient(conn,person_id);
//			}else {
//				System.out.println("Enter Your Name : ");
//				String name = sc.next();
//				System.out.println("Enter Your DOB (YYYY-MM-DD) : ");
//				String dob = sc.next();
//				PreparedStatement stmt = conn.prepareStatement("SELECT PID FROM PATIENT WHERE NAME = ? AND DOB = ?");
//				stmt.setString(1, name);
//				stmt.setString(2, dob);
//				ResultSet rs = stmt.executeQuery();
//				
//				rs.next();
//				pid = rs.getInt("PID");
//				
//			}
//			
//			System.out.println("Is Bed Required ?(Y/N)");
//			choice = sc.next();
//			if(choice.equals("Y") || choice.equals("y")) {
//				if(!assignBed(conn,person_id,pid))
//				{
//					System.out.println("Bed not assigned!!");
//					operatorMenu(conn, person_id);
//				}
//			}
//			
//			PreparedStatement stmt = conn.prepareStatement("INSERT INTO registrations values(?,?)");
//			stmt.setInt(1, person_id);
//			stmt.setInt(2, pid);
//			stmt.execute();
//			
//			
//			
//			PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO medical_records(PID,start_date) values(?,curdate());");
//			stmt1.setInt(1, pid);
//			stmt1.execute();
//			
//			System.out.println("Generating Billing Account ...");
//			System.out.println("Enter Payment Method :--> ");
//			String payment_method = sc.next();
//			System.out.println("Enter Card Number :--> ");
//			String card_number = sc.next();
//			System.out.println("Enter SSN for Payer :--> ");
//			String SSN_payer = sc.next();
//			System.out.println("Enter Billing Address :--> ");
//			String billing_address = sc.next();
//			
//			
//						
//			PreparedStatement stm2 = conn.prepareStatement("INSERT INTO billing_account(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,visit_date) \r\n" + 
//					"values (?,?,?,?,?,100,curdate())");
//			stm2.setInt(1, pid);
//			stm2.setString(2,payment_method );
//			stm2.setString(3, card_number);
//			stm2.setString(4, SSN_payer);
//			stm2.setString(5, billing_address);
//			stm2.execute();
//			System.out.println("Patient " + pid + "successfully chekedin!!");
//			conn.commit();
//			conn.setAutoCommit(true);
//			operatorMenu(conn, person_id);
//		}
//		catch(Exception ex) {
//				conn.rollback();
//				conn.setAutoCommit(true);
//		}
//	}
	
	private void releaseBed(Connection conn, int person_id,int pid, String end_d) { 		//function to release bed 
		try {
			if(pid == 0) {
				System.out.println("Enter the patient ID :-> ");
				pid=sc.nextInt();
			}
			
			
			String end_date = end_d;
			
			
			PreparedStatement stmt1 = conn.prepareStatement("select WID,Bed_ID from PATIENT_IS_ASSIGNED_BED where PID = ? AND end_date is null");
			stmt1.setInt(1, pid);
			ResultSet rs = stmt1.executeQuery();			//sql query which ouputs ward id and bed id of current patients
			rs.next();
			
			int bed_id = rs.getInt("Bed_ID");
			int wid = rs.getInt("WID");
			
			PreparedStatement stmt = conn.prepareStatement("update PATIENT_IS_ASSIGNED_BED set end_date = ? where PID = ? AND end_date is null");
			stmt.setString(1, end_date);
			stmt.setInt(2, pid);
			stmt.execute();					//updates table 
			
			PreparedStatement stmt2 = conn.prepareStatement("update BED set availability = 0 where Bed_ID = ? AND WID = ?"); //updates table of bed availability
			stmt2.setInt(1, bed_id);			
			stmt2.setInt(2, wid);
			stmt2.execute();

			System.out.println("BED released successfully");
			
		}catch(Exception ex) {
			
			System.out.println("Exception" + ex);
		}
		
	}
	
	private boolean assignBed(Connection conn, int pid, int person_id) throws SQLException {			//function to assign bed to patient
		try {				// function takes preference from the user for ward and assigns ward 
			if(pid == 0) {
				System.out.println("Enter the patient ID :-> ");
				pid=sc.nextInt();
			}
			
			System.out.println("Enter Preference of Patient(1 bed, 2 beds and 4 beds). Press 0 to exit :-> ");
			int pref = sc.nextInt();
			if(pref == 0)
			return false;	
			
			PreparedStatement stmt = conn.prepareStatement("select WID,Bed_ID from BED where WID IN (select WID from BED group by WID having count(WID) = ?) and availability = 0;");
			stmt.setInt(1, pref);
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next()) {
				System.out.println("Preferred choice is not available");		// if preferred choice of ward is not available
				return false;
				
			}
			else {
				conn.setAutoCommit(false);			//begins transaction
				
				int wid = rs.getInt("WID");
				int bid = rs.getInt("Bed_ID");
				PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO PATIENT_IS_ASSIGNED_BED values(?,?,?,curdate(),?)");
				stmt1.setInt(1, pid);
				stmt1.setInt(2, wid);
				stmt1.setInt(3, bid);
				stmt1.setString(4, null);
				stmt1.execute();
				
				PreparedStatement stmt2 = conn.prepareStatement("UPDATE BED SET AVAILABILITY = 1 WHERE WID = ? AND Bed_ID = ?");
				stmt2.setInt(1, wid);
				stmt2.setInt(2, bid);
				stmt2.execute();
				
				conn.commit();							//transaction complete
				conn.setAutoCommit(true);			//transaction ends
				System.out.println("Patient is assigned a Bed "+ bid + " in ward " + wid + "\n");
				return true;
			}	
			
		}catch(Exception ex) {
			
				conn.rollback();				//transaction fails, rollback
				conn.setAutoCommit(true);		//transaction ends
				System.out.println("Exception" + ex);
				return false;
		}
		
	}

	private int registerPatient(Connection conn, int person_id) {		//function to register patient
		// TODO Auto-generated method stub
		int pid = 0;
		Manager m = new Manager();
		try {
			pid = m.addPatient(conn, person_id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pid; 
	}
	
	private void assignDoc(Connection conn, int person_id) {		//function to assign doctor
		try {
		System.out.println("Enter doctor ID :=> ");
		int DID = sc.nextInt();
		System.out.println("Enter Patient ID :=> ");
		int PID = sc.nextInt();
		String insert_treats = "INSERT INTO TREATS VALUES(?,?)";
		
		PreparedStatement stmt = conn.prepareStatement(insert_treats);
		
		stmt.setInt(1,PID);
		stmt.setInt(2,DID);
		
		stmt.execute();
		
		System.out.println("Doctor " + DID +" successfully assigned to patient " + PID);
		
		operatorMenu(conn,person_id);		//displays operator menu
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void removeDocandupdateMedRec(Connection conn, int PID) {		// function to remove doctor and update medical record
		try {
				//updates treats table and assigns doctor in medical record table
		PreparedStatement stmt = null;	
		String getdocAssigned = "SELECT SID FROM TREATS WHERE PID = ?"; 	
			stmt = conn.prepareStatement(getdocAssigned);
			stmt.setInt(1, PID);
			ResultSet rs = stmt.executeQuery();
			int DID = 0;
			if(rs.next()) {
				DID = rs.getInt("SID");
			}
			
			
		String update_med_rec = "UPDATE MEDICAL_RECORDS SET responsible_doctor = ? WHERE PID = ? AND end_date IS NULL";
			stmt = conn.prepareStatement(update_med_rec);
			stmt.setInt(1, DID);
			stmt.setInt(2, PID);
			stmt.execute();
		
		String del_treats = "DELETE FROM TREATS WHERE PID = ?";
		stmt = conn.prepareStatement(del_treats);
		stmt.setInt(1, PID);
		stmt.execute();
		System.out.println("updated Treats and Medical Record");
		
		} catch (SQLException e) {
			
				e.printStackTrace();
			}
			
		}
		
		
	}
