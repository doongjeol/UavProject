package gcs.mission;

import gcs.network.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MissionMain extends Application {	
	public static MissionMain instance;
	public Stage primaryStage;
	public Scene scene;
	public String theme;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			instance = this;
			this.primaryStage = primaryStage;
			
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("mission.fxml"));
			scene = new Scene(root);
			
			primaryStage.setTitle("UAV Ground Control Station");
			primaryStage.setScene(scene);
			
			//primaryStage.setWidth(1300);
			//primaryStage.setHeight(870);
			
			primaryStage.setMaximized(true);
			
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		try {
			Network.getUav().disconnect();
		} catch(Exception e) {}
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
}


