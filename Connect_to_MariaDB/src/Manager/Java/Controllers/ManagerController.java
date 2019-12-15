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
	
	// Socket 관련 필드
	private String ID;
	private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
	@FXML Button closeButton;
    
    public void setField(String ID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
    	this.ID = ID;
    	this.socket = socket;
    	this.outputStream = outputStream;
    	this.inputStream = inputStream;
	}
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	Platform.runLater(() -> {
        	idLabel.setText(ID);
    	});
	}
    
	@FXML // 뒤로가기
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Login/resource/LoginForm.css").toExternalForm());
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			closeAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML // 스쿠터 관리로 넘어감
	public void ScooterManagementButtonHandler(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/ScooterManagementGui.fxml"));
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root);
			ScooterManagementController controller = loader.getController();
			controller.setField(ID, socket, outputStream, inputStream);
			
			Stage primaryStage = (Stage) ScooterManagementButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("ScooterManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML // 사용자 관리로 넘어감
	public void MemberManagementButtonHandler(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/MemberManagementGui.fxml"));
			
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root);
			MemberManagementController controller = loader.getController();
			controller.setField(ID, socket, outputStream, inputStream);
			
			Stage primaryStage = (Stage) MemberManagementButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemberManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 로그아웃
    public void closeAction() {
        try {
			//outputStream.writeUTF("Member changeManager "+ID+" 0");
        	//만약 소켓이 안 닫혀 있다면 닫기
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@FXML
	public void closeButtonHandler() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}
}
