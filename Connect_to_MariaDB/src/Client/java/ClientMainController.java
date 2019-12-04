package Client.java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import Manager.Java.Controllers.ManagerController;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class ClientMainController implements Initializable {
	@FXML
	Label idLabel;
	@FXML
	ListView<String> scooterListview;
	@FXML
	Button SelectBotton;
	@FXML
	Label numOfScooter;

	private ObservableList<String> scooterList;
	
	// socket 관련 필드
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private String userID;
	
	public void setField(String userID, Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
		this.userID = userID;
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		findCanUseScooter();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			idLabel.setText(userID);
		});
		
		Platform.runLater(() -> {
			numOfScooter.setText(numOfScooter());
		});
		scooterList = FXCollections.observableArrayList();
		scooterListview.setItems(scooterList);
	}

	@FXML
	public void selectScooter() {
		// TODO 해당 스쿠터가 이용 가능 할때만 사용할 수 있게 바꿔야함

		String selectScooter = scooterListview.getSelectionModel().getSelectedItem();
		if (selectScooter == null) {
			new Alert(Alert.AlertType.WARNING, "선택된 스쿠터가 없습니다.", ButtonType.CLOSE).show();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/resource/ClientRunning.fxml"));
			
			Parent root = (Parent)loader.load();
			ClientRunningController controller = loader.getController();
			
			Scene scene = new Scene(root);
			controller.setField(userID, socket, inputStream, outputStream);
			Stage primaryStage = (Stage) SelectBotton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("ClientRunning");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void findCanUseScooter() {
		String resultStatus = null;
		try {
			outputStream.writeUTF("Scooter findScooterList");
			resultStatus = inputStream.readUTF();
			
			if(resultStatus.equals("-1")) {
				throw new IOException();
			}
		} catch (IOException e) {
			new Alert(Alert.AlertType.INFORMATION, "스쿠터 데이터를 불러오는 중 오류가 생겼습니다!", ButtonType.CLOSE).show();
		}
		StringTokenizer allScooterList = new StringTokenizer(resultStatus, "/");
		while(allScooterList.hasMoreTokens()) {
			String scooterID;
			String location;
			String scooterNowUse;
			StringTokenizer scooter = new StringTokenizer(allScooterList.nextToken(), ";");
			scooterID = scooter.nextToken();
			location = scooter.nextToken();
			scooterNowUse = scooter.nextToken();
			if(scooterNowUse.equals("0")) {
				scooterList.add(scooterID);
			}
		}
		
	}
	
	public String numOfScooter() {
		String number = "총" + ((Integer) scooterList.size()).toString() + " 대";
		return number;
	}

}
