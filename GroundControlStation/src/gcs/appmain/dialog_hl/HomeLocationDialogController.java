package gcs.appmain.dialog_hl;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.ctrlstat.CtrlstatController;
import gcs.mission.MissionController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeLocationDialogController implements Initializable {
	@FXML private TextField txtLat;
	@FXML private TextField txtLng;
	@FXML private TextField txtAbsAlt;	
	@FXML private Button btnConfirm;
	@FXML private Button btnCancel;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		UAV uav = Network.getUav();
		txtLat.setText(String.valueOf(uav.homeLat));
		txtLng.setText(String.valueOf(uav.homeLng));
		txtAbsAlt.setText(String.valueOf(uav.altitudeAbs));
		
		
		btnConfirm.setOnAction((event)->{
			double lat = 0, lng = 0, absAlt = 0;
			int parseFlag = 0;
			
			// 값 파싱 및 예외처리
			try {
				lat = Double.parseDouble(txtLat.getText()); 
				txtLat.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtLat.setStyle("-fx-background-color:red;"); 
				parseFlag++; 
			}
			try { 
				lng = Double.parseDouble(txtLng.getText());
				txtLng.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtLng.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			try {
				absAlt = Double.parseDouble(txtAbsAlt.getText());
				txtAbsAlt.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) {
				txtAbsAlt.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			
			if(parseFlag>0) return; // 숫자가 아닌 값이 하나라도 들어오면 홈위치지정 불가
			
			// Home Location 변경 기능 실행
			MissionController.setHome(lat, lng, absAlt);
			
			((Stage)btnCancel.getScene().getWindow()).close();
		});
		
		btnCancel.setOnAction((event)->{
			((Stage)btnCancel.getScene().getWindow()).close();
		});
	}
}
