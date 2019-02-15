package gcs.appmain.dialog_distcalc;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.appmain.AppMainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DistCalcDialogController implements Initializable {
	@FXML private TextField txtHomeLat;
	@FXML private TextField txtHomeLng;
	@FXML private TextField txtHomeAlt;	
	@FXML private TextField txtWpLat;
	@FXML private TextField txtWpLng;
	@FXML private TextField txtWpAlt;	
	@FXML private TextField txtDpLat;
	@FXML private TextField txtDpLng;
	@FXML private TextField txtDpAlt;	
	@FXML private Button btnConfirm;
	@FXML private Button btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtHomeLat.setText(String.valueOf(AppMainController.instance.homeLat));
		txtHomeLng.setText(String.valueOf(AppMainController.instance.homeLng));
		txtHomeAlt.setText(String.valueOf(AppMainController.instance.homeAlt));
		txtWpLat.setText(String.valueOf(AppMainController.instance.wpLat));
		txtWpLng.setText(String.valueOf(AppMainController.instance.wpLng));
		txtWpAlt.setText(String.valueOf(AppMainController.instance.wpAlt));
		txtDpLat.setText(String.valueOf(AppMainController.instance.dpLat));
		txtDpLng.setText(String.valueOf(AppMainController.instance.dpLng));
		txtDpAlt.setText(String.valueOf(AppMainController.instance.dpAlt));
		
		btnConfirm.setOnAction((event)->{
			double homeLat = 0, homeLng = 0, homeAlt = 0;
			double wpLat = 0, wpLng = 0, wpAlt = 0;
			double dpLat = 0, dpLng = 0, dpAlt = 0;
			int parseFlag = 0;
			
			// 값 파싱 및 예외처리
			try {
				homeLat = Double.parseDouble(txtHomeLat.getText()); 
				txtHomeLat.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtHomeLat.setStyle("-fx-background-color:red;"); 
				parseFlag++; 
			}
			try { 
				homeLng = Double.parseDouble(txtHomeLng.getText());
				txtHomeLng.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtHomeLng.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			try {
				homeAlt = Double.parseDouble(txtHomeAlt.getText());
				txtHomeAlt.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) {
				txtHomeAlt.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			
			try {
				wpLat = Double.parseDouble(txtWpLat.getText()); 
				txtWpLat.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtWpLat.setStyle("-fx-background-color:red;"); 
				parseFlag++; 
			}
			try { 
				wpLng = Double.parseDouble(txtWpLng.getText());
				txtWpLng.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtWpLng.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			try {
				wpAlt = Double.parseDouble(txtWpAlt.getText());
				txtWpAlt.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) {
				txtWpAlt.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			
			try {
				dpLat = Double.parseDouble(txtDpLat.getText()); 
				txtDpLat.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtDpLat.setStyle("-fx-background-color:red;"); 
				parseFlag++; 
			}
			try { 
				dpLng = Double.parseDouble(txtDpLng.getText());
				txtDpLng.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) { 
				txtDpLng.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			try {
				dpAlt = Double.parseDouble(txtDpAlt.getText());
				txtDpAlt.setStyle("-fx-background-color:#103554;");
			} catch(Exception e) {
				txtDpAlt.setStyle("-fx-background-color:red;");
				parseFlag++;
			}
			
			if(parseFlag>0) return; // 숫자가 아닌 값이 하나라도 들어오면 값 변경 불가
			
			// Base Location 변경
			AppMainController.instance.homeLat = homeLat;
			AppMainController.instance.homeLng = homeLng;
			AppMainController.instance.homeAlt = homeAlt;
			AppMainController.instance.wpLat = wpLat;
			AppMainController.instance.wpLng = wpLng;
			AppMainController.instance.wpAlt = wpAlt;
			AppMainController.instance.dpLat = dpLat;
			AppMainController.instance.dpLng = dpLng;
			AppMainController.instance.dpAlt = dpAlt;
			/*System.out.println(homeLat + "__" + homeLng + "__" + homeAlt);
			System.out.println(wpLat + "__" + wpLng + "__" + wpAlt);
			System.out.println(dpLat + "__" + dpLng + "__" + dpAlt);*/
			((Stage)btnCancel.getScene().getWindow()).close();
		});
		
		btnCancel.setOnAction((event)->{
			((Stage)btnCancel.getScene().getWindow()).close();
		});
	}

}
