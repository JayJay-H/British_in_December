package Server_Class.Main;

import Server_Class.AuthorityManagement.*;
import Server_Class.MemberManagement.*;
import Server_Class.ScooterManagement.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain {
	ExecutorService service;
	private ServerSocket serverSocket;
	private List<Client> clientList = new Vector<>();
	
	public void startServer() {
		service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
        try {
        	// TODO 서버 소켓을 생성하여 serverSocket 필드에 대입하기
        	serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8000));
        } catch (Exception e) {
            // 만약 서버 소켓이 닫혀있다면 서버 종료.
            if (!serverSocket.isClosed()) {
                stopServer();
            }
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();  // 연결 수락
                        
                        System.out.println(socket.getInetAddress());
                        
                        clientList.add(new Client(socket));
                    } catch (Exception e) {
                    	// 서버 소켓이 닫히면 서버 종료
                        if (!serverSocket.isClosed()) {
                            stopServer();
                        }
                        break;
                    }

                }
            }
        };
        // 스레드 풀에서 처리
        service.submit(runnable);
	}
	
	public void stopServer() {
		try {
			Iterator<Client> iterator = clientList.iterator();
            
            // 접속해 있는 사람들의 소켓을 모두 닫음.
            while (iterator.hasNext()) {
                iterator.next().socket.close();
                iterator.remove();
            }
            // 만약 서버 소켓이 열려있다면 닫기.
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            
            // 스레드 풀이 실행되고있다면 종료.
            if (service != null && !service.isShutdown()) {
                service.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	class Client{
		Socket socket;
		DataInputStream in;
        DataOutputStream out;
        
		public Client(Socket socket) {
            this.socket = socket;
            try {
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {}
            receive();
        }
		
		private void receive() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
            		try {
            			while(true) { //로그인
            				if(login()==0) {
            					break;
            				} else if(login()==-2) {
            					throw new IOException();
            				}
            			}
            			
            			while(true) { //메소드 실행
            				if(method()==0) {
            					break;
            				} else if(login()==-2) {
            					throw new IOException();
            				}
            			}
            			
            		} catch (IOException e) {
            			//접속이 끊긴상태
            		}
                }
            };
            
            // 스레드풀에 넣기
            service.submit(runnable);
        }
		
		private int login() {
			StringTokenizer authInfo;
			try {
				authInfo = new StringTokenizer(in.readUTF());
			} catch (Exception e) {
				return -1;
			}
			String 			ID = null;
			String 			Pass = null;
			String 			loginStatus = null;
			String			q = authInfo.nextToken();
			try {
				if(q.equals("Member")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					loginStatus = authMember.authenticateMember(ID, Pass);
					
					if(loginStatus.equals("0")) {
						out.writeUTF(loginStatus);
					} else {
						out.writeUTF(loginStatus);
					}
				}
				
				if(q.equals("Manager")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					loginStatus = authManager.authenticateManager(ID, Pass);
					
					if(loginStatus.equals("0")) {
						out.writeUTF(loginStatus);
					} else {
						out.writeUTF(loginStatus);
					}
				}
				
				if(q.equals("SignUp")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					out.writeBoolean(memberManagement.addMember(ID, Pass));
				}
			} catch (IOException e) {
				return -2;
			}
			return 0;
		}
		
		private int method() {
			StringTokenizer authInfo;
			try {
				authInfo = new StringTokenizer(in.readUTF());
			} catch(Exception e) {
				return -1;
			}
			String request = authInfo.nextToken();
			String method = authInfo.nextToken();
			try {
				if(request.equals("Member")) {
					switch(method) {
						case "add":
							out.writeBoolean(memberManagement.addMember(authInfo.nextToken(), authInfo.nextToken()));
							break;
							
						case "delete":
							out.writeBoolean(memberManagement.deleteMember(authInfo.nextToken()));
							break;
							
						case "findMember":
							try {
								String result = memberManagement.findMember(authInfo.nextToken());
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
								out.writeInt(memberManagement.findToken(authInfo.nextToken()));
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
							out.writeBoolean(scooterManagement.addScooter(authInfo.nextToken(), authInfo.nextToken()));
							break;
							
						case "delete":
							out.writeBoolean(scooterManagement.deleteScooter(authInfo.nextToken()));
							break;
							
						case "findScooter":
							try {
								String result = scooterManagement.findScooter(authInfo.nextToken());
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
			} catch (IOException e) {
				return -2;
			}
			return 0;
		}
	}
	
	public static void main(String[] args) {
		ServerMain start = new ServerMain();
		start.startServer();
	}
}
