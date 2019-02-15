package gcs.lowercam;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.lowercam.MjpgStreamBottom;
import gcs.network.Network;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class LowerCamViewerController implements Initializable {
    public static LowerCamViewerController instance;
    @FXML private Canvas canvas1;
    @FXML private Canvas canvas2;
    @FXML private BorderPane borderPane;
    
    private GraphicsContext ctx2;
    
    private double width;
    private double height;
    
    private double degree = 2*Math.PI/360; // 라디안 단위의 1도 값
    private double r; // 반지름
    
    public MjpgStreamBottom camStream;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       LowerCamViewerController.instance = this;
       width = canvas1.getWidth();
       height = canvas1.getHeight();
       
       initCanvasLayer2();       
       ViewLoop viewLoop = new ViewLoop();
       viewLoop.start();
    }
    
    private void initCanvasLayer2() {
        ctx2 = canvas2.getGraphicsContext2D();
        ctx2.setStroke(Color.rgb(0x00, 0x00, 0x00, 0.5));
     }
    
    class ViewLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
           canvas1.setWidth(borderPane.getWidth());
           canvas1.setHeight(borderPane.getHeight());
           canvas2.setWidth(borderPane.getWidth());
           canvas2.setHeight(borderPane.getHeight());
           width = canvas1.getWidth();
           height = canvas1.getHeight();
           r = width/3.5;
           layer2Draw();
           
           /*Platform.runLater(()->{
              lblTime.setFont(new Font(16/960*width));
              lblSecond.setFont(new Font(16/960*width));});*/
        }
     }
    
    private void layer2Draw() {
        ctx2.translate(width/2, height/2);
        ctx2.clearRect(-width, -height, 2*width, 2*height);
        
        // 중심부의 중점 그리기
        ctx2.setStroke(Color.rgb(0xff,0x00,0x00, 0.5));
        ctx2.setLineWidth(3*width/960);
        ctx2.strokeLine(-width/55/2, 0, width/55/2, 0);
        ctx2.strokeLine(0, -width/55/2, 0, width/55/2);
        ctx2.strokeOval(-width/55, -width/55, width/55*2, width/55*2);
        
        // 지시부 그리기
        ctx2.setStroke(Color.rgb(0x00, 0x00, 0x00, 0.5));
        ctx2.strokeLine(0, r/1.6, 0, r);
        ctx2.strokeLine(0, -r/1.6, 0, -r);
        ctx2.strokeLine(r/1.6, 0, r, 0);
        ctx2.strokeLine(-r/1.6, 0, -r, 0);
        
        // 설정 되돌아가기(원점이동, 모든 색상)
        ctx2.translate(-width/2, -height/2);
        
    }

    public void mqttView() {
        try {
            camStream = new MjpgStreamBottom(canvas1);
            camStream.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}