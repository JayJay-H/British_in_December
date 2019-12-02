package Client.java;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ClientRunningController implements Initializable{

	@FXML
	Label useTime; // 이용시간
	@FXML
	Label cost; // 이용요금
	@FXML
	Button ReturnBotton;

	@FXML
	public void returnScooter() {
		// TODO 스쿠터 반납 할떄 DB에서 해당 스쿠터 위치변환 밑 멤버의 위치 변화
		// 서버에서  돌아가야하는 부분임... 해결방법좀여
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Client/resource/ClientMain.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) ReturnBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO 매초마다 1분식 이용시간 증가, 이용시간 5분마다 위치변화 +-1, 이용요금 +1000원
		
	}

}
