package Manager.Java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerGuiController {

	@FXML
	Button backButton;
	@FXML
	Button ScooterManagementButton;
	@FXML
	Button MemberManagementButton;
	@FXML
	Button MemberInfoButton;

	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			Scene scene = new Scene(root);;
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
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("ScooterManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void MemberManagementButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/MemeberManagementGui.fxml"));
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemeberManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void MemberInfoButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/MemberInfoGui.fxml"));
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemberInfoGui");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/*
 * #해야할 일
 * 1. 'BackButton'을 'Login'화면으로 바꾸든 다른 버튼으로 바꾸도록한다.
 * 2. 'Member' Gui, Class 구현하기.
 * 3. Css 
 */
