package Login;


import java.sql.*;
import java.util.Scanner;


import Connection.*;
import Users.Doctor;
import Users.Manager;
import Users.Operator;
import Users.Patient;

public class Login {

	public static void main(String[] args) throws Exception
	{
		loginmenu();
	}
	
	public static void loginmenu() throws Exception {
		
		Connection conn = DBConnection.ConnectDB();
		Scanner scan = new Scanner(System.in);
			System.out.println("----------------------------WolfHospital Management System--------------------------");
			System.out.println("1. Login");
			System.out.println("2. Exit");
	        System.out.println("Enter your choice :-> ");
			int select = scan.nextInt();
			if(select == 1)
			{
				userCheck(conn);
			}
			else
			{
				if(select !=2){
					System.out.println("Enter a valid number");
					loginmenu();
				}
				System.exit(0);
			}
	}
	
	public static void userCheck(Connection conn)throws Exception
	{Scanner scan = new Scanner(System.in);
	System.out.println("Enter Username:-> ");
	String user = scan.nextLine();
	System.out.println("Enter Password:-> ");
	String pwd = scan.nextLine();
	PreparedStatement stmt = conn.prepareStatement("SELECT USERNAME,PWD,ROLE,PERSON_ID FROM USER_LOGIN WHERE USERNAME=?");
	stmt.setString(1, user);
	ResultSet rs = stmt.executeQuery();
	if (!rs.next()) {
		System.out.println("Login Incorrect.\n");
		loginmenu();
	}
	String data_pwd = rs.getString("PWD");
    String username = rs.getString("USERNAME");
    String role = rs.getString("ROLE");
    String person_id = rs.getString("PERSON_ID");
    
   
    if (!pwd.equals(data_pwd)) 
    {
        System.out.println("Login Incorrect.");
        loginmenu();
    }
    else {
    	if(role.equals("D")) {
    		Doctor d = new Doctor();
    		d.doctorMenu(conn,person_id);
    	}
    	else if(role.equals("P")) {
    		Patient p = new Patient();
    		p.patientMenu(conn,person_id);
    	}
    	else if(role.equals("M")){
    		Manager m = new Manager();
    		m.managerMenu(conn,person_id);
    	}else if(role.equals("O")) {
    		Operator o = new Operator();
    		o.operatorMenu(conn,person_id);
    	}
    }
	}
	
}