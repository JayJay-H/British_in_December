package Login.java.Register;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

public class RegisterController implements Initializable {

	@FXML
	Label ErrorMessageLabel;
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
	
	private Socket socket;	
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
	
	public void initialize(URL location, ResourceBundle resources) {
        // 클라이언트 시작
        startClient();
    }

	@FXML
	public void register() {
		String ID = IdTextField.getText();
		String Pass = PassTextField.getText();
		String PassVerify = PassVerifyTextField.getText();
		
		boolean signUpStatus = true;
		boolean PassStatus = false;

		// 비밀번호 필드랑 비밀번호 확인 필드랑 같은 입력인지 확인
		if (!Pass.equals(PassVerify)) {
			PassStatus = false;
			ErrorMessageLabel.setText("재입력 부분이 틀립니다.");
		} else {
			PassStatus = true;
		}
		
		
		// 패스워드 에러가 없다면
		if(PassStatus) {
			try {
				outputStream.writeUTF("SignUp "+ID+" "+Pass);
				signUpStatus = inputStream.readBoolean();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (!signUpStatus) {
				ErrorMessageLabel.setText("사용불가능한 아이디 입니다.");
			}
			
			// 회원가입 성공, 실패시 메시지 출력
			if (signUpStatus && PassStatus) {
				ErrorMessageLabel.setText("회원가입 성공!");
				try {
					closeAction();
					Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
					Scene scene = new Scene(root);
					Stage primaryStage = (Stage) CancleBotton.getScene().getWindow();
					primaryStage.setScene(scene);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void cancle() {
		try {
			closeAction();
			Parent root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) CancleBotton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void startClient() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                	socket = new Socket();
                    socket.connect(new InetSocketAddress("localhost", 8000));
                    
            		inputStream = new DataInputStream(socket.getInputStream());
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    disconnectServer();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

	void disconnectServer() {
        if (!socket.isClosed()) {
        	closeAction();
        }
    }
    
    public void closeAction() {
        try {
        	//만약 소켓이 안 닫혀 있다면 닫기
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
