package Login.java.Register;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class RegisterController {

	@FXML
	Label IdErrorMessageLabel;
	@FXML
	Label PassErrorMessageLabel;
	@FXML
	Label PassVerifyTextLabel;
	@FXML
	Label RegisterErrorMessageTextLabel;
	@FXML
	TextField IdTextField;
	@FXML
	PasswordField PassVerifyTextField;
	@FXML
	PasswordField PassTextField;
	@FXML
	Button CancleBotton;
	@FXML
	Button RegisterBotton;
	@FXML
	Button ToLogin;

	@FXML
	public void cancle() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) CancleBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void register() {
		String ID = IdTextField.getText();
		String Pass = PassTextField.getText();
		String PassVerify = PassVerifyTextField.getText();

		boolean IdErr = false, PassErr = false, PassVerifyErr = false, RegisterErr = false;

		// 비밀번호 필드랑 비밀번호 확인 필드랑 같은 입력인지 확인
		if (Pass.equals(PassVerify)) {
			PassVerifyErr = false;
		} else {
			PassVerifyErr = true;
		}

		// TODO 회원가입 메소드 완성하기
		/*
		 * 사용자가 입력한 아이디가 중복인지 아닌지 체크 .
		 * 가능하면 IdErr = false.
		 * 아니면 IdErr = true.
		 * 출력 여기까지 해주시면 됩니다.
		 */
		if (IdErr) {
			IdErrorMessageLabel.setText("불가능합니다.");
		}
		if (PassErr) {
			PassErrorMessageLabel.setText("불가능합니다.");
		}
		if (PassVerifyErr) {
			PassVerifyTextLabel.setText("불일치합니다.");
		}
		// 회원가입 성고, 실패시 메시지 출력
		if (!IdErr && !PassErr && !PassVerifyErr) {
			RegisterErr = false;
			RegisterErrorMessageTextLabel.setText("회원가입 성공!");
		} else {
			RegisterErrorMessageTextLabel.setText("다시 시도 하세요.");
		}
	}

	@FXML
	public void toLogin() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) CancleBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
