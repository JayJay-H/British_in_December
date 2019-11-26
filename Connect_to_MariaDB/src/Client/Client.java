package Client;

import java.sql.SQLException;
import Server_Class.AuthorityManagetment.authMember;
import Server_Class.ScooterManagement.scooterManagement;

public class Client {
	boolean islogin = false, isUsing = false;
	
	static void Login(String inputID, String inputPassword) {
		authMember.authenticateMember(inputID, inputPassword);
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
