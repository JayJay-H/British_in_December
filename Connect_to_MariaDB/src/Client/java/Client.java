package Client.java;

import java.sql.SQLException;
import Server_Class.AuthorityManagetment.authMember;
import Server_Class.ScooterManagement.scooterManagement;

public class Client {
	static boolean islogin = false;
	boolean isUsing = false;
	
	static void Login(String inputID, String inputPassword) {
		String Loginstatus = authMember.authenticateMember(inputID, inputPassword);
        if (Loginstatus.equals("0")){
            islogin = true;
        } else {
            islogin = false;
        }
        }
	}
	
	static String findScooterList() throws SQLException {
		return scooterManagement.findScooterList();
	}
	
	static String findScooter(String ID) throws SQLException {
		return scooterManagement.findScooter(ID);
	}
	
	static int getNumberOfCanUseScooter() throws SQLException {
		return scooterManagement.getNumberOfCanUseScooter();
	}
}
