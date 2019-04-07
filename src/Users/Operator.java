package Users;

import java.sql.Connection;

public class Operator {

	
	public void operatorMenu(Connection conn, int person_id) {
		
		// TODO Auto-generated method stub
		
		int space_available = 0;
		
		System.out.println("----------------------------Welcome Operator----------------------------");
		System.out.println("1. Register New Patient");
		System.out.println("2. Checkout Patient");
		System.out.println("3. Generate Bills for Patient");
		System.out.println("4. Check status of Wards");
		System.out.println("5. Assign available bed in a Ward to patient");
		System.out.println("6. Reserve Wards and Beds");
		System.out.println("7. Release Wards and Beds");
		System.out.println("8. Update Medical Records for Patients");
		System.out.println("9. Maintain Billing account of Patient");
		if(space_available == 0) System.out.println("10. Generate Billing account for Patient");
		if(space_available == 1) System.out.println("11. Do not admit Patient");
		System.out.println("12. Logout");
		System.out.println("Enter your choice :-> ");
	}

}
