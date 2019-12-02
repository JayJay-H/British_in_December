package Manager.Java.Controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.FocusModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MemberManagementGuiController implements Initializable {

	@FXML
	ListView<String> memberListView;
	@FXML
	Button backButton;
	@FXML
	TextField memberIDTextField;
	@FXML
	Button findMemberIDButton;
	@FXML
	Button checkMemberInfoButton;
	@FXML
	Button deleteMemberInfoButton;

	private ObservableList<String> memberList;
	private FilteredList<String> filteredMemberList;

	@FXML
	public void backButtonHandler() {
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

	// TODO ID를 입력하고 조회버튼을 누르면 입력된 ID를 리스트에서 focus한다
	@FXML
	public void findMemberIDButtonHandler() {
		FocusModel<String> member = new FocusModel<String>() {
			@Override
			protected String getModelItem(int arg0) {
				return null;
			}

			@Override
			protected int getItemCount() {
				
				return 0;
			}
		};
		memberListView.setFocusModel(member);
	}

	@FXML
	public void checkMemberInfoButtonHandler() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/MemberInformationGui.fxml"));
			Scene scene = new Scene(root);
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteMemberInfoButtonHandler() {
		int selectedIndex = memberListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "삭제할 항목을 선택하세요.", ButtonType.CLOSE).show();
		} else {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "정말 삭제하시겠습니까?", ButtonType.OK, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				memberList.remove(selectedIndex);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberList = FXCollections.observableArrayList();
		//에시 ID
		memberList.add("example ID");
		memberListView.setItems(memberList);
		filteredMemberList = new FilteredList<String>(memberList);
	}

}
