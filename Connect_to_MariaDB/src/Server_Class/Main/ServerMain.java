package Server_Class.Main;

import java.net.*;

public class ServerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket 			socket = null;
		ServerSocket 	server_socket = null;
				
		try {
			server_socket = new ServerSocket(8000);
			
			while(true) {
				socket = server_socket.accept();
				System.out.println(socket.getInetAddress());
				Receiver receiver = new Receiver(socket);
				receiver.start();
			}
		} catch(Exception e) {}
	}
}
