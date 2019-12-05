package Client.java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;

import Manager.Java.Controllers.ManagerController;
import Server_Class.MemberManagement.memberManagement;
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
	ListView<String> bookedScooterListView;
	@FXML
	Button SelectBotton;
	@FXML
	Label numOfScooter;

	private ObservableList<String> scooterList;
	private ObservableList<String> bookedScooterList;
	
	// socket 관련 필드
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private String userID;
	
	public void setField(String userID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
		this.userID = userID;
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		findCanUseScooter();
		Platform.runLater(() -> {
			numOfScooter.setText(numOfScooter());
		});
		updateListener();
	}
	
	@Override // 초기화
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			idLabel.setText(userID);
		});
		
		scooterList 		= FXCollections.observableArrayList();
		bookedScooterList 	= FXCollections.observableArrayList();
		
		scooterListview.setItems(scooterList);
		bookedScooterListView.setItems(bookedScooterList);
	}

	@FXML // 스쿠터 사용
	public void selectScooter() {

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
	
	@FXML
	public void bookScooter() throws IOException {
		int selectedIndex = scooterListview.getSelectionModel().getSelectedIndex();
		String selectedScooter = scooterListview.getSelectionModel().getSelectedItem();
		if(selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "예약하실 스쿠터를 선택하세요.", ButtonType.CLOSE).show();
			return;
		}
		if(bookedScooterList.size()<1) {
			StringTokenizer scooter = new StringTokenizer(selectedScooter, " ");
			scooter.nextToken();
			String scooterID = scooter.nextToken();
			outputStream.writeUTF("Scooter changeScooterNowUse "+ scooterID + " 1");
			
			String bookedScooter = scooterList.remove(selectedIndex);
			bookedScooterList.add(bookedScooter);
			numOfScooter.setText(numOfScooter());
		} else {
			new Alert(Alert.AlertType.WARNING, "스쿠터는 한 대 이상 예약하실 수 없습니다.", ButtonType.CLOSE).show();
		}
	}
	
	@FXML
	public void cancleBookingScooter() throws IOException {
		int selectedIndex = bookedScooterListView.getSelectionModel().getSelectedIndex();
		String selectedScooter = bookedScooterListView.getSelectionModel().getSelectedItem();
		if(selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "예약취소하실 스쿠터를 선택하세요.", ButtonType.CLOSE).show();
			return;
		}
		
		StringTokenizer scooter = new StringTokenizer(selectedScooter, " ");
		scooter.nextToken();
		String scooterID = scooter.nextToken();
		outputStream.writeUTF("Scooter changeScooterNowUse "+ scooterID + " 0");
		
		String bookedScooter = bookedScooterList.remove(selectedIndex);
		scooterList.add(bookedScooter);
		numOfScooter.setText(numOfScooter());
	}
	
	public void updateListener() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                receive();
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // FX스레드를 데몬으로 실행시켜서 닫는 창을 눌렀을 때 child thread들을 같이 종료시킬 수 있다.
        thread.start();
	}
	
	void receive() {
        while (true) {
            try {
                String data = inputStream.readUTF();
                
                switch(data) {
                case "Update":
                	Platform.runLater(() -> {
                    	scooterList.clear();
                	});
                	findCanUseScooter();
                	Platform.runLater(() -> {
                		numOfScooter.setText(numOfScooter());
                	});
                    break;
                }
            } catch (IOException e) {
                break;
            }
        }
    }
	
	public void findCanUseScooter() {
		String inputScooterList = null;
		try {
			outputStream.writeUTF("Scooter findScooterList");
			inputScooterList = inputStream.readUTF();
			
			if(inputScooterList.equals("-1")) {
				throw new IOException();
			}
		} catch (IOException e) {
			new Alert(Alert.AlertType.INFORMATION, "스쿠터 데이터를 불러오는 중 오류가 생겼습니다!", ButtonType.CLOSE).show();
		}
		StringTokenizer allScooterList = new StringTokenizer(inputScooterList, "/");
		while(allScooterList.hasMoreTokens()) {
			String scooterID;
			String location;
			String scooterNowUse;
			StringTokenizer scooter = new StringTokenizer(allScooterList.nextToken(), ";");
			scooterID = scooter.nextToken();
			location = scooter.nextToken();
			scooterNowUse = scooter.nextToken();
			if(scooterNowUse.equals("0")) {
				Platform.runLater(() -> {
					scooterList.add("ID: " + scooterID + " 위치: " + location + " ");
				});
			}
		}
		
	}
	
	public String numOfScooter() {
		String number = "총 " + ((Integer) scooterList.size()).toString() + " 대";
		return number;
	}

}
