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
	}
	
	public void findScooterList(){
		String resultStatus = null;
		scooterList.clear();
		try {
			outputStream.writeUTF("Scooter findScooterList");
			resultStatus = inputStream.readUTF();
			if(resultStatus.equals("-1")) {
				throw new IOException("DB error");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringTokenizer sList = new StringTokenizer(resultStatus, "/");
		while(sList.hasMoreTokens()) {
			scooterList.add(sList.nextToken());
		}
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
		boolean addStatus = false;
		if (!addScooterID.equals("") && !addScooterLocation.equals("")) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Scooter을 /를 정말 추가하시겠습니까?", ButtonType.OK,
					ButtonType.CANCEL);
			//Optional<ButtonType> result = alert.showAndWait();
			try {
				outputStream.writeUTF("Scooter add "+ addScooterID + " " + addScooterLocation);
				addStatus = inputStream.readBoolean();
				if(!addStatus) {
					throw new Exception();
				}
			} catch (Exception e) {
				new Alert(Alert.AlertType.INFORMATION, "추가 실패!", ButtonType.CLOSE).show();
			}
			if(addStatus) {
				/*
				if (result.get() == ButtonType.OK) {
					scooterList.add(addScooterID 
									+ "의 위치 : " 
									+ addScooterLocation
									+ "\n사용상태 : 0"
								);
					scooterListView.setItems(scooterList);
					scooterID.setText("");
					scooterXLocation.setText("");
				}
				*/
				findScooterList();
			}
		} else {
			new Alert(Alert.AlertType.WARNING, "Scooter의 정보를 모두 입력해주세요.", ButtonType.CLOSE).show();
		}
	}

	@FXML
	public void checkButtonHandler() {
		findScooterList();
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

	@Override
	public void initialize(URL location, ResourceBundle resources){
		scooterList = FXCollections.observableArrayList();
		scooterListView.setItems(scooterList);
		filteredList = new FilteredList<String>(scooterList);
		
	}

	/*
	 * #해야할 일 1. scooterList를 데이터 베이스와 연결해서 추가를 하면 데이터베이스에도 scooter가 추가될 수 있도록 한다.
	 * 2. scooterListView에서 선택한 scooter마다의 정보를 출력할 수 있도록 해야 한다.
	 */
}
