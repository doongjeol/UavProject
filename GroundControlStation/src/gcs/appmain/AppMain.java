package gcs.appmain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class AppMain extends Application{
	
	public static AppMain instance;
	public Stage primaryStage;
	public Scene scene;
	  
	@Override
	public void start(Stage primaryStage) {
		try {
			instance = this;
			this.primaryStage = primaryStage;
			
			Parent root;
			root = FXMLLoader.load(getClass().getResource("appmain.fxml"));
			scene = new Scene(root);
			
			//ctrlstat css
			scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("UAV Ground Control Station");
			primaryStage.setMaximized(true);
//			primaryStage.setFullScreen(true); 
//			primaryStage.setFullScreenExitHint("");
			primaryStage.show();
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	
	@Override
	public void stop() throws Exception {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		try {
			launch(args);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
