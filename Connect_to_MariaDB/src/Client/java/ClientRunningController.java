package Client.java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class ClientRunningController implements Initializable {

	private Timeline timeline;
	private long milliseconds;

	@FXML
	Label useTime; // 이용시간
	@FXML
	Label cost; // 이용요금
	@FXML
	Button ReturnBotton;
	@FXML
	Button startBotton;

	// socket 관련 필드
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private String userID;
	private String scooterID;
	
	public void setField(String userID, String scooterID, Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
		this.userID = userID;
		this.scooterID = scooterID;
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO 매초마다 1분식 이용시간 증가, 이용시간 5분마다 위치변화 +-1, 이용요금 +1000원

		KeyFrame keyFrame = new KeyFrame(Duration.ONE, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				useTime.setText(millisecondsToTime(milliseconds));
				cost.setText(millisecondsToCost(milliseconds));

			}
		});
		timeline = new Timeline(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(false);
	}

	private String millisecondsToTime(long milliseconds) {
		int h = 0, m = 0, s = 0;

		this.milliseconds++;

		s = (int) ((milliseconds / 1000) % 60);
		m = (int) ((milliseconds / 1000 / 60) % 60);
		h = (int) (milliseconds / 1000 / 3600);

		return String.format("%02d:%02d:%02d", h, m, s);
	}

	private String millisecondsToCost(long milliseconds) {
		int m = 0;

		//this.milliseconds++;

		m = (int) ((milliseconds / 1000 / 60) % 60);

		return String.format("%d 원", m * 500 + 1000);
	}
	
	private void updateScooterLocation() {
		
		int nowLocation = (int)(Math.random()*11); // 무작위 위치로 간다.
		
		try {
			outputStream.writeUTF("Scooter changeScooterLocation "+scooterID+" "+nowLocation); 	// 스쿠터의 위치 변경을 DB에 저장한다.
			outputStream.writeUTF("Scooter changeScooterNowUse " + scooterID + " 0");			// 스쿠터를 사용가능하다는 것을 DB에 저장한다.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void startAction() {
		timeline.play();
		startBotton.setDisable(true);
	}
	
	@FXML
	public void returnScooter() throws InterruptedException, IOException {
		timeline.stop();
		updateScooterLocation();
		
		new Alert(Alert.AlertType.INFORMATION, cost.getText()+"\n결제되셨습니다.", ButtonType.CLOSE).show();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/resource/ClientMain.fxml"));
			
			Parent root = (Parent)loader.load();
			ClientMainController controller = loader.getController();
			
			Scene scene = new Scene(root);
			controller.setField(userID, socket, outputStream, inputStream);
			Stage primaryStage = (Stage) ReturnBotton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Client");
			
			outputStream.writeUTF("Scooter changeScooterUse "+ userID +" 0"); // 사용자가 스쿠터를 사용하지 않음을 DB에 저장한다.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
