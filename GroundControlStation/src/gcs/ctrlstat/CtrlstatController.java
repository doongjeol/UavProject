package gcs.ctrlstat;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import gcs.appmain.AppMainController;
import gcs.hud.HudViewController;
import gcs.mission.MissionController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CtrlstatController implements Initializable{
	public static CtrlstatController instance;
	@FXML public TextField txtAltitude;
	@FXML public Button btnArm;
	@FXML private Button btnDisarm;
	@FXML private Button btnChangeAlt;
	@FXML private Button btnTakeoff;
	@FXML private Button btnLand;
	@FXML private Button btnRtl;
	@FXML private Button btnResetHome;
	@FXML private Button btnGoTo;
	@FXML private Button btnAttach;
	@FXML private Button btnDettach;
	
	public boolean changeAltFlag;
	public boolean takeOffFlag;
	public boolean landFlag;
	public boolean rtlFlag;
	public boolean gotoFlag;
	public boolean homeResetFlag = true;
	
	@FXML private Label lblPosture;
	@FXML private Label lblPosition;
	@FXML private Label lblSpeed;
	@FXML private Label lblBattery;
	@FXML private Label lblGPS;
	@FXML private Label lblRngfnd;
	
	@FXML private Label lblRoll;
	@FXML private Label lblPitch;
	@FXML private Label lblYaw;
	@FXML private Label lblLat;
	@FXML private Label lblLng;
	@FXML private Label lblAlt;
	@FXML private Label lblAltAbs;	
	@FXML private Label lblHead;
	@FXML private Label lblAspeed;
	@FXML private Label lblGspeed;
	@FXML private Label lblVspeed;
	//@FXML private Label lblOpticalFlowQuality;	
	@FXML private Label lblRangeFinderDistance;
	@FXML private Label lblRangeFinderVoltage;
	
	@FXML private Label lblGpsHdop;
	@FXML private Label lblGpsVdop;
	@FXML private Label lblGpsFixType;
	public String gpsFixType;
	@FXML private Label lblGpsSatellites;
	@FXML private Label lblGpsTime;
	@FXML private Label lblBatPer;
	@FXML private Label lblBatVolt;
	@FXML private Label lblBatCur;
	
	@FXML private Label lblCpuLoadTip;
	@FXML private Label lblRamLoadTip;
	@FXML private Label lblNetUpSpeedTip;
	@FXML private Label lblNetDownSpeedTip;
	
	@FXML private Label lblCpuLoad;
	@FXML private Label lblRamLoad;
	@FXML private Label lblNetUpSpeed;
	@FXML private Label lblNetDownSpeed;
	
	@SuppressWarnings("rawtypes") @FXML private AreaChart chartCpuLoad;
	@SuppressWarnings("rawtypes") @FXML private AreaChart chartRamLoad;
	@SuppressWarnings("rawtypes") @FXML private AreaChart chartNetSpeed;
	@SuppressWarnings("rawtypes") @FXML private XYChart.Series lineCpuLoad;
	@SuppressWarnings("rawtypes") @FXML private XYChart.Series lineRamLoad;
	@SuppressWarnings("rawtypes") @FXML private XYChart.Series lineNetUpSpeed;
	@SuppressWarnings("rawtypes") @FXML private XYChart.Series lineNetDownSpeed;
	@FXML private NumberAxis AxisCpuLoad;
	@FXML private NumberAxis AxisRamLoad;
	@FXML private NumberAxis AxisNetSpeed;
	private float yvalueCpuLoad;
	private float yvalueRamLoad;
	private float yvalueNetUpSpeed;
	private float yvalueNetDownSpeed;	
	private int[] sequence;
	
	@FXML private TextField txtMicroControl;
	@FXML private Button btnUp;
	@FXML private Button btnDown;
	@FXML private Button btnLeft;
	@FXML private Button btnRight;
	@FXML private Button btnN;
	
	private Parent lowerCamViewer;
	@FXML private VBox vboxLower;
	
	String timerBuffer;
	public int secs;
	private int min, sec, milisec;
	public boolean timerStatus;
	private int btnArmCount;
	private double microControl = 0.7;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CtrlstatController.instance = this;
		initButton();
		initChart();
		txtMicroControl.setText(String.valueOf(microControl));
		
		try {
			lowerCamViewer = FXMLLoader.load(getClass().getResource("../lowercam/lowercamviewer.fxml"));
			vboxLower.getChildren().add(lowerCamViewer);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void initButton() {
		btnArm.setOnAction((e)-> {handleBtnArm(e);});
		btnChangeAlt.setOnAction((e)-> {handleBtnChangeAlt(e);});
		btnTakeoff.setOnAction((e)-> {handleBtnTakeoff(e);});
		btnLand.setOnAction((e)-> {handleBtnLand(e);});
		btnRtl.setOnAction((e)-> {handleBtnRtl(e);});
		btnResetHome.setOnAction((e)-> {handleBtnResetHome(e);});
		btnGoTo.setOnAction((e)->{handleBtnGoto(e);});
		btnAttach.setOnAction((e)-> {handleBtnAttach(e);});
		btnDettach.setOnAction((e)-> {handleBtnDettach(e);});
		
		btnUp.setGraphic(new ImageView(  getClass().getResource("../images/ctr_up.png").toExternalForm() ) );
		btnDown.setGraphic(new ImageView(  getClass().getResource("../images/ctr_down.png").toExternalForm() ) );
		btnLeft.setGraphic(new ImageView(  getClass().getResource("../images/ctr_left.png").toExternalForm() ) );
		btnRight.setGraphic(new ImageView(  getClass().getResource("../images/ctr_right.png").toExternalForm() ) );
		btnN.setGraphic(new ImageView(  getClass().getResource("../images/ctr_n.png").toExternalForm() ) );
		
		btnUp.setOnAction((e)-> {handleBtnUp(e);});
		btnDown.setOnAction((e)-> {handleBtnDown(e);});
		btnLeft.setOnAction((e)-> {handleBtnLeft(e);});
		btnRight.setOnAction((e)-> {handleBtnRight(e);});
		btnN.setOnAction((e)-> {handleBtnHeadingToNorth(e);});
	}
	
	public void initChart() {
		sequence = new int[3];
		for(int i : sequence) i = 0; // 초기화
		chartCpuLoad();
		chartRamLoad();
		chartNetSpeed();
	}
	
	// 드론 모드 조종 버튼 이벤트
	public void handleBtnArm(ActionEvent e) {
		if(btnArm.getText().equals("Arm")) {
			btnArmCount++;
			Network.getUav().arm();
		} else {
			Network.getUav().disarm();
		}
		changeAltFlag = false;
		takeOffFlag = false;
		landFlag = false;
		rtlFlag = false;
		gotoFlag = false;
	}
	
	public void handleBtnChangeAlt(ActionEvent e) {
		int altitude = Integer.parseInt(txtAltitude.getText());
		HudViewController.destAlt = altitude;
		changeAltFlag = true;
		takeOffFlag = false;
		landFlag = false;
		rtlFlag = false;
		gotoFlag = false;
		Network.getUav().changeAlt(altitude);
		MissionController.instance.setImgSelectFlag(false);
		if(btnArm.getText().equals("Disarm") && AppMainController.instance.isConnectStatus()) {
			if(!timerStatus) {
				secToHHMMSS();
			}
		}
	}
	
	public void handleBtnTakeoff(ActionEvent e) {
//		SpeechRecognition.instance.speech("이륙시키겠습니다");
		int altitude = Integer.parseInt(txtAltitude.getText());
		HudViewController.destAlt = altitude;
		changeAltFlag = false;
		takeOffFlag = true;
		landFlag = false;
		rtlFlag = false;
		gotoFlag = false;
		MissionController.instance.setImgSelectFlag(false);
		Network.getUav().takeoff(altitude);
		if(btnArm.getText().equals("Disarm") && AppMainController.instance.isConnectStatus()) {
			if(!timerStatus) {
				secToHHMMSS();
			}
		}
	}
	
	public void handleBtnLand(ActionEvent e) {
//		SpeechRecognition.instance.speech("착륙시키겠습니다");
		changeAltFlag = false;
		takeOffFlag = false;
		landFlag = true;
		rtlFlag = false;
		gotoFlag = false;
		MissionController.instance.setImgSelectFlag(false);
		Network.getUav().land();	
	}
	
	public void handleBtnRtl(ActionEvent e) {
		changeAltFlag = false;
		takeOffFlag = false;
		landFlag = false;
		rtlFlag = true;
		gotoFlag = false;
		MissionController.instance.setImgSelectFlag(false);
		
//		SpeechRecognition.instance.speech("복귀시키겠습니다");
		Network.getUav().rtl();
		MissionController.instance.rtl();
	}
	
	public void handleBtnResetHome(ActionEvent e) {
		btnArmCount=0;
		changeAltFlag = false;
		homeResetFlag=true;
		takeOffFlag = false;
		landFlag = false;
		rtlFlag = false;
		gotoFlag = false;
		MissionController.instance.setImgSelectFlag(false);
	}
	public void handleBtnGoto(ActionEvent e) {
		changeAltFlag = false;
		takeOffFlag = false;
		landFlag = false;
		rtlFlag = false;
		gotoFlag = true;
		MissionController.instance.setImgSelectFlag(false);
		
		MissionController.instance.gotoMake();
	}
	
	public void handleBtnAttach(ActionEvent e) {
		Network.getUav().luggageAttach();
	}
	
	public void handleBtnDettach(ActionEvent e) {
		Network.getUav().luggageDettach();
	}
	
	// 화물 미세 조종 버튼 이벤트
	public void handleBtnUp(ActionEvent e) {
		Network.getUav().move(5, 0, 0, getMicroControlValue());
	}

	public void handleBtnDown(ActionEvent e) {
		Network.getUav().move(-5, 0, 0, getMicroControlValue());
	}

	public void handleBtnLeft(ActionEvent e) {
		Network.getUav().move(0, -5, 0, getMicroControlValue());
	}

	public void handleBtnRight(ActionEvent e) {
		Network.getUav().move(0, 5, 0, getMicroControlValue());
	}
	
	// 화물 헤딩 조종 버튼 이벤트
	public void handleBtnHeadingToNorth(ActionEvent e) {
		Network.getUav().changeHeading(0);
	}
	
	public void setStatus(UAV uav) {
		if(btnArmCount==1) {
			Network.getUav().home(uav.latitude, uav.longitude, uav.altitude);
			HudViewController.destLat = uav.latitude;
			HudViewController.destLng = uav.longitude;
			HudViewController.destAlt = uav.altitude; // goto 좌표 초기화
			homeResetFlag = false;
			btnArmCount++;
		}
		Platform.runLater(() -> {
			lblRoll.setText(String.valueOf(Math.round(uav.roll*100)/100F));
			lblPitch.setText(String.valueOf(Math.round(uav.pitch*100)/100F));
			if(uav.yaw<0)
				uav.yaw += 360;
			lblYaw.setText(String.valueOf(Math.round(uav.yaw*100)/100F));
			
			lblLat.setText(String.valueOf(uav.latitude));
			lblLng.setText(String.valueOf(uav.longitude));
			lblAlt.setText(String.valueOf(uav.altitude));
			lblAltAbs.setText(String.valueOf(uav.altitudeAbs));
			
			lblHead.setText(String.valueOf(Math.round(uav.heading*100)/100F));
			lblAspeed.setText(String.valueOf(Math.round(uav.airSpeed*100)/100F));
			lblGspeed.setText(String.valueOf(Math.round(uav.groundSpeed*100)/100F));
			lblVspeed.setText(String.valueOf(Math.round(uav.verticalSpeed*100)/100F));
			
			lblGpsHdop.setText(String.valueOf(Math.round(uav.gpsHdop*100)/100F));
			lblGpsVdop.setText(String.valueOf(Math.round(uav.gpsVdop*100)/100F));
			switch(uav.gpsFixType) {
			case 1:
				gpsFixType = "NO_FIX";
				break;
			case 2:
				gpsFixType = "2D_FIX";
				break;
			case 3:
				gpsFixType = "3D_FIX";
				break;
			case 4:
				gpsFixType = "DGPS";
				break;
			case 5:
				gpsFixType = "RTK_FLOAT";
				break;
			case 6:
				gpsFixType = "RTK_FIXED";
				break;
			case 7:
				gpsFixType = "STATIC";
				break;
			case 8:
				gpsFixType = "PPP";
				break;
			}
			lblGpsFixType.setText(gpsFixType);
			lblGpsSatellites.setText(String.valueOf(uav.gpsSatellites));
			lblGpsTime.setText(String.valueOf(uav.gpsTime));
			
			//lblOpticalFlowQuality.setText(String.valueOf(Math.round(uav.opticalFlowQuality*100)/100F));
			lblRangeFinderVoltage.setText(String.valueOf(Math.round(uav.rangeFinderVoltage*100)/100F));
			lblRangeFinderDistance.setText(String.valueOf(Math.round(uav.rangeFinderDistance*100)/100F));
			
			if(uav.batteryLevel<0 || uav.batteryLevel>100) {
				uav.batteryLevel=0;
			}
			// 배터리가 일정량 줄어들면 폰트 색 변화
			if(uav.batteryLevel<=70 && uav.batteryLevel>50)
				lblBatPer.setStyle("-fx-text-fill: #ff9933;");
			else if(uav.batteryLevel<=50)
				lblBatPer.setStyle("-fx-text-fill: #ff0000;");
			else
				lblBatPer.setStyle("-fx-text-fill: #ffffff;");
			
			if(uav.batteryVoltage<=11.5 && uav.batteryVoltage>10.5)
				lblBatVolt.setStyle("-fx-text-fill: #ff9933;");
			else if(uav.batteryVoltage<=10.5)
				lblBatVolt.setStyle("-fx-text-fill: #ff0000;");
			else
				lblBatVolt.setStyle("-fx-text-fill: #ffffff;");
			lblBatPer.setText(uav.batteryLevel + "%");
			lblBatVolt.setText(uav.batteryVoltage + "V");
			lblBatCur.setText(uav.batteryCurrent + "A");
			
			if(uav.connected) {
				//btnUAVConnect.setText("UAV 연결 끊기");
				if(uav.armed) {
					btnArm.setText("Disarm");
				} else {
					btnArm.setText("Arm");
					timerStatus = false;
				}
			} else {
				//btnUAVConnect.setText("UAV 연결");
			}
			if(uav.rpiCpuLoad>0) {
				yvalueCpuLoad = Math.round(uav.rpiCpuLoad*100)/100F;
				yvalueRamLoad = Math.round(uav.rpiRamLoad*100)/100F;
				lblCpuLoad.setText(String.valueOf(yvalueCpuLoad) + "%");
				lblRamLoad.setText(String.valueOf(yvalueRamLoad) + "%");
				yvalueNetUpSpeed = Math.round(uav.rpiNetUpSpeed*100)/100F;
				yvalueNetDownSpeed = Math.round(uav.rpiNetDownSpeed*100)/100F;
				if(uav.rpiNetUpSpeed>1024) lblNetUpSpeed.setText(String.valueOf(Math.round(uav.rpiNetUpSpeed/1024*100)/100F + "Mbps"));
				else lblNetUpSpeed.setText(String.valueOf(yvalueNetUpSpeed) + "Kbps");
				if(uav.rpiNetDownSpeed>1024) lblNetDownSpeed.setText(String.valueOf(Math.round(uav.rpiNetDownSpeed/1024*100)/100F + "Mbps"));
				else lblNetDownSpeed.setText(String.valueOf(yvalueNetDownSpeed) + "Kbps");
			}
		});
		buttonCss();
	}
	
	public void buttonCss(){
		if(btnArm.getText().equals("Arm") || 
				!AppMainController.instance.isConnectStatus() || 
				MissionController.instance.isImgSelectFlag()) {
			changeAltFlag = false;
			takeOffFlag = false;
			landFlag = false;
			rtlFlag = false;
			gotoFlag = false;
		}
		if(changeAltFlag)
			btnChangeAlt.setStyle("-fx-background-color: #FF3C43;");
		else
			btnChangeAlt.setStyle("-fx-background-color: #103554;");
		if(takeOffFlag)
			btnTakeoff.setStyle("-fx-background-color: #FF3C43;");
		else
			btnTakeoff.setStyle("-fx-background-color: #103554;");
		if(landFlag)
			btnLand.setStyle("-fx-background-color: #FF3C43;");
		else
			btnLand.setStyle("-fx-background-color: #103554;");
		if(rtlFlag)
			btnRtl.setStyle("-fx-background-color: #FF3C43;");
		else
			btnRtl.setStyle("-fx-background-color: #103554;");
		if(gotoFlag)
			btnGoTo.setStyle("-fx-background-color: #FF3C43;");
		else
			btnGoTo.setStyle("-fx-background-color: #103554;");
		if(HudViewController.saveDataFlag)
			HudViewController.instance.btnSaveData.setStyle("-fx-background-color: #FF3C43;");
		else
			HudViewController.instance.btnSaveData.setStyle("-fx-background-color: #2C3034;");
	}
	
	//스톱워치
    public void secToHHMMSS() {
    	Thread thread = new Thread() {
    		@Override
    		public void run() {
    			timerStatus = true;
    			while(timerStatus) {	
    				secs++;
    				
    				milisec  = secs % 100;
    				sec  = secs / 100 % 60;
    				min = secs / 6000 % 60;
    				
    				timerBuffer = String.format("%02d:%02d:%02d", min, sec, milisec);
    				Platform.runLater(()->{
    					MissionController.instance.lblFlight.setText(timerBuffer);
    				});
    				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}	
    			}
    		}	
    	};
    	thread.start();
    }
    
    public void chartCpuLoad() {
    	chartCpuLoad.setAnimated(false);
    	
    	lineCpuLoad = new XYChart.Series();
    	chartCpuLoad.getData().add(lineCpuLoad);
    	
    	// 색상 설정
    	Node fill = lineCpuLoad.getNode().lookup(".chart-series-area-fill");
    	Node line = lineCpuLoad.getNode().lookup(".chart-series-area-line");
    	Color color = Color.rgb(0x11, 0x7D, 0xBB);
    	String rgb = String.format("%d, %d, %d",
    			(int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    	fill.setStyle("-fx-fill: rgba(" + rgb + ", 0.15);");
    	line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    	
    	CategoryAxis xAxis = (CategoryAxis) chartCpuLoad.getXAxis();
    	xAxis.setVisible(false);
    	
    	NumberAxis yAxisCpuLoad = (NumberAxis) chartCpuLoad.getYAxis();
    	yAxisCpuLoad.setUpperBound(100);
    	yAxisCpuLoad.setLowerBound(0);
    	yAxisCpuLoad.setAutoRanging(false);
    	chartCpuLoad.getXAxis().setTickLabelsVisible(false);
    	chartCpuLoad.getXAxis().setTickMarkVisible(false);
    	chartCpuLoad.setCreateSymbols(false);
    	displayChartCpuLoad();
    }
    public void chartRamLoad() {
    	chartRamLoad.setAnimated(false);
    	
    	lineRamLoad = new XYChart.Series();
    	chartRamLoad.getData().add(lineRamLoad);
    	
    	// 색상 설정
    	Node fill = lineRamLoad.getNode().lookup(".chart-series-area-fill");
    	Node line = lineRamLoad.getNode().lookup(".chart-series-area-line");
    	Color color = Color.rgb(0x95, 0x28, 0xB4);
    	String rgb = String.format("%d, %d, %d",
    			(int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    	fill.setStyle("-fx-fill: rgba(" + rgb + ", 0.15);");
    	line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    	
    	CategoryAxis xAxis = (CategoryAxis) chartRamLoad.getXAxis();
    	xAxis.setVisible(false);
    	
    	NumberAxis yAxisRamLoad = (NumberAxis) chartRamLoad.getYAxis();
    	yAxisRamLoad.setUpperBound(100);
    	yAxisRamLoad.setLowerBound(0);
    	yAxisRamLoad.setAutoRanging(false);
    	chartRamLoad.getXAxis().setTickLabelsVisible(false);
    	chartRamLoad.getXAxis().setTickMarkVisible(false);
    	chartRamLoad.setCreateSymbols(false);
    	displayChartRamLoad();
    }
	public void chartNetSpeed(){
		chartNetSpeed.setAnimated(false);
    	chartNetSpeed.setLegendVisible(false);
    	
    	lineNetUpSpeed = new XYChart.Series();
    	lineNetDownSpeed = new XYChart.Series();
    	chartNetSpeed.getData().addAll(lineNetUpSpeed,lineNetDownSpeed);
    	
    	// 색상 설정
    	Node fill1 = lineNetUpSpeed.getNode().lookup(".chart-series-area-fill");
    	Node line1 = lineNetUpSpeed.getNode().lookup(".chart-series-area-line");
    	Color color1 = Color.rgb(0xFF, 0x2A, 0x55);
    	String rgb1 = String.format("%d, %d, %d",
    			(int) (color1.getRed() * 255), (int) (color1.getGreen() * 255), (int) (color1.getBlue() * 255));
    	fill1.setStyle("-fx-fill: rgba(" + rgb1 + ", 0.15);");
    	line1.setStyle("-fx-stroke: rgba(" + rgb1 + ", 1.0);");
    	Node fill2 = lineNetDownSpeed.getNode().lookup(".chart-series-area-fill");
    	Node line2 = lineNetDownSpeed.getNode().lookup(".chart-series-area-line");
    	Color color2 = Color.rgb(0xFF, 0xD4, 0x00);
    	String rgb2 = String.format("%d, %d, %d",
    			(int) (color2.getRed() * 255), (int) (color2.getGreen() * 255), (int) (color2.getBlue() * 255));
    	fill2.setStyle("-fx-fill: rgba(" + rgb2 + ", 0.15);");
    	line2.setStyle("-fx-stroke: rgba(" + rgb2 + ", 1.0);");
    	
    	CategoryAxis xAxis = (CategoryAxis) chartNetSpeed.getXAxis();
    	xAxis.setVisible(false);
    	
    	NumberAxis yAxisNetSpeed = (NumberAxis) chartNetSpeed.getYAxis();
    	yAxisNetSpeed.setLowerBound(0);
    	yAxisNetSpeed.setAutoRanging(true);
    	chartNetSpeed.getXAxis().setTickLabelsVisible(false);
    	chartNetSpeed.getXAxis().setTickMarkVisible(false);
    	chartNetSpeed.setCreateSymbols(false);
    	displayChartNetSpeed();
	}
	
	public void displayChartCpuLoad() {
		Thread thread = new Thread(()->{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:s");
			while(true) {
				String xValue = dateFormat.format(new Date());
				Platform.runLater(()->{
					XYChart.Data dataCpuLoad = new XYChart.Data(xValue,yvalueCpuLoad);                           
					lineCpuLoad.getData().addAll(dataCpuLoad);
                    if(sequence[0]<60) {
                        ++sequence[0];  
                    } else 
                    	lineCpuLoad.getData().remove(0);
				});
				try {Thread.sleep(1000);}catch (Exception e) {}
			}
		});
		thread.start();
	}
	public void displayChartRamLoad() {
		Thread thread = new Thread(()->{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:s");
			while(true) {
				String xValue = dateFormat.format(new Date());
				Platform.runLater(()->{
					XYChart.Data dataRamLoad = new XYChart.Data(xValue,yvalueRamLoad);                           
					lineRamLoad.getData().addAll(dataRamLoad);
                    if(sequence[1]<60) {
                        ++sequence[1];  
                    } else 
                    	lineRamLoad.getData().remove(0);
				});
				try {Thread.sleep(1000);}catch (Exception e) {}
			}
		});
		thread.start();
	}
	public void displayChartNetSpeed() {
		Thread thread = new Thread(()->{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:s");
			while(true) {
				String xValue = dateFormat.format(new Date());
				Platform.runLater(()->{
					XYChart.Data dataNetUpSpeed = new XYChart.Data(xValue,yvalueNetUpSpeed);                       
					XYChart.Data dataNetDownSpeed = new XYChart.Data(xValue,yvalueNetDownSpeed);                       
					lineNetUpSpeed.getData().addAll(dataNetUpSpeed);
					lineNetDownSpeed.getData().addAll(dataNetDownSpeed);
                    if(sequence[2]<60) {
                        ++sequence[2];  
                    } else {
                    	lineNetUpSpeed.getData().remove(0);
                    	lineNetDownSpeed.getData().remove(0);
                    }
				});
				try {Thread.sleep(1000);}catch (Exception e) {}
			}
		});
		thread.start();
	}
	
	private double getMicroControlValue() {
		try{
			return Double.parseDouble(txtMicroControl.getText());
		} catch (Exception e) {
			System.out.println("txtMicroControl parsing error");
			return 0;
		}
	}
	
	public void toolTip(boolean tooltipFlag) {
		if(tooltipFlag) {
			//Ctrlstat 버튼 info
			Tooltip tooltipTxtTakeoffHeight = new Tooltip();
			tooltipTxtTakeoffHeight.setStyle(toolTipCss());
			tooltipTxtTakeoffHeight.setText("드론의 고도 설정");
			txtAltitude.setTooltip(tooltipTxtTakeoffHeight);
			
			Tooltip tooltipBtnArm = new Tooltip();
			tooltipBtnArm.setStyle(toolTipCss());
			tooltipBtnArm.setText("Arm : 드론 시동 걸기\nDisarm : 드론 시동 끄기\n* Arm할 때마다 홈 위치 고정");
			btnArm.setTooltip(tooltipBtnArm);
			
			Tooltip tooltipBtnChangeAlt = new Tooltip();
			tooltipBtnChangeAlt.setStyle(toolTipCss());
			tooltipBtnChangeAlt.setText("고도 수정");
			btnChangeAlt.setTooltip(tooltipBtnChangeAlt);
			
			Tooltip tooltipBtnTakeOff = new Tooltip();
			tooltipBtnTakeOff.setStyle(toolTipCss());
			tooltipBtnTakeOff.setText("이륙");
			btnTakeoff.setTooltip(tooltipBtnTakeOff);
			
			Tooltip tooltipBtnLand = new Tooltip();
			tooltipBtnLand.setStyle(toolTipCss());
			tooltipBtnLand.setText("착륙");
			btnLand.setTooltip(tooltipBtnLand);
			
			Tooltip tooltipBtnRtl = new Tooltip();
			tooltipBtnRtl.setStyle(toolTipCss());
			tooltipBtnRtl.setText("Home으로 가기");
			btnRtl.setTooltip(tooltipBtnRtl);
			
			Tooltip tooltipBtnResetHome = new Tooltip();
			tooltipBtnResetHome.setStyle(toolTipCss());
			tooltipBtnResetHome.setText("Home위치 Reset (Arm했을 때의 위치로 설정)");
			btnResetHome.setTooltip(tooltipBtnResetHome);
			
			Tooltip tooltipBtnGoto = new Tooltip();
			tooltipBtnGoto.setStyle(toolTipCss());
			tooltipBtnGoto.setText("Map에 설정한 위치로 이동");
			btnGoTo.setTooltip(tooltipBtnGoto);
			
			//Ctrlstat 드론의 정보 info
			Tooltip tooltipLblPosture = new Tooltip();
			tooltipLblPosture.setStyle(toolTipCss());
			tooltipLblPosture.setText("Roll\nPitch\nYaw 값");
			lblPosture.setTooltip(tooltipLblPosture);
			
			Tooltip tooltipLblPosition = new Tooltip();
			tooltipLblPosition.setStyle(toolTipCss());
			tooltipLblPosition.setText("위도\n경도\n고도\n헤딩 값");
			lblPosition.setTooltip(tooltipLblPosition);
			
			Tooltip tooltipLblSpeed = new Tooltip();
			tooltipLblSpeed.setStyle(toolTipCss());
			tooltipLblSpeed.setText("Air Speed\nGround Speed\nVertical Speed");
			lblSpeed.setTooltip(tooltipLblSpeed);
			
			Tooltip tooltipLblBattery = new Tooltip();
			tooltipLblBattery.setStyle(toolTipCss());
			tooltipLblBattery.setText("잔여 배터리 %\n전압\n전류");
			lblBattery.setTooltip(tooltipLblBattery);
			
			Tooltip tooltipLblGPS = new Tooltip();
			tooltipLblGPS.setStyle(toolTipCss());
			tooltipLblGPS.setText("GPS"); //수정 요망
			lblGPS.setTooltip(tooltipLblGPS);
			
			Tooltip tooltipLblRngfnd = new Tooltip();
			tooltipLblRngfnd.setStyle(toolTipCss());
			tooltipLblRngfnd.setText("rngfnd"); //수정 요망
			lblRngfnd.setTooltip(tooltipLblRngfnd);
			
			//Ctrlstat 라즈베리파이 정보
			Tooltip tooltipLblCpuLoadTip = new Tooltip();
			tooltipLblCpuLoadTip.setStyle(toolTipCss());
			tooltipLblCpuLoadTip.setText("CPU 사용량");
			lblCpuLoadTip.setTooltip(tooltipLblCpuLoadTip);
			
			Tooltip tooltipLblRamLoadTip = new Tooltip();
			tooltipLblRamLoadTip.setStyle(toolTipCss());
			tooltipLblRamLoadTip.setText("Ram 사용량");
			lblRamLoadTip.setTooltip(tooltipLblRamLoadTip);
			
			Tooltip tooltipLblNetUpSpeedTip = new Tooltip();
			tooltipLblNetUpSpeedTip.setStyle(toolTipCss());
			tooltipLblNetUpSpeedTip.setText("업로드 속도");
			lblNetUpSpeedTip.setTooltip(tooltipLblNetUpSpeedTip);
			
			Tooltip tooltipLblNetDownSpeedTip = new Tooltip();
			tooltipLblNetDownSpeedTip.setStyle(toolTipCss());
			tooltipLblNetDownSpeedTip.setText("다운로드 속도");
			lblNetDownSpeedTip.setTooltip(tooltipLblNetDownSpeedTip);
			
			//Ctrlstat 하방카메라 탭 정보
			Tooltip tooltipBtnUp = new Tooltip();
			tooltipBtnUp.setStyle(toolTipCss());
			tooltipBtnUp.setText("드론 미세조정 - 헤딩 방향쪽으로 이동");
			btnUp.setTooltip(tooltipBtnUp);
			
			Tooltip tooltipBtnDown = new Tooltip();
			tooltipBtnDown.setStyle(toolTipCss());
			tooltipBtnDown.setText("드론 미세조정 - 헤딩 반대 방향쪽으로 이동");
			btnDown.setTooltip(tooltipBtnDown);
			
			Tooltip tooltipBtnLeft = new Tooltip();
			tooltipBtnLeft.setStyle(toolTipCss());
			tooltipBtnLeft.setText("드론 미세조정 - 헤딩 기준 왼쪽으로 이동");
			btnLeft.setTooltip(tooltipBtnLeft);
			
			Tooltip tooltipBtnRight = new Tooltip();
			tooltipBtnRight.setStyle(toolTipCss());
			tooltipBtnRight.setText("드론 미세조정 - 헤딩 기준 오른쪽으로 이동");
			btnRight.setTooltip(tooltipBtnRight);
			
			Tooltip tooltipBtnN = new Tooltip();
			tooltipBtnN.setStyle(toolTipCss());
			tooltipBtnN.setText("드론 미세조정 - 헤딩을 정북쪽으로 변경");
			btnN.setTooltip(tooltipBtnN);
			
			Tooltip tooltipBtnAttach = new Tooltip();
			tooltipBtnAttach.setStyle(toolTipCss());
			tooltipBtnAttach.setText("화물 부착");
			btnAttach.setTooltip(tooltipBtnAttach);
			
			Tooltip tooltipBtnDettach = new Tooltip();
			tooltipBtnDettach.setStyle(toolTipCss());
			tooltipBtnDettach.setText("화물 탈착");
			btnDettach.setTooltip(tooltipBtnDettach);
		} else {
			txtAltitude.setTooltip(null);
			btnArm.setTooltip(null);
			btnChangeAlt.setTooltip(null);
			btnTakeoff.setTooltip(null);
			btnLand.setTooltip(null);
			btnRtl.setTooltip(null);
			btnResetHome.setTooltip(null);
			btnGoTo.setTooltip(null);
			lblPosture.setTooltip(null);
			lblPosition.setTooltip(null);
			lblSpeed.setTooltip(null);
			lblBattery.setTooltip(null);
			lblGPS.setTooltip(null);
			lblRngfnd.setTooltip(null);
			lblCpuLoadTip.setTooltip(null);
			lblRamLoadTip.setTooltip(null);
			lblNetUpSpeedTip.setTooltip(null);
			lblNetDownSpeedTip.setTooltip(null);
			btnUp.setTooltip(null);
			btnDown.setTooltip(null);
			btnLeft.setTooltip(null);
			btnRight.setTooltip(null);
			btnN.setTooltip(null);
			btnAttach.setTooltip(null);
			btnDettach.setTooltip(null);
		}
	}
	
	public String toolTipCss() {
		return "-fx-font-size: 20; -fx-font-family: D2Coding;";
	}
}
