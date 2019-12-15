package Login.java.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StartSceneMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Stage dialog = new Stage(StageStyle.UNDECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(primaryStage);
			
			Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource("/Login/resource/StartSceneGUI.fxml"));
			Scene scene = new Scene(root);
			dialog.setScene(scene);
			dialog.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
