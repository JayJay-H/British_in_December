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

		// TODO
		this.milliseconds++;

		s = (int) ((milliseconds / 1000) % 60);
		m = (int) ((milliseconds / 1000 / 60) % 60);
		h = (int) (milliseconds / 1000 / 3600);

		return String.format("%02d:%02d:%02d", h, m, s);
	}

	private String millisecondsToCost(long milliseconds) {
		int m = 0;

		// TODO
		//this.milliseconds++;

		m = (int) ((milliseconds / 1000 / 60) % 60);

		return String.format("%d", m * 500 + 1000);
	}
	
	private void updateScooterLocation(long milliseconds) throws IOException {
		int m;
		m = (int) ((milliseconds / 1000 / 60) % 60);
		
		outputStream.writeUTF("Scooter findScooter "+ scooterID);
		String scooterInfoInput = inputStream.readUTF();
		
		StringTokenizer scooterInfo = new StringTokenizer(scooterInfoInput, ";");
		scooterInfo.nextToken();
		String scooterLocation = scooterInfo.nextToken();
		int pastLocation = Integer.parseInt(scooterLocation);
		int min = pastLocation - m;
		int max = 2*m + min;
		if(min < 0) min = 0;
		if(max > 10) max = 10;
		int nowLocation = (int)(Math.random()*(max - min +1)) + min;
		
		outputStream.writeUTF("Scooter changeScooterLocation "+scooterID+" "+nowLocation);
		//System.out.println(inputStream.readUTF());
		outputStream.writeUTF("Scooter changeScooterNowUse " + scooterID + " 0");
		inputStream.readUTF();
	}

	@FXML
	public void startAction() {
		timeline.play();
		startBotton.setDisable(true);
	}
	
	@FXML
	public void returnScooter() throws InterruptedException, IOException {
		// TODO 스쿠터 반납 할떄 DB에서 해당 스쿠터 위치변환 밑 멤버의 위치 변화
		// 서버에서 돌아가야하는 부분임... 해결방법좀여
		
		timeline.stop();
		updateScooterLocation(milliseconds);
		
		new Alert(Alert.AlertType.INFORMATION, cost.getText()+"원\n결제되셨습니다.", ButtonType.CLOSE).show();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/resource/ClientMain.fxml"));
			
			Parent root = (Parent)loader.load();
			ClientMainController controller = loader.getController();
			
			Scene scene = new Scene(root);
			controller.setField(userID, socket, outputStream, inputStream);
			Stage primaryStage = (Stage) ReturnBotton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Client");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
