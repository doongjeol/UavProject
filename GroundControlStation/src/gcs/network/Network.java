package gcs.network;

import gcs.hud.HudViewController;
import gcs.lowercam.LowerCamViewerController;

public class Network {
	public static String mqttIp = "106.253.56.122"; // KOSA IoT External IP
//	public static String mqttIp = "112.168.84.25"; // KJ Home External IP
//	public static String mqttIp = "localhost";
	public static int mqttPort = 1883;
	public static String uavPubTopic = "/uav3/pub";
	public static String uavSubTopic = "/uav3/sub";
	public static String uavCameraFrontSubTopic = "/uav3/cameraFront/sub";
	public static String uavCameraBottomSubTopic = "/uav3/cameraBottom/sub";
	public static String uavCameraFrontPubTopic = "/uav3/cameraFront/pub";
	public static String uavCameraBottomPubTopic = "/uav3/cameraBottom/pub";
	
	private static UAV uav;
	
	public static void connect() {
		uav = new UAV();
		uav.connect();
		HudViewController.instance.mqttView();
		LowerCamViewerController.instance.mqttView();
	}
	
	public static void disconnect() {
		uav.disconnect();
		HudViewController.instance.camStream.stop();
		LowerCamViewerController.instance.camStream.stop();
	}
	
	public static UAV getUav() {
		return uav;
	}
}
