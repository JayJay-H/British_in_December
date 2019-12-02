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
import javafx.scene.control.Button;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MemberManagementGuiController implements Initializable{

	@FXML ListView<String> memberListView;
	@FXML Button backButton;
	@FXML TextField memberIDTextField;
	@FXML Button findMemberIDButton;
	@FXML Button checkMemberInfoButton;
	@FXML Button deleteMemberInfoButton;
	@FXML public void backButtonHandler() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			Scene scene = new Scene(root);;
			Stage primaryStage = (Stage) backButton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Manager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML public void findMemberIDButtonHandler() {
		/*
		 * ID를 입력하고 조회버튼을 누르면 입력된 ID를 리스트에서 focus한다
		 */
		FocusModel<String> member = new FocusModel<String>() {
			@Override
			protected String getModelItem(int arg0) {
				return null;
			}
			@Override
			protected int getItemCount() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		memberListView.setFocusModel(member);
	}
	@FXML public void checkMemberInfoButtonHandler() {
		
	}
	@FXML public void deleteMemberInfoButtonHandler() {
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
