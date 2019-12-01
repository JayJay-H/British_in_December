package Manager.Java.Controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ScooterManagementGuiController implements Initializable {

	@FXML
	ListView<String> scooterListView;
	@FXML
	Button scooterInfoButton;
	@FXML
	Button scooterLocationButton;
	@FXML
	Button backButton;
	@FXML
	TextField scooterID;
	@FXML
	TextField scooterXLocation;
	@FXML
	TextField scooterYLocation;
	@FXML
	Button addButton;

	private ObservableList<String> scooterList;
	
	public String getScooterInfo() {
		int selectedIndex = scooterListView.getSelectionModel().getSelectedIndex();
		String scooterInfo = scooterList.get(selectedIndex); 
		return scooterInfo;
	}
			
	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void scooterInfoButtonHandler(ActionEvent event) {
		getScooterInfo();
		/*
		 * 현재 scooter의 정보가 return된 상태다. String형의 scooter 정보와 데이터베이스의 scooter객체와의 비교를 통해 선택된 scooter의 정보를 다음 창에서 보여주도록 해야 한다.
		 * String형이 아니라 새로운 Scooter객체를 만들어야 할지도 모르겠다.  
		 */
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/scooterInformationGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void scooterLocationButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/scooterLocationGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addButtonHandler() {
		String addScooterID = scooterID.getText();
		String addScooterXLocation = scooterXLocation.getText();
		String addScooterYLocation = scooterYLocation.getText();
		
		if(!addScooterID.equals("") && !addScooterXLocation.equals("") && !addScooterYLocation.equals("")) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Scooter을 /를 정말 추가하시겠습니까?", ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				scooterList.add("ScooterID : " + addScooterID + "\nScooter의 X좌표 : " + addScooterXLocation + "\nScooter의 Y좌표 : "
						+ addScooterYLocation);
				scooterListView.setItems(scooterList);
				scooterID.setText("");
				scooterXLocation.setText("");
				scooterYLocation.setText("");
			}
		}
		else {
			new Alert(Alert.AlertType.WARNING, "Scooter의 정보를 모두 입력해주세요.", ButtonType.CLOSE).show();
			return ;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scooterList = FXCollections.observableArrayList();
		//스쿠터 리스트 예시
		scooterList.add("ScooterID : exampleScooter" + "\nScooter의 X좌표 : 0" + "\nScooter의 Y좌표 : 0");
		scooterListView.setItems(scooterList);
	}
	
	/*
	 * #해야할 일
	 * 1. scooterList를 데이터 베이스와 연결해서 추가를 하면 데이터베이스에도 scooter가 추가될 수 있도록 한다.
	 * 2. scooterListView에서 선택한 scooter마다의 정보를 출력할 수 있도록 해야 한다.
	 */
}
