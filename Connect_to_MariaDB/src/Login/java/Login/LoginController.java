package Login.java.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LoginController {

	@FXML
	TextField IdTextField;
	@FXML
	PasswordField PassTextField;
	@FXML
	Button RegisterBotton;
	@FXML
	Button LoginBotton;
	@FXML
	Label LoginErrTextLabel;

	@FXML
	public void register() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/RegisterGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) RegisterBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void login() {
		String ID = IdTextField.getText();
		String Pass = PassTextField.getText();
		boolean IdErr = false, PassErr = false, LoginErr = false; // 아이디 비밀번호, 로그인에 오류가 없는지 확인
		boolean admin = false; // Manager로 로그안 하면 true

		// TODO 로그인 하는 로직 작성 - 재헌씨 부탁 드려요
		/*
		*/
		if (admin && !LoginErr) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/Manager/Main/ManagerGuiMain.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) RegisterBotton.getScene().getWindow();
				primaryStage.setScene(scene);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!admin && !LoginErr){
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/Client/resource/ClientMain.fxml"));
				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) RegisterBotton.getScene().getWindow();
				primaryStage.setScene(scene);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			LoginErrTextLabel.setText("로그인 실패");
		}

	}

}
