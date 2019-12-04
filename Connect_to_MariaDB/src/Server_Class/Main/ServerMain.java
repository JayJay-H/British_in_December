package Server_Class.Main;

import Server_Class.AuthorityManagement.*;
import Server_Class.MemberManagement.*;
import Server_Class.ScooterManagement.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
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
        
        String ID;
        String Pass;
        String loginTable;
        
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
            				
            				byte[] bytes = new byte[256];
            				InputStream inputStream = socket.getInputStream();
                            // 클라이언트가 비정상 종료를 했을 경우 IOException 발생
                            int readByteCount = inputStream.read(bytes);
                            // 클라이언트가 정상적으로 Socket의 close()를 호출했을 경우
                            if (readByteCount == -1) {
                                throw new IOException();
                            }
                            
                            // 클라이언트의 비정상 종료를 감지하기 위해 위와같은 방법을 써서 한번받은 bytes배열을 가지고 명령을 수행.
                            // 맨 bytes[0], bytes[1] 은 빈칸과 null 이므로 for문은 2번째 인덱스부터 시작한다.
                            // 이 후 bytes[i]들은 char형으로 바꾸어 StringBuilder에 append
                            // StringBuilder를 String으로 바꾸고 쿼리를 실행시킨다.
                            StringBuilder sb = new StringBuilder();
                            for(int i=2; i<bytes.length; i++) {
                            	if(bytes[i] == 0)
                            		break;
                            	sb.append((char)bytes[i]);
                            }
                            String data = sb.toString();
            				
                            int loginStatus = login(data);
                            System.out.println("login: "+loginStatus);
            				if(loginStatus==0) {
            					break;
            				} else if(loginStatus==-2) {
            					throw new IOException();
            				}
            			}
            			
            			while(true) { //메소드 실행
            				// 비 정상적으로 접속이 끊겼는지 아닌지를 파악함
            				byte[] bytes = new byte[256];
            				InputStream inputStream = socket.getInputStream();
                            // 클라이언트가 비정상 종료를 했을 경우 IOException 발생
                            int readByteCount = inputStream.read(bytes);
                            // 클라이언트가 정상적으로 Socket의 close()를 호출했을 경우
                            if (readByteCount == -1) {
                                throw new IOException();
                            }
                            
                            // 클라이언트의 비정상 종료를 감지하기 위해 위와같은 방법을 써서 한번받은 bytes배열을 가지고 명령을 수행.
                            // 맨 bytes[0], bytes[1] 은 빈칸과 null 이므로 for문은 2번째 인덱스부터 시작한다.
                            // 이 후 bytes[i]들은 char형으로 바꾸어 StringBuilder에 append
                            // StringBuilder를 String으로 바꾸고 쿼리를 실행시킨다.
                            StringBuilder sb = new StringBuilder();
                            for(int i=2; i<bytes.length; i++) {
                            	if(bytes[i] == 0)
                            		break;
                            	sb.append((char)bytes[i]);
                            }
                            String data = sb.toString();
                            
                            System.out.println(data);
            				// 쿼리를 실행하는 부분
            				int methodStatus = method(data);
            				
            				if(methodStatus==0) {
            					break;
            				} else if(methodStatus==-2) {
            					throw new IOException();
            				}
            			}
            			
            		} catch (IOException e) {
            			//접속이 끊긴상태
            			if(loginTable.equals("Member")) {
            				memberManagement.changeNowUse("Member", ID, "0");
            				disconnectClient();
            			}else {
            				memberManagement.changeNowUse("Manager", ID, "0");
            				disconnectClient();
            			}
            		}
                }
            };
            
            // 스레드풀에 넣기
            service.submit(runnable);
        }
		
		private int login(String data) {
			StringTokenizer authInfo = new StringTokenizer(data);
			String 			loginStatus = null;
			String			q = authInfo.nextToken();
			
			ID = null;
			Pass = null;
			
			try {
				if(q.equals("Member")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					if(ID.equals("!")) {
						return 0;
					}
					loginStatus = authMember.authenticateMember(ID, Pass);
					
					if(loginStatus.equals("0")) {
						out.writeUTF(loginStatus);
						memberManagement.changeNowUse("Member", ID, "1");
						loginTable = "Member";
						return 0;
					} else {
						out.writeUTF(loginStatus);
						return Integer.parseInt(loginStatus);
					}
				}
				
				if(q.equals("Manager")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					if(ID.equals("!")) {
						return 0;
					}
					loginStatus = authManager.authenticateManager(ID, Pass);
					
					if(loginStatus.equals("0")) {
						out.writeUTF(loginStatus);
						memberManagement.changeNowUse("Manager", ID, "1");
						loginTable = "Manager";
						return 0;
					} else {
						out.writeUTF(loginStatus);
						return Integer.parseInt(loginStatus);
					}
				}
				
				if(q.equals("SignUp")) {
					ID = authInfo.nextToken();
					Pass = authInfo.nextToken();
					boolean signUpStatus = memberManagement.addMember(ID, Pass);
					out.writeBoolean(signUpStatus);
					if(signUpStatus) {
						return 0;
					} else {
						return -1;
					}
				}
			} catch (IOException e) {
				return -2;
			}
			return -3;
		}
		
		private int method(String data) {
			StringTokenizer authInfo = new StringTokenizer(data);
			
			String request = authInfo.nextToken();
			String method = authInfo.nextToken();
			
			try {
				if(request.equals("Member")) {
					switch(method) {
						case "add": // Member add ID PASS
							out.writeBoolean(memberManagement.addMember(authInfo.nextToken(), authInfo.nextToken()));
							break;
							
						case "delete": // Member delete ID
							out.writeBoolean(memberManagement.deleteMember(authInfo.nextToken()));
							break;
						
						case "changeMember": // Member changeMemger ID nowUSe
							out.writeBoolean(memberManagement.changeNowUse(request, authInfo.nextToken(), authInfo.nextToken()));
							break;
						
						case "changeManager": // Member changeManager ID nowUse
							out.writeBoolean(memberManagement.changeNowUse("Manager", authInfo.nextToken(), authInfo.nextToken()));
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
		
		private void disconnectClient() {
            try {
                clientList.remove(Client.this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
	}
	
	
	
	public static void main(String[] args) {
		ServerMain start = new ServerMain();
		start.startServer();
	}
}
