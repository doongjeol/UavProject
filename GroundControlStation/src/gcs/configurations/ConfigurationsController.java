package gcs.configurations;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.appmain.AppMainController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConfigurationsController implements Initializable{
	public static ConfigurationsController instance;
	@FXML private TextField txtMqttIp;
	@FXML private TextField txtMqttPort;
	@FXML private TextField txtUavPubTopic;
	@FXML private TextField txtUavSubTopic;
	@FXML private TextField txtCameraPubTopicFront;
	@FXML private TextField txtCameraSubTopicFront;
	@FXML private TextField txtCameraPubTopicBottom;
	@FXML private TextField txtCameraSubTopicBottom;
	
	@FXML public Label lblNotice;
	@FXML private TextField txtRtlAlt;
	@FXML private TextField txtRngFnd;
	@FXML private TextField txtFenceEnable;
	@FXML private TextField txtFenceAction;
	@FXML private TextField txtFenceAltMax;
	@FXML private TextField txtFenceMargin;
	@FXML private TextField txtBattCapacity;
	@FXML private TextField txtBattLowVolt;
	@FXML private TextField txtLandSpeed;
	@FXML private TextField txtWpnavRadius;
	@FXML private TextField txtWpnavSpeed;
	@FXML private TextField txtWpnavDn;
	@FXML private TextField txtWpnavUp;
	@FXML private TextField txtWpnavAccel;
	@FXML private TextField txtWpnavAccelZ;
	
	@FXML private Button btnSaveNetwork;
	@FXML private Button btnResetNetwork;
	
	@FXML private Button btnWriteParams;
	@FXML private Button btnResetParams;
	
	public static boolean settingsRenewal;
	public static boolean settingsReset;
	public static boolean settingsNotice = false;
	
	public String mqttIp;
	public int mqttPort;
	public String uavPubTopic;
	public String uavSubTopic;
	public String uavCameraFrontSubTopic;
	public String uavCameraBottomSubTopic;
	public String uavCameraFrontPubTopic;
	public String uavCameraBottomPubTopic;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConfigurationsController.instance = this;
		initNetworkTxt();
		initResetNetworkSetting();

		btnWriteParams.setOnAction((e)-> {handleBtnWriteParams(e);});
		btnResetParams.setOnAction((e)-> {handleBtnResetParams(e);});
		btnSaveNetwork.setOnAction((e)-> {handleBtnSaveNetwork(e);});
		btnResetNetwork.setOnAction((e)-> {handleBtnResetNework(e);});
	}
	
	public void initNetworkTxt(){
		txtMqttIp.setText(Network.mqttIp);
		txtMqttPort.setText(String.valueOf(Network.mqttPort));
		txtUavPubTopic.setText(Network.uavPubTopic);
		txtUavSubTopic.setText(Network.uavSubTopic);
		txtCameraPubTopicFront.setText(Network.uavCameraFrontPubTopic);
		txtCameraSubTopicFront.setText(Network.uavCameraFrontSubTopic);
		txtCameraPubTopicBottom.setText(Network.uavCameraBottomPubTopic);
		txtCameraSubTopicBottom.setText(Network.uavCameraBottomSubTopic);
	}
	
	public void initResetNetworkSetting() {
		mqttIp = Network.mqttIp;
		mqttPort = Network.mqttPort;
		uavPubTopic = Network.uavPubTopic;
		uavSubTopic = Network.uavSubTopic;
		uavCameraFrontSubTopic = Network.uavCameraFrontSubTopic;
		uavCameraBottomSubTopic = Network.uavCameraBottomSubTopic;
		uavCameraFrontPubTopic = Network.uavCameraFrontPubTopic;
		uavCameraBottomPubTopic = Network.uavCameraBottomPubTopic;
	}
	
	public void handleBtnSaveNetwork(ActionEvent e) {
		Platform.runLater(() -> {
			txtMqttIp.setText(txtMqttIp.getText());
			txtMqttPort.setText(txtMqttPort.getText());
			txtUavPubTopic.setText(txtUavPubTopic.getText());
			txtUavSubTopic.setText(txtUavSubTopic.getText());
			txtCameraPubTopicFront.setText(txtCameraPubTopicFront.getText());
			txtCameraSubTopicFront.setText(txtCameraSubTopicFront.getText());
			txtCameraPubTopicBottom.setText(txtCameraPubTopicBottom.getText());
			txtCameraSubTopicBottom.setText(txtCameraSubTopicBottom.getText());
		});
		
		Network.mqttIp = txtMqttIp.getText();
		Network.mqttPort = Integer.parseInt(txtMqttPort.getText());
		Network.uavPubTopic = txtUavPubTopic.getText();
		Network.uavSubTopic = txtUavSubTopic.getText();
		Network.uavCameraFrontPubTopic = txtCameraPubTopicFront.getText();
		Network.uavCameraFrontSubTopic = txtCameraSubTopicFront.getText();
		Network.uavCameraBottomPubTopic = txtCameraPubTopicBottom.getText();
		Network.uavCameraBottomSubTopic = txtCameraSubTopicBottom.getText();
	}
	
	public void handleBtnResetNework(ActionEvent e) {
		Platform.runLater(() -> {
			txtMqttIp.setText(mqttIp);
			txtMqttPort.setText(String.valueOf(mqttPort));
			txtUavPubTopic.setText(uavPubTopic);
			txtUavSubTopic.setText(uavSubTopic);
			txtCameraPubTopicFront.setText(uavCameraFrontPubTopic);
			txtCameraSubTopicFront.setText(uavCameraFrontSubTopic);
			txtCameraPubTopicBottom.setText(uavCameraBottomPubTopic);
			txtCameraSubTopicBottom.setText(uavCameraBottomSubTopic);
		});
		
		Network.mqttIp = mqttIp;
		Network.mqttPort = mqttPort;
		Network.uavPubTopic = uavPubTopic;
		Network.uavSubTopic = uavSubTopic;
		Network.uavCameraFrontPubTopic = uavCameraFrontPubTopic;
		Network.uavCameraFrontSubTopic = uavCameraFrontSubTopic;
		Network.uavCameraBottomPubTopic = uavCameraBottomPubTopic;
		Network.uavCameraBottomSubTopic = uavCameraBottomSubTopic;
	}
	
	public void handleBtnWriteParams(ActionEvent e) {
		if(AppMainController.instance.isConnectStatus()) {
			settingsNotice = true;
			settingsReset = false;
			getStatus();
			Platform.runLater(() -> {
				lblNotice.setText("Updating Params Accomplished");
			});
		} else {
			Platform.runLater(() -> {
				lblNotice.setText("Error: Not Connected between GCS and Drone");
			});
		}
	}
	
	public void handleBtnResetParams(ActionEvent e) {
		if(AppMainController.instance.isConnectStatus()) {
			settingsNotice = true;
			settingsReset = true;
			settingsRenewal = true;
			getStatus();
			Platform.runLater(() -> {
				lblNotice.setText("Resetting Params Accomplished");
			});
		} else {
			Platform.runLater(() -> {
				lblNotice.setText("Error: Not Connected between GCS and Drone");
			});
		}
	}
	
	public void setStatus(UAV uav) {
		settingsRenewal=true;
		Network.getUav().settingsDownload();
		Platform.runLater(() -> {
			txtRtlAlt.setText(String.valueOf(uav.rtl_alt));
			txtLandSpeed.setText(String.valueOf(uav.land_speed));
			txtRngFnd.setText(String.valueOf(uav.rng_fnd));
			txtFenceEnable.setText(String.valueOf(uav.fence_enable));
			txtFenceAction.setText(String.valueOf(uav.fence_action));
			txtFenceAltMax.setText(String.valueOf(uav.fence_alt_max));
			txtFenceMargin.setText(String.valueOf(uav.fence_margin));
			txtBattCapacity.setText(String.valueOf(uav.batt_capacity));
			txtBattLowVolt.setText(String.valueOf(uav.batt_low_volt));
			txtWpnavRadius.setText(String.valueOf(uav.wpnav_radius));
			txtWpnavSpeed.setText(String.valueOf(uav.wpnav_speed));
			txtWpnavDn.setText(String.valueOf(uav.wpnav_dn));
			txtWpnavUp.setText(String.valueOf(uav.wpnav_up));
			txtWpnavAccel.setText(String.valueOf(uav.wpnav_accel));
			txtWpnavAccelZ.setText(String.valueOf(uav.wpnav_accel_z));
		});
	}
	
	public void getStatus() {
		UAVFlightConfigurations settings = new UAVFlightConfigurations();
		
		if(!settingsReset) {
			settings.rtl_alt = Double.parseDouble(txtRtlAlt.getText());
			settings.land_speed = Double.parseDouble(txtLandSpeed.getText());
			settings.rng_fnd = Integer.parseInt(txtRngFnd.getText());
			settings.fence_enable = Integer.parseInt(txtFenceEnable.getText());
			settings.fence_action = Integer.parseInt(txtFenceAction.getText());
			settings.fence_alt_max = Double.parseDouble(txtFenceAltMax.getText());
			settings.fence_margin = Double.parseDouble(txtFenceMargin.getText());
			settings.batt_capacity = Double.parseDouble(txtBattCapacity.getText());
			settings.batt_low_volt = Double.parseDouble(txtBattLowVolt.getText());
			settings.wpnav_radius = Double.parseDouble(txtWpnavRadius.getText());
			settings.wpnav_speed = Double.parseDouble(txtWpnavSpeed.getText());
			settings.wpnav_dn = Double.parseDouble(txtWpnavDn.getText());
			settings.wpnav_up = Double.parseDouble(txtWpnavUp.getText());
			settings.wpnav_accel = Double.parseDouble(txtWpnavAccel.getText());
			settings.wpnav_accel_z = Double.parseDouble(txtWpnavAccelZ.getText());
		} else {
			settings.rtl_alt = 0;
			settings.land_speed = 11;
			settings.rng_fnd = 15;
			settings.fence_enable = 0;
			settings.fence_action = 1;
			settings.fence_alt_max = 5;
			settings.fence_margin = 1;
			settings.batt_capacity = 8400;
			settings.batt_low_volt = 10.5;
			settings.wpnav_radius = 1000;
			settings.wpnav_speed = 100;
			settings.wpnav_dn = 100;
			settings.wpnav_up = 100;
			settings.wpnav_accel = 100;
			settings.wpnav_accel_z = 100;
		}
		
		Network.getUav().settingsUpload(settings);
	}
	
	public void setDisableEditable(boolean connectStatus) {
		if(connectStatus) {
			txtMqttIp.setEditable(false);
			txtMqttPort.setEditable(false);
			txtUavPubTopic.setEditable(false);
			txtUavSubTopic.setEditable(false);
			txtCameraPubTopicFront.setEditable(false);
			txtCameraSubTopicFront.setEditable(false);
			txtCameraPubTopicBottom.setEditable(false);
			txtCameraSubTopicBottom.setEditable(false);
		} else {
			txtMqttIp.setEditable(true);
			txtMqttPort.setEditable(true);
			txtUavPubTopic.setEditable(true);
			txtUavSubTopic.setEditable(true);
			txtCameraPubTopicFront.setEditable(true);
			txtCameraSubTopicFront.setEditable(true);
			txtCameraPubTopicBottom.setEditable(true);
			txtCameraSubTopicBottom.setEditable(true);
		}
	}

}