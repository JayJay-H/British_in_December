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
	
	private String[] locationArray = {"정문", "공과대학 2호관", "공과대학 3호관", 
			"공과대학 4호관","공과대학 5호관", "공과대학 1호관",
			"경상대학", "도서관", "백마교양관", "인문대학", "서문"};
	
	private ObservableList<String> scooterList;
	private FilteredList<String> filteredList;
	
	private String userID;
	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public void setField(String ID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
		this.userID = ID;
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		findScooterList();
		updateListener();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		scooterList = FXCollections.observableArrayList();
		scooterListView.setItems(scooterList);
		filteredList = new FilteredList<String>(scooterList);
		
	}
	
	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			
			Parent root = (Parent)loader.load();
			ManagerController controller = loader.getController();
			
			Scene scene = new Scene(root);
			controller.setField(userID, socket, outputStream, inputStream);
			Stage primaryStage = (Stage) checkButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addButtonHandler() {
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

	@FXML
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
                receive();
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true); // FX스레드를 데몬으로 실행시켜서 닫는 창을 눌렀을 때 child thread들을 같이 종료시킬 수 있음.
        thread.start();
	}
	
	void receive() {
        while (true) {
            try {
            	
            	// "Update"라는 문자열을 받으면 현재 scooterList를 갱신한다.
                String data = inputStream.readUTF();
                //System.out.println(data);
                
                if(data.equals("Update")) {
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
	
	public void findScooterList(){
		String resultStatus = null;
		scooterList.clear();
		try {
			outputStream.writeUTF("Scooter findScooterList");
			resultStatus = inputStream.readUTF();
			if(resultStatus.equals("-1")) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringTokenizer allScooterList = new StringTokenizer(resultStatus, "/");
		while(allScooterList.hasMoreTokens()) {
			StringTokenizer scooter = new StringTokenizer(allScooterList.nextToken(), ";");
			String scooterID = scooter.nextToken();
			String scooterLocation = locationArray[Integer.parseInt(scooter.nextToken())];
			String scooterNowUse = scooter.nextToken();
			
			String addToList = "ID : "+ scooterID
								+ "\n현재 위치 : "
								+ scooterLocation
								+ "\n사용 가능 여부 : "
								+ scooterNowUse;
			scooterList.add(addToList);
		}
	}
}
