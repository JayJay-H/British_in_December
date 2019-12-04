package Server_Class.ScooterManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBController.deleteFromDB;
import DBController.insertIntoDB;
import DBController.searchFromDB;

public class scooterManagement {
	
	public static boolean addScooter(String ID, String Location) {
		return insertIntoDB.addScooter(ID, Location);
	}
	
	public static boolean deleteScooter(String ID) {
		return deleteFromDB.deleteScooter(ID);
	}
	
	public static String findScooter(String ID) throws SQLException {
		ResultSet scooterList = searchFromDB.searchObjects("Scooter");
		String scooter=null;
		
		while(scooterList.next()) {
			if(ID.equals(scooterList.getString(1))) {
				scooter = scooterList.getString(1)
						+"의 위치: " + scooterList.getString(2)
						+"\n사용상태: " + scooterList.getString(3) + "/\n";
				break;
			}
		}
		
		return scooter;
	}

	public static String findScooterList() throws SQLException {
		ResultSet scooterList = searchFromDB.searchObjects("Scooter");
		StringBuilder showList = new StringBuilder();
		
		while(scooterList.next()){
	
			String scooter = scooterList.getString(1)
					+ "의 위치 : " + scooterList.getString(2)
					+ "\n사용상태 : " + scooterList.getString(3) + "/";
			showList.append(scooter);
			
        }
		
		return showList.toString();
	}
	
	public static int getNumberOfScooter() throws SQLException {
		ResultSet memberList = searchFromDB.searchObjects("Scooter");
		int count = 0;
		while(memberList.next()){
			count++;
        }
		
		return count;
	}
	
	public static int getNumberOfNowUseScooter() throws SQLException {
		ResultSet memberList = searchFromDB.searchObjects("Scooter");
		int count = 0;
		while(memberList.next()){
				if(memberList.getString(3).equals("1"))
					count++;
        }
		
		return count;
	}
	
	public static int getNumberOfCanUseScooter() throws SQLException {
		return getNumberOfScooter() - getNumberOfCanUseScooter();
	}
}
