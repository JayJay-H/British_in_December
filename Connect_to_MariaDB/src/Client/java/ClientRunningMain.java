package Client.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientRunningMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/Client/resource/ClientRunning.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Client/resource/ClientRunningForm.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ClientRunningMain");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
