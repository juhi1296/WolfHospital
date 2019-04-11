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
		
		try {
		System.out.println("----------------------------Welcome Operator----------------------------");
		System.out.println("1. Register New Patient");
		System.out.println("2. Checkout Patient");
		System.out.println("3. Generate Bills for Patient");
		System.out.println("4. Check status of Wards");
		System.out.println("5. Assign Bed in a Ward to patient");
		System.out.println("6. Release Wards and Beds");
		System.out.println("7. Update Medical Records for Patients");
		System.out.println("8. Maintain Billing account of Patient");
		System.out.println("9. Generate Billing account for Patient");
		System.out.println("10. Do not admit Patient");
		System.out.println("11. Logout");
		System.out.println("Enter your choice :-> ");
		
		int doctor_choice = sc.nextInt();
		
		switch(doctor_choice) {
		case 1: 
			registerPatient(conn,person_id);
			break;
			
		case 2:
			checkoutPatient(conn,person_id);
			break;
			
		case 5:
			assignBed(conn,person_id);
			break;
			
		case 6:
			releaseBed(conn,person_id);
			break;
			
		}
		
		}catch(Exception ex) {
			System.out.println("Exception" + ex);
		}
	}
	private void releaseBed(Connection conn, int person_id) {
		try {
			System.out.println("Enter the patient ID :-> ");
			int pid=sc.nextInt();
			System.out.println("Enter End Date :-> ");
			String end_date = sc.next();
			
			conn.setAutoCommit(false);
			
			PreparedStatement stmt1 = conn.prepareStatement("select WID,Bed_ID from patient_is_assigned_bed where PID = ? AND end_date is null");
			stmt1.setInt(1, pid);
			ResultSet rs = stmt1.executeQuery();
			rs.next();
			
			int bed_id = rs.getInt("Bed_ID");
			int wid = rs.getInt("WID");
			
			PreparedStatement stmt = conn.prepareStatement("update patient_is_assigned_bed set end_date = ? where PID = ? AND end_date is null");
			stmt.setString(1, end_date);
			stmt.setInt(2, pid);
			stmt.execute();
			
			PreparedStatement stmt2 = conn.prepareStatement("update BED set availability = 0 where Bed_ID = ? AND WID = ?");
			stmt2.setInt(1, bed_id);
			stmt2.setInt(2, wid);
			stmt2.execute();
			
			conn.commit();
			
			System.out.println("BED released successfully");
			
		}catch(Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Exception" + ex);
		}
		
	}
	
	private void assignBed(Connection conn, int person_id) {
		try {
			System.out.println("Enter the patient ID :-> ");
			int pid=sc.nextInt();
			System.out.println("Enter Preference of Patient(1 bed, 2 beds and 4 beds) :-> ");
			int pref = sc.nextInt();
			System.out.println("Enter Today's Date :-> ");
			String date = sc.next();
			
			
			PreparedStatement stmt = conn.prepareStatement("select WID,Bed_ID from bed where WID IN (select WID from bed group by WID having count(WID) = ?) and availability = 0;");
			stmt.setInt(1, pref);
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next()) {
				System.out.println("Preferred choice is not available");
			}
			else {
				conn.setAutoCommit(false);
				
				int wid = rs.getInt("WID");
				int bid = rs.getInt("Bed_ID");
				PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO patient_is_assigned_bed values(?,?,?,?,?)");
				stmt1.setInt(1, pid);
				stmt1.setInt(2, wid);
				stmt1.setInt(3, bid);
				stmt1.setString(4, date);
				stmt1.setString(5, null);
				stmt1.execute();
				
				PreparedStatement stmt2 = conn.prepareStatement("UPDATE BED SET AVAILABILITY = 1 WHERE WID = ? AND Bed_ID = ?");
				stmt2.setInt(1, wid);
				stmt2.setInt(2, bid);
				stmt2.execute();
				
				conn.commit();
				
				System.out.println("Patient is assigned a Bed" + bid + "in ward" + wid + "\n");
				
			}	
			
		}catch(Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Exception" + ex);
		}
		
	}
	private void checkoutPatient(Connection conn, int person_id) {
		// TODO Auto-generated method stub
		
		
	}
	private void registerPatient(Connection conn, int person_id) {
		// TODO Auto-generated method stub
		Manager m = new Manager();
		try {
			m.addPatient(conn, person_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	

}
