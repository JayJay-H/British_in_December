package Manager.Java.Manager;

import java.sql.SQLException;

import Server_Class.AuthorityManagement.authManager;
import Server_Class.MemberManagement.memberManagement;
import Server_Class.ScooterManagement.scooterManagement;

public class Manager {
	
	static scooterManagement scootermanagement = new scooterManagement();
	static authManager authmanager = new authManager();
	static boolean addScooter(String ID, String Location) {
		return scooterManagement.addScooter(ID, Location);
	}
	
	static String showScooterLocation(String ID) throws SQLException {
		return scootermanagement.findScooterList();
	}
	
	static String getMemberList() throws SQLException {
		return memberManagement.findMemberList();
	}
	
	static String getScooterInfo(String ID) throws SQLException {
		return scooterManagement.findScooter(ID);
	}
	
	static boolean deleteMember(String ID) {
		return memberManagement.deleteMember(ID);
	}
	
	static String authManager(String ID, String Password) {
		return authmanager.authenticateManager(ID, Password);
	}
}
