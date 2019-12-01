package Manager.Java.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class ScooterInformationGuiController {

	@FXML Button backButton;
	@FXML Label IDLabel;
	@FXML ProgressBar leftBattery;
	@FXML Label usedLabel;
	@FXML Label locationLabel;

	@FXML public void backButtonHandler() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ScooterManagementGui.fxml"));
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
