package Server_Class.AuthorityManagetment;
import java.sql.ResultSet;

import DBController.*;

public class authManager {
	public static String authenticateManager(String inputID, String inputPassword) {
		try {
			ResultSet client_list = searchFromDB.searchObjects("Manager"); 	//Manager테이블을 ResultSet 형태로 가져옴.
			
			while(client_list.next()) {
				
				if(inputID.equals(client_list.getString(1))) {				// 아이디 확인
					
					
					if(inputPassword.equals(client_list.getString(2))) {	//패스워드 확인
						
						
						if(client_list.getString(5).equals("0")) 			//사용중인지 아닌지 확인
							return "0"; 		//확인 성공!
						
						else 
							return "3"; 	//errorcase_3 : 이미 사용 중
						
					}else 
						return "2"; 	//errorcase_2 : 비밀번호가 틀림
				}
			}
			
			return "4"; 			//errorcase_4 : 아이디가 없음
		}catch(Exception e) {
			e.printStackTrace();
			return "1"; 						//errorcase_1 : DB관련 오류
		}
	}
}
