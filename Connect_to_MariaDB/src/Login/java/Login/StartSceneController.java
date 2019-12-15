package Login.java.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class StartSceneController implements Initializable {
	@FXML
	AnchorPane startScene;
	@FXML
	ImageView startImage;

	@Override
	public void initialize(URL location, ResourceBundle resoruces) {
		startImage.setOnMouseClicked(evnet -> makeFadeOut());
	}

	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(startScene);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);

		fadeTransition.setOnFinished((ActionEvent event) -> {
			try {
				loginStart();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		fadeTransition.play();
		/*
		 * Thread loginStart = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { try { Parent root = Scene scene = new
		 * Scene(root); Stage primaryStage = (Stage) startButton.getScene().getWindow();
		 * primaryStage.setTitle("Login"); primaryStage.setScene(scene); } catch
		 * (Exception e) { e.printStackTrace(); } } }); loginStart.sleep(1000);
		 * loginStart.start();
		 */
	}

	private void loginStart() throws IOException {
		try {
			Parent secondView = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(secondView);
			Stage primaryStage = (Stage) startScene.getScene().getWindow();
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
