package gcs.appmain;

import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import gcs.capture.CaptureController;
import gcs.configurations.ConfigurationsController;
import gcs.ctrlstat.CtrlstatController;
import gcs.hud.HudViewController;
import gcs.mission.MissionCalculator;
import gcs.mission.MissionController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppMainController implements Initializable{

	public static AppMainController instance;
	@FXML public HBox hboxStatGroup;
	@FXML public Label lblAutopilotVersion;
	@FXML private ImageView imgBtnHLD;
	public static Stage hlDialog;
	@FXML public Label lblHomeLat;
	@FXML public Label lblHomeLng;
	@FXML public Label lblLat;
	@FXML public Label lblLng;
	@FXML public Label lblBattery;
	@FXML public Label lblGps;
	@FXML public Label lblCheckContestPrep;
	private String checkContestPrep;
	@FXML public Label lblDate;
	@FXML public Label lblTime;
	private Date dateTime;
	private Format dateFormatter;
	private Format timeFormatter;
		 
	@FXML private HBox hboxBtnConnect;
	@FXML private ImageView imgConnectStatus;
	@FXML private Label lblConnect;
	private String connectBtnString;
	private String periodString;
	private int periodNumber;
	private boolean connectStatus;
	private String connectColor;

	@FXML private ImageView imgBtnHome;
	@FXML private ImageView imgBtnGallery;
	@FXML private ImageView imgBtnConfig;
	@FXML private ImageView imgBtnInfo;
	private String menuMode;
	
	@FXML private VBox vboxDistGroup; 
	@FXML private ImageView imgBtnBLD;
	public static Stage distCalcDialog;
	public double homeLat, homeLng, homeAlt;
	public double wpLat, wpLng, wpAlt;
	public double dpLat, dpLng, dpAlt;	
	@FXML private TextField txtHd;
	@FXML private TextField txtWd;
	@FXML private TextField txtLd;
	
	@FXML private ToggleGroup captureGroup;
	@FXML private RadioButton radioBtnPicture;
	@FXML private RadioButton radioBtnRecord;
	@FXML private ImageView imgBtnCapture;
	private String captureMode;
	private boolean recordStatus;
	
	@FXML private HBox homePane;
	@FXML private HBox capturePane;
	@FXML private HBox configPane;
	@FXML private HBox infoPane;
	
	private Parent hudView;
	private Parent ctrlstatView;
	private Parent missionView;
	private Parent configView;
	
	@FXML private VBox homeLeftPane;
	@FXML public Pane homeHudPane;
	@FXML private VBox homeRightPane;
	
	public WritableImage snapImage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		initStatusBar();
		initValue();
		initRootMenuPane();
		executeCheckContestPrepThread();
		
		imgBtnHLD.setOnMouseClicked(imgBtnHomeLocationDialogOpenHandler);
		imgBtnBLD.setOnMouseClicked(imgBtnBaseLocationDialogOpenHandler);
		initBaseLocationValue();
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
	}
	
	private void initStatusBar() {
		// 상태표시줄 라벨 초기화
		hboxStatGroup.setVisible(false);
		lblAutopilotVersion.setText("");
		lblHomeLat.setText("");
		lblHomeLng.setText("");
		lblLat.setText("");
		lblLng.setText("");
		lblBattery.setText("");
		lblGps.setText("");
		lblCheckContestPrep.setText("");
		
		// 날짜, 시간 포맷 완성
		dateFormatter = new SimpleDateFormat("E, dd MMM yyyy",new Locale("en", "US"));
        timeFormatter = new SimpleDateFormat("HH:mm:ss");
	}
	
	private void initValue() {
		// 초기값 설정S
		connectStatus = false;
		connectColor = "red";
		menuMode = "home";
		captureMode = "picture";
		recordStatus = false;
	}
	
	private void initRootMenuPane() {
		// 연결 버튼 초기화
		imgConnectStatus.setImage(new Image(getClass().getResource("../images/trafficlight_red.png").toExternalForm()));
		// 연결 버튼 이벤트 설정
		hboxBtnConnect.setOnMouseClicked(hboxBtnConnectHandler);
		// 메뉴 이미지 초기화
		imgBtnHome.setImage(new Image(getClass().getResource("../images/home_selected.png").toExternalForm()));
		imgBtnGallery.setImage(new Image(getClass().getResource("../images/gallery_unselected.png").toExternalForm()));
		imgBtnConfig.setImage(new Image(getClass().getResource("../images/config_unselected.png").toExternalForm()));
		imgBtnInfo.setImage(new Image(getClass().getResource("../images/info_unselected.png").toExternalForm()));
		// pane전환 버튼 이벤트 설정
		imgBtnHome.setOnMouseClicked(imgBtnHomeHandler);
		imgBtnGallery.setOnMouseClicked(imgBtnGalleryHandler);
		imgBtnConfig.setOnMouseClicked(imgBtnConfigHandler);
		imgBtnInfo.setOnMouseClicked(imgBtnInfoHandler);
		
		// 캡처 모드 라디오버튼 속성감시 설정 설정
		captureGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue == radioBtnPicture) {captureMode = "picture";}
			else if( newValue == radioBtnRecord) {captureMode = "record";}
		});
		
		// 캡처 버튼 초기화
		imgBtnCapture.setImage(new Image(getClass().getResource("../images/capture_unselected.png").toExternalForm()));
		// 캡처 버튼 이벤트 설정
		imgBtnCapture.setOnMouseClicked(imgBtnCaptureHandler);
		
		// 기능별 pane 초기화
		// fxml 가져오기
		try {
			hudView = FXMLLoader.load(getClass().getResource("../hud/hudview.fxml"));
			homeHudPane.getChildren().add(hudView);
			
			ctrlstatView = FXMLLoader.load(getClass().getResource("../ctrlstat/ctrlstat.fxml"));
			homeLeftPane.getChildren().add(ctrlstatView);
			
        	missionView = FXMLLoader.load(getClass().getResource("../mission/mission.fxml"));
        	homeRightPane.getChildren().add(missionView);
        	
        	configView = FXMLLoader.load(getClass().getResource("../configurations/configurations.fxml"));
        	configPane.getChildren().add(configView);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private void executeCheckContestPrepThread() {
		Thread checkContestPrepThread = new Thread(()->{
			while(true) {
				try {
					UAV uav = Network.getUav();
					if(!uav.armed) {
						// 전자석용 화물 on && NFZ on && 자동이륙프로세스 on && 비행DB on
						if(uav.cargoStatus && MissionController.instance.isNoFlyZone
						   && MissionController.instance.autoStatus && HudViewController.saveDataFlag) { 
							checkContestPrep = "UPLOAD & START MISSION";
						} else {
							checkContestPrep = "NOT READY";
						}
					} else {
						checkContestPrep = "OPERATING...";
					}
					
					Thread.sleep(500);
				} catch(Exception e) {
					checkContestPrep = "NOT CONNECTED";
				}
			}
		});
		checkContestPrepThread.setDaemon(true);
		checkContestPrepThread.start();
	}
	
	class ViewLoop extends AnimationTimer{
		@Override
		public void handle(long now) {
            dateTime = new Date();
            lblDate.setText(dateFormatter.format(dateTime));
            lblTime.setText(timeFormatter.format(dateTime));
        }
    }
	
	public boolean isConnectStatus() {
		return connectStatus;
	}
	
	public void viewStatus(UAV uav) {
		try {
			Platform.runLater(()-> {
				lblAutopilotVersion.setText(uav.autopilotVersion);
				lblHomeLat.setText(String.valueOf(uav.homeLat));
				lblHomeLng.setText(String.valueOf(uav.homeLng));
				lblLat.setText(String.valueOf(uav.latitude));
				lblLng.setText(String.valueOf(uav.longitude));
				lblBattery.setText(String.valueOf(uav.batteryLevel + "% / "
				+ uav.batteryVoltage + "V / " + uav.batteryCurrent + "A"));
				lblGps.setText(CtrlstatController.instance.gpsFixType + " / " + uav.gpsSatellites);
				lblCheckContestPrep.setText(checkContestPrep);
				
				// 계산
				double homeDistance, wpDistance, dpDistance;
				try {
					homeDistance = MissionCalculator.distance3D(uav.latitude, homeLat,
							uav.longitude, homeLng, uav.altitude, homeAlt);
					wpDistance = MissionCalculator.distance3D(uav.latitude, wpLat,
							uav.longitude, wpLng, uav.altitude, wpAlt);
					dpDistance = MissionCalculator.distance3D(uav.latitude, dpLat,
							uav.longitude, dpLng, uav.altitude, dpAlt);
					txtHd.setText(String.valueOf(homeDistance));
					txtWd.setText(String.valueOf(wpDistance));
					txtLd.setText(String.valueOf(dpDistance));
				} catch(Exception e) {
					System.out.println("거리 값 파싱불가");
				}
				
			});
			CtrlstatController.instance.setStatus(uav);
			HudViewController.instance.setStatus(uav);
			MissionController.instance.setStatus(uav);
			if(ConfigurationsController.settingsRenewal) {
				ConfigurationsController.instance.setStatus(Network.getUav());
				ConfigurationsController.settingsRenewal = false;
			}
//			LowerCamViewerController.instance.setStatus(uav);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private EventHandler<Event> hboxBtnConnectHandler = (e) -> {
		if(connectStatus && connectColor.equals("green")) {
			// 연결 끊기
			Network.disconnect();
			// 미연결 상태로 상태값 변경
			connectStatus = false;
			ConfigurationsController.instance.setDisableEditable(connectStatus);
			connectColor = "red";
			imgConnectStatus.setImage(new Image(getClass().getResource("../images/trafficlight_red.png").toExternalForm()));
			lblConnect.setText("Connect");
			hboxStatGroup.setVisible(false);
			vboxDistGroup.setVisible(false);
		} else if(!connectStatus && connectColor.equals("red")) {
			// 연결 시작
			Network.connect();
			ConfigurationsController.settingsNotice = false;
			connectColor = "yellow";
			imgConnectStatus.setImage(new Image(getClass().getResource("../images/trafficlight_yellow.png").toExternalForm()));
			Thread loadParamsThread = new Thread(()-> {   
				int connectWaitTime = 0;
				periodString = "";
				periodNumber = 0;
				connectBtnString = "Connecting";
				try {
					while(!Network.getUav().settingsStatus) {
							// 신호등 노란색으로
							Network.getUav().settingsDownload(); // 세팅정보 요청
							if(periodNumber < 3) {
								periodNumber++;
								periodString += ".";
							} else {
								periodNumber = 0;
								periodString = "";
							}
							Platform.runLater(() -> {lblConnect.setText(connectBtnString + periodString);}); // 연결중 버튼 UI 변경
							Thread.sleep(1000); // 세팅정보 요청 주기 1초 부여
							connectWaitTime++;
							if(connectWaitTime>=10) {
								// 연결 안되었다는 dialog창 띄우기
								Thread.currentThread().interrupt(); // 10초 이상 미연결 시, 연결 해제
							}
	               	}
					// param값 수신 시, 연결 상태로 상태값 변경
	               	connectStatus = true;
	               	ConfigurationsController.instance.setDisableEditable(connectStatus);
	               	ConfigurationsController.instance.initNetworkTxt();
	               	connectColor = "green";
		   			Platform.runLater(()->{
		   				imgConnectStatus.setImage(new Image(getClass().getResource("../images/trafficlight_green.png").toExternalForm()));
			   			lblConnect.setText("Disconnect");
			   			hboxStatGroup.setVisible(true);
			   			vboxDistGroup.setVisible(true);
		   			});
				} catch(Exception e2) {
					// param값 미 수신 시, 미연결 상태로 상태값 변경
					connectWaitTime = 0;
					connectStatus = false;
					connectColor = "red";
					Platform.runLater(()->{
						imgConnectStatus.setImage(new Image(getClass().getResource("../images/trafficlight_red.png").toExternalForm()));
						lblConnect.setText("Connect");
					});
					Network.disconnect();
				}
	        });
	        loadParamsThread.start();
		}
	};
	
	private EventHandler<Event> imgBtnHomeHandler = (e) -> {
		if(!menuMode.equals("home")) {
			ConfigurationsController.settingsNotice = false;
			menuMode = "home";
			// 메뉴 이미지 변경
			imgBtnHome.setImage(new Image(getClass().getResource("../images/home_selected.png").toExternalForm()));
			imgBtnGallery.setImage(new Image(getClass().getResource("../images/gallery_unselected.png").toExternalForm()));
			imgBtnConfig.setImage(new Image(getClass().getResource("../images/config_unselected.png").toExternalForm()));
			imgBtnInfo.setImage(new Image(getClass().getResource("../images/info_unselected.png").toExternalForm()));
			
			homePane.setVisible(true);
			capturePane.setVisible(false);
			configPane.setVisible(false);
			infoPane.setVisible(false);
			
			HudViewController.instance.toolTip(false);
			CtrlstatController.instance.toolTip(false);
			MissionController.instance.toolTip(false);	
			
			MissionController.instance.toolTipVBox.setVisible(false);
		}
	};
	
	private EventHandler<Event> imgBtnGalleryHandler = (e) -> {
		if(!menuMode.equals("capture")) {
			ConfigurationsController.settingsNotice = false;
			menuMode = "capture";
			// 메뉴 이미지 변경
			imgBtnHome.setImage(new Image(getClass().getResource("../images/home_unselected.png").toExternalForm()));
			imgBtnGallery.setImage(new Image(getClass().getResource("../images/gallery_selected.png").toExternalForm()));
			imgBtnConfig.setImage(new Image(getClass().getResource("../images/config_unselected.png").toExternalForm()));
			imgBtnInfo.setImage(new Image(getClass().getResource("../images/info_unselected.png").toExternalForm()));
			
			homePane.setVisible(false);
			capturePane.setVisible(true);
			configPane.setVisible(false);
			infoPane.setVisible(false);
			
			HudViewController.instance.toolTip(false);
			CtrlstatController.instance.toolTip(false);
			MissionController.instance.toolTip(false);	
			
			MissionController.instance.toolTipVBox.setVisible(false);
		}
	};
	
	private EventHandler<Event> imgBtnConfigHandler = (e) -> {
		if(!menuMode.equals("config")) {
			if(!ConfigurationsController.settingsNotice) {
				Platform.runLater(() -> {
					ConfigurationsController.instance.lblNotice.setText("");;
				});
			}
			if(connectStatus) {
				ConfigurationsController.instance.setStatus(Network.getUav());
				ConfigurationsController.settingsNotice = false;
			} else {
				// 미 연결 시, uav로부터 받은 param값 갱신X
			}
			
			menuMode = "config";
			// 메뉴 이미지 변경
			imgBtnHome.setImage(new Image(getClass().getResource("../images/home_unselected.png").toExternalForm()));
			imgBtnGallery.setImage(new Image(getClass().getResource("../images/gallery_unselected.png").toExternalForm()));
			imgBtnConfig.setImage(new Image(getClass().getResource("../images/config_selected.png").toExternalForm()));
			imgBtnInfo.setImage(new Image(getClass().getResource("../images/info_unselected.png").toExternalForm()));
			
			homePane.setVisible(false);
			capturePane.setVisible(false);
			configPane.setVisible(true);
			infoPane.setVisible(false);
			
			HudViewController.instance.toolTip(false);
			CtrlstatController.instance.toolTip(false);
			MissionController.instance.toolTip(false);	
			
			MissionController.instance.toolTipVBox.setVisible(false);
		}
	};
	
	private EventHandler<Event> imgBtnInfoHandler = (e) -> {
		if(!menuMode.equals("info")) {
			ConfigurationsController.settingsNotice = false;
			menuMode = "info";
			// 메뉴 이미지 변경
			imgBtnHome.setImage(new Image(getClass().getResource("../images/home_unselected.png").toExternalForm()));
			imgBtnGallery.setImage(new Image(getClass().getResource("../images/gallery_unselected.png").toExternalForm()));
			imgBtnConfig.setImage(new Image(getClass().getResource("../images/config_unselected.png").toExternalForm()));
			imgBtnInfo.setImage(new Image(getClass().getResource("../images/info_selected.png").toExternalForm()));
			
			homePane.setVisible(true);
			capturePane.setVisible(false);
			configPane.setVisible(false);
			infoPane.setVisible(false);
			
			HudViewController.instance.toolTip(true);
			CtrlstatController.instance.toolTip(true);
			MissionController.instance.toolTip(true);	
			MissionController.instance.missionVBox.setVisible(false);
			MissionController.instance.fenceVBox.setVisible(false);
			MissionController.instance.noFlyZoneVBox.setVisible(false);
			MissionController.instance.consoleVBox.setVisible(false);
			MissionController.instance.toolTipVBox.setVisible(true);
		}
	};

	private EventHandler<Event> imgBtnCaptureHandler = (e) -> {
		if(captureMode.equals("picture")) {
			System.out.println("사진 캡처 완료");
			CaptureController.captureImage();

		} else if(captureMode.equals("record") && !recordStatus){
			System.out.println("영상 캡처 시작");
			imgBtnCapture.setImage(new Image(getClass().getResource("../images/capture_selected.png").toExternalForm()));
			recordStatus = true;
//			CaptureController.startRecoding();
			// 영상 캡처 진행
			// (추가요망)
		} else if(captureMode.equals("record") && recordStatus) {
			
			imgBtnCapture.setImage(new Image(getClass().getResource("../images/capture_unselected.png").toExternalForm()));
			recordStatus = false;
//			CaptureController.stopRecoding();
			System.out.println("영상 캡처 완료");
			// 영상 캡처 종료
			// (추가요망)
		}
	};

	private EventHandler<Event> imgBtnHomeLocationDialogOpenHandler = (e) -> {
		hlDialog = new Stage();
		hlDialog.initModality(Modality.WINDOW_MODAL);
		hlDialog.initOwner(AppMain.instance.primaryStage);
		hlDialog.setWidth(320);
		hlDialog.setHeight(240);
		hlDialog.setTitle("Set Home Location of UAV...");
			
		try {
			Parent root = FXMLLoader.load(getClass().getResource("dialog_hl/homelocationdialog.fxml")); 
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
			hlDialog.setScene(scene);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		hlDialog.setResizable(false);
		hlDialog.show();
			
	};
	
	private EventHandler<Event> imgBtnBaseLocationDialogOpenHandler = (e) -> {
		distCalcDialog = new Stage();
		distCalcDialog.initModality(Modality.WINDOW_MODAL);
		distCalcDialog.initOwner(AppMain.instance.primaryStage);
		distCalcDialog.setWidth(600);
		distCalcDialog.setHeight(240);
		distCalcDialog.setTitle("Set base location to calculate 3D distance from UAV location...");
			
		try {
			Parent root = FXMLLoader.load(getClass().getResource("dialog_distcalc/distcalcdialog.fxml")); 
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
			distCalcDialog.setScene(scene);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		distCalcDialog.setResizable(false);
		distCalcDialog.show();
	};
	
	private void initBaseLocationValue() {
		homeLat = 37.171208;	homeLng = 128.471961;	homeAlt = 0;
		wpLat = 37.171039;		wpLng = 128.471128;		wpAlt = 30;
		dpLat = 37.171039;		dpLng = 128.471961;		dpAlt = 0;
	}
}
