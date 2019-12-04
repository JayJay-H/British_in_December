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

public class MemberManagementController implements Initializable {

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
	
	// Socket 관련 필드
	private String userID;
	private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    
    public void setField(String ID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
    	this.userID = ID;
    	this.socket = socket;
    	this.outputStream = outputStream;
    	this.inputStream = inputStream;
    	findUserList();
	}
    
    public void findUserList() {
    	String resultStatus = null;
		memberList.clear();
		System.out.println(1);
		try {
			outputStream.writeUTF("Member findMemberList");
			resultStatus = inputStream.readUTF();
			if(resultStatus.equals("-1")) {
				throw new IOException("DB error");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resultStatus);
		StringTokenizer mList = new StringTokenizer(resultStatus, "/");
		while(mList.hasMoreTokens()) {
			StringTokenizer ID = new StringTokenizer(mList.nextToken(), ";");
			memberList.add(ID.nextToken());
		}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberList = FXCollections.observableArrayList();
		memberListView.setItems(memberList);
		filteredMemberList = new FilteredList<String>(memberList);
	}
	
	@FXML // 뒤로가기
	public void backButtonHandler() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/ManagerGui.fxml"));
			
			Parent root = (Parent)loader.load();
			ManagerController controller = loader.getController();
			
			Scene scene = new Scene(root);
			controller.setField(userID, socket, outputStream, inputStream);
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
		filteredMemberList.setPredicate(new Predicate<String>() {
			@Override
			public boolean test(String t) {
				if (t.contains(memberIDTextField.getText())) {
					return true;
				}
				return false;
			}
		});
		memberListView.setItems(filteredMemberList);
	}

	@FXML // 사용자 정보 확인
	public void checkMemberInfoButtonHandler() {
		try {
			
			String selectedID = memberListView.getSelectionModel().getSelectedItem();
			System.out.println(selectedID);
			if(selectedID!=null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Resource/View/MemberInformationGui.fxml"));
				
				Parent root = (Parent)loader.load();
				Scene scene = new Scene(root);
				MemberInformationController controller = loader.getController();
				controller.setField(userID, socket, outputStream, inputStream, selectedID);
				
				Stage primaryStage = (Stage) backButton.getScene().getWindow();
				primaryStage.setScene(scene);
				primaryStage.setTitle("Member Information");
			} else {
				new Alert(Alert.AlertType.INFORMATION, "조회하고자 하는 ID를 선택해주세요!", ButtonType.CLOSE).show();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML // 사용자 삭제
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

}
