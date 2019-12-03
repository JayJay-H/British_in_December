package Manager.Java.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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
	

	@FXML
	public void backButtonHandler() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/MemberManagementGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("MemberManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
