package Server_Class.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.StringTokenizer;

import Server_Class.AuthorityManagement.authManager;
import Server_Class.AuthorityManagement.authMember;
import Server_Class.MemberManagement.memberManagement;
import Server_Class.ScooterManagement.scooterManagement;

public class Receiver extends Thread {
	private DataInputStream 	in;
	private DataOutputStream 	out;
	
	public Receiver(Socket socket) throws IOException{
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			while(true) { //로그인
				StringTokenizer authInfo = new StringTokenizer(in.readUTF());
				String 			ID = null;
				String 			Pass = null;
				String 			loginStatus = null;
				
				if(authInfo.nextToken().equals("Member")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					loginStatus = authMember.authenticateMember(ID, Pass);
					
					if(loginStatus.equals("0")) {
						break;
					} else {
						out.writeUTF(loginStatus);
					}
				}
				
				if(authInfo.nextToken().equals("Manager")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					loginStatus = authManager.authenticateManager(ID, Pass);
					
					if(loginStatus.equals("0")) {
						break;
					} else {
						out.writeUTF(loginStatus);
					}
				} 
			}
			
			while(true) { //메소드 실행
				StringTokenizer st = new StringTokenizer(in.readUTF());
				String request = st.nextToken();
				String method = st.nextToken();
				
				if(request.equals("Member")) {
					switch(method) {
						case "add":
							out.writeBoolean(memberManagement.addMember(st.nextToken(), st.nextToken()));
							break;
							
						case "delete":
							out.writeBoolean(memberManagement.deleteMember(st.nextToken()));
							break;
							
						case "findMember":
							try {
								String result = memberManagement.findMember(st.nextToken());
								out.writeUTF(result);
							}catch (SQLException e) {
								out.writeUTF("DB 오류");
							}
							break;
							
						case "findMemberList":
							try {
								String result = memberManagement.findMemberList();
								out.writeUTF(result);
							}catch (SQLException e) {
								out.writeUTF("DB 오류");
							}
							break;
							
						case "findToken":
							try {
								out.writeInt(memberManagement.findToken(st.nextToken()));
							} catch (SQLException e) {
								out.writeInt(-2); // DB 관련 오류
							}
							break;
							
						case "getNum":
							try {
								out.writeInt(memberManagement.getNumberOfMember());
							} catch (SQLException e) {
								out.writeInt(-1);
							}
							break;
					}
				}
				
				if(request.equals("Scooter")) {
					switch(method) {
						case "add":
							out.writeBoolean(scooterManagement.addScooter(st.nextToken(), st.nextToken()));
							break;
							
						case "delete":
							out.writeBoolean(scooterManagement.deleteScooter(st.nextToken()));
							break;
							
						case "findScooter":
							try {
								String result = scooterManagement.findScooter(st.nextToken());
								out.writeUTF(result);
							}catch (SQLException e) {
								out.writeUTF("DB 오류");
							}
							break;
							
						case "findScooterList":
							try {
								String result = scooterManagement.findScooterList();
								out.writeUTF(result);
							}catch (SQLException e) {
								out.writeUTF("DB 오류");
							}
							break;
							
						case "getNum":
							try {
								out.writeInt(scooterManagement.getNumberOfScooter());
							} catch (SQLException e) {
								out.writeInt(-1);
							}
							break;
							
						case "getNumNowUse":
							try {
								out.writeInt(scooterManagement.getNumberOfNowUseScooter());
							} catch (SQLException e) {
								out.writeInt(-1);
							}
							break;
							
						case "getNumCanUse":
							try {
								out.writeInt(scooterManagement.getNumberOfCanUseScooter());
							} catch (SQLException e) {
								out.writeInt(-1);
							}
							break;
					}
				}
			}
			
		} catch (IOException e) {
			//접속이 끊긴상태
		}
	}
}
