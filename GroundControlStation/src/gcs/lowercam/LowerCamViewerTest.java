package gcs.lowercam;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LowerCamViewerTest extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("lowercamviewer.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		// fx창을 끌 경우, 해당 메인 스레드도 종료되도록 설정
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
				
			}
		});
		//임베디드 환경에서 풀스크린 모드(제목표시줄도 X)
		/*primaryStage.setFullScreen(true);// 시작 시 전체화면으로 뜨게함 (임베디드 활용 시)
		primaryStage.setFullScreenExitHint(" "); // 전체화면 끄는 도움말 변경
		primaryStage.minHeightProperty().bind(scene.widthProperty().divide(1.7777));
		primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(1.7777)); //가로 세로 비
		 */
		
		primaryStage.show();
		
//		Thread thread = new Thread() {
//			double value = 0;
//			@Override
//			public void run() { // -20도 ~ 20도 방향으로 roll하겠다.
//				while(true) {
//					
//					while(true) {
//						//HudViewController.instance.setRollPitchYaw(-value, value, value/4);
//						HudViewController.instance.setDirection(6*(value+30));
//						//HudViewController.instance.setAltitude(value+30);
//						value++;
//						if(value>30) break;
//						try {Thread.sleep(100);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//					
//					while(true) {
//						//HudViewController.instance.setRollPitchYaw(-value, value, value/4);
//						HudViewController.instance.setDirection(6*(value+30));
//						//HudViewController.instance.setAltitude(value+30);
//						value--;
//						if(value<-30) break;
//						try {Thread.sleep(100);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				
//			}
//		};
//		thread.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
