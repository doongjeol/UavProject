package gcs.hud;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.appmain.AppMainController;
import gcs.ctrlstat.CtrlstatController;
import gcs.db.service.DroneService;
import gcs.mission.MissionCalculator;
import gcs.mission.MissionController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class HudViewController implements Initializable {
   public static HudViewController instance;
   
   public MjpgStreamFront camStream;
   
   @FXML private BorderPane borderPane;
   @FXML private StackPane stackPane;
   
   @FXML private Button btnResetTimer;
   @FXML public Button btnSaveData;
   @FXML private Button btnCameraToggle;
   @FXML private Button btnOnlyCamera;
   private boolean backgroundStatus = false;
   private boolean colorStatus = false;
   @FXML private ImageView imgArmed;
   @FXML private ImageView imgMode;
   @FXML private ImageView imgHomeFix;
   @FXML private ImageView imgCargoInfo;
   @FXML private ImageView imgWaypointInfo;
   @FXML private ImageView imgHomeDistance;
   
   @FXML public Canvas canvasCamera; 
   @FXML private Canvas canvas1; 
   private GraphicsContext ctx1;
   @FXML private Canvas canvas2;
   private GraphicsContext ctx2;
   @FXML private Canvas canvasIndicator;
   private GraphicsContext ctxIndicator;
   @FXML private Canvas canvas3;
   private GraphicsContext ctx3;
   
   @FXML private Label lblArmed;
   @FXML private Label lblMode;
   @FXML private Label lblHomeFix;
   @FXML private Label lblCargoInfo;
   @FXML private Label lblWaypointInfo;
   @FXML private Label lblHomeDistance;
   public static int homeDistance;
   public static int goToDistance;
   public static int wpDistance;
   public static double destLat;
   public static double destLng;
   public static double destAlt;
   
   private long startTime;
   
   private double direction;
   private double tempDirection;
   private double drDistance;
   
   private double altitude;
   private double tempAltitude;
   private double altDistance;
   
   private double aspeed;
   private double tempAspeed;
   private double aspeedDistance;
   
   private double width;
   private double height;
   
   // #### Layer 드로잉용 변수 ####
   private double degree = 2*Math.PI/360; // 라디안 단위의 1도 값
   private double r; // 반지름
   private double r3xl; 
   private double r2xl;
   private double rxl;
   private double r2xs;
   
   // ########################
   
   private double translateX;
   private double translateY;
   
   private double roll;
   private double tempRoll;
   private double pitch;
   private double tempPitch;
   private double yaw;
   
   private double pitchDistance;
   
   private String mode;
   public static boolean saveDataFlag;
   
   @Override
   public void initialize(URL arg0, ResourceBundle arg1) {
	  HudViewController.instance = this;
      startTime = System.currentTimeMillis()/10;
      width = canvas1.getWidth();
      height = canvas1.getHeight();
      
      initCanvasLayer1();
      initCanvasLayer2();
      initCanvasLayerIndicator();
      initCanvasLayer3();
      btnCameraToggle.setOnAction((e)-> {handleBtnCameraToggle(e);});
      btnOnlyCamera.setOnAction((e)-> {handleBtnOnlyCamera(e);});
      btnSaveData.setOnAction((e)-> {try {handleBtnSaveData(e);} catch (Exception e1) {}});
      btnResetTimer.setOnAction((e)-> {handleBtnResetTimer(e);});
      ViewLoop viewLoop = new ViewLoop();
      viewLoop.start();
      
   }
   
   public void handleBtnCameraToggle(ActionEvent e) {
	   if(btnCameraToggle.getText().equals("Show Camera")){
		   backgroundStatus = true;
		   colorStatus = true;
		   canvas1.setVisible(true);
		   canvas2.setVisible(true);
		   canvas3.setVisible(true);
		   btnCameraToggle.setText("Show HUD");
	   } else{
		   backgroundStatus = false;
		   colorStatus = false;
		   canvas1.setVisible(true);
  		   canvas2.setVisible(true);
  		   canvas3.setVisible(true);
  		   canvasIndicator.setVisible(true);
  		 btnCameraToggle.setText("Show Camera");
       }
   }
   
   public void handleBtnOnlyCamera(ActionEvent e) {
	   canvasCamera.setVisible(true);
	   canvas1.setVisible(false);
	   canvas2.setVisible(false);
	   canvas3.setVisible(false);
	   canvasIndicator.setVisible(false);
	   backgroundStatus = false;
	   colorStatus = true;
   }
   
   public void handleBtnSaveData(ActionEvent e) throws Exception {
	   UAV uav = Network.getUav();
	   if(btnSaveData.getText().equals("Save Data")) {
		   btnSaveData.setText("Save Cancel");
		   saveDataFlag = true;
		   if(AppMainController.instance.isConnectStatus()) {
			   Thread saveDataThread = new Thread() {
				   @Override
				   public void run() {
					   DroneService droneService = DroneService.getInstance();
					   while(saveDataFlag) {
						   if(uav.mode.equals("AUTO")) uav.autoflag = 1;
						   else uav.autoflag = 0;
						   droneService.write(uav);
						   try { Thread.sleep(100);} catch (Exception e) {}
					   }
				   }
			   };
			   saveDataThread.start();
		   }
	   } else if(btnSaveData.getText().equals("Save Cancel")) {
		   btnSaveData.setText("Save Data");
		   saveDataFlag = false;
	   } 
	}
   
   public void handleBtnResetTimer(ActionEvent e) {
	   CtrlstatController.instance.secs = 0;
	   MissionController.instance.lblFlight.setText("00:00:00");
   }
	  
   
   private void initCanvasLayer1() {
      ctx1 = canvas1.getGraphicsContext2D();
      ctx1.setTextBaseline(VPos.CENTER);
      ctx1.setFont(Font.font("D2Coding", FontWeight.BOLD, 30));
   }
   
   private void initCanvasLayer2() {
      ctx2 = canvas2.getGraphicsContext2D();
      ctx2.setTextAlign(TextAlignment.CENTER);
      ctx2.setTextBaseline(VPos.CENTER);
      ctx2.setFont(Font.font("D2Coding", FontWeight.BOLD, 30));
   }
   
   private void initCanvasLayerIndicator() {
      ctxIndicator= canvasIndicator.getGraphicsContext2D();
      ctxIndicator.setTextBaseline(VPos.CENTER);
   }
   
   private void initCanvasLayer3() {
	   ctx3= canvas3.getGraphicsContext2D();
	   ctx3.setTextBaseline(VPos.CENTER);
   }
   
   class ViewLoop extends AnimationTimer{
      @Override
      public void handle(long now) {
         canvas1.setWidth(borderPane.getWidth());
         canvas1.setHeight(borderPane.getHeight());
         canvas2.setWidth(borderPane.getWidth());
         canvas2.setHeight(borderPane.getHeight());
         canvas3.setWidth(borderPane.getWidth());
         canvas3.setHeight(borderPane.getHeight());
         width = canvas1.getWidth();
         height = canvas1.getHeight();
         
         r = width*(1.9/10.5);
         r3xl = r*1.2;
         r2xl = r*1.1;
         rxl = r*1.05;
         r2xs = r*0.9;
         
         drDistance = width/2/45;
         altDistance = height/2/45;
         pitchDistance = height/2/45; // 한 눈금의 크기 정의
         aspeedDistance = height/2/45;
         
         layer1Draw();
         layer2Draw();
         layerIndicatorDraw();
         layer3Draw();
         
         /*Platform.runLater(()->{
            lblTime.setFont(new Font(16/960*width));
            lblSecond.setFont(new Font(16/960*width));});*/
      }
   }
   
   private void layer1Draw() {
      // 좌표 복원
      ctx1.rotate(tempRoll); // roll값 복원
      ctx1.translate(-translateX, -translateY); // 원점 복귀(맨 처음에는 0,0이므로 변화없음), yaw,pitch값도 복원
      
      // 이전 그림을 모두 지우기
      ctx1.clearRect(0, 0, width, height);
      
      // 원점 이동
      tempPitch = pitch;
      translateX = width/2; 
      translateY = height/2 + pitchDistance*tempPitch; // pitch 상승 시, 원점은 아래로 내려가야하므로, 부호는 (+)
      ctx1.translate(translateX, translateY);  //  원점을 창의 가로와 새로의 정 중앙으로 설정(원점 이동 시, 계산이 편리!)
      
      // 회전
      tempRoll = roll; // 좌표복원에서 돌지 않으면서, 회전에서 먼저 돌고 좌표n복원 파트에서 이전으로 돌아가기 위해 tempRoll을 추가한다.
      ctx1.rotate(-tempRoll); // 원점 중심 회전, 좌표계와는 반대로 시계방향으로 회전함(roll pitch yaw 중 roll에 해당됨)
      
      // 배경 드로잉
      if(!backgroundStatus) {
    	  ctx1.setFill(Color.rgb(0x22, 0x22, 0x22));
    	  ctx1.fillRect(-2*width, -2*height, 4*width, 4*height); // 사각형그리기(기준점의 x, y좌표, 사각형의 길이, 높이)
	      // 배경 아랫부분 그리기
	      ctx1.setStroke(Color.WHITE);
	      for(int i=0;i<200;i++) {
	    	  ctx1.setStroke(Color.rgb(0x80, 0x80, 0x80));
	    	  ctx1.strokeLine(-width*0.07*i/4 +width, 0, width, height*0.07*i/4);
	      }
	      for(int i=0;i<100;i++) {
	    	  ctx1.setStroke(Color.rgb(0x22, 0x22, 0x22));
	    	  ctx1.strokeLine(-width/2*0.3*i/4 +width/2, 0, -width/2*0.3*i/4 +width/2, i*height/2);
	      }
      }
      
      // pitch 가변 지시부 그리기
      ctx1.setLineWidth(2*width/960);
      if(colorStatus) {
    	  ctx1.setFill(Color.BLACK);
	      ctx1.setStroke(Color.BLACK);
      } else {
    	  ctx1.setFill(Color.WHITE);
    	  ctx1.setStroke(Color.WHITE);
      }
      for(int i=5; i<90; i+=5) {
         if(i <=tempPitch+15 && i>= tempPitch-15) {
         ctx1.strokeLine(((i%2==0)?-r/2:-r/3), -pitchDistance*i, ((i%2==0)?r/2:r/3), -pitchDistance*i);
         if(i%2 == 0) ctx1.fillText(" "+String.valueOf(i), -r/2*1.45, -pitchDistance*i);
         }
      }
      ctx1.setLineDashes(1*width/960,3*width/960);
      for(int i=5; i<90; i+=5) {
         if(i <=-tempPitch+15 && i>= -tempPitch-15) {
         ctx1.strokeLine(((i%2==0)?-r/2:-r/3), pitchDistance*i, ((i%2==0)?r/2:r/3), pitchDistance*i);
         if(i%2 == 0) ctx1.fillText(String.valueOf(-i), -r/2*1.45, pitchDistance*i);
         }
      }
      ctx1.setLineDashes(null);
      ctx1.setLineWidth(1*width/960);
      
      ctx1.setFont(Font.font("D2Coding", FontWeight.BOLD, 20*width/960));
     
   }
   
   private void layer2Draw() {
      ctx2.translate(width/2, height/2);
      ctx2.clearRect(-width, -height, 2*width, 2*height); // 이전 그림 지우기
      
      if(colorStatus) {
    	  ctx2.setFill(Color.BLACK);
	      ctx2.setStroke(Color.BLACK);
      } else {
    	  ctx2.setFill(Color.WHITE);
    	  ctx2.setStroke(Color.WHITE);
      }
      
      // 기준선 그리기
      ctx1.strokeLine(-r, 0, r, 0);
      
      // 중심부의 기체 모형 그리기
      ctx2.setLineWidth(3*width/960);
      ctx2.strokeLine(-width/55/2, 0, width/55/2, 0);
      ctx2.strokeLine(0, -width/55/2, 0, width/55/2);
      ctx2.strokeOval(-width/55, -width/55, width/55*2, width/55*2);
      ctx2.strokeLine(-width/45, width/45, -width/55/Math.sqrt(2), width/55/Math.sqrt(2));
      ctx2.strokeLine(width/45, width/45, width/55/Math.sqrt(2), width/55/Math.sqrt(2));
      ctx2.strokeLine(-width/45, width/45, -width/25, width/45);
      ctx2.strokeLine(width/45, width/45, width/25, width/45);
      
      // roll 불변 지시부 그리기
      ctx2.strokeArc(-r, -r, 2*r, 2*r, 45, 90, ArcType.OPEN);
      for(int i = 45; i<=135; i++) {
         if(i%15!=0) {
            ctx2.setLineWidth(1*width/960);
            ctx2.strokeLine(-rxl*Math.cos(i*degree), -rxl*Math.sin(i*degree), -r*Math.cos(i*degree), -r*Math.sin(i*degree));
         }
         else if(i%90==0) ;
         else if(i%30==0) {
            ctx2.setLineWidth(3*width/960);
            ctx2.strokeLine(-r3xl*Math.cos(i*degree), -r3xl*Math.sin(i*degree), -r*Math.cos(i*degree), -r*Math.sin(i*degree));
         }
         else {
            ctx2.setLineWidth(2*width/960);
            ctx2.strokeLine(-r2xl*Math.cos(i*degree), -r2xl*Math.sin(i*degree), -r*Math.cos(i*degree), -r*Math.sin(i*degree));
         }
      }
      ctx2.setLineWidth(3*width/960);
      double[] pointX1 = {0, -r2xl+r, r2xl-r, 0}; double[] pointY1 = {-r, -r3xl*0.95, -r3xl*0.95, -r};
      ctx2.strokePolyline(pointX1, pointY1, 4);
      ctx2.fillText("-30", -r3xl*1.2*Math.cos(60*degree), -r3xl*1.05*Math.sin(60*degree));
      ctx2.fillText("30", -r3xl*0.9*Math.cos(120*degree), -r3xl*1.05*Math.sin(120*degree));
      
      // 방위 불변 지시부 그리기
      ctx2.setFill(Color.rgb(0xFF, 0x3C, 0x43, 0.75));
      ctx2.strokeLine(-width/2/1.4, (-height/2)*9/10, width/2/1.4, (-height/2)*9/10);
      ctx2.strokeLine(-width/2/1.4+2, (-height/2)*9/10+1, -width/2/1.4+2, (-height/2)*8/10);
      ctx2.strokeLine(width/2/1.4-2, (-height/2)*9/10+1, width/2/1.4-2, (-height/2)*8/10);
      pointY1[0] = (-height/2)*9/10; pointY1[3] = (-height/2)*9/10;
      pointY1[1] = (-height/2)*9.5/10; pointY1[2] = (-height/2)*9.5/10;
      ctx2.fillPolygon(pointX1, pointY1, 4);
      
      // 속도 & 고도 불변 지시부 그리기
      ctx2.strokeLine(-1.1*width/2/1.2, -r, -1.1*width/2/1.2, r);
      ctx2.strokeLine(-1.1*width/2/1.2, -r, -1.1*width/2/1.3, -r);
      ctx2.strokeLine(-1.1*width/2/1.2, r, -1.1*width/2/1.3, r);
      double[] pointX2 = {-1.1*width/2/1.2, -1.1*width/2/1.1, -1.1*width/2/1.1, -1.1*width/2/1.2}; double[] pointY2 = {0, -r2xl+r, r2xl-r, 0};
      ctx2.fillPolygon(pointX2, pointY2, 4);
      ctx2.strokeLine(1.1*width/2/1.2, -r, 1.1*width/2/1.2, r);
      ctx2.strokeLine(1.1*width/2/1.2, -r, 1.1*width/2/1.3, -r);
      ctx2.strokeLine(1.1*width/2/1.2, r, 1.1*width/2/1.3, r);
      for(int i = 0; i<pointX2.length; i++) pointX2[i]= -pointX2[i];
      ctx2.fillPolygon(pointX2, pointY2, 4);
      
      // roll 가변 지시부 그리기
      double[] pointX3 = {r*Math.cos((90-roll)*degree), r2xs*Math.cos((93-roll)*degree), r2xs*Math.cos((87-roll)*degree), r*Math.cos((90-roll)*degree)};
      double[] pointY3 = {-r*Math.sin((90-roll)*degree), -r2xs*Math.sin((93-roll)*degree), -r2xs*Math.sin((87-roll)*degree), -r*Math.sin((90-roll)*degree)};
      ctx2.fillPolygon(pointX3, pointY3, 4);
      
      // 방위 가변 지시부 그리기
      ctx2.setFill(Color.WHITE);
      tempDirection = -yaw;
      double drPointY1 = (-height/2)*9/10+3; // (공통)눈금의 접점
      double drPointY2 = (-height/2)*8.5/10; // 짧은 눈금의 하단 점
      double drPointY3 = (-height/2)*8/10;   // 긴 눈금의 하단 점
      
      double interval = tempDirection % 10;
      int intTemp10 = ((int)(tempDirection)) / 10 *10;
      int strDir;
      for(int i = 45; i>=-45 ; i-=5) {
         if(i%10 == 0) {
            ctx2.strokeLine((i+interval)*drDistance*1.3, drPointY1, (i+interval)*drDistance*1.3, drPointY3);
            strDir = i-intTemp10;
            if(strDir%90 == 0) {
               ctx2.setFont(Font.font("D2Coding", FontWeight.BOLD, 35*width/960));
               switch(strDir/90) {
               case 0:
                  ctx2.fillText(String.valueOf("N"), (i+interval)*drDistance*1.3-width/140, drPointY3*0.93);
                  break;
               case 1:
                  ctx2.fillText(String.valueOf("E"), (i+interval)*drDistance*1.3-width/140, drPointY3*0.93);
                  break;
               case 2:
                  ctx2.fillText(String.valueOf("S"), (i+interval)*drDistance*1.3-width/140, drPointY3*0.93);
                  break;
               case 3:
                  ctx2.fillText(String.valueOf("W"), (i+interval)*drDistance*1.3-width/140, drPointY3*0.93);
                  break;
               case 4:
                  ctx2.fillText(String.valueOf("N"), (i+interval)*drDistance*1.3-width/140, drPointY3*0.93);
                  break;
               }
            } else {
            ctx2.setFill(Color.GRAY);
            ctx2.setFont(Font.font("D2Coding", FontWeight.BOLD, 28*width/960));
            ctx2.fillText(String.valueOf(strDir%360>=0?strDir%360:(strDir%-360)+360),
                  (strDir%360>=100)?(i+interval)*drDistance*1.295-width/50 :(i+interval)*drDistance*1.3-width/102,
                	  drPointY3*0.94);
            }
         } else {
         ctx2.strokeLine((i+interval)*drDistance*1.3, drPointY1, (i+interval)*drDistance*1.3, drPointY2);
         }
      }
      
      // 속도 가변 지시부 그리기
      ctx2.setFill(Color.GRAY);
      ctx2.setFont(Font.font("D2Coding", FontWeight.BOLD, 28*width/960));
      tempAspeed = aspeed;
      double altPointX1 = -width/2/1.2; // (공통)눈금의 접점
      double altPointX2 = -width/2/1.25; // 짧은 눈금의 우측 점
      double altPointX3 = -width/2/1.3;   // 긴 눈금의 우측 점
      
      interval = tempAspeed % 10;
      intTemp10 = ((int)(tempAspeed)) /10 *10;
      int strAlt;
      for(int i = 30; i>=-30 ; i-=5) {
         strAlt = i-intTemp10;
         if(-strAlt>=0) {
            if(i%10 == 0) {
               ctx2.strokeLine(altPointX1*1.1, (i+interval)*aspeedDistance*1.3, altPointX3*1.1, (i+interval)*aspeedDistance*1.3);
            ctx2.fillText(String.valueOf(-strAlt),altPointX3*1.1+width/50,(i+interval)*aspeedDistance*1.3);
            } else {
               ctx2.strokeLine(altPointX1*1.1, (i+interval)*aspeedDistance*1.3, altPointX2*1.1, (i+interval)*aspeedDistance*1.3);
            }
         }
      }
      
      // 고도 가변 지시부 그리기
      tempAltitude = altitude;
      altPointX1 = width/2/1.2; // (공통)눈금의 접점
      altPointX2 = width/2/1.25; // 짧은 눈금의 우측 점
      altPointX3 = width/2/1.3;   // 긴 눈금의 우측 점
      
      interval = tempAltitude % 10;
      intTemp10 = ((int)(tempAltitude)) /10 *10;
      for(int i = 30; i>=-30 ; i-=5) {
         strAlt = i-intTemp10;
         if(-strAlt>=0) {
            if(i%10 == 0) {
               ctx2.strokeLine(altPointX1*1.1, (i+interval)*altDistance*1.3, altPointX3*1.1, (i+interval)*altDistance*1.3);
               ctx2.fillText(String.valueOf(-strAlt),altPointX3*1.1-width/40,(i+interval)*altDistance*1.3);
            } else {
               ctx2.strokeLine(altPointX1*1.1, (i+interval)*altDistance*1.3, altPointX2*1.1, (i+interval)*altDistance*1.3);
            }
         }
      }
      
      // 고도, 방위 지시부  garbage 지우기
      ctx2.clearRect(-width/2, -height/2, width/2-width/2/1.4, height/2-r-3); //좌 상단
      ctx2.clearRect(width/2/1.4, -height/2, width/2-width/2/1.4, height/2-r-3); //우 상단
      ctx2.clearRect(-width/2, r+3, width/2-width/2/1.4, height/2-r); //좌 하단
      ctx2.clearRect(width/2/1.4, r+3, width/2-width/2/1.4, height/2-r); // 우 하단
      
      // 방위 지시부 사각형 지우기
      ctx2.clearRect(-width/20, -height/2.5, width/10, height/20);
      
      // 고도, 속도 지시부 사각형 지우기
      ctx2.clearRect(-width/2.20, -height/40, width/10, height/20);
      ctx2.clearRect(width/2.81, -height/40, width/10.1, height/20);
      
      
      // 설정 되돌아가기(원점이동, 모든 색상, 선굵기, 글자)
      ctx2.setLineWidth(3*width/960);
      ctx2.setFont(Font.font("D2Coding", FontWeight.BOLD, 20*width/960));
      ctx2.translate(-width/2, -height/2);
   }
   
   private void layerIndicatorDraw() {
	  ctxIndicator.translate(width/2, height/2);
	  ctxIndicator.clearRect(-width, -height, 2*width, 2*height); // 이전 그림 지우기
  
	  // 카메라와 같이 hud를 볼 때 가독성을 위한 사각형
	  if(btnCameraToggle.getText().equals("Show HUD")){
		  // 방위 값
		  ctxIndicator.setFill(Color.rgb(0xff, 0xff, 0xff,0.9));
		  ctxIndicator.setFont(Font.font("D2Coding", FontWeight.BOLD, 35*width/960));
		  if(yaw == 360 || yaw == 0) 
			  ctxIndicator.fillText("N", -width/100.1, -height/2.85);
		  else if(yaw == 270)
			  ctxIndicator.fillText("W", -width/100.1, -height/2.85);
		  else if(yaw == 180)
			  ctxIndicator.fillText("S", -width/100.1, -height/2.85);
		  else if(yaw == 90)
			  ctxIndicator.fillText("E", -width/100.1, -height/2.85);
		  else 
			  ctxIndicator.fillText(String.valueOf((int)yaw), -width/30.1, -height/2.85);
		  
		  
		  // 속도, 고도 값
		  ctxIndicator.fillText(String.valueOf(Math.round(aspeed*10)/10F),-width/2.37, 1);
		  ctxIndicator.fillText(String.valueOf(Math.round(altitude*10)/10F), width/2.67, 1);

		  ctxIndicator.setFill(Color.rgb(0xff, 0xff, 0xff,0.6));
		  // 방위 지시부 사각형
		  ctxIndicator.fillRect(-width/20, -height/2.6, width/10, height/18);
		  
		  // 고도, 속도 지시부 사각형
		  ctxIndicator.fillRect(-width/2.20, -height/40, width/10, height/20);
		  ctxIndicator.fillRect(width/2.81, -height/40, width/10.1, height/20);
		  
		  ctxIndicator.fillRect(-width/2, height/3.5, width, height/2);
		  
	  }      
	  
	  ctxIndicator.translate(-width/2, -height/2);
   }
   
   private void layer3Draw() {
	  ctx3.translate(width/2, height/2);
	  ctx3.clearRect(-width, -height, 2*width, 2*height); // 이전 그림 지우기
	  
	  if(colorStatus) {
    	  ctx3.setFill(Color.BLACK);
	      ctx3.setStroke(Color.BLACK);
      } else {
    	  ctx3.setFill(Color.WHITE);
    	  ctx3.setStroke(Color.WHITE);
      }
	  
	  if(colorStatus) {
		  lblArmed.setStyle("-fx-text-fill: #000000;");
		  lblMode.setStyle("-fx-text-fill: #000000;");
		  lblHomeFix.setStyle("-fx-text-fill: #000000;");
		  lblCargoInfo.setStyle("-fx-text-fill: #000000;");
		  lblWaypointInfo.setStyle("-fx-text-fill: #000000;");
		  lblHomeDistance.setStyle("-fx-text-fill: #000000;");
		  imgArmed.setImage(new Image(getClass().getResource("../images/hud_arm_black.png").toExternalForm()));
		  imgMode.setImage(new Image(getClass().getResource("../images/hud_mode_black.png").toExternalForm()));
		  imgWaypointInfo.setImage(new Image(getClass().getResource("../images/hud_waypoint_black.png").toExternalForm()));
		  imgHomeDistance.setImage(new Image(getClass().getResource("../images/hud_home_distance_black.png").toExternalForm()));
	   } else {
		  lblArmed.setStyle("-fx-text-fill: #ffffff;");
		  lblMode.setStyle("-fx-text-fill: #ffffff;");
		  lblHomeFix.setStyle("-fx-text-fill: #ffffff;");
		  lblCargoInfo.setStyle("-fx-text-fill: #ffffff;");
		  lblWaypointInfo.setStyle("-fx-text-fill: #ffffff;");
		  lblHomeDistance.setStyle("-fx-text-fill: #ffffff;");
		  imgArmed.setImage(new Image(getClass().getResource("../images/hud_arm_white.png").toExternalForm()));
		  imgMode.setImage(new Image(getClass().getResource("../images/hud_mode_white.png").toExternalForm()));
		  imgWaypointInfo.setImage(new Image(getClass().getResource("../images/hud_waypoint_white.png").toExternalForm()));
		  imgHomeDistance.setImage(new Image(getClass().getResource("../images/hud_home_distance_white.png").toExternalForm()));
	   }
	  
	  // 방위 값
	  ctx3.setFont(Font.font("D2Coding", FontWeight.BOLD, 35*width/960));
	  if(yaw == 360 || yaw == 0) 
		  ctx3.fillText("N", -width/100, -height/2.8);
	  else if(yaw == 270)
		  ctx3.fillText("W", -width/100, -height/2.8);
	  else if(yaw == 180)
		  ctx3.fillText("S", -width/100, -height/2.8);
	  else if(yaw == 90)
		  ctx3.fillText("E", -width/100, -height/2.8);
	  else 
		  ctx3.fillText(String.valueOf((int)yaw), -width/30, -height/2.8);
	  
	  
	  // 속도, 고도 값
	  ctx3.fillText(String.valueOf(Math.round(aspeed*10)/10F),-width/2.35, 0);
	  ctx3.fillText(String.valueOf(Math.round(altitude*10)/10F), width/2.7, 0);
	  
//	  // 카메라와 같이 hud를 볼 때 가독성을 위한 폰트 외곽선
//	  if(btnCameraToggle.getText().equals("Show HUD")){
//		  // 속도, 고도 값
//		  ctx3.strokeText(String.valueOf(Math.round(aspeed*10)/10F),-width/2.35, 0);
//		  ctx3.strokeText(String.valueOf(Math.round(altitude*10)/10F), width/2.7, 0);
//		  
//		  // 방위 값
//		  if(yaw == 360 || yaw == 0) 
//			  ctx3.strokeText("N", -width/100, -height/2.8);
//		  else if(yaw == 270)
//			  ctx3.strokeText("W", -width/100, -height/2.8);
//		  else if(yaw == 180)
//			  ctx3.strokeText("S", -width/100, -height/2.8);
//		  else if(yaw == 90)
//			  ctx3.strokeText("E", -width/100, -height/2.8);
//		  else 
//			  ctx3.strokeText(String.valueOf((int)yaw), -width/30, -height/2.8);
//	  }      
	  
	  // 속도, 고도 text
	  ctx3.setFont(Font.font("D2Coding", FontWeight.BOLD, 25*width/960));
	  ctx3.fillText("Speed",-width/2.18,-height/3.2);
	  ctx3.fillText("Alt",width/2.38,-height/3.2);
	  ctx3.setFill(Color.GRAY);
	  ctx3.setFont(Font.font("D2Coding", FontWeight.BOLD, 21*width/960));
	  ctx3.fillText("m/s",-width/2.2,-height/3.6);
	  ctx3.fillText("m",width/2.25,-height/3.6);
      
	  ctx3.translate(-width/2, -height/2);
   }
   
   public void setDirection(double direction) {
	   this.direction = direction;
   }
   
   public void setStatus(UAV uav) {
	   roll = uav.roll;
	   pitch = uav.pitch;
	   yaw = uav.yaw;
	   altitude = uav.altitude;
	   aspeed=uav.airSpeed;
	   
	   if(yaw<0)
		   yaw = (int) (yaw+360);
	   else
		   yaw = (int) yaw;
	   
	   mode = uav.mode;
	   mode.toUpperCase();
	   
	   Platform.runLater(()->{
		   if(uav.armed) {
			   if(!lblArmed.getText().equals("ARMED")) {
				   lblArmed.setText("ARMED");
				//SpeechRecognition.instance.speech("시동되었습니다");
			   }
		   }
		   else {
			   if(!lblArmed.getText().equals("DISARMED")) {
				lblArmed.setText("DISARMED");
				//SpeechRecognition.instance.speech("시동이 끄졌습니다");
				}
		   }
		   
		   lblMode.setText(mode);
		   
		   if(CtrlstatController.instance.homeResetFlag) {
			   lblHomeFix.setText("H.Unfix");
			   imgHomeFix.setImage(new Image(getClass().getResource("../images/hud_unfix.png").toExternalForm()));
		   } else {
			   lblHomeFix.setText("H.Fix");
			   imgHomeFix.setImage(new Image(getClass().getResource("../images/hud_fix.png").toExternalForm()));
		   }
		   
		   if(Network.getUav().cargoStatus) {
			   lblCargoInfo.setText("Attach");
			   imgCargoInfo.setImage(new Image(getClass().getResource("../images/ctr_cargoattach.png").toExternalForm()));
		   } else {
			   lblCargoInfo.setText("Dettach");
			   imgCargoInfo.setImage(new Image(getClass().getResource("../images/ctr_cargodettach.png").toExternalForm()));
		   }
		   
		   if(AppMainController.instance.isConnectStatus()) {
			   homeDistance = (int) Math.round(MissionCalculator.distance3D(Network.getUav().latitude, Network.getUav().homeLat,
					   Network.getUav().longitude, Network.getUav().homeLng, Network.getUav().altitude, 0));
			   lblHomeDistance.setText(homeDistance + "m up to Home");
			   
			   if(mode.equals("RTL")) lblWaypointInfo.setText("RTL...");
			   else if(mode.equals("LAND")) {
				   goToDistance = (int) Math.round(Network.getUav().altitude);
				   lblWaypointInfo.setText(goToDistance+"m up to " + "WP[GoTo]");
			   } else if(mode.equals("GUIDED")) {
				   if(CtrlstatController.instance.gotoFlag) {
					   goToDistance = (int) Math.round(MissionCalculator.distance3D(Network.getUav().latitude, destLat,
							   Network.getUav().longitude, destLng, Network.getUav().altitude, destAlt));
					   lblWaypointInfo.setText(goToDistance+"m up to " + "WP[GoTo]");
				   } else if(CtrlstatController.instance.takeOffFlag || CtrlstatController.instance.changeAltFlag) {
					   goToDistance = (int) Math.round(Math.abs(destAlt - Network.getUav().altitude));
					   lblWaypointInfo.setText(goToDistance+"m up to " + "WP[GoTo]");
				   }
			   }
			   else if(mode.equals("AUTO")){
				   if(!(Network.getUav().nextWaypointNo == 0))
					   wpDistance = (int) Math.round(MissionCalculator.distance3D(Network.getUav().latitude, destLat,
							   Network.getUav().longitude, destLng, Network.getUav().altitude, destAlt));
				   else 
					   wpDistance = (int) Math.round(MissionCalculator.distance3D(Network.getUav().latitude, Network.getUav().homeLat,
							   Network.getUav().longitude, Network.getUav().homeLng, Network.getUav().altitude, 0));
				   lblWaypointInfo.setText(wpDistance+"m up to " + "WP[" + Network.getUav().nextWaypointNo + "]");
			   }
		   }
		});
	}    
	
   public void setRollPitchYaw(double roll, double pitch, double yaw) {
	   this.roll = roll;
	   this.pitch = pitch;
	   this.yaw = yaw;
   }
   
   public void setAltitude(double altitude) {
	   this.altitude = altitude;
   }
   
   public void toolTip(boolean tooltipFlag) {
	   if(tooltipFlag) {
		   Tooltip tooltipBtnSaveData = new Tooltip();
			tooltipBtnSaveData.setStyle(toolTipCss());
			tooltipBtnSaveData.setText("데이터 베이스에 비행정보 저장하기");
			btnSaveData.setTooltip(tooltipBtnSaveData);
			   
			Tooltip tooltipBtnCameraToggle = new Tooltip();
			tooltipBtnCameraToggle.setStyle(toolTipCss());
			tooltipBtnCameraToggle.setText("Show Camera : camera 보기\nShow HUD : HUD 배경 보기");
			btnCameraToggle.setTooltip(tooltipBtnCameraToggle);
			
			Tooltip tooltipBtnOnlyCamera = new Tooltip();
			tooltipBtnOnlyCamera.setStyle(toolTipCss());
			tooltipBtnOnlyCamera.setText("지시계 없이 camera만 보기");
			btnOnlyCamera.setTooltip(tooltipBtnOnlyCamera);
			
			Tooltip tooltipLblArmed = new Tooltip();
			tooltipLblArmed.setStyle(toolTipCss());
			tooltipLblArmed.setText("드론 Arm/Disarm 여부 표시");
			lblArmed.setTooltip(tooltipLblArmed);
			
			Tooltip tooltipLblMode = new Tooltip();
			tooltipLblMode.setStyle(toolTipCss());
			tooltipLblMode.setText("모드 표시");
			lblMode.setTooltip(tooltipLblMode);
			
			Tooltip tooltipLblCargoInfo = new Tooltip();
			tooltipLblCargoInfo.setStyle(toolTipCss());
			tooltipLblCargoInfo.setText("화물 탈/부착 여부 표시");
			lblCargoInfo.setTooltip(tooltipLblCargoInfo);
			
			Tooltip tooltipLblHomeFix = new Tooltip();
			tooltipLblHomeFix.setStyle(toolTipCss());
			tooltipLblHomeFix.setText("Home위치 고정 여부 표시");
			lblHomeFix.setTooltip(tooltipLblHomeFix);
			
			Tooltip tooltipLblWaypointInfo = new Tooltip();
			tooltipLblWaypointInfo.setStyle(toolTipCss());
			tooltipLblWaypointInfo.setText("WayPoint까지 남은 거리");
			lblWaypointInfo.setTooltip(tooltipLblWaypointInfo);
			
			Tooltip tooltipLblHomeDistance = new Tooltip();
			tooltipLblHomeDistance.setStyle(toolTipCss());
			tooltipLblHomeDistance.setText("Home까지 남은 거리");
			lblHomeDistance.setTooltip(tooltipLblHomeDistance);
	   } else {
		   btnSaveData.setTooltip(null);
		   btnCameraToggle.setTooltip(null);
		   btnOnlyCamera.setTooltip(null);
		   lblArmed.setTooltip(null);
		   lblMode.setTooltip(null);
		   lblCargoInfo.setTooltip(null);
		   lblHomeFix.setTooltip(null);
		   lblWaypointInfo.setTooltip(null);
		   lblHomeDistance.setTooltip(null);
	   }
   }
   
   public String toolTipCss() {
		return "-fx-font-size: 20; -fx-font-family: D2Coding;";
	}
   
   public void mqttView() {
       try {
           camStream = new MjpgStreamFront(canvasCamera);
           camStream.start();
       } catch(Exception e) {
           e.printStackTrace();
       }
   }
}