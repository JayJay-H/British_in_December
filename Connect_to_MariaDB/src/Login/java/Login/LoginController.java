package Login.java.Login;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

import Client.java.ClientMainController;
import Client.java.ClientRunningController;
import Manager.Java.Controllers.ManagerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class LoginController implements Initializable {

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
	ToggleGroup whoareyou;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    
    public void initialize(URL location, ResourceBundle resources) {
        // 클라이언트 시작
        startClient();
    }
    
	
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
	public void login() throws IOException {
		String ID = IdTextField.getText();
		String Pass = PassTextField.getText();

		// 멤버인지 매니저인지 확인한 후 로그인 진행.
		if(whoareyou.getSelectedToggle() == null) {
			new Alert(Alert.AlertType.WARNING, "사용자로 로그인하실지 매니저로 로그인하실지 정해주세요!", ButtonType.CLOSE).show();
		} else {
			
			// 멤버라면
			if(whoareyou.getSelectedToggle().getUserData().toString().equals("Client")) {
				String loginStatus = null; // 로그인 상태를 받아온다.
				try {
					outputStream.writeUTF("Member "+ID+" "+Pass); // 쿼리를 보냄.
					loginStatus = inputStream.readUTF(); //쿼리를 받음.
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 로그인이 성공할 시 클라이언트 세션에 들어감.
				if(loginStatus.equals("0")) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/resource/ClientMain.fxml"));
						
						Parent root = (Parent)loader.load();
						ClientMainController controller = loader.getController();
						
						Scene scene = new Scene(root);
						controller.setField(ID, socket, outputStream, inputStream);
						Stage primaryStage = (Stage) LoginBotton.getScene().getWindow();
						primaryStage.setScene(scene);
						primaryStage.setTitle("Client");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					switch(loginStatus) {
						case "1":
							LoginErrTextLabel.setText("DB관련 오류");
							break;
						case "2":
							LoginErrTextLabel.setText("비밀번호가 틀립니다.");
							break;
						case "3":
							LoginErrTextLabel.setText("이미 사용 중인 아이디입니다.");
							break;
						case "4":
							LoginErrTextLabel.setText("존재하지 않는 아이디입니다.");
							break;
					}
				}	// 매니저일 때.
			} else if(whoareyou.getSelectedToggle().getUserData().toString().equals("Manager")){
				String loginStatus = null;
				try {
					outputStream.writeUTF("Manager "+ID+" "+Pass);
					loginStatus = inputStream.readUTF();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(loginStatus.equals("0")) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
						
						Parent root = (Parent)loader.load();
						ManagerController controller = loader.getController();
						
						Scene scene = new Scene(root);
						controller.setField(ID, socket, outputStream, inputStream);
						Stage primaryStage = (Stage) RegisterBotton.getScene().getWindow();
						primaryStage.setScene(scene);
						primaryStage.setTitle("Manager");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					switch(loginStatus) {
						case "1":
							LoginErrTextLabel.setText("DB관련 오류");
							break;
						case "2":
							LoginErrTextLabel.setText("비밀번호가 틀립니다.");
							break;
						case "3":
							LoginErrTextLabel.setText("이미 사용 중인 아이디입니다.");
							break;
						case "4":
							LoginErrTextLabel.setText("존재하지 않는 아이디입니다.");
							break;
					}
				}
			}
		}
	}
	
	void startClient() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                	socket = new Socket();
                    socket.connect(new InetSocketAddress("cs-cnu.tk", 8000));
                    Platform.runLater(() -> {
                        LoginErrTextLabel.setText("반갑습니다!");
                    });
                    
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
    	Platform.runLater(() -> {
            LoginErrTextLabel.setText("서버가 열려있지 않습니다.");
        });
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
