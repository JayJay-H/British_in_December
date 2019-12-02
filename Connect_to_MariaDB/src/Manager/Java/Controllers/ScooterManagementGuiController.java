package Manager.Java.Controllers;

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

	private String selectedScooterID;
	private String selectedScooterLocation;
	private String selectedScooterNowUse;

	// 데이터베이스에 저장되어있는 scooterID와 비교해서 같은 스쿠터라면 그 스쿠터의 정보를 확인하게 한다.
	public String getScooterID() {
		int selectedIndex = scooterListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "항목을 선택하세요.", ButtonType.CLOSE).show();
			return null;
		} else {
			String[] scooterIDInfo = scooterList.get(selectedIndex).split(" ");
			String[] IDInfo = scooterIDInfo[2].split("\n");
			selectedScooterID = IDInfo[0];
			System.out.println(selectedScooterID);
			return selectedScooterID;
		}
	}

	public String getScooterLocation() {
		int selectedIndex = scooterListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "항목을 선택하세요.", ButtonType.CLOSE).show();
			return null;
		} else {
			String[] scooterLocationInfo = scooterList.get(selectedIndex).split(" ");
			String[] locationInfo = scooterLocationInfo[5].split("\n");
			selectedScooterLocation = locationInfo[0];
			System.out.println(selectedScooterLocation);
			return selectedScooterLocation;
		}
	}

	public String getScooterNowUse() {
		int selectedIndex = scooterListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "항목을 선택하세요.", ButtonType.CLOSE).show();
			return null;
		} else {
			String[] usingInfo = scooterList.get(selectedIndex).split(" ");
			selectedScooterNowUse = usingInfo[8];
			System.out.println(selectedScooterNowUse);
			return selectedScooterNowUse;
		}
	}

	@FXML
	public void backButtonHandler(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addButtonHandler() {
		String addScooterID = scooterID.getText();
		String addScooterXLocation = scooterXLocation.getText();

		if (!addScooterID.equals("") && !addScooterXLocation.equals("")) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Scooter을 /를 정말 추가하시겠습니까?", ButtonType.OK,
					ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				scooterList.add("ScooterID : " + addScooterID + "\nScooter의 X좌표 : " + addScooterXLocation
						+ "\nScooter 사용정보 : 사용가능");
				scooterListView.setItems(scooterList);
				scooterID.setText("");
				scooterXLocation.setText("");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scooterList = FXCollections.observableArrayList();
		// 스쿠터 리스트 예시
		scooterList.add("ScooterID : exampleScooter" + "\nScooter의 X좌표 : 0" + "\nScooter 사용정보 : 사용가능");
		scooterListView.setItems(scooterList);
		filteredList = new FilteredList<String>(scooterList);
		
	}

	/*
	 * #해야할 일 1. scooterList를 데이터 베이스와 연결해서 추가를 하면 데이터베이스에도 scooter가 추가될 수 있도록 한다.
	 * 2. scooterListView에서 선택한 scooter마다의 정보를 출력할 수 있도록 해야 한다.
	 */
}
