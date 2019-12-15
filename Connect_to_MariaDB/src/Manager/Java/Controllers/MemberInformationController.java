package Manager.Java.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class MemberInformationController {

	@FXML
	Label IDLabel;
	@FXML
	Label PASSLabel;
	@FXML
	Label phoneLabel;
	@FXML
	Label mailLabel;
	@FXML
	Label nowUseLabel;
	@FXML
	Button backButton;
	
	private String selectedID;
	
	// Socket 관련 필드
	private String userID;
	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	@FXML Button closeButton;
	    
	public void setField(String ID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream, String selectedID) {
	    this.userID = ID;
	    this.socket = socket;
	    this.outputStream = outputStream;
	    this.inputStream = inputStream;
	    this.selectedID = selectedID;
	    findMemberInfo();
	}
	
	public void findMemberInfo() {
    	String resultStatus = null;
		try {
			outputStream.writeUTF("Member findMember "+ selectedID);
			resultStatus = inputStream.readUTF();
			if(resultStatus.equals("-1")) {
				throw new IOException("DB error");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resultStatus);
		StringTokenizer userInfo = new StringTokenizer(resultStatus, ";");
		IDLabel.setText(userInfo.nextToken());
		PASSLabel.setText(userInfo.nextToken());
		phoneLabel.setText(userInfo.nextToken());
		mailLabel.setText(userInfo.nextToken());
		nowUseLabel.setText(userInfo.nextToken());
	}

	@FXML
	public void backButtonHandler() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/MemberManagementGui.fxml"));
			
			Parent root = (Parent)loader.load();
			Scene scene = new Scene(root);
			MemberManagementController controller = loader.getController();
			controller.setField(userID, socket, outputStream, inputStream);
			
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemberManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void closeButtonHandler() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}

	@FXML public void LabelDragged(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}

	@FXML public void LabelPressed(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}
	@FXML public void LabelReleased(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}
}
