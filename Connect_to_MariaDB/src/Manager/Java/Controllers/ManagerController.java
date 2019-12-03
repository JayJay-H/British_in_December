package Manager.Java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerController {

	@FXML
	Button backButton;
	@FXML
	Button ScooterManagementButton;
	@FXML
	Button MemberManagementButton;

	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void ScooterManagementButtonHandler(ActionEvent event) {
		try {
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
}


//TODO 1. 'BackButton'을 'Login'화면으로 전환하도록 변경.
