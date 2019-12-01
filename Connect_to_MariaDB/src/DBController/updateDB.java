package DBController;

import java.sql.Statement;

public class updateDB {
	
	public static boolean updateMember(String[] updateTarget, String[] originalContent) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
			String[] 	columns = {"Password", "Phone", "Email", "nowUse"};
			
	        for(int i=0; i<updateTarget.length; i++) {
	        	
	        	if(updateTarget[i].equals("null"))
	        		continue;
	        	
	        	else {
	        		
		        	String sql = "update `Member` set "
		        			+ columns[i]
		        			+ "=\""
		        			+ updateTarget[i]
		        			+ "\" where "
		        			+ columns[i]
		        			+ "=\""
		        			+ originalContent[i]
		        			+ "\"";
		        	
		        	stmt.executeUpdate(sql);
		        	
	        	}
	        	
	        }
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateManager(String[] updateTarget, String[] originalContent) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
			String[] 	columns = {"Password", "nowUse"};
			
	        for(int i=0; i<updateTarget.length; i++) {
	        	
	        	if(updateTarget[i].equals("null"))
	        		continue;
	        	
	        	else {
	        		
		        	String sql = "update Manager set "
		        			+ columns[i]
		        			+ "=\""
		        			+ updateTarget[i]
		        			+ "\" where "
		        			+ columns[i]
		        			+ "=\""
		        			+ originalContent[i]
		        			+ "\"";
		        	
		        	stmt.executeUpdate(sql);
		        	
	        	}
	        	
	        }
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateScooter(String[] updateTarget, String[] originalContent) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
			String[] 	columns = {"Location", "nowUse"};
			
	        for(int i=0; i<updateTarget.length; i++) {
	        	
	        	if(updateTarget[i].equals("null"))
	        		continue;
	        	
	        	else {
	        		
		        	String sql = "update Scooter set "
		        			+ columns[i]
		        			+ "=\""
		        			+ updateTarget[i]
		        			+ "\" where "
		        			+ columns[i]
		        			+ "=\""
		        			+ originalContent[i]
		        			+ "\"";
		        	
		        	stmt.executeUpdate(sql);
		        	
	        	}
	        	
	        }
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
