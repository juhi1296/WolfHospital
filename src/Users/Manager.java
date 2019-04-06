package Users;

import java.sql.Connection;

public class Manager {

	public void managerMenu(Connection conn, String person_id) {
		// TODO Auto-generated method stub
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
	}

}
