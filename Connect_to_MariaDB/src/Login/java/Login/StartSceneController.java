package Login.java.Login;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class StartSceneController implements Initializable {
	@FXML
	AnchorPane startScene;
	@FXML
	Button startButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startButton.setOnAction(e -> handleStartButton(e));
	}

	public void handleStartButton(ActionEvent event) {
		try {
			Parent login = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			StackPane root = (StackPane) startButton.getScene().getRoot();
			root.getChildren().add(login);

			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(login.opacityProperty(), 0.5);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(0.5), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("안녕");
				}
			}, keyValue);
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
