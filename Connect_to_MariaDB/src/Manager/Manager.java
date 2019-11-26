package Manager;

import java.sql.SQLException;

import Server_Class.AuthorityManagetment.authManager;
import Server_Class.MemberManagement.memberManagement;
import Server_Class.ScooterManagement.scooterManagement;

public class Manager {
	static boolean addScooter(String ID, String Location) {
		return scooterManagement.addScooter(ID, Location);
	}
	
	static String showScooterLocation(String ID) throws SQLException {
		return scooterManagement.findScooterList();
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
		return authManager.authenticateManager(ID, Password);
	}
}
