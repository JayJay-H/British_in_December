package Manager.Java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ScooterManagementGuiController {

	@FXML
	ListView scooterListView;
	@FXML
	Button scooterInfoButton;
	@FXML
	Button scooterLocationButton;
	@FXML
	Button backButton;
	@FXML
	TextField scooterID;
	@FXML
	TextField scooterXLocation;
	@FXML
	TextField scooterYLocation;
	@FXML
	Button addButton;

	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@FXML
	public void scooterInfoButtonHandler() {

	}

	@FXML
	public void scooterLocationButtonHandler() {
	}

	@FXML
	public void addButtonHandler() {
	}

}
