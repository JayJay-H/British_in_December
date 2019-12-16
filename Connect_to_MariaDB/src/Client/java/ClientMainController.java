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
import javafx.scene.input.MouseEvent;
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
	@FXML
	Button CancleBotton;
	@FXML
	Button UseBotton;
	@FXML
	Label Title;
	@FXML
	Button LogoutButton;

	private ObservableList<String> scooterList;
	private ObservableList<String> bookedScooterList;
	private boolean receiveThreadStopFlag;
	private String[] locationArray = { "정문", "공과대학 2호관", "공과대학 3호관", "공과대학 4호관", "공과대학 5호관", "공과대학 1호관", "경상대학", "도서관",
			"백마교양관", "인문대학", "서문" };

	int scooterNowUse;
	// socket 관련 필드
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private String userID;
	@FXML
	Button closeButton;

	// 아이디를 받아와서 현재 어떤 아이디로 접속했는지를 알고, 소켓을 받아와서 로그아웃시 소켓을 닫아줘야함을 서버에 알려준다.
	// 또한 initialize하기 전에 이 메소드로 fxml에 필요한 데이터, 스레드들을 모두 받아오고 실행시킨다.
	public void setField(String userID, Socket socket, DataOutputStream outputStream, DataInputStream inputStream)
			throws InterruptedException {
		this.userID = userID;
		this.socket = socket;
		this.inputStream = inputStream; // input, output 스트림은 따로 만들기 귀찮아서 이렇게 받아온거다.
		this.outputStream = outputStream;

		// 실시간 업데이트를 받아야하므로 flag를 false로 바꾼다
		receiveThreadStopFlag = false;

		// 현재 스쿠터 리스트를 받아온다
		findCanUseScooter();

		/*
		 * 스쿠터의 수를 받아온다. 그냥 setText를 하면 오류가난다. javaFX와 관련된 스레드에 영향을 주기때문이다. 이것을 해결하려면
		 * Platform.runLater() 를 통해 임시 스레드를 만들고, 이 스레드로 스쿠터의 수를 받아온다.
		 */
		Platform.runLater(() -> {
			numOfScooter.setText(numOfScooter());
		});

		// 예약된 스쿠터 리스트를 초기화 시킨다.
		bookedScooterList.clear();

		// 실시간 업데이트를 위한 리스너 스레드를 시작시킨다.
		updateListener();
	}

	@Override // 초기화
	public void initialize(URL arg0, ResourceBundle arg1) {

		// setField의 스쿠터수 업데이트와 같은 이유로 Platform.runlater()를 사용한다.
		// 이렇게 안하면 스레드 겹친다고 이클립스가 화낸다.
		Platform.runLater(() -> {
			idLabel.setText(userID);
		});

		// 스쿠터 리스트와 예약된 스쿠터 리스트를 observableArrayList로 만들고 각각의 ListView에 할당시킨다.
		scooterList = FXCollections.observableArrayList();
		bookedScooterList = FXCollections.observableArrayList();

		scooterListview.setItems(scooterList);
		bookedScooterListView.setItems(bookedScooterList);
	}

	@FXML // 스쿠터를 사용할 때, 즉 진짜 돈을 측정하는 부분으로 넘어가는 버튼에 쓰인다.
	public void selectScooter() {

		// bookedListView에서 선택된 스쿠터를 받아와서 ClientRunning 컨트롤러에 던져준다.
		// 왜냐면 사용을 종료했을 때, 이를 데이터베이스에 반영시켜야하기 때문이다.
		String selectScooter = bookedScooterListView.getSelectionModel().getSelectedItem();
		if (selectScooter == null) {
			new Alert(Alert.AlertType.WARNING, "선택된 스쿠터가 없습니다.", ButtonType.CLOSE).show();
			return;
		}
		try {
			// ListView에서 스쿠터 선택했을 때, 아이디 하나 쪼개서 넣어줄라고 이런짓을 한다.
			StringTokenizer scooterString = new StringTokenizer(selectScooter, " ");
			scooterString.nextToken();
			StringTokenizer scooterInfo = new StringTokenizer(scooterString.nextToken(), "\n");
			String scooterID = scooterInfo.nextToken();

			// 아이디를 얻었으면 이를 Running컨트롤러에 던져준다.
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/resource/ClientRunning.fxml"));

			Parent root = (Parent) loader.load();
			ClientRunningController controller = loader.getController();

			Scene scene = new Scene(root);

			// 마찬가지로 setField를 통해 Running과 관련된 FXML에 필요한 데이터들을 미리 로드시킨다.
			controller.setField(userID, scooterID, socket, inputStream, outputStream);
			Stage primaryStage = (Stage) SelectBotton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("ClientRunning");

			/*
			 * receive 스레드멈추기 안 멈추면 Running에서 다시 돌아왔을 때 오류가 난다. Running에서 Main으로 넘어올 때
			 * scooterList를 갱신하기 위해 findCanUseScooter()를 실행시키는데 findCanUseScooter()에서는 메소드에
			 * 있는 그 inputStream으로 받아야하지만 이 녀석이 while(true)로 인해 계속 돌고있기 때문에 그 데이터를 뺏어다가
			 * Update 문자열인지 아닌지 확인하고 버린다. 따라서 그 스레드를 멈추지 않으면 정작 가야할 곳에 데이터가 안간다.
			 */
			receiveThreadStopFlag = true;
			outputStream.writeUTF("Scooter changeScooterUse " + userID + " 1"); // 사용자가 스쿠터를 사용 중임을 DB에 저장한다.
			outputStream.writeUTF("nothing !"); // 스레드가 멈출 수 있게 의미없는 쿼리를 보낸다.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML // 스쿠터 예약 관련 메소드
	public void bookScooter() throws IOException, InterruptedException {
		// selectedIndex를 통해 선택했나 안 했나 체크.
		int selectedIndex = scooterListview.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "예약하실 스쿠터를 선택하세요.", ButtonType.CLOSE).show();
			return;
		}

		// 선택한 칸에 대한 정보를 저장
		String selectedScooter = scooterListview.getSelectionModel().getSelectedItem();

		// 예약할 시 scooterList에서 선택한 스쿠터 삭제, bookedList에 추가
		// 선택한 스쿠터에 대한 nowUse를 1로 바꾸어 데이터 베이스에 저장.
		if (bookedScooterList.size() < 1) {
			StringTokenizer scooterString = new StringTokenizer(selectedScooter, " ");
			scooterString.nextToken();
			StringTokenizer scooterInfo = new StringTokenizer(scooterString.nextToken(), "\n");
			String scooterID = scooterInfo.nextToken();
			outputStream.writeUTF("Scooter getScooterNowUse " + scooterID);

			Thread.sleep(200);

			if (scooterNowUse == -100) {
				new Alert(Alert.AlertType.WARNING, "이미 사용중인 스쿠터 입니다.", ButtonType.CLOSE).show();
				return;
			}else if(scooterNowUse == 100){
				outputStream.writeUTF("Scooter changeScooterNowUse " + scooterID + " 1");
				
				String bookedScooter = scooterList.remove(selectedIndex);
				bookedScooterList.add(bookedScooter);
				numOfScooter.setText(numOfScooter());
			}
		} else {
			new Alert(Alert.AlertType.WARNING, "스쿠터는 한 대 이상 예약하실 수 없습니다.", ButtonType.CLOSE).show();
		}
	}

	@FXML // 예약취소관련 메소드
	public void cancleBookingScooter() throws IOException {
		// selectedIndex를 통해 선택했나 안 했나 체크.
		int selectedIndex = bookedScooterListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0) {
			new Alert(Alert.AlertType.WARNING, "예약취소하실 스쿠터를 선택하세요.", ButtonType.CLOSE).show();
			return;
		}

		// 선택한 칸에 대한 정보를 저장
		String selectedScooter = bookedScooterListView.getSelectionModel().getSelectedItem();

		// 선택한 스쿠터를 bookedScooterList에서 제거, scooterList에 추가.
		// 선택한 스쿠터에 대한 nowUse를 0으로 바꿈.
		StringTokenizer scooterString = new StringTokenizer(selectedScooter, " ");
		scooterString.nextToken();
		StringTokenizer scooterInfo = new StringTokenizer(scooterString.nextToken(), "\n");
		String scooterID = scooterInfo.nextToken();
		outputStream.writeUTF("Scooter changeScooterNowUse " + scooterID + " 0");
		String bookedScooter = bookedScooterList.remove(selectedIndex);
		scooterList.add(bookedScooter);
		numOfScooter.setText(numOfScooter());
	}

	// 로그아웃
	@FXML
	public void Logout() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/Login/resource/LoginGUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Login/resource/LoginForm.css").toExternalForm());
			Stage primaryStage = (Stage) CancleBotton.getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			closeAction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			// 스쿠터를 사용할 때, 실시간 업데이트 종료
			if (receiveThreadStopFlag) {
				break;
			}
			try {

				// "Update"라는 문자열을 받으면 현재 scooterList를 갱신한다.
				int data = inputStream.readInt();

				if (data == 10) {
					Platform.runLater(() -> {
						scooterList.clear();
					});
					findCanUseScooter();
					Platform.runLater(() -> {
						numOfScooter.setText(numOfScooter());
					});
				}

				if (data == 100) {
					scooterNowUse = data;
				}

				if (data == -100) {
					scooterNowUse = data;
				}
			} catch (IOException e) {
				break;
			}
		}
	}

	// scooterList를 갱신하는 메소드
	public void findCanUseScooter() throws InterruptedException {
		String inputScooterList = null;
		try {
			// 스쿠터 리스트를 데이터베이스에서 가져온다.
			// 못가져왔다면 IOException시킴.

			// 서버가 클라이언트로부터 오는 쿼리들을 놓침을 방지한다.
			Thread.sleep(100);
			
			outputStream.writeUTF("Scooter findScooterList");
			inputScooterList = inputStream.readUTF();
			if (inputScooterList.equals("-1")) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.INFORMATION, "스쿠터 데이터를 불러오는 중 오류가 생겼습니다!", ButtonType.CLOSE).show();
		}

		// 받아온 리스트를 가공하여 scooterList에 넣는다.
		StringTokenizer allScooterList = new StringTokenizer(inputScooterList, "/");
		while (allScooterList.hasMoreTokens()) {
			String scooterID;
			String location;
			String scooterNowUse;

			StringTokenizer scooter = new StringTokenizer(allScooterList.nextToken(), ";");
			scooterID = scooter.nextToken();
			location = locationArray[Integer.parseInt(scooter.nextToken())];
			scooterNowUse = scooter.nextToken();

			if (scooterNowUse.equals("0")) {
				Platform.runLater(() -> {
					scooterList.add("ID: " + scooterID + "\n위치: " + location + " ");
				});
			}
		}

	}

	// 스쿠터가 총 몇개인지를 String으로 반환시킴.
	public String numOfScooter() {
		String number = "총 " + ((Integer) scooterList.size()).toString() + " 대";
		return number;
	}

	// 로그아웃시 소켓을 닫는 메소드
	public void closeAction() {
		try {

			// 만약 소켓이 안 닫혀 있다면 닫기
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void closeButtonHandler() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void LabelDragged(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}

	@FXML
	public void LabelPressed(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}

	@FXML
	public void LabelReleased(MouseEvent event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.setX(event.getScreenX());
		stage.setY(event.getScreenY());
	}

}
