package gcs.ctrlstat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CtrlstatTest extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ctrlstat.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
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
