package Manager.Java.Controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ScooterManagementController implements Initializable {

	@FXML
	ListView<String> scooterListView;
	@FXML
	Button scooterInfoButton;
	@FXML
	Button scooterLocationButton;
	@FXML
	Button backButton;
	@FXML
	Button addButton;
	@FXML
	Button checkButton;
	@FXML
	TextField scooterID;
	@FXML
	TextField scooterXLocation;
	@FXML
	TextField scooterCheckID;
	
	// 스쿠터의 위치 표현을 위한 String 배열
	private String[] locationArray = {"정문", "공과대학 2호관", "공과대학 3호관", 
			"공과대학 4호관","공과대학 5호관", "공과대학 1호관",
			"경상대학", "도서관", "백마교양관", "인문대학", "서문"};
	
	private ObservableList<String> scooterList;
	private FilteredList<String> filteredList;
	private boolean updateListenerStopFlag;
	
	// 소켓 관련 필드
	private String userID;
	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	@FXML Button closeButton;
	
	// 로그아웃을 위한 ID, Socket 그리고 만들기 귀찮아서 넘긴 output, input 스트림들
	// 그리고 ScooterManagement를 위한 데이터를 미리 로드하는 작업을 수행하는 메소드
	public void setField(String ID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream) throws InterruptedException {
		this.userID = ID;
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		
		// 실시간 업데이트를 위해 updateListenerStopFlag를 false로 만듬.
		updateListenerStopFlag = false;
		
		// 스쿠터 리스트를 로드해서 scooterList에 저장함.
		findScooterList();
		
		// 실시간 업데이트를 위한 Thread를 실행시킴.
		updateListener();
	}
	
	@Override // 초기화
	public void initialize(URL location, ResourceBundle resources){
		// scooterList에 observarbleArrayList 할당 후, scooterListView에 넣어주기.
		scooterList = FXCollections.observableArrayList();
		scooterListView.setItems(scooterList);
		
		// 검색을 위한 filteredList 할당.
		filteredList = new FilteredList<String>(scooterList);
		
	}
	
	@FXML // 뒤로가기에 쓰이는 메소드
	public void backButtonHandler(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			
			Parent root = (Parent)loader.load();
			ManagerController controller = loader.getController();
			
			Scene scene = new Scene(root);
			
			// scooterInformation과 관련된 FXML파일에  userID, socket, Stream들을 할당시킨다.
			controller.setField(userID, socket, outputStream, inputStream);
			Stage primaryStage = (Stage) checkButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
			
			// updateListener를 멈추기 위해 Flag를 true로 만든다음 의미없는 쿼리를 보낸다.
			updateListenerStopFlag = true;
			outputStream.writeUTF("Scooter findScooterList"); // 스레드가 멈출 수 있게 의미없는 쿼리를 보낸다.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML // 스쿠터 추가할 때 쓰이는 메소드
	public void addButtonHandler() {
		
		// 아이디랑 x좌표를 받아와서 추가한다.
		String addScooterID = scooterID.getText();
		String addScooterLocation = scooterXLocation.getText();
		if (!addScooterID.equals("") && !addScooterLocation.equals("")) {
			new Alert(Alert.AlertType.CONFIRMATION, "Scooter을 /를 정말 추가하시겠습니까?", ButtonType.OK,
					ButtonType.CANCEL);
			//Optional<ButtonType> result = alert.showAndWait();
			try {
				outputStream.writeUTF("Scooter add "+ addScooterID + " " + addScooterLocation);
				scooterID.setText("");
				scooterXLocation.setText("");
				String addToList = 
						"ID : "+ addScooterID
						+ "\n현재 위치 : "
						+ locationArray[Integer.parseInt(addScooterLocation)]
						+ "\n사용 가능 여부 : "
						+ "0";
				scooterList.add(addToList);
			} catch (Exception e) {
				new Alert(Alert.AlertType.INFORMATION, "추가 실패!", ButtonType.CLOSE).show();
			}
		} else {
			new Alert(Alert.AlertType.WARNING, "Scooter의 정보를 모두 입력해주세요.", ButtonType.CLOSE).show();
		}
	}

	@FXML // 스쿠터 조회시 쓰이는 메소드
	public void checkButtonHandler() {
		filteredList.setPredicate(new Predicate<String>() {
			@Override
			public boolean test(String t) {
				if (t.contains(scooterCheckID.getText())) {
					return true;
				}
				return false;
			}
		});
		scooterListView.setItems(filteredList);
	}



	// 실시간 업데이트를 위한 리스너메소드.
	public void updateListener() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
					receive();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // FX스레드를 데몬으로 실행시켜서 닫는 창을 눌렀을 때 child thread들을 같이 종료시킬 수 있음.
        thread.start();
	}
	
	void receive() throws InterruptedException {
        while (true) {
        	if(updateListenerStopFlag) {
        		break;
        	}
            try {
            	
            	// 10 이라는 정수를 받으면 현재 scooterList를 갱신한다.
                int data = inputStream.readInt();
                
                if(data == 10) {
                	Platform.runLater(() -> {
                    	scooterList.clear();
                	});
                	findScooterList();
                }
            } catch (IOException e) {
                break;
            }
        }
    }
	
	// 스쿠터 리스트에 보여줄 데이터를 가져오는 메소드
	public void findScooterList() throws InterruptedException{
		String resultStatus = null;
		try {
			
			// 서버가 클라이언트로부터 오는 쿼리들을 놓침을 방지한다.
			Thread.sleep(100);

			outputStream.writeUTF("Scooter findScooterList");
			
			resultStatus = inputStream.readUTF();
			if(resultStatus.equals("-1")) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 데이터를 가공해서 scooterList에 넣어준다.
		StringTokenizer allScooterList = new StringTokenizer(resultStatus, "/");
		while(allScooterList.hasMoreTokens()) {
			StringTokenizer scooter = new StringTokenizer(allScooterList.nextToken(), ";");
			String scooterID = scooter.nextToken();
			String scooterLocation = locationArray[Integer.parseInt(scooter.nextToken())];
			String scooterNowUse = scooter.nextToken();
			if(scooterNowUse.equals("0")) {
				scooterNowUse = "사용 가능";
			} else {
				scooterNowUse = "사용 중";
			}
			String addToList = "ID : "+ scooterID
								+ "\n현재 위치 : "
								+ scooterLocation
								+ "\n사용 가능 여부 : "
								+ scooterNowUse;
			Platform.runLater(() -> {
				scooterList.add(addToList);
			});
		}
	}
	@FXML
	public void closeButtonHandler() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}

	@FXML public void LabelDragged(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}

	@FXML public void LabelPressed(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}
	@FXML public void LabelReleased(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}
}
