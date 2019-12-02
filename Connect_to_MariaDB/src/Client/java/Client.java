package Client.java;

import java.sql.SQLException;
import Server_Class.AuthorityManagement.authMember;
import Server_Class.ScooterManagement.scooterManagement;

public class Client {
	//TODO 어떻게 해서 각각의 클라이언트의 위치 및 이용시간, 이용금액을 체크 할 것인지 구현

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
