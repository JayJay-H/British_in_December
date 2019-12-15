package Server_Class.MemberManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBController.*;

public class memberManagement {
	
	// 아이디를 추가한다.
	public synchronized boolean addMember(String ID, String Password) { // add에 성공하면 true 반대는 false
		return insertIntoDB.addMember(ID, Password);
	}
	
	// 아이디를 삭제한다.
	public static boolean deleteMember(String ID) {			// add에 성공하면 true 반대는 false
		return deleteFromDB.deleteMember(ID);
	}
	
	// 아이디의 사용상태를 변경한다.
	public static boolean changeNowUse(String table, String ID, String nowUse) {
		String[] updateTarget = null; // Member인지 Manager인지에 따라 관련된 칼럼이 달라지기때문에 먼저 null로 선언한다.
		
		if(table.equals("Member") && nowUse.equals("0")) { 			// Member의 nowUse를 0으로 바꾸기.
			
			updateTarget = new String[] {"null", "null", "null", "0", "null"};
			
			return updateDB.updateMember(ID, updateTarget);
		} else if(table.equals("Member") && nowUse.equals("1")){	// Member의 nowUse를 1으로 바꾸기.
			
			updateTarget = new String[] {"null", "null", "null", "1", "null"};
			
			return updateDB.updateMember(ID, updateTarget);
		} else if(table.equals("Manager") && nowUse.equals("0")) {	// Manager의 nowUse를 0으로 바꾸기.
			
			updateTarget = new String[] {"null", "0"};
			
			return updateDB.updateManager(ID, updateTarget);
		} else {													// Manager의 nowUse를 1으로 바꾸기.
			
			updateTarget = new String[] {"null", "1"};
			
			return updateDB.updateManager(ID, updateTarget);
		}
	}
	
	// 사용자의 스쿠터 사용상태를 변경
	public static boolean changeScooterUse(String ID, String scooterUse) {
		String[] updateTarget = null;
		
		if(scooterUse.equals("0")) { 		// 스쿠터 사용상태를 0으로 바꾼다.
			updateTarget = new String[] {"null", "null", "null", "null", "0"};
			
			return updateDB.updateMember(ID, updateTarget);
		} else {							// 스쿠터 사용상태를 1로 바꾼다.
			updateTarget = new String[] {"null", "null", "null", "null", "1"};
			
			return updateDB.updateMember(ID, updateTarget);
		}
	}
	
	// 특정 회원을 찾아 그 정보를 반환.
	public static String findMember(String ID) throws SQLException {		
		ResultSet memberList = searchFromDB.searchObjects("Member");
		
		while(memberList.next()){
			
			if(memberList.getString(1).equals(ID)) {
				String show = ID
						+ ";" + memberList.getString(2) // password
						+ ";" + memberList.getString(3) // phone
						+ ";" + memberList.getString(4) // email
						+ ";" + memberList.getString(5); // nowUse
				return show;
			}
			
        }
		
		return "찾는 ID가 존재하지 않습니다.";
	}
	
	// 모든 회원의 리스트를 반환
	public static String findMemberList() throws SQLException {			
		ResultSet memberList = searchFromDB.searchObjects("Member");
		StringBuilder showList = new StringBuilder();
		
		while(memberList.next()){
	
			String member = memberList.getString(1)
					+ ";님의 Password : " + memberList.getString(2)
					+ "Phone : " + memberList.getString(3)
					+ "Email : " + memberList.getString(4)
					+ "사용상태 : " + memberList.getString(5) + "/";
			showList.append(member);
			
        }
		
		return showList.toString();
	}
	
	// 모든 회원의 수를 반환.
	public static int getNumberOfMember() throws SQLException {			
		ResultSet memberList = searchFromDB.searchObjects("Member");
		int count = 0;
		while(memberList.next()){
			count++;
        }
		
		return count;
	}
	
	// 관련 유저가 스쿠터를 사용중인지 아닌지를 반환
	public static int getMemberScooterUse(String ID) throws SQLException {
		ResultSet memberList = searchFromDB.searchObjects("Member");
		while(memberList.next()){
			
			if(memberList.getString(1).equals(ID)) {
				if(memberList.getString(6).equals("0"))
					return 0;
				else 
					return 1;
			}
			
        }
		return -1; // 찾는 아이디가 없음
	}
	
}
