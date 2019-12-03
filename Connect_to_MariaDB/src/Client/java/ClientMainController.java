package Client.java;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClientMainController implements Initializable{

	@FXML
	TextArea currentLocation;
	@FXML
	ListView<String> scooterListview;
	@FXML
	Button SelectBotton;
	@FXML
	Label numOfScooter;

	private ObservableList<String> scooterList;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO DB에서 이용가능한 스쿠터 받아오는 방식으로 수정
		// 필요한 메서드는 Client 클래스에 있음
		scooterList = FXCollections.observableArrayList();
		scooterListview.setItems(scooterList);
	}

	@FXML
	public void selectScooter() {
		// TODO 해당 스쿠터가 이용 가능 할때만 사용할 수 있게 바꿔야함
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Client/resource/ClientRunning.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) SelectBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
