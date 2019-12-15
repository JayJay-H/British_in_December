package Server_Class.ScooterManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBController.deleteFromDB;
import DBController.insertIntoDB;
import DBController.searchFromDB;
import DBController.updateDB;

public class scooterManagement {
	
	static boolean getStatus;
	static boolean canChangeGetStatus = false;
	
	// 스쿠터를 추가한다.
	public static boolean addScooter(String ID, String Location) {
		return insertIntoDB.addScooter(ID, Location);
	}
	
	// 스쿠터를 삭제한다.
	public static boolean deleteScooter(String ID) {
		return deleteFromDB.deleteScooter(ID);
	}
	
	// 스쿠터의 사용상태를 변경한다.
	public synchronized boolean changeNowUse(String table, String ID, String nowUse) {

		String[] updateTarget = null;
		
		if(nowUse.equals("0")) { 	// 스쿠터의 사용상태를 0으로 변경한다.
			
			updateTarget = new String[] {"null", "0"};
			return updateDB.updateScooter(ID, updateTarget);
		} else {					// 스쿠터의 사용상태를 1로 변경한다.
			
			updateTarget = new String[] {"null", "1"};
			return updateDB.updateScooter(ID, updateTarget);
		}	
	}
	
	// 스쿠터의 위치를 바꾼다.
	public synchronized boolean changeLocation(String table, String ID, String location) {
		String[] updateTarget = null;
		
		updateTarget = new String[] {location, "null"};
		
		return updateDB.updateScooter(ID, updateTarget);
	}
	
	// 특정 스쿠터의 정보를 반환한다.
	public static String findScooter(String ID) throws SQLException {
		ResultSet 	scooterList = searchFromDB.searchObjects("Scooter"); 	// DB에서 모든 스쿠터의 정보를 받아온다.
		String 		scooter = null;											// 스쿠터 정보가 저장될 String 객체이다.
		
		while(scooterList.next()) {
			if(ID.equals(scooterList.getString(1))) {
				scooter = scooterList.getString(1) // ID
						+";" + scooterList.getString(2) // Location
						+";" + scooterList.getString(3); // nowUse
				break;
			}
		}
		
		return scooter;
	}

	// 모든 스쿠터의 정보를 반환한다.
	public synchronized String findScooterList() throws SQLException {
		ResultSet 		scooterList = searchFromDB.searchObjects("Scooter"); 	// DB에서 정보를 받아온다.
		StringBuilder 	showList = new StringBuilder();							// 반환될 String 객체이다.
		
		while(scooterList.next()){
	
			String scooter = scooterList.getString(1) // ID
					+ ";" + scooterList.getString(2) // location
					+ ";" + scooterList.getString(3) + "/"; // nowUSe
			showList.append(scooter);	
        }
		
		return showList.toString();
	}
	
	public synchronized int getScooterNowUse(String ID) throws SQLException {
		if(canChangeGetStatus) {
			getStatus = false;
			notifyAll();
		}
		
		while(getStatus) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		getStatus = true;
		canChangeGetStatus = false;
		
		ResultSet scooterList = searchFromDB.searchObjects("Scooter"); // DB에서 모든 스쿠터 정보를 받아온다.
		
		while(scooterList.next()){
			if(scooterList.getString(1).equals(ID)) {
				return Integer.parseInt(scooterList.getString(3));
			}
        }
		return -1; // 존재하지 않는 ID.
	}
	
	// 스쿠터의 모든 수를 반환한다.
	public static int getNumberOfScooter() throws SQLException {
		ResultSet memberList = searchFromDB.searchObjects("Scooter"); // DB에서 모든 스쿠터 정보를 받아온다.
		int count = 0;
		while(memberList.next()){
			count++;
        }
		
		return count;
	}
	
	// 사용중인 스쿠터 수를 반환한다.
	public static int getNumberOfNowUseScooter() throws SQLException {
		ResultSet memberList = searchFromDB.searchObjects("Scooter"); // DB에서 모든 스쿠터 정보를 받아온다.
		int count = 0;
		while(memberList.next()){
			
			if(memberList.getString(3).equals("1"))
				count++;
			
        }
		
		return count;
	}
	
	// 사용중이지 않은 스쿠터 수를 반환한다.
	public static int getNumberOfCanUseScooter() throws SQLException {
		return getNumberOfScooter() - getNumberOfCanUseScooter();
	}
	
	public static void setCanChangeGetStatus() {
		canChangeGetStatus = true;
	}
}
