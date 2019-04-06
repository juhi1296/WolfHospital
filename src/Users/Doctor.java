package Users;

import java.sql.Connection;

public class Doctor {

	public void doctorMenu(Connection conn, String person_id) {
		// TODO Auto-generated method stub
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
	}

}
