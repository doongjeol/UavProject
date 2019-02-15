package gcs.capture;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CaptureTest extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("capture.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		// fx창을 끌 경우, 해당 메인 스레드도 종료되도록 설정
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		primaryStage.show();

	}
	public static void main(String[] args) {
		launch(args);
	}

}
