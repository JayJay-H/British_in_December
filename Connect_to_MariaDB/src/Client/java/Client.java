package Client.java;

import java.sql.SQLException;
import Server_Class.AuthorityManagement.authMember;
import Server_Class.ScooterManagement.scooterManagement;

public class Client {
	boolean islogin = false;
	boolean isUsing = false;
	String[] errorMessage = {"로그인 성공!", "DB관련 오류","비밀번호가 틀립니다.", "이미 사용중입니다.", "아이디가 없습니다."};

	public String Login(String inputID, String inputPassword) {
		String Loginstatus = authMember.authenticateMember(inputID, inputPassword);
		if (Loginstatus.equals("0")) {
			islogin = true;
		} else {
			islogin = false;
		}
		return errorMessage[Integer.parseInt(Loginstatus)]; //에러 메시지 출력
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
