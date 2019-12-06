package Server_Class.MemberManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBController.*;

public class memberManagement {
	
	public static boolean addMember(String ID, String Password) { // add에 성공하면 true 반대는 false
		return insertIntoDB.addMember(ID, Password);
	}
	
	public static boolean deleteMember(String ID) {			// add에 성공하면 true 반대는 false
		return deleteFromDB.deleteMember(ID);
	}
	
	public synchronized static boolean changeNowUse(String table, String ID, String nowUse) {
		String[] updateTarget = null;
		
		System.out.println(table+" "+nowUse+" "+ID);
		
		if(table.equals("Member") && nowUse.equals("0")) {
			updateTarget = new String[] {"null", "null", "null", "0"};
			
			return updateDB.updateMember(ID, updateTarget);
		} else if(table.equals("Member") && nowUse.equals("1")){
			updateTarget = new String[] {"null", "null", "null", "1"};
			
			return updateDB.updateMember(ID, updateTarget);
		} else if(table.equals("Manager") && nowUse.equals("0")) {
			updateTarget = new String[] {"null", "0"};
			
			return updateDB.updateManager(ID, updateTarget);
		} else {
			updateTarget = new String[] {"null", "1"};
			
			return updateDB.updateManager(ID, updateTarget);
		}
	}
	
	public static String findMember(String ID) throws SQLException {		// 특정 회원을 찾음
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
	
	public static String findMemberList() throws SQLException {			// 모든 회원의 리스트를 반환
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
	
	public static void findEvent(String ID) { 		// ??? 왜 있는거야??
		
	}
	
	public static int findToken(String ID) throws SQLException {  			// 토큰이 있는다면 1 반대는 0 ID가 존재하지 않으면 -1
		ResultSet memberList = searchFromDB.searchObjects("Member");
		
		while(memberList.next()){
			
			if(memberList.getString(1).equals(ID)) {
				if(memberList.getString(5).equals("1"))
					return 1;
				else
					return 0;
			}
			
        }
		
		return -1;
	}
	
	public static int getNumberOfMember() throws SQLException {			// 모든 회원의 수를 반환.
		ResultSet memberList = searchFromDB.searchObjects("Member");
		int count = 0;
		while(memberList.next()){
			count++;
        }
		
		return count;
	}
	
}
