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
		
		System.out.println("1. Register new patient");
		System.out.println("2. Get patient ID of registered patients");
		System.out.println("3. Assign Bed");
		System.out.println("4 Assign Doctor to Patient");
		System.out.println("5. Create Billing Account and Medical Record for the visit");
		
		System.out.println("6. Check Out Patient");
		System.out.println("7. Get Ward and Bed assigned to a patient");
		
		//System.out.println("8. remove doc assigned");
		
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();
		
		switch(doctor_choice) {
		
		case 1: pid = registerPatient(conn,person_id);
				System.out.println("Patient with PID " + pid + " successfully added");
				operatorMenu(conn, person_id);
				break;
		
		case 2: pid = getRegisteredPatient(conn,person_id);		
				System.out.println("PID of patient: " + pid);
				operatorMenu(conn, person_id);
				break;
				
		case 3:	
				System.out.println("Enter patient ID");
				pid = sc.nextInt();
				if(!assignBed(conn,pid,person_id))
				{
				System.out.println("Bed not assigned!!");
				operatorMenu(conn, person_id);
				}
				operatorMenu(conn, person_id);
				break;
		
		case 4:
				assignDoc(conn, person_id);
				break;
			
		case 5: 	
				System.out.println("Enter patient ID");
				pid = sc.nextInt();
				genMedAndBillAcc(conn,person_id,pid);
				operatorMenu(conn, person_id);
				break;
				
	//	case 1:
		//	checkin(conn,person_id);
			//break;
		
		case 6:
			checkout(conn,person_id);
			break;
		
		case 7: 
				System.out.println("Enter patient ID");
				pid = sc.nextInt();
				getBedWardAssigned(conn,pid,person_id);
				operatorMenu(conn, person_id);
				break;
			
			
		default:
			System.out.println("Enter a valid choice: ");
			operatorMenu(conn, person_id);
		}
		
		}catch(Exception ex) {
			System.out.println("Exception" + ex);
		}
	}
	private void getBedWardAssigned(Connection conn, int pid, int person_id) {
		try {
			String sel_query = "select * from PATIENT_IS_ASSIGNED_BED where pid = ?;" ;
			PreparedStatement stmt = conn.prepareStatement(sel_query);
			stmt.setInt(1, pid);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
			System.out.println(" Patient PID " + pid + " is assigned Ward " + rs.getInt("WID") + " Bed " + rs.getInt("Bed_ID"));			
			return;
			}
			else {
			System.out.println("Record not found !!");
			return;
			}
			
		} catch (SQLException e) {
			System.out.println("Something went wrong. Please try again!!");
			operatorMenu(conn, person_id);
			e.printStackTrace();
		}
		
		
	}
	private void genMedAndBillAcc(Connection conn, int person_id, int pid) throws SQLException {
		try {
		
		conn.setAutoCommit(false);	
		
		PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO MEDICAL_RECORDS(PID,start_date) values(?,curdate());");
		stmt1.setInt(1, pid);
		stmt1.execute();
		
		System.out.println("Generating Billing Account ...");
		System.out.println("Enter Payment Method :--> ");
		String payment_method = sc.next();
		System.out.println("Enter Card Number :--> ");
		String card_number = sc.next();
		System.out.println("Enter SSN for Payer :--> ");
		String SSN_payer = sc.next();
		System.out.println("Enter Billing Address :--> ");
		String billing_address = sc.next();
		
		
					
		PreparedStatement stm2 = conn.prepareStatement("INSERT INTO BILLING_ACCOUNT(PID,payment_method,card_number,SSN_payer,billing_address,registration_fee,visit_date) \r\n" + 
				"values (?,?,?,?,?,100,curdate())");
		stm2.setInt(1, pid);
		stm2.setString(2,payment_method );
		stm2.setString(3, card_number);
		stm2.setString(4, SSN_payer);
		stm2.setString(5, billing_address);
		stm2.execute();
		System.out.println("Patient " + pid + " successfully chekedin!!");
		conn.commit();
		conn.setAutoCommit(true);
		operatorMenu(conn, person_id);
	}
	catch(Exception ex) {
			System.out.println(ex);
			conn.rollback();
			conn.setAutoCommit(true);
			operatorMenu(conn, person_id);
	}
		
	}
	private int getRegisteredPatient(Connection conn, int person_id) {
		try {
		System.out.println("Enter Your Name : ");
		String name = sc.next();
		System.out.println("Enter Your DOB (YYYY-MM-DD) : ");
		String dob = sc.next();
		PreparedStatement stmt = conn.prepareStatement("SELECT PID FROM PATIENT WHERE NAME = ? AND DOB = ?");
		stmt.setString(1, name);
		stmt.setString(2, dob);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		return rs.getInt("PID");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	private void checkout(Connection conn, int person_id) throws SQLException {
		try {
			conn.setAutoCommit(false);
			System.out.println("Enter Patient ID for Check out : ");
			int pid = sc.nextInt();
			System.out.println("Enter End Date : ");
			String end_d = sc.next();
			
			releaseBed(conn,person_id,pid,end_d);
			
			generateBill(conn,person_id,pid);
			
			removeDocandupdateMedRec(conn,pid);
			
			PreparedStatement stmt = conn.prepareStatement("delete from PATIENT_IS_ASSIGNED_BED where PID = ?");
			stmt.setInt(1, pid);
			stmt.execute();
			
			PreparedStatement stmt1 = conn.prepareStatement("update MEDICAL_RECORDS set end_date = ? where end_date is null and PID = ? ");
			stmt1.setString(1, end_d);
			stmt1.setInt(2, pid);
			stmt1.execute();
			
			System.out.println("Patient " + pid + " successfully checked out!!");
			conn.commit();
			conn.setAutoCommit(true);
			operatorMenu(conn, person_id);
		}catch(Exception ex) {
			conn.rollback();
			conn.setAutoCommit(true);
			System.out.println(ex);
			System.out.println("Something went wrong, please try again \n");
			operatorMenu(conn, person_id);
		}
		
	}
	
	private void generateBill(Connection conn, int person_id, int pid) {
		try {
			
			PreparedStatement stmt = conn.prepareStatement("select datediff(end_date,start_date) * (select charges from WARD where WID = (select WID from PATIENT_IS_ASSIGNED_BED where PID = ?)) as accomodation_charges \r\n" + 
					"from PATIENT_IS_ASSIGNED_BED where PID = ?;");
			stmt.setInt(1, pid);
			stmt.setInt(2, pid);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			int accomodation_charges = rs.getInt("accomodation_charges");
			
			PreparedStatement stmt1 = conn.prepareStatement("select max(Bill_ID) as Bill_ID from BILLING_ACCOUNT where PID = ?");
			stmt1.setInt(1, pid);
			ResultSet rs1 = stmt1.executeQuery();
			
			rs1.next();
			int bill_id = rs1.getInt("Bill_ID");
			
			PreparedStatement stmt2 = conn.prepareStatement("update BILLING_ACCOUNT set accomodation_fee= ?  where PID = ? and Bill_id = ?");
			stmt2.setInt(1, accomodation_charges);
			stmt2.setInt(2, pid);
			stmt2.setInt(3, bill_id);
			stmt2.execute();
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
	
	private void releaseBed(Connection conn, int person_id,int pid, String end_d) {
		try {
			if(pid == 0) {
				System.out.println("Enter the patient ID :-> ");
				pid=sc.nextInt();
			}
			
			
			String end_date = end_d;
			
			
			PreparedStatement stmt1 = conn.prepareStatement("select WID,Bed_ID from PATIENT_IS_ASSIGNED_BED where PID = ? AND end_date is null");
			stmt1.setInt(1, pid);
			ResultSet rs = stmt1.executeQuery();
			rs.next();
			
			int bed_id = rs.getInt("Bed_ID");
			int wid = rs.getInt("WID");
			
			PreparedStatement stmt = conn.prepareStatement("update PATIENT_IS_ASSIGNED_BED set end_date = ? where PID = ? AND end_date is null");
			stmt.setString(1, end_date);
			stmt.setInt(2, pid);
			stmt.execute();
			
			PreparedStatement stmt2 = conn.prepareStatement("update BED set availability = 0 where Bed_ID = ? AND WID = ?");
			stmt2.setInt(1, bed_id);
			stmt2.setInt(2, wid);
			stmt2.execute();

			System.out.println("BED released successfully");
			
		}catch(Exception ex) {
			
			System.out.println("Exception" + ex);
		}
		
	}
	
	private boolean assignBed(Connection conn, int pid, int person_id) throws SQLException {
		try {
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
				System.out.println("Preferred choice is not available");
				return false;
				
			}
			else {
				conn.setAutoCommit(false);
				
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
				
				conn.commit();
				conn.setAutoCommit(true);
				System.out.println("Patient is assigned a Bed "+ bid + " in ward " + wid + "\n");
				return true;
			}	
			
		}catch(Exception ex) {
			
				conn.rollback();
				conn.setAutoCommit(true);
				System.out.println("Exception" + ex);
				return false;
		}
		
	}

	private int registerPatient(Connection conn, int person_id) {
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
	
	private void assignDoc(Connection conn, int person_id) {
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
		
		operatorMenu(conn,person_id);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void removeDocandupdateMedRec(Connection conn, int PID) {
		try {

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
