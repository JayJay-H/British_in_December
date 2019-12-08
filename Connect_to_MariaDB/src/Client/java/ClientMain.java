package Client.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/Client/resource/ClientMain.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Client/resource/ClientMainForm.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ClientMain");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
