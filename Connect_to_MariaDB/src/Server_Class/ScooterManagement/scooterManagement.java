package Server_Class.ScooterManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBController.deleteFromDB;
import DBController.insertIntoDB;
import DBController.searchFromDB;
import DBController.updateDB;

public class scooterManagement {
	
	public static boolean addScooter(String ID, String Location) {
		return insertIntoDB.addScooter(ID, Location);
	}
	
	public static boolean deleteScooter(String ID) {
		return deleteFromDB.deleteScooter(ID);
	}
	
	public synchronized static boolean changeNowUse(String table, String ID, String nowUse) {
		String[] updateTarget = null;
		
		System.out.println(table+" "+nowUse+" "+ID);
		
		if(nowUse.equals("0")) {
			updateTarget = new String[] {"null", "0"};
			return updateDB.updateScooter(ID, updateTarget);
		} else {
			updateTarget = new String[] {"null", "1"};
			return updateDB.updateScooter(ID, updateTarget);
		}	
	}
	
	public static boolean changeLocation(String table, String ID, String location) {
		String[] updateTarget = null;
		
		System.out.println(table+" "+location+" "+ID);
		
		updateTarget = new String[] {location, "null"};
		
		return updateDB.updateScooter(ID, updateTarget);
	}
	
	public synchronized static String findScooter(String ID) throws SQLException {
		ResultSet scooterList = searchFromDB.searchObjects("Scooter");
		String scooter=null;
		
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

	public synchronized static String findScooterList() throws SQLException {
		ResultSet scooterList = searchFromDB.searchObjects("Scooter");
		StringBuilder showList = new StringBuilder();
		
		while(scooterList.next()){
	
			String scooter = scooterList.getString(1) // ID
					+ ";" + scooterList.getString(2) // location
					+ ";" + scooterList.getString(3) + "/"; // nowUSe
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
