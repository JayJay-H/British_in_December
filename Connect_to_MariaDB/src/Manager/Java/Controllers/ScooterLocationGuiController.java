package Manager.Java.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ScooterLocationGuiController {

	@FXML
	ImageView scooterLoationView;
	@FXML
	Button backButton;
	@FXML
	Button checkInfoButton;
	@FXML
	Label scooterIDLabel;
	@FXML
	Label scooterXLocationLabel;
	@FXML
	Label scooterYLocationLabel;

	@FXML
	public void backButtonHandler() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ScooterManagementGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void checkInfoButtonHandler() {
	}

	/*
	 * #해야할 일 1. scooter의 Location을 어떻게 나타낼 것인가. 2. scooterImageView의 좌표점선 나타내기 3.
	 * scooter의 위치별 하이퍼링크를 통한 정보확인. 4.
	 */
}
