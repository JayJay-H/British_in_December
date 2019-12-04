package Manager.Java.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ManagerController implements Initializable{
	
	@FXML
	Label idLabel;
	@FXML
	Button backButton;
	@FXML
	Button ScooterManagementButton;
	@FXML
	Button MemberManagementButton;
	
	private String ID; 
	private Socket socket;
    private DataOutputStream outputStream;
    
    public ManagerController(String ID) {
    	this.ID = ID;
	}
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	Platform.runLater(() -> {
    		idLabel.setText(ID);
        });
		startClient();
	}
    
	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			outputStream.writeUTF("Member changeManager "+ID+" 0");
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			closeAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void ScooterManagementButtonHandler(ActionEvent event) {
		try {
			System.out.println(ID);
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ScooterManagementGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) ScooterManagementButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("ScooterManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void MemberManagementButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/MemberManagementGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) MemberManagementButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemberManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public
	
	void startClient() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                	socket = new Socket();
                    socket.connect(new InetSocketAddress("localhost", 8000));
                    
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    disconnectServer();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
	
	void disconnectServer() {
        if (!socket.isClosed()) {
        	closeAction();
        }
    }
	
    public void closeAction() {
        try {
        	//만약 소켓이 안 닫혀 있다면 닫기
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
