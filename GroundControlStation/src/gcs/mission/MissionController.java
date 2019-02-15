package gcs.mission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import gcs.appmain.AppMain;
import gcs.appmain.AppMainController;
import gcs.ctrlstat.CtrlstatController;
import gcs.hud.HudViewController;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import netscape.javascript.JSObject;

public class MissionController implements Initializable{
	public static MissionController instance;
	
	private Date dateTime;
	private Format dateFormatter;
	private Format timeFormatter;
	
	@FXML private WebView webView;
	private WebEngine webEngine;
	@FXML private ImageView imgBtnMap;
	@FXML private ImageView imgBtnSatellite;
	private boolean isSatellite;
	@FXML private ImageView imgBtnMapFix;
	private boolean mapFixStatus;
	
	public boolean autoStatus;

	@FXML private Label lblSatellite;
	@FXML private Label lblMap;
	@FXML private Canvas canvas;
	private GraphicsContext ctx;
	private LinearGradient blueDistGaugeLinearGradient;
	
	private JSObject jsproxy;
	
	@FXML private Slider zoomSlider;
	
	@FXML public VBox missionVBox;
	@FXML public VBox fenceVBox;
	@FXML public VBox noFlyZoneVBox;
	@FXML public VBox consoleVBox;
	@FXML public VBox toolTipVBox;
	
	@FXML private Button btnGetMissionFromFile;
	@FXML private Button btnSaveMissionToFile;
	@FXML private Button btnMissionUpload;
	@FXML private Button btnMissionDownload;
	@FXML private Button btnMissionStart;	
	@FXML private Button btnMissionStop;
	@FXML public TextField insertAlt;
	
	@FXML private Button btnAddTakeoff;
	@FXML private Button btnAddRTL;
	@FXML private Button btnAddROI;
	@FXML private Button btnAddJump;
	@FXML private Button btnAddLand;
	@FXML private Button btnMissionClear;
	@FXML private Button btnAutoTakeoff;

	@FXML private Button btnFenceUpload;
	@FXML private Button btnFenceDownload;
	@FXML private Button btnFenceEnable;
	@FXML private Button btnFenceDisable;

	@FXML private Button btnFenceClear;

	@FXML private TextField insertLat;
	@FXML private TextField insertLng;
	@FXML private TextField insertRad;
	
	@FXML private Button btnNoFlyZoneAdd;
	@FXML private Button btnNoFlyZoneEnable;
	public boolean isNoFlyZone;
	@FXML private Button btnNoFlyZoneDisable;
	
	@FXML private ImageView imgMissionView;
	@FXML private ImageView imgFenceView;
	@FXML private ImageView imgNoFlyZoneView;
	@FXML private ImageView imgConsoleView;
	
	private boolean imgSelectFlag;
	public boolean isClicked_imgNoFlyZoneView;
	private boolean consoleReadStatus;
	
	private WayPoint wayPoint;
	private FencePoint fencePoint;
	
	@FXML public TableView<WayPoint> missionTableView;
	@FXML public TableView<FencePoint> fenceTableView;
	@FXML public TableView<NoFlyZonePoint> noFlyZoneTableView;
	@FXML public ListView<String> consoleListView;
	
	@FXML private CheckBox chkConsoleListenOption;
	@FXML private Button btnConsoleClear;
	
	private int restDistance;
	public boolean wp_distance;
	private double width;
	private double height;
	private double w;
	private double h;
	private double[] markerArray;
	public boolean missionStatus;
	private boolean missionPausedStatus;
	@FXML private BorderPane borderPane;
	
	@FXML public Label lblFlight;
	@FXML public Label lblDistance;
	@FXML public Label lblTotal;
	private int currWaypointNo;
	
	@FXML private Label lblMessage;
	@FXML private StackPane paneMessage;
	
	private Thread messageThread;
	public boolean threadStatus2;
	private int listSize;
	public int landNo;
	
	private double nowLat;
	private double nowLog;
	
	private String tempMessage = "";
	
	private List<WayPoint> missionList = new ArrayList<WayPoint> ();
	private List<WayPoint> originalMissionList = new ArrayList<WayPoint> ();
	private List<NoFlyZonePoint> circleList = new ArrayList<NoFlyZonePoint> ();
	private List<String> consoleList = new ArrayList<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		isNoFlyZone = false;
		isSatellite = true;
		mapFixStatus = true;
		wp_distance = false;
		missionStatus = false;
		missionPausedStatus= false;
		consoleReadStatus = true;
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd",new Locale("en", "US"));
		timeFormatter = new SimpleDateFormat("HH:mm:ss");
		autoStatus = false;
		width = canvas.getWidth();
		height = canvas.getHeight();
		h = 3.2*height/5;
		
		initCanvasLayer();
		initWebView();
		initButton();
		initImageView();
		initMissionTableView();
		initFenceTableView();
		initNoFlyZoneTableView();
		
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
	}	
	
	class ViewLoop extends AnimationTimer{
		@Override
		public void handle(long now) {
			try {
				restDistance = (int) MissionCalculator.restDistance3D();
				Platform.runLater(()->lblDistance.setText(restDistance + "m"));
			} catch(Exception e) {
				Platform.runLater(()->lblDistance.setText(0 + "m"));
			}
			
			canvas.setWidth(borderPane.getWidth()-10);
			width = canvas.getWidth();
			w = 5*width/100;
			
			layer1Draw();
		}
	}
	
	private void initCanvasLayer() {
		ctx = canvas.getGraphicsContext2D();
		
		ctx.setTextBaseline(VPos.CENTER);
		blueDistGaugeLinearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
				new Stop(0, Color.rgb(0xBD, 0xD7, 0xEE)),
				new Stop(0.5, Color.rgb(0x5B, 0x9B, 0xD5)),
				new Stop(1, Color.rgb(0x1F, 0x4E, 0x79)));
	}
	
	private void layer1Draw() {
		ctx.clearRect(0, 0, width, height);
		ctx.setLineWidth(7);
		ctx.setStroke(Color.GRAY);
		ctx.strokeLine(w, h, 19*w, h);

		ctx.setLineWidth(5);
		if(!missionStatus) {
			ctx.setStroke(Color.WHITE);
			ctx.strokeLine(w, h, 19*w, h);
		} else {
			ctx.setStroke(blueDistGaugeLinearGradient);
			ctx.strokeLine(w, h, 19*w, h);
			if(restDistance>2) {
				ctx.setStroke(Color.WHITE);
				ctx.strokeLine(w+(MissionCalculator.totalDistance-restDistance)/MissionCalculator.totalDistance*18*w, h, 19*w, h);
				try {
					int size = MissionCalculator.uavTravelList.size();		
					markerArray = new double[size]; // stateArray+45 된 값으로 marker가 그려지는 위치
					if(missionStatus) {
						for(int i=0;i<size;i++) {
							markerArray[i] = MissionCalculator.propCuDistArray[i]*0.1806*w+w*38/50;
							Image img = new Image(getClass().getResource("../images/point.png").toExternalForm());
							ctx.drawImage(img, markerArray[i], h/3.2);
							//System.out.print(i +": " +  markerArray[i] + "   ");
						}
						//System.out.println("");
					}	
				} catch(Exception e) {}
			}
		}
	}
	
	private ChangeListener<State> webEngineLoadStateListener = (observable, oldValue, newValue) -> {
		if(newValue == State.SUCCEEDED) {
			Platform.runLater(() -> {
				try {
					webEngine.executeScript("console.log = function(message) { jsproxy.java.log(message); };");
					jsproxy = (JSObject) webEngine.executeScript("jsproxy");
					jsproxy.setMember("java", MissionController.this);
					MissionController.instance.setJsproxy(jsproxy);
					//setMapSize();
				} catch(Exception e) {
					e.printStackTrace();
				}
			});
		}
	};
	
	private ChangeListener<Number> sliderValueListener = (observable, oldValue, newValue) -> {
		int value = newValue.intValue();
		if(value < 3) {
			zoomSlider.setValue(3);
			return;
		}
		jsproxy.call("setMapZoom", value);	
	};
	
	private void initWebView() {		
		webEngine = webView.getEngine();
		webEngine.getLoadWorker().stateProperty().addListener(webEngineLoadStateListener);	
		webEngine.load(getClass().getResource("javascript/map.html").toExternalForm());
		zoomSlider.valueProperty().addListener(sliderValueListener);
	}
	private void initButton() {
		btnAutoTakeoff.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
		btnNoFlyZoneEnable.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
		imgBtnMapFix.setOnMouseClicked(imgBtnMapFixHandler);
		imgBtnMap.setOnMouseClicked(imgBtnMapHandler);
		imgBtnSatellite.setOnMouseClicked(imgBtnSatelliteHandler);
		
		btnGetMissionFromFile.setOnAction((event)->{handleBtnGetMissionFromFile(event);});
		btnSaveMissionToFile.setOnAction((event)->{handleBtnSaveMissionToFile(event);});
		btnMissionUpload.setOnAction((event)->{handleBtnMissionUpload(event);});
		btnMissionDownload.setOnAction((event)->{handleBtnMissionDownload(event);});
		btnMissionStart.setOnAction((event)->{handleBtnMissionStart(event);});
		btnMissionStop.setOnAction((event)->{handleBtnMissionStop(event);});
		
		btnAddTakeoff.setOnAction((event)->{handleBtnAddTakeoff(event);});
		btnAddROI.setOnAction((event)->{handleBtnAddROI(event);});
		btnAddRTL.setOnAction((event)->{handleBtnAddRTL(event);});
		btnAddJump.setOnAction((event)->{handleBtnAddJump(event);});
		btnAddLand.setOnAction((event)->{handleBtnAddLand(event);});
		btnMissionClear.setOnAction((event)->{handleBtnMissionClear(event);});
		
		btnFenceClear.setOnAction((event)->{handleBtnFenceClear(event);});
		btnFenceUpload.setOnAction((event)->{handleBtnFenceUpload(event);});
		btnFenceDownload.setOnAction((event)->{handleBtnFenceDownload(event);});
		btnFenceEnable.setOnAction((event)->{handleBtnFenceEnable(event);});
		btnFenceDisable.setOnAction((event)->{handleBtnFenceDisable(event);});
		
		btnNoFlyZoneAdd.setOnAction((event)->{handleBtnNoFlyZoneAdd(event);});
		btnNoFlyZoneEnable.setOnAction((event)->{handleBtnNoFlyZoneEnable(event);});
		btnAutoTakeoff.setOnAction((event)->{handleBtnAutoTakeoff(event);});
		btnNoFlyZoneDisable.setOnAction((event)->{handleBtnNoFlyZoneDisable(event);});
		
		chkConsoleListenOption.setOnAction(checkBoxConsoleListenOption);
		btnConsoleClear.setOnAction((event)->{handleBtnConsoleClear(event);});
		
		missionTableView.setOnKeyPressed((event)->{handleDeleteKeyEvent("mission", event);});
		fenceTableView.setOnKeyPressed((event)->{handleDeleteKeyEvent("fence", event);});
		noFlyZoneTableView.setOnKeyPressed((event)->{handleDeleteKeyEvent("noflyzone", event);});
	}
	
	private void initImageView() {
		imgMissionView.setOnMouseClicked((event)->{handleImgMissionView(event);});
		imgFenceView.setOnMouseClicked((event)->{handleImgFenceView(event);});
		imgNoFlyZoneView.setOnMouseClicked((event)->{handleImgNoFlyZoneView(event);});
		imgConsoleView.setOnMouseClicked((event)->{handleImgConsoleView(event);});
	}
	
	private void initMissionTableView() {	
		insertAlt.setText("30");
		missionTableView.setEditable(true);
		
		TableColumn<WayPoint, Integer> column1 = new TableColumn<WayPoint, Integer>("No");
		column1.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("no"));
		column1.setPrefWidth(40);
		column1.setSortable(false);
		column1.impl_setReorderable(false); 
		
		TableColumn<WayPoint, String> column2 = new TableColumn<WayPoint, String>("Kind");
		column2.setCellValueFactory(new PropertyValueFactory<WayPoint, String>("kind"));
		column2.setPrefWidth(95);
		column2.setSortable(false);
		column2.impl_setReorderable(false);
		column2.setCellFactory(TextFieldTableCell.forTableColumn());
		column2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint,String>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, String> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setKind(t.getNewValue());
				wayPoint.kind = t.getNewValue();
			}
		});
		
		TableColumn<WayPoint, Double> column3 = new TableColumn<WayPoint, Double>("Lat");
		column3.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("latitude"));
		column3.setPrefWidth(135);
		column3.setSortable(false);
		column3.impl_setReorderable(false);
		column3.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column3.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint,Double>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, Double> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				int selecetedIndex= missionTableView.getSelectionModel().getSelectedIndex();
				
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setLatitude(t.getNewValue());
				wayPoint.latitude = t.getNewValue();
				modifyMissionMarker(wayPoint.latitude, wayPoint.longitude, selecetedIndex, wayPoint.kind, wayPoint.altitude, wayPoint.jump, wayPoint.repeat);
			}
		});
		
		TableColumn<WayPoint, Double> column4 = new TableColumn<WayPoint, Double>("Lng");
		column4.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("longitude"));
		column4.setPrefWidth(150);
		column4.setSortable(false);
		column4.impl_setReorderable(false);
		column4.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column4.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint,Double>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, Double> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				int selecetedIndex= missionTableView.getSelectionModel().getSelectedIndex();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setLongitude(t.getNewValue());
				wayPoint.longitude = t.getNewValue();
				modifyMissionMarker(wayPoint.latitude, wayPoint.longitude, selecetedIndex, wayPoint.kind, wayPoint.altitude, wayPoint.jump, wayPoint.repeat);
			}
		});
		
		TableColumn<WayPoint, Double> column5 = new TableColumn<WayPoint, Double>("Alt");
		column5.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("altitude"));
		column5.setPrefWidth(60);
		column5.setSortable(false);
		column5.impl_setReorderable(false);
		column5.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column5.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint,Double>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, Double> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setAltitude(t.getNewValue());
				wayPoint.altitude = t.getNewValue();
			}
		});

		TableColumn<WayPoint, Integer> column6 = new TableColumn<WayPoint, Integer>("Jump");
		column6.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("jump"));
		column6.setPrefWidth(40);
		column6.setSortable(false);
		column6.impl_setReorderable(false);
		column6.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		column6.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint, Integer>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, Integer> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				int selecetedIndex= missionTableView.getSelectionModel().getSelectedIndex();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setJump(t.getNewValue());
				if (t.getNewValue() != 0) {
					wayPoint.jump = t.getNewValue();
					
					List<WayPoint> list = new ArrayList<WayPoint>();
					list = missionTableView.getItems();				
					wayPoint.latitude = list.get(wayPoint.jump-1).latitude;
					wayPoint.longitude = list.get(wayPoint.jump-1).longitude;
					modifyMissionMarker(wayPoint.latitude, wayPoint.longitude, selecetedIndex, wayPoint.kind, wayPoint.altitude, wayPoint.jump, wayPoint.repeat);
				}
				else {
					wayPoint.jump = 1;
					
				}
			}
		});
		
		TableColumn<WayPoint, Integer> column7 = new TableColumn<WayPoint, Integer>("Rep#");
		column7.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("repeat"));
		column7.setPrefWidth(40);
		column7.setSortable(false);
		column7.impl_setReorderable(false);
		column7.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		column7.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WayPoint, Integer>>() {
			
			@Override
			public void handle(CellEditEvent<WayPoint, Integer> t) {
				wayPoint = missionTableView.getSelectionModel().getSelectedItem();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setRepeat(t.getNewValue());
				wayPoint.repeat = t.getNewValue();
			}
		});
		
		TableColumn<WayPoint, String> columnDelete = new TableColumn<WayPoint, String>("Del");
		columnDelete.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		columnDelete.setPrefWidth(40);
		columnDelete.setSortable(false);
		columnDelete.impl_setReorderable(false);
		columnDelete.setStyle("alignment: center");
		columnDelete.setCellFactory(new Callback<TableColumn<WayPoint, String>, TableCell<WayPoint, String>>() {
			
			@Override
			public TableCell<WayPoint, String> call(TableColumn<WayPoint, String> param) {
				TableCell<WayPoint, String> cell = new TableCell<WayPoint, String>() {
                    Button btnDelete = new Button("X");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                        	btnDelete.setOnAction(event -> {
                            	WayPoint wp = getTableView().getItems().get(getIndex());
                            	int no = wp.getNo()-1;
                            	deletePoint("WayPoint", no);
                            });
                            setGraphic(btnDelete);
                            setText(null);
                        }
                    }
                };
                return cell;
			}
		});
        
        TableColumn<WayPoint, String> columnMoveUp = new TableColumn<WayPoint, String>("Up");
        columnMoveUp.setCellValueFactory(new PropertyValueFactory<>("dummy"));
        columnMoveUp.setPrefWidth(40);
        columnMoveUp.setSortable(false);
        columnMoveUp.impl_setReorderable(false);
        columnMoveUp.setCellFactory(new Callback<TableColumn<WayPoint, String>, TableCell<WayPoint, String>>() {
        	
        	@Override
        	public TableCell<WayPoint, String> call(TableColumn<WayPoint, String> param) {
        		final TableCell<WayPoint, String> cell = new TableCell<WayPoint, String>() {
        			final Button btn = new Button("↑");
        			
        			@Override
        			public void updateItem(String item, boolean empty) {
        				super.updateItem(item, empty);
        				if (empty) {
        					setGraphic(null);
        					setText(null);
        				} else {
        					btn.setOnAction(event -> {
        						WayPoint wp = getTableView().getItems().get(getIndex());
        						int no = wp.no;
        						moveWayPoint("up", no-1);
        					});
        					setGraphic(btn);
        					setText(null);
        				}
        			}
        		};
        		return cell;
        	}
		});;
        
		TableColumn<WayPoint, String> columnMoveDown = new TableColumn<WayPoint, String>("Down");
		columnMoveDown.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		columnMoveDown.setPrefWidth(40);
		columnMoveDown.setSortable(false);
		columnMoveDown.impl_setReorderable(false);
        columnMoveDown.setCellFactory(new Callback<TableColumn<WayPoint,String>, TableCell<WayPoint,String>>() {
			
        	@Override
			public TableCell<WayPoint, String> call(TableColumn<WayPoint, String> param) {
				final TableCell<WayPoint, String> cell = new TableCell<WayPoint, String>() {
        			final Button btn = new Button("↓");
        			
        			@Override
        			public void updateItem(String item, boolean empty) {
        				super.updateItem(item, empty);
        				if (empty) {
        					setGraphic(null);
        					setText(null);
        				} else {
        					btn.setOnAction(event -> {
        						WayPoint wp = getTableView().getItems().get(getIndex());
        						int no = wp.no;
        						moveWayPoint("down", no-1);
        					});
        					setGraphic(btn);
        					setText(null);
        				}
        			}
        		};
        		return cell;
			}
		});
        
		missionTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, columnDelete, columnMoveUp, columnMoveDown);
	}
	
	public void setMissionTableViewItems(List<WayPoint> list) {
		missionTableView.getItems().clear();
		missionTableView.setItems(FXCollections.observableArrayList(list));
	}
	
	private void initFenceTableView() {
		fenceTableView.setEditable(true);
		
		TableColumn<FencePoint, Integer> column1 = new TableColumn<FencePoint, Integer>("No");
		column1.setCellValueFactory(new PropertyValueFactory<FencePoint, Integer>("no"));
		column1.setPrefWidth(50);
		column1.setSortable(false);
		column1.impl_setReorderable(false);
		
		TableColumn<FencePoint, Double> column2 = new TableColumn<FencePoint, Double>("Lat");
		column2.setCellValueFactory(new PropertyValueFactory<FencePoint, Double>("lat"));
		column2.setPrefWidth(135);
		column2.setSortable(false);
		column2.impl_setReorderable(false);
		column2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FencePoint, Double>>() {
			
			@Override
			public void handle(CellEditEvent<FencePoint, Double> t) {
				fencePoint = fenceTableView.getSelectionModel().getSelectedItem();
				int selecetedIndex= fenceTableView.getSelectionModel().getSelectedIndex();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setLat(t.getNewValue());
				fencePoint.lat = t.getNewValue();
				modifyFenceMarker(fencePoint.lat, fencePoint.lng, selecetedIndex);
			}
		});

		TableColumn<FencePoint, Double> column3 = new TableColumn<FencePoint, Double>("Lng");
		column3.setCellValueFactory(new PropertyValueFactory<FencePoint, Double>("lng"));
		column3.setPrefWidth(150);
		column3.setSortable(false);
		column3.impl_setReorderable(false);
		column3.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column3.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FencePoint, Double>>() {
			
			@Override
			public void handle(CellEditEvent<FencePoint, Double> t) {
				fencePoint = fenceTableView.getSelectionModel().getSelectedItem();
				int selecetedIndex= fenceTableView.getSelectionModel().getSelectedIndex();
				t.getTableView().getItems().get(
						t.getTablePosition().getRow()).setLng(t.getNewValue());
				fencePoint.lng = t.getNewValue();
				modifyFenceMarker(fencePoint.lat, fencePoint.lng, selecetedIndex);
			}

		});
		
		TableColumn<FencePoint, String> columnDelete = new TableColumn<FencePoint, String>("Del");
		columnDelete.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		columnDelete.setPrefWidth(40);
		columnDelete.setSortable(false);
		columnDelete.impl_setReorderable(false);
		columnDelete.setCellFactory(new Callback<TableColumn<FencePoint, String>, TableCell<FencePoint, String>>() {
			
			@Override
			public TableCell<FencePoint, String> call(TableColumn<FencePoint, String> param) {
				TableCell<FencePoint, String> cell = new TableCell<FencePoint, String>() {
                    Button btnDelete = new Button("X");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                        	btnDelete.setOnAction(event -> {
                        		FencePoint fp = getTableView().getItems().get(getIndex());
                            	int no = fp.getNo()-1;
                            	deletePoint("FencePoint", no);
                            });
                            setGraphic(btnDelete);
                            setText(null);
                        }
                    }
                };
                return cell;
			}
		});
		
		fenceTableView.getColumns().addAll(column1, column2, column3, columnDelete);
	}
	public void setFenceTableViewItems(List<FencePoint> list) {
		fenceTableView.getItems().clear();
		fenceTableView.setItems(FXCollections.observableArrayList(list));
	}
	
	private void initNoFlyZoneTableView() {
		TableColumn<NoFlyZonePoint, Integer> column1 = new TableColumn<NoFlyZonePoint, Integer>("No");
		column1.setCellValueFactory(new PropertyValueFactory<NoFlyZonePoint, Integer>("no"));
		column1.setPrefWidth(50);
		column1.setSortable(false);
		column1.impl_setReorderable(false);
		
		TableColumn<NoFlyZonePoint, Double> column2 = new TableColumn<NoFlyZonePoint, Double>("Lat");
		column2.setCellValueFactory(new PropertyValueFactory<NoFlyZonePoint, Double>("lat"));
		column2.setPrefWidth(135);
		column2.setSortable(false);
		column2.impl_setReorderable(false);

		TableColumn<NoFlyZonePoint, Double> column3 = new TableColumn<NoFlyZonePoint, Double>("Lng");
		column3.setCellValueFactory(new PropertyValueFactory<NoFlyZonePoint, Double>("lng"));
		column3.setPrefWidth(150);
		column3.setSortable(false);
		column3.impl_setReorderable(false); 

		TableColumn<NoFlyZonePoint, Double> column4 = new TableColumn<NoFlyZonePoint, Double>("Rad");
		column4.setCellValueFactory(new PropertyValueFactory<NoFlyZonePoint, Double>("rad"));
		column4.setPrefWidth(60);
		column4.setSortable(false);
		column4.impl_setReorderable(false); 
		
		TableColumn<NoFlyZonePoint, String> columnDelete = new TableColumn<NoFlyZonePoint, String>("Del");
		columnDelete.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		columnDelete.setPrefWidth(40);
		columnDelete.setSortable(false);
		columnDelete.impl_setReorderable(false);
		columnDelete.setCellFactory(new Callback<TableColumn<NoFlyZonePoint, String>, TableCell<NoFlyZonePoint, String>>() {
			
			@Override
			public TableCell<NoFlyZonePoint, String> call(TableColumn<NoFlyZonePoint, String> param) {
				TableCell<NoFlyZonePoint, String> cell = new TableCell<NoFlyZonePoint, String>() {
                    Button btnDelete = new Button("X");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                        	btnDelete.setOnAction(event -> {
                        		NoFlyZonePoint nfz = getTableView().getItems().get(getIndex());
                            	int no = nfz.getNo()-1;
                            	deletePoint("NoFlyZonePoint", no);
                            });
                            setGraphic(btnDelete);
                            setText(null);
                        }
                    }
                };
                return cell;
			}
		});
		
		noFlyZoneTableView.getColumns().addAll(column1, column2, column3, column4, columnDelete);
	}
	
	public void setNoFlyZoneTableView(List<NoFlyZonePoint> list) {
		noFlyZoneTableView.getItems().clear();
		noFlyZoneTableView.setItems(FXCollections.observableArrayList(list));
	}
	
	public void setZoomSliderValue(int zoom) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				zoomSlider.setValue(zoom);
			}
		});
	}
	
	public void setJsproxy(JSObject jsproxy) {
		this.jsproxy = jsproxy;
	}
	
	public void setStatus(UAV uav) {
		/*try {System.out.println(MissionCalculator.distance3D(uav.latitude, uav.longitude, uav.altitude,missionTableView.getItems().get(8)));
		} catch(Exception e) {}*/
		Platform.runLater(() -> {
			String lowerStatusText = uav.statusText.toLowerCase();
			if(lowerStatusText.contains("flight plan received")) {
				showMessage("Mission Upload Accomplished");
				consoleView("Mission Upload Accomplished");
			}else if(lowerStatusText.contains("mission download")) {
				showMessage("Mission Download Accomplished");
				consoleView("Mission Download Accomplished");
			} else if(lowerStatusText.contains("mission start")) {
				showMessage("Mission Start");
				consoleView("Mission Start");
			} else if(lowerStatusText.contains("mission stop")) {
				showMessage("Mission Abort");
				consoleView("Mission Abort");
			} else if(lowerStatusText.contains("mission clear")) {
				showMessage("Mission Clear");
				consoleView("Mission Clear");
			} else if(lowerStatusText.contains("takeoff")) {
				showMessage("Takeoff");
				consoleView("Takeoff");
			} else if(lowerStatusText.contains("rtl")) {
				showMessage("Return to Launch");
				consoleView("Return to Launch");
			} else if(lowerStatusText.contains("land")) {
				showMessage("Land");
				consoleView("Land");
			} else if(lowerStatusText.contains("goto")) {
				showMessage("Goto");
				consoleView("Goto");
			} else if(lowerStatusText.contains("reached command")) {
				int waypointNo = Integer.parseInt(uav.statusText.substring(uav.statusText.indexOf("#")+1).trim());
				showMessage("Waypoint #" + waypointNo + " Reached");
				consoleView("Waypoint #" + waypointNo + " Reached");
			} else if(lowerStatusText.contains("fence enable")) {
				showMessage("Fence Activated");
				consoleView("Fence Activated");
			} else if(lowerStatusText.contains("fence disable")) {
				showMessage("Fence Deactivated");
				consoleView("Fence Deactivated");
			} else if(lowerStatusText.contains("fence received")) {
				showMessage("Fence Upload Accomplished");
				consoleView("Fence Upload Accomplished");
			} else if(lowerStatusText.contains("fence download")) {
				showMessage("Fence Download Accomplished");
				consoleView("Fence Download Accomplished");
			} else if(lowerStatusText.contains("fence clear")) {
				showMessage("Fence Clear");
				consoleView("Fence Clear");
			} else if(lowerStatusText.contains("noflyzone enable")) {
				showMessage("NoFlyZone Activated");
				consoleView("NoFlyZone Activated");
			} else if(lowerStatusText.contains("noflyzone disable")) {
				showMessage("NoFlyZone Deactivated");
				consoleView("NoFlyZone Deactivated");
			} else if(consoleReadStatus && !uav.statusText.trim().equals("")){
				showMessage(uav.statusText);
				consoleView(uav.statusText);
			}
			
			if(uav.homeLat != 0.0) {
				jsproxy.call("setHomeLocation", uav.homeLat, uav.homeLng);
			}
			jsproxy.call("setUavLocation", uav.latitude, uav.longitude, uav.heading);
			nowLat = uav.latitude;
			nowLog = uav.longitude;
			
			
			if(uav.wayPoints.size() != 0) {
				System.out.println("가져온 미션 수:" + uav.wayPoints.size());
				for(WayPoint wp : uav.wayPoints) {
					System.out.println(wp.toString());
				}
				setMission(uav.wayPoints);
			}			
			jsproxy.call("setNextWaypointNo", uav.nextWaypointNo);
			
			
			if(Network.getUav().mode.equals("AUTO")) {
				for(int i=0; i<missionTableView.getItems().size(); i++) {
					WayPoint wp = missionTableView.getItems().get(i);
					if(wp.no == Network.getUav().nextWaypointNo) {
						missionTableView.getSelectionModel().select(wp);
						int selectedIndex = wp.no - 1;
//						자동 스크롤
//						missionTableView.scrollTo(selectedIndex);
						
						HudViewController.instance.destLat = wp.latitude;
						HudViewController.instance.destLng = wp.longitude;
						HudViewController.instance.destAlt = wp.altitude;
					}
				}
			}
			
			if(uav.fenceEnable == 0) {
				btnFenceEnable.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
			} else {
				
				btnFenceEnable.setGraphic(new Circle(5, Color.RED)); 
			}
			
			if(uav.fenceEnable == 0) {
				btnFenceEnable.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
			} else {
				btnFenceEnable.setGraphic(new Circle(5, Color.RED)); 
			}
			
			if(uav.fencePoints.size() != 0) {
				System.out.println("가져온 펜스 수:" + uav.fencePoints.size());
				setFence(uav.fencePoints);
			}
			
			
			if(uav.nextWaypointNo != currWaypointNo) {
				MissionCalculator.baseIndex++;
				//System.out.println("baseIndex: " + MissionCalculator.baseIndex);
			}
			currWaypointNo = uav.nextWaypointNo;

			if(missionStatus && uav.nextWaypointNo == missionTableView.getItems().size()) {
				if(MissionCalculator.distance3D(uav.latitude, uav.longitude, uav.altitude,
												missionTableView.getItems().get(missionTableView.getItems().size()-1)) <5) {
					missionStatus = false;
					missionPausedStatus = false;
					//Network.getUav().uploadStatus = false; // mission 종료 시, takeOff 후 RTL을 추가하는 스레드 동작 방지
					System.out.println("missionCompleted");
				}
			}
			
			if(!uav.armed) {
				MissionCalculator.baseIndex = 0;
				missionStatus = false;
				missionPausedStatus = false;
			}
		});
	}		

	//MapViewTest.java 에서 사용
	public void setUavLocation(double latitude, double longitude, double heading) {
		if(jsproxy != null) {
			Platform.runLater(() -> {
				jsproxy.call("setUavLocation", latitude, longitude, heading);
			});
		}
	}
	
	private EventHandler<Event> imgBtnMapFixHandler = (e) -> {
		if(mapFixStatus) {
			mapFixStatus = false;
			jsproxy.call("setMapFix", mapFixStatus);
			imgBtnMapFix.setImage(new Image(getClass().getResource("../images/pin_unselected.png").toExternalForm()));
		} else {
			mapFixStatus = true;
			jsproxy.call("setMapFix", mapFixStatus);
			imgBtnMapFix.setImage(new Image(getClass().getResource("../images/pin_selected.png").toExternalForm()));
		}
		
	};
	
	private EventHandler<Event> imgBtnSatelliteHandler = (e) -> {
		isSatellite = true;
		jsproxy.call("setMapType", isSatellite);
		imgBtnSatellite.setImage(new Image(getClass().getResource("../images/satellite_selected.png").toExternalForm()));
		imgBtnMap.setImage(new Image(getClass().getResource("../images/map_unselected.png").toExternalForm()));
	};

	private EventHandler<Event> imgBtnMapHandler = (e) -> {
		isSatellite = false;
		jsproxy.call("setMapType", isSatellite);
		imgBtnSatellite.setImage(new Image(getClass().getResource("../images/satellite_unselected.png").toExternalForm()));
		imgBtnMap.setImage(new Image(getClass().getResource("../images/map_selected.png").toExternalForm()));
	};
	
	private void handleImgMissionView(MouseEvent e) {
		imgSelectFlag = true;
		imgMissionView.setImage(new Image(getClass().getResource("../images/mission_selected.png").toExternalForm()));
		imgFenceView.setImage(new Image(getClass().getResource("../images/fence_unselected.png").toExternalForm()));
		imgNoFlyZoneView.setImage(new Image(getClass().getResource("../images/noflyzone_unselected.png").toExternalForm()));
		imgConsoleView.setImage(new Image(getClass().getResource("../images/console_unselected.png").toExternalForm()));
		missionVBox.setVisible(true);
		fenceVBox.setVisible(false);
		noFlyZoneVBox.setVisible(false);
		consoleVBox.setVisible(false);
		toolTipVBox.setVisible(false);
		missionMake();
		insertAlt.textProperty().addListener((observable, oldValue, newValue) -> {
			jsproxy.call("missionMake", Integer.parseInt(newValue));
		});
		// noflyzoneTable을 빠져나왔음에도 남아있는 마커를 지워주기 위한 함수 호출 및 필드 이용
		isClicked_imgNoFlyZoneView = false;
		Platform.runLater(() -> {
			jsproxy.call("removeNoFlyZone", 0, isClicked_imgNoFlyZoneView);
		});
	}
	private void handleImgFenceView(MouseEvent e) {
		imgSelectFlag = true;
		isClicked_imgNoFlyZoneView = false;
		imgMissionView.setImage(new Image(getClass().getResource("../images/mission_unselected.png").toExternalForm()));
		imgFenceView.setImage(new Image(getClass().getResource("../images/fence_selected.png").toExternalForm()));
		imgNoFlyZoneView.setImage(new Image(getClass().getResource("../images/noflyzone_unselected.png").toExternalForm()));
		imgConsoleView.setImage(new Image(getClass().getResource("../images/console_unselected.png").toExternalForm()));
		missionVBox.setVisible(false);
		fenceVBox.setVisible(true);
		noFlyZoneVBox.setVisible(false);
		consoleVBox.setVisible(false);
		toolTipVBox.setVisible(false);
		fenceMake();
		// noflyzoneTable을 빠져나왔음에도 남아있는 마커를 지워주기 위한 함수 호출 및 필드 이용
		isClicked_imgNoFlyZoneView = false;
		Platform.runLater(() -> {
			jsproxy.call("removeNoFlyZone", 0, isClicked_imgNoFlyZoneView);
		});
	}
	private void handleImgNoFlyZoneView(MouseEvent e) {
		isClicked_imgNoFlyZoneView = true;
		imgSelectFlag = true;
		imgMissionView.setImage(new Image(getClass().getResource("../images/mission_unselected.png").toExternalForm()));
		imgFenceView.setImage(new Image(getClass().getResource("../images/fence_unselected.png").toExternalForm()));
		imgNoFlyZoneView.setImage(new Image(getClass().getResource("../images/noflyzone_selected.png").toExternalForm()));
		imgConsoleView.setImage(new Image(getClass().getResource("../images/console_unselected.png").toExternalForm()));
		missionVBox.setVisible(false);
		fenceVBox.setVisible(false);
		noFlyZoneVBox.setVisible(true);
		consoleVBox.setVisible(false);
		toolTipVBox.setVisible(false);
		noFlyZoneMake();
	}
	private void handleImgConsoleView(MouseEvent e) {
		imgSelectFlag = true;
		isClicked_imgNoFlyZoneView = false;
		imgMissionView.setImage(new Image(getClass().getResource("../images/mission_unselected.png").toExternalForm()));
		imgFenceView.setImage(new Image(getClass().getResource("../images/fence_unselected.png").toExternalForm()));
		imgNoFlyZoneView.setImage(new Image(getClass().getResource("../images/noflyzone_unselected.png").toExternalForm()));
		imgConsoleView.setImage(new Image(getClass().getResource("../images/console_selected.png").toExternalForm()));
		missionVBox.setVisible(false);
		fenceVBox.setVisible(false);
		noFlyZoneVBox.setVisible(false);
		consoleVBox.setVisible(true);
		toolTipVBox.setVisible(false);
		// noflyzoneTable을 빠져나왔음에도 남아있는 마커를 지워주기 위한 함수 호출 및 필드 이용
		isClicked_imgNoFlyZoneView = false;
		Platform.runLater(() -> {
			jsproxy.call("removeNoFlyZone", 0, isClicked_imgNoFlyZoneView);
		});
	}
	
	// missionTable handlers ---------------------------------
	
	public void handleBtnGetMissionFromFile(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Mission Plan", "*.pln"));
		File selectedFile = fileChooser.showOpenDialog(AppMain.instance.primaryStage);
		if (selectedFile == null) return;
		try {
			FileInputStream fis = new FileInputStream(selectedFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			List<WayPoint> list = (List<WayPoint>) ois.readObject();
			ois.close();
			fis.close();
			setMission(list);
			showMessage("Mission Read Accomplished");
			consoleView("Mission Read Accomplished");
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void handleBtnSaveMissionToFile(ActionEvent e) {
		ObservableList<WayPoint> observableList = missionTableView.getItems();
		if(observableList.size() == 0) {
			showMessage("Cannot save mission: len(mission) is 0.");
		}
		List<WayPoint> list = observableList.stream().collect(Collectors.toList());
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Mission Plan", "*.pln"));
		File selectedFile = fileChooser.showSaveDialog(AppMain.instance.primaryStage);
		if(selectedFile == null) return;
		try {
			FileOutputStream fos = new FileOutputStream(selectedFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
			fos.close();
			showMessage("Mission Save Accomplished");
			consoleView("Mission Save Accomplished");
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void handleBtnMissionUpload(ActionEvent e) {
		List<WayPoint> list = missionTableView.getItems();
		System.out.println("올린 미션 수" + list.size());
		for(WayPoint wp : list) {
			System.out.println(wp.toString());
		}
		Network.getUav().missionUpload(list);
		MissionCalculator.missionDistance2(missionTableView);
	}
	
	public void handleBtnMissionDownload(ActionEvent e) {
		Network.getUav().missionDownload();
		System.out.println("미션 다운");
	}
	
	public void handleBtnMissionStart(ActionEvent e) {
		Network.getUav().missionStart();
		missionStart();
		if(!missionStatus && !missionPausedStatus) {
			missionStatus = true;
			MissionCalculator.baseIndex = Network.getUav().nextWaypointNo -1;
			//System.out.println(MissionCalculator.baseIndex);
			currWaypointNo = Network.getUav().nextWaypointNo-1;
		} else if(missionPausedStatus) {
			missionPausedStatus = false;
			missionStatus = true;
			//System.out.println(MissionCalculator.baseIndex);
		}
		if(CtrlstatController.instance.btnArm.getText().equals("Disarm") && AppMainController.instance.isConnectStatus()) {
			if(!CtrlstatController.instance.timerStatus) {
				CtrlstatController.instance.secToHHMMSS();
			}
		}
	}
		
	public void handleBtnMissionStop(ActionEvent e) {
		Network.getUav().missionStop();
		missionStatus = false;
		missionPausedStatus = true;
		missionStop();
	}
	
	public void handleBtnAddTakeoff(ActionEvent e) {
		addTakeoff();
	}
	
	public void handleBtnAddROI(ActionEvent e) {
		roiMake();
	}
	public void handleBtnAddRTL(ActionEvent e) {
		addRTL();
	}
	
	public void handleBtnAddJump(ActionEvent e) {
		addJump();
	}
	
	public void handleBtnAddLand(ActionEvent e) {
		addLand();
	}
	
	public void handleBtnMissionClear(ActionEvent e) {
		missionClear();
	}
	
	public void handleBtnRemoveWaypoint(ActionEvent e) {
		removeWaypoint();
	}
	
	// fenceTable handlers ---------------------------------
	public void handleBtnFenceClear(ActionEvent e) {
		Network.getUav().fenceClear();
		jsproxy.call("fenceClear");
	}
	public void handleBtnFenceUpload(ActionEvent e) {
		jsproxy.call("fenceUpload");
	}
	public void handleBtnFenceDownload(ActionEvent e) {
		Network.getUav().fenceDownload();
	}
	public void handleBtnFenceEnable(ActionEvent e) {
		Network.getUav().fenceEnable();
	}
	
	public void handleBtnFenceDisable(ActionEvent e) {
		Network.getUav().fenceDisable();
	}
	
	// NoFlyZone handlers ---------------------------------
	public void handleBtnNoFlyZoneAdd(ActionEvent e) {
		addNoFlyZone();
	}
	
	public void handleBtnNoFlyZoneEnable(ActionEvent e) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				System.out.println("handleBtnNoFlyZoneEnable() - begin --------------------------------------");
				List<WayPoint> cloneMissionList = new ArrayList<WayPoint> ();
				List<Object> angleList = new ArrayList<Object> ();	// 0: rotation, 1: start, 2: end
				
				NoFlyZonePoint noFlyZonePoint = new NoFlyZonePoint();
				WayPoint wayPoint = new WayPoint();
				
				listSize = 0;
				missionList.clear();
				originalMissionList.clear();
				cloneMissionList.clear();
				
				for(int i=0; i<missionTableView.getItems().size(); i++) {
					originalMissionList.add(i, missionTableView.getItems().get(i));
					if(missionTableView.getItems().get(i).kind.equals("waypoint")) {
						if(missionTableView.getItems().get(i+1).kind.equals("land")) {
							WayPoint oldWp = missionTableView.getItems().get(i);
							WayPoint newWp1 = new WayPoint(oldWp.no, oldWp.kind, oldWp.latitude, oldWp.longitude, oldWp.altitude, oldWp.jump, oldWp.repeat);
							WayPoint newWp2 = new WayPoint(oldWp.no, oldWp.kind, oldWp.latitude, oldWp.longitude, oldWp.altitude*2/3, oldWp.jump, oldWp.repeat);
							WayPoint newWp3 = new WayPoint(oldWp.no, oldWp.kind, oldWp.latitude, oldWp.longitude, oldWp.altitude*1/3, oldWp.jump, oldWp.repeat);
							missionList.add(newWp1);
							missionList.add(newWp2);
							missionList.add(newWp3);
							cloneMissionList.add(newWp1);
							cloneMissionList.add(newWp2);
							cloneMissionList.add(newWp3);
						} else {
							missionList.add(missionTableView.getItems().get(i));
							missionList.add(missionTableView.getItems().get(i));
							missionList.add(missionTableView.getItems().get(i));
							cloneMissionList.add(missionTableView.getItems().get(i));
							cloneMissionList.add(missionTableView.getItems().get(i));
							cloneMissionList.add(missionTableView.getItems().get(i));
						}
					}else {
						missionList.add(missionTableView.getItems().get(i));
						cloneMissionList.add(missionTableView.getItems().get(i));
					}
				}
				
				
				
				// HomeLocation의 lng, lat를 cloneMissionList 첫 인덱스에 추가
				wayPoint.kind = "home";
				wayPoint.longitude = nowLog;
				wayPoint.latitude = nowLat;
				cloneMissionList.add(0, wayPoint);
				
				for(int i=0; i<cloneMissionList.size()-1; i++) {					
					double Wx1 = cloneMissionList.get(i).longitude;
					double Wy1 = cloneMissionList.get(i).latitude;
					double Wx2 = cloneMissionList.get(i+1).longitude;
					double Wy2 = cloneMissionList.get(i+1).latitude;
					double seq_alt;
					
					if(cloneMissionList.get(i).kind.equals("land")) {
						seq_alt = cloneMissionList.get(i-1).altitude;
					} else {
						seq_alt = cloneMissionList.get(i).altitude;
					}					
					
					for(int j=0; j<circleList.size(); j++) {
						double Ox = circleList.get(j).getLng();
						double Oy = circleList.get(j).getLat();
						double r = circleList.get(j).getRad();

						if(NoFlyZonePoint.instance.ifNoflyzone(Ox, Oy, Wx1, Wy1, Wx2, Wy2) <= r +5) {
							// 4.1 noflyzone을 지나지 않으면 아무것도 추가하지 않음 
							System.out.println("NoFlyZone을 지난다.");
							angleList = rotationCase(Ox, Oy, r, Wx1, Wy1, Wx2, Wy2);
							
							if(!(boolean)angleList.get(0)) {
								circleWP1(Ox, Oy, r, Wx1, Wy1, Wx2, Wy2, i, (int)angleList.get(1), (int)angleList.get(2), seq_alt);
							}else{
								circleWP2(Ox, Oy, r, Wx1, Wy1, Wx2, Wy2, i, (int)angleList.get(1), (int)angleList.get(2), seq_alt);
							}
						}
						else {
							// 4.2 W1과 W2를 잇는 직선이 노플라이존과 겹치지 않음
							System.out.println("NoFlyZone을 지나지 않는다.");
						}
					}
				}
				for(WayPoint t: missionList) {
					System.out.println(t.getKind()+ "   "+t.getNo());
				}
				Platform.runLater(()->btnNoFlyZoneEnable.setGraphic(new Circle(5, Color.RED)));
				isNoFlyZone = true;
				System.out.println("handleBtnNoFlyZoneEnable() - end --------------------------------------");
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public void handleBtnNoFlyZoneDisable(ActionEvent e) {
		setMission(originalMissionList);
		Platform.runLater(()->btnNoFlyZoneEnable.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35))));
		isNoFlyZone = false;
	}

	public void handleBtnAutoTakeoff(ActionEvent e) {
		if(autoStatus == true) {
			btnAutoTakeoff.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
			autoStatus = false;
		} else {
			btnAutoTakeoff.setGraphic(new Circle(5, Color.RED));
			autoStatus = true;
		}
	}
	
	// 각 table delete key 이벤트 핸들러
	public void handleDeleteKeyEvent(String kind, KeyEvent e) {
		if(kind == "mission") {
			int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex == -1) {
				return;
			}
			
		    if(e.getCode() == KeyCode.DELETE) {
		    	missionTableView.getItems().remove(selectedIndex);
				for(int i=0; i<missionTableView.getItems().size(); i++) {
					WayPoint wp = missionTableView.getItems().get(i);
					wp.no = i+1;
				}
				
				Platform.runLater(() -> {
					jsproxy.call("removeMissionMarker", selectedIndex);
				});
		    }
		}
		else if(kind == "fence") {
			int selectedIndex = fenceTableView.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex == -1) {
				return;
			}
			
		    if(e.getCode() == KeyCode.DELETE) {
		    	fenceTableView.getItems().remove(selectedIndex);
				for(int i=0; i<fenceTableView.getItems().size(); i++) {
					FencePoint fp = fenceTableView.getItems().get(i);
					fp.no = i+1;
				}
				
				Platform.runLater(() -> {
					jsproxy.call("removeFenceMarker", selectedIndex);
				});
		    }
		}
		else if(kind == "noflyzone") {
			int selectedIndex = noFlyZoneTableView.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex == -1) {
				return;
			}
			
		    if(e.getCode() == KeyCode.DELETE) {
		    	noFlyZoneTableView.getItems().remove(selectedIndex);
		    	circleList.remove(selectedIndex);
				for(int i=0; i<noFlyZoneTableView.getItems().size(); i++) {
					NoFlyZonePoint noflypoint = noFlyZoneTableView.getItems().get(i);
					noflypoint.no = i+1;
				}
				
				Platform.runLater(() -> {
					jsproxy.call("removeNoFlyZone", selectedIndex, isClicked_imgNoFlyZoneView);
				});
		    }
		}
	}
	//chkConsoleListenOption Handler
	private EventHandler<ActionEvent> checkBoxConsoleListenOption = (event) -> {
		if(chkConsoleListenOption.isSelected()) {
			consoleReadStatus = true;
		} else {
			consoleReadStatus = false;
		}
	};
	
	//console clear ------------------------------------------
	public void handleBtnConsoleClear(ActionEvent e) {
		consoleListView.getItems().clear();
		consoleList.clear();
	}
	// mission 기능 ------------------------------------------	
	private void addTakeoff() {
		if(missionTableView.getItems().size() == 0) {
			return;
		}
		if(missionTableView.getItems().get(0).kind.equals("takeoff")) {
			return;
		}
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "takeoff";
		waypoint.latitude = nowLat;
		waypoint.longitude = nowLog;
		waypoint.altitude = Integer.parseInt(insertAlt.getText());
		missionTableView.getItems().add(0, waypoint);
		for(int i=0; i<missionTableView.getItems().size(); i++) {
			WayPoint wp = missionTableView.getItems().get(i);
			wp.no = i+1;
		}
		Platform.runLater(() -> {
			jsproxy.call("addTakeoff", waypoint.latitude, waypoint.longitude, 0, waypoint.altitude);
		});
	}
	
	private void addJump() {
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "jump";
		waypoint.repeat = 1;
		
		// 선택한 index 번호를 selectedIndex에 저장
		int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
		waypoint.jump = selectedIndex+1;
		
		if(selectedIndex == -1) {
			return;
		}
		
		WayPoint wp = missionTableView.getItems().get(selectedIndex);
		
		if(wp.kind.equals("jump")) {
			return;
		}
		if(selectedIndex < missionTableView.getItems().size()-1) {
			wp = missionTableView.getItems().get(selectedIndex+1);
			if(wp.kind.equals("jump")) {
				return;
			}
		}
		
		if(selectedIndex != missionTableView.getItems().size()-1) {
			missionTableView.getItems().add(selectedIndex+1, waypoint);
		} else {
			missionTableView.getItems().add(waypoint);
		}
		
		for(int i=0; i<missionTableView.getItems().size(); i++) {
			wp = missionTableView.getItems().get(i);
			wp.no = i+1;
			System.out.println(wp.toString());
		}
		
		wp = missionTableView.getItems().get(waypoint.jump-1);		
		
		System.out.println(wp.toString());
		double jumpWPLat = wp.latitude;
		double jumpWPLng = wp.longitude;
		
		Platform.runLater(() -> {
			jsproxy.call("addJump", jumpWPLat, jumpWPLng, selectedIndex+1, 0, selectedIndex+1, 1);
		});
	}
	
	public void addROI(String data) {
		Platform.runLater(() -> {
			WayPoint waypoint = new WayPoint();
			waypoint.kind = "roi";
			JSONObject jsonObject = new JSONObject(data);
			waypoint.latitude = jsonObject.getDouble("lat");
			waypoint.longitude = jsonObject.getDouble("lng");
			int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
			if(selectedIndex != missionTableView.getItems().size()-1) {
				missionTableView.getItems().add(selectedIndex+1, waypoint);
			} else {
				missionTableView.getItems().add(waypoint);
			}
			for(int i=0; i<missionTableView.getItems().size(); i++) {
				WayPoint wp = missionTableView.getItems().get(i);
				wp.no = i+1;
			}
		});
	}
	
	private void addRTL() {
		if(missionTableView.getItems().size() == 0) {
			if(nowLat != Network.getUav().homeLat || nowLog != Network.getUav().homeLng) {
				WayPoint waypoint = new WayPoint();
				waypoint.kind = "rtl";
				waypoint.latitude = Network.getUav().homeLat;
				waypoint.longitude = Network.getUav().homeLng;
				missionTableView.getItems().add(waypoint);
				waypoint.no = missionTableView.getItems().size();
				Platform.runLater(() -> {
					jsproxy.call("addRTL");
				});
			} else {
				return;
			}
		} else {
			if(missionTableView.getItems().get(missionTableView.getItems().size()-1).kind.equals("rtl")) {
				return;
			} else {
				WayPoint waypoint = new WayPoint();
				waypoint.kind = "rtl";
				waypoint.latitude = Network.getUav().homeLat;
				waypoint.longitude = Network.getUav().homeLng;
				missionTableView.getItems().add(waypoint);
				waypoint.no = missionTableView.getItems().size();
				Platform.runLater(() -> {
					jsproxy.call("addRTL");
				});
			}
		}
	}
	
	private void addLand() {
		if(missionTableView.getItems().size() == 0) {
			return;
		}
		
		int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
		
		if(selectedIndex == -1) {
			return;
		}
		
		WayPoint wp = missionTableView.getItems().get(selectedIndex);
		
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "land";
		waypoint.latitude = wp.getLatitude();
		waypoint.longitude = wp.getLongitude();
		
		if(selectedIndex != missionTableView.getItems().size()-1) {
			missionTableView.getItems().add(selectedIndex+1, waypoint);
		} else {
			missionTableView.getItems().add(waypoint);
		}
		
		for(int i=0; i<missionTableView.getItems().size(); i++) {
			wp = missionTableView.getItems().get(i);
			wp.no = i+1;
		}
		
		Platform.runLater(() -> {
			jsproxy.call("addLand", waypoint.latitude, waypoint.longitude, selectedIndex+1);
		});
	}
	
	public void getMission() {
		Platform.runLater(() -> {
			jsproxy.call("getMission");
		});
	}
	
	public void getMissionResponse(String data) {
		Platform.runLater(() -> {
			List<WayPoint> list = new ArrayList<WayPoint>();			
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				WayPoint wayPoint = new WayPoint();
				wayPoint.no = jsonObject.getInt("no");
				wayPoint.kind = jsonObject.getString("kind");
				wayPoint.latitude = jsonObject.getDouble("lat");
				wayPoint.longitude = jsonObject.getDouble("lng");
				wayPoint.jump = jsonObject.getInt("jump");
				wayPoint.repeat = jsonObject.getInt("repeat");
				wayPoint.altitude = jsonObject.getDouble("alt");
				list.add(wayPoint);
			}
			setMissionTableViewItems(list);
			MissionCalculator.missionDistance2(missionTableView);
		});
	}
	
	public void missionMake() {
		Platform.runLater(() -> {
			jsproxy.call("missionMake", Integer.parseInt(insertAlt.getText()));
		});
	}
	
	private void missionClear() {
		missionTableView.getItems().removeAll();
		for(int i=0; i<missionTableView.getItems().size(); i++) {
			WayPoint wp = missionTableView.getItems().get(i);
		}
		Platform.runLater(() -> {
			jsproxy.call("missionClear");
		});
		getMission();
		Network.getUav().missionClear();
		MissionCalculator.missionDistance2(missionTableView);
	}
	
	public void missionStart() {
		Platform.runLater(() -> {
			jsproxy.call("missionStart");
		});
	}
	
	public void missionStop() {
		Platform.runLater(() -> {
			jsproxy.call("missionStop");
		});
	}

	public void setMission(List<WayPoint> wayPoints) {
		setMissionTableViewItems(wayPoints);
		JSONArray jsonArray = new JSONArray();
		WayPoint formerWayPoint = null;
		for(WayPoint wayPoint : wayPoints) {
			JSONObject jsonObject = new JSONObject();
			if(wayPoint.kind.equals("takeoff")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", Network.getUav().latitude);
				jsonObject.put("lng", Network.getUav().longitude);
				jsonObject.put("alt", wayPoint.altitude);
			} else if(wayPoint.kind.equals("waypoint")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
				jsonObject.put("alt", wayPoint.altitude);
			} else if(wayPoint.kind.equals("jump")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
				jsonObject.put("jump", wayPoint.jump);
				jsonObject.put("repeat", wayPoint.repeat);
			} else if(wayPoint.kind.equals("rtl")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", Network.getUav().homeLat);
				jsonObject.put("lng", Network.getUav().homeLng);
			} else if(wayPoint.kind.equals("roi")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
			} else if(wayPoint.kind.equals("home")) {
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
			} else if(wayPoint.kind.equals("land")) {
				landNo = wayPoint.no;
				System.out.println(landNo);
				jsonObject.put("kind", wayPoint.kind);
				jsonObject.put("lat", formerWayPoint.latitude);
				jsonObject.put("lng", formerWayPoint.longitude);
			}
			jsonArray.put(jsonObject);
			formerWayPoint = wayPoint;
		}
		String strMissionArr = jsonArray.toString();
		Platform.runLater(() -> {
			jsproxy.call("setMission", strMissionArr);
		});
	}
	
	public void gotoMake() {
		Platform.runLater(() -> {
			jsproxy.call("gotoMake");
		});
	}
	
	public void gotoStart(String data) {
		Platform.runLater(() -> {
			JSONObject jsonObject = new JSONObject(data);
			double latitude = jsonObject.getDouble("lat");
			double longitude = jsonObject.getDouble("lng");
			double altitude = Integer.parseInt(CtrlstatController.instance.txtAltitude.getText());
			CtrlstatController.instance.changeAltFlag = false;
			CtrlstatController.instance.takeOffFlag = false;
			CtrlstatController.instance.landFlag = false;
			CtrlstatController.instance.rtlFlag = false;
			CtrlstatController.instance.gotoFlag = true;
			HudViewController.destLat = latitude;
			HudViewController.destLng = longitude;
			HudViewController.destAlt = altitude;
			Network.getUav().gotoStart(latitude, longitude, altitude);
		});
	}
	
	private void roiMake() {
		int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			return;
		}
		
		if(selectedIndex == missionTableView.getItems().size()-1) {
			return;
		}
		
		WayPoint wp = missionTableView.getItems().get(selectedIndex);
		if(wp.kind.equals("roi")) {
			return;
		}
		
		wp = missionTableView.getItems().get(selectedIndex+1);
		if(wp.kind.equals("roi")) {
			return;
		}
		
		Platform.runLater(() -> {
			jsproxy.call("roiMake", selectedIndex);
		});
	}
	
	public void rtl() {
		Platform.runLater(() -> {
			jsproxy.call("rtlStart");
		});
	}
	
	//Fence----------------------------------------------------------
	public void fenceMake() {
		Platform.runLater(() -> {
			jsproxy.call("fenceMake");
		});
	}

	public void fenceUpload(String jsonFencePoints) {
		Network.getUav().fenceUpload(jsonFencePoints);
	}
	
	public void getFence() {
		Platform.runLater(() -> {
			jsproxy.call("getFence");
		});
	}
	
	public void getFenceResponse(String data) {
		Platform.runLater(() -> {
			List<FencePoint> list = new ArrayList<FencePoint>();			
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				FencePoint fencePoint = new FencePoint();
				fencePoint.no = jsonObject.getInt("no");
				fencePoint.lat = jsonObject.getDouble("lat");
				fencePoint.lng = jsonObject.getDouble("lng");
				list.add(fencePoint);
			}
			setFenceTableViewItems(list);
		});
	}
	
	public void setFence(List<FencePoint> fencePoints) {
		JSONArray jsonArray = new JSONArray();
		for(FencePoint fencePoint : fencePoints) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("no", fencePoint.no);
			jsonObject.put("count", fencePoint.count);
			jsonObject.put("lat", fencePoint.lat);
			jsonObject.put("lng", fencePoint.lng);
			jsonArray.put(jsonObject);
		}
		String strFenceArr = jsonArray.toString();
		Platform.runLater(() -> {
			jsproxy.call("setFence", strFenceArr);
		});	
	}
	
	//noFlyZone--------------------------------------------------------
	public void noFlyZoneMake() {
		Platform.runLater(() -> {
			jsproxy.call("noFlyZoneMake");
		});
	}
	
	public void addNoFlyZone() {
		NoFlyZonePoint noflyzonepoint = new NoFlyZonePoint();
		noflyzonepoint.lat = Double.parseDouble(insertLat.getText());
		noflyzonepoint.lng = Double.parseDouble(insertLng.getText());
		noflyzonepoint.rad = Double.parseDouble(insertRad.getText());
		noFlyZoneTableView.getItems().add(0, noflyzonepoint);
		
		for(int i=0; i<noFlyZoneTableView.getItems().size(); i++) {
			NoFlyZonePoint noflyp = noFlyZoneTableView.getItems().get(i);
			noflyp.no = i+1;
		}
		
		// 정리 후, noflyzone circle list에 addnoFlyzone을 할 때마다 추가
		circleList.clear();
		for(int i=0; i<noFlyZoneTableView.getItems().size(); i++) {
			NoFlyZonePoint noflyp = noFlyZoneTableView.getItems().get(i);
			circleList.add(noflyp);
		}
		double latitude = noflyzonepoint.lat;
		double longtitude = noflyzonepoint.lng;
		double radius = noflyzonepoint.rad;
		
		Platform.runLater(() -> {
			jsproxy.call("addNoFlyZone", latitude, longtitude, radius);
		});
	}
	
	public void getNoFlyZone() {
		Platform.runLater(() -> {
			jsproxy.call("getNoFlyZone");
		});
	}
	
	public void getNoFlyZoneResponse(String data) {
		Platform.runLater(() -> {
			List<NoFlyZonePoint> list = new ArrayList<NoFlyZonePoint>();			
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				NoFlyZonePoint noflyzonepoint = new NoFlyZonePoint();
				
				noflyzonepoint.no = jsonObject.getInt("no");
				noflyzonepoint.lat = jsonObject.getDouble("lat");
				noflyzonepoint.lng = jsonObject.getDouble("lng");

				insertLat.setText(Double.toString(noflyzonepoint.lat));
				insertLng.setText(Double.toString(noflyzonepoint.lng));
				
				list.add(noflyzonepoint);
			}
		});
	}
	
	private double angle(double Wx1, double Wy1, double Wx2, double Wy2){
		Wx1 *= 88799.53629131494;
		Wy1 *= 111189.57696002942;
		Wx2 *= 88799.53629131494;
		Wy2 *= 111189.57696002942;
		double dx = Wx2 - Wx1;
		double dy = Wy2 - Wy1;
		
		double rad= Math.atan2(dy, dx);
		double degree = (rad*180)/Math.PI ;
		return degree;
	}
	
	private void circleWP1(double Ox,double Oy, double r,double Wx1,double Wy1,double Wx2,double Wy2, int j, int start, int end, double alt) {
		System.out.println(">> circleWP1() - begin --------------------------------------");
		List<WayPoint> list1 = new ArrayList<WayPoint> ();
		list1.clear();

		Platform.runLater(() -> {
			int cnt= 1;
			// 15도씩 증가시키면서 찍는다.
			
			for(int i= start; i<end; i+=15) {
				WayPoint wayPoint = new WayPoint();
				wayPoint.no = j+listSize+cnt;
				wayPoint.kind = "waypoint";
				wayPoint.setLongitude((Ox+(r+5)*Math.cos(Math.PI/180*i)/88799.53629131494));
				wayPoint.setLatitude((Oy+(r+5)*Math.sin(Math.PI/180*i)/111189.57696002942));
				wayPoint.setAltitude(alt);
				list1.add(wayPoint);
				cnt++;
			}
			
			for(int i=list1.size()-1; i>=0; i--) {
				missionList.add(j+listSize, list1.get(i));
			}
			
			for(int i=0; i<missionList.size(); i++) {
				WayPoint wp = missionList.get(i);
				wp.no = i+1;
			}
			
			listSize += list1.size();
			
			for(WayPoint t: missionList) {
				System.out.println("서클에서 ::: "+t.getKind()+"   "+t.getNo());
			}
			
			setMission(missionList);
			setMissionTableViewItems(missionList);
		});
		System.out.println(">> circleWP1() - end --------------------------------------");
	}
	
	private void circleWP2(double Ox,double Oy, double r,double Wx1,double Wy1,double Wx2,double Wy2, int j, int start, int end, double alt) {
		System.out.println(">> circleWP2() - begin --------------------------------------");
		List<WayPoint> list2 = new ArrayList<WayPoint> ();
		list2.clear();

		Platform.runLater(() -> {
			int cnt=1;
			// 15도씩 증가시키면서 찍는다.
			for(int i=start; i>end; i-=15) {
				WayPoint wayPoint = new WayPoint();
				wayPoint.no = j+listSize+cnt;
				wayPoint.kind = "waypoint";
				wayPoint.setLongitude((Ox+(r+5)*Math.cos(Math.PI/180*i)/88799.53629131494));
				wayPoint.setLatitude((Oy+(r+5)*Math.sin(Math.PI/180*i)/111189.57696002942));
				wayPoint.setAltitude(alt);
				list2.add(wayPoint);
				cnt++;
			}
			for(int i=list2.size()-1; i>=0; i--) {
				missionList.add(j+listSize, list2.get(i));
			}
			
			for(int i=0; i<missionList.size(); i++) {
				WayPoint wp = missionList.get(i);
				wp.no = i+1;
			}
			
			listSize += list2.size();
			
			for(WayPoint t: missionList) {
				System.out.println("서클에서 ::: "+t.getKind()+"   "+t.getNo());
			}
			
			setMission(missionList);
			setMissionTableViewItems(missionList);
		});
		System.out.println(">> circleWP2() - end --------------------------------------");
	}

	private List<Object> rotationCase(double Ox, double Oy, double r, double Wx1, double Wy1, double Wx2, double Wy2) {		
		System.out.println("rotationCase() - begin --------------------------------");
		List<Object> angleList = new ArrayList<Object> ();

		double angle1=0.0, angle2=0.0;
		boolean rotation = false;	// false: 반시계, true: 시계 방향
		int start = 0, end = 0;
		
		Ox *= 88799.53629131494;
		Oy *= 111189.57696002942;
		Wx1 *= 88799.53629131494;
		Wy1 *= 111189.57696002942;
		Wx2 *= 88799.53629131494;
		Wy2 *= 111189.57696002942;
		
		double l1 = Math.sqrt(Math.pow((Ox-Wx1), 2) + Math.pow((Oy-Wy1), 2));
		double l2 = Math.sqrt(Math.pow((Ox-Wx2), 2) + Math.pow((Oy-Wy2), 2));
		double d= r + 5;
		
		double rad1 = Math.acos(d/l1);
		double rad2 = Math.acos(d/l2);
		
		double x1 = (rad1*180)/Math.PI;
		double x2 = (rad2*180)/Math.PI;
		
		if(angle(Ox,Oy,Wx1,Wy1)<0) {
			angle1 =360 +angle(Ox,Oy,Wx1,Wy1);
		}else {
			angle1= angle(Ox,Oy,Wx1,Wy1);
		}
		
		if(angle(Ox,Oy,Wx2,Wy2)<0) {
			angle2=360+angle(Ox,Oy,Wx2,Wy2);
		}else {
			angle2=angle(Ox,Oy,Wx2,Wy2);
		}

		if(0 <=angle1 && angle1 <90) {
			if(angle1 <angle2 && angle2 <180+angle1) {
				rotation=false;
				start= (int)angle1 + (int)x1;
				end= (int)angle2 - (int)x2;
			}else {
				if(angle2 >angle1) {
					angle2 -=360;
				}
				rotation =true;
				start =(int)angle1 - (int)x1;
				end =(int)angle2 + (int)x2;
			}
			angleList.add(0, rotation);
			angleList.add(1, start);
			angleList.add(2, end);
		} else if(90 <=angle1 && angle1 <180) {
			if(angle1 <angle2 && angle2 <180+angle1) {
				rotation =false;
				start =(int)angle1 + (int)x1;
				end =(int)angle2 - (int)x2;
			}else {
				rotation =true;
				if(angle2 >angle1) {
					angle2 -=360;
				}
				start=(int)angle1 - (int)x1;
				end=(int)angle2 + (int)x2;
			}
			angleList.add(0, rotation);
			angleList.add(1, start);
			angleList.add(2, end);
		} else if(180 <=angle1 && angle1 <270) {
			if(angle2<angle1&&angle2>angle1-180) {
				rotation=true;
				start=(int)angle1 - (int)x1;
				end=(int)angle2 + (int)x2;
			}else {
				rotation=false;
				if(angle2<angle1) {
					angle2+=360;
				}
				start=(int)angle1 + (int)x1;
				end=(int)angle2 - (int)x2;
			}
			angleList.add(0, rotation);
			angleList.add(1, start);
			angleList.add(2, end);
		} else if(270 <=angle1 && angle1 <360) {
			if(angle2<angle1&&angle2>angle1-180) {
				rotation=true;
				start=(int)angle1 - (int)x1;
				end=(int)angle2 + (int)x2;
			}else {
				rotation=false;
				if(angle2<angle1) {
					angle2+=360;
				}
				start=(int)angle1 + (int)x1;
				end=(int)angle2 - (int)x2;
			}
			angleList.add(0, rotation);
			angleList.add(1, start);
			angleList.add(2, end);
		}
		System.out.println("rotationCase() - end --------------------------------");
		return angleList;
	}
	
	public void log(String message) {
		System.out.println(message);
	}

	public boolean isImgSelectFlag() {
		return imgSelectFlag;
	}

	public void setImgSelectFlag(boolean imgSelectFlag) {
		this.imgSelectFlag = imgSelectFlag;
	}
	
	public void showMessage(String message) {
		if(messageThread != null) {
			messageThread.interrupt();
		}
		messageThread = new Thread() {
			@Override
			public void run() {
				try {
					Platform.runLater(()->{lblMessage.setText(message);});
					Thread.sleep(3000);}
				catch(Exception e) {
					Platform.runLater(()->{lblMessage.setText("");});
				} finally {
					Platform.runLater(()->{lblMessage.setText("");});
				}
			}
		};
		messageThread.setDaemon(true);
		messageThread.start();
	}
	
	// table 수정하는 기능 구현부
	private void modifyMissionMarker(double latitude, double longitude, int selectedIndex, String kind, double altitude, int jump, int repeat) {
		// 1. 기존의 마커를 지우고
		Platform.runLater(() -> {
			jsproxy.call("removeMissionMarker", selectedIndex);
		});
		// 2. 새로운 마커를 찍는다
		Platform.runLater(() -> {
			jsproxy.call("modifyWayPoint", latitude, longitude, selectedIndex, altitude, kind, jump, repeat);
		});
		
	}
	
	private void modifyFenceMarker(double lat, double lng, int selectedIndex) {
		Platform.runLater(() -> {
			jsproxy.call("removeFenceMarker", selectedIndex);
		});
		
		Platform.runLater(() -> {
			jsproxy.call("modifyFencePoint", lat, lng, selectedIndex);
		});
	}
	
	private void deletePoint(String kind, int index) {
		if(index == -1) {
			return;
		}
		if(kind == "WayPoint") {
			missionTableView.getItems().remove(index);
			for(int i=0; i<missionTableView.getItems().size(); i++) {
				WayPoint wp = missionTableView.getItems().get(i);
				wp.no = i+1;
			}
			Platform.runLater(() -> {
				jsproxy.call("removeMissionMarker", index);
			});
		}
		else if (kind == "FencePoint") {
			fenceTableView.getItems().remove(index);
			for(int i=0; i<fenceTableView.getItems().size(); i++) {
				FencePoint fp = fenceTableView.getItems().get(i);
				fp.no = i+1;
			}
			Platform.runLater(() -> {
				jsproxy.call("removeFenceMarker", index);
			});
		}
		else if (kind == "NoFlyZonePoint") {
			noFlyZoneTableView.getItems().remove(index);
			for(int i=0; i<noFlyZoneTableView.getItems().size(); i++) {
				NoFlyZonePoint nfz = noFlyZoneTableView.getItems().get(i);
				nfz.no = i+1;
			}
			Platform.runLater(() -> {
				jsproxy.call("removeNoFlyZone", index, isClicked_imgNoFlyZoneView);
			});
		}
	}
	
	private void removeWaypoint() {
		int selectedIndex = missionTableView.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			return;
		}
		missionTableView.getItems().remove(selectedIndex);
		for(int i=0; i<missionTableView.getItems().size(); i++) {
			WayPoint wp = missionTableView.getItems().get(i);
			wp.no = i+1;
		}
		Platform.runLater(() -> {
			jsproxy.call("removeMissionMarker", selectedIndex);
		});
	}
	
	private void moveWayPoint(String direction, int selectedIndex) {
		WayPoint waypoint = new WayPoint();
		switch (direction) {
		// 1.1 direction이 "up"으로 넘어올 경우
		case "up":
			// 첫번째 아이템은 up을 할 수없음.
			if(selectedIndex == 0) {
				return;
			}
			waypoint = missionTableView.getItems().get(selectedIndex);
			missionTableView.getItems().add(selectedIndex-1, waypoint);
			
			double lat1 =  waypoint.latitude;
			double lng1 = waypoint.longitude;
			double alt1 = waypoint.altitude;
			String kind1 = waypoint.kind;
			int jump1 = waypoint.jump;
			int repeat1 = waypoint.repeat;
			Platform.runLater(() -> {
				jsproxy.call("modifyWayPoint", lat1, lng1, selectedIndex-1, alt1, kind1, jump1, repeat1);
			});
			
			missionTableView.getItems().remove(selectedIndex+1);
			for(int i=0; i<missionTableView.getItems().size(); i++) {
				WayPoint wp = missionTableView.getItems().get(i);
				wp.no = i+1;
			}
			for(int i=0; i<missionTableView.getItems().size(); i++) {
				System.out.println(missionTableView.getItems().get(i).toString());
			}
			Platform.runLater(() -> {
				jsproxy.call("removeMissionMarker", selectedIndex+1);
			});
			break;
		// 1.2 direction이 "down"으로 넘어올 경우
		case "down":
			// 테이블 전체 아이템의 갯수 -1 == 마지막 index, 마지막 item은 down 하지 않는다.
			if(selectedIndex == missionTableView.getItems().size()-1) {
				return;
			}
			waypoint = missionTableView.getItems().get(selectedIndex+1);
			missionTableView.getItems().add(selectedIndex, waypoint);
			double lat2 = waypoint.latitude;
			double lng2 = waypoint.longitude;
			double alt2 = waypoint.altitude;
			String kind2 = waypoint.kind;
			int jump2 = waypoint.jump;
			int repeat2 = waypoint.repeat;
			Platform.runLater(() -> {
				jsproxy.call("modifyWayPoint", lat2, lng2, selectedIndex, alt2, kind2, jump2, repeat2);
			});
			
			missionTableView.getItems().remove(selectedIndex+2);
			for(int i=0; i<missionTableView.getItems().size(); i++) {
				WayPoint wp = missionTableView.getItems().get(i);
				wp.no = i+1;
			}
			Platform.runLater(() -> {
				jsproxy.call("removeMissionMarker", selectedIndex+2);
			});
			break;
		}	
	}
	
	public void toolTip(boolean tooltipFlag) {
		Tooltip tooltipImgBtnMapFix= new Tooltip();
		Tooltip tooltipImgMissionView= new Tooltip();
		Tooltip tooltipImgFenceView= new Tooltip();
		Tooltip tooltipImgNoFlyZoneView= new Tooltip();
		Tooltip tooltipImgConsoleView = new Tooltip();
		
		if(tooltipFlag) {
			//Mission 지도 info
			tooltipImgBtnMapFix.setStyle(toolTipCss());
			tooltipImgBtnMapFix.setText("Google Map 자동 중앙 설정/해제"); 
			imgBtnMapFix.setPickOnBounds(true);
			Tooltip.install(imgBtnMapFix, tooltipImgBtnMapFix);
			  
			Tooltip tooltipLblFlight= new Tooltip();
			tooltipLblFlight.setStyle(toolTipCss());
			tooltipLblFlight.setText("주행시간"); 
			lblFlight.setTooltip(tooltipLblFlight);
			  
			Tooltip tooltipLblDistance= new Tooltip();
			tooltipLblDistance.setStyle(toolTipCss());
			tooltipLblDistance.setText("Home까지의 거리"); 
			lblDistance.setTooltip(tooltipLblDistance);
			  
			Tooltip tooltipLblTotal= new Tooltip();
			tooltipLblTotal.setStyle(toolTipCss());
			tooltipLblTotal.setText("총 미션 수행 거리"); 
			lblTotal.setTooltip(tooltipLblTotal);
			  
			//Mission 하단 info
			tooltipImgMissionView.setStyle(toolTipCss());
			tooltipImgMissionView.setText("미션 설정 탭\n이미지를 누른 후 미션 WayPoint 설정"); 
			imgMissionView.setPickOnBounds(true);
			Tooltip.install(imgMissionView, tooltipImgMissionView);
			  
			
			tooltipImgFenceView.setStyle(toolTipCss());
			tooltipImgFenceView.setText("펜스 설정 탭\n이미지를 누른 후 FencePoint 설정"); 
			imgFenceView.setPickOnBounds(true);
			Tooltip.install(imgFenceView, tooltipImgFenceView);
			  
			tooltipImgNoFlyZoneView.setStyle(toolTipCss());
			tooltipImgNoFlyZoneView.setText("비행금지구역 설정 탭"); 
			imgNoFlyZoneView.setPickOnBounds(true);
			Tooltip.install(imgNoFlyZoneView, tooltipImgNoFlyZoneView);
			
			tooltipImgConsoleView.setStyle(toolTipCss());
			tooltipImgConsoleView.setText("드론의 메시지 콘솔 탭"); 
			imgConsoleView.setPickOnBounds(true);
			Tooltip.install(imgConsoleView, tooltipImgConsoleView);
			  
			//Mission 미션 button info
			Tooltip tooltipBtnMissionUpload= new Tooltip();
			tooltipBtnMissionUpload.setStyle(toolTipCss());
			tooltipBtnMissionUpload.setText("설정한 미션을 드론에게 MAVLINK 보내기"); 
			btnMissionUpload.setTooltip(tooltipBtnMissionUpload);
			  
			Tooltip tooltipBtnMissionDownload= new Tooltip();
			tooltipBtnMissionDownload.setStyle(toolTipCss());
			tooltipBtnMissionDownload.setText("업로드 되어있는 미션 불러오기"); 
			btnMissionDownload.setTooltip(tooltipBtnMissionDownload);
			  
			Tooltip tooltipBtnAutoTakeoff = new Tooltip();
			tooltipBtnAutoTakeoff.setStyle(toolTipCss());
			tooltipBtnAutoTakeoff.setText("Auto Takeoff 활성화"); 
			btnAutoTakeoff.setTooltip(tooltipBtnAutoTakeoff);
			
			Tooltip tooltipBtnMissionStart= new Tooltip();
			tooltipBtnMissionStart.setStyle(toolTipCss());
			tooltipBtnMissionStart.setText("설정한 미션 수행"); 
			btnMissionStart.setTooltip(tooltipBtnMissionStart);
			  
			Tooltip tooltipBtnMissionStop= new Tooltip();
			tooltipBtnMissionStop.setStyle(toolTipCss());
			tooltipBtnMissionStop.setText("미션 중지"); 
			btnMissionStop.setTooltip(tooltipBtnMissionStop);
			
			Tooltip tooltipInsertAlt= new Tooltip();
			tooltipInsertAlt.setStyle(toolTipCss());
			tooltipInsertAlt.setText("미션에 지정할 고도 설정"); 
			insertAlt.setTooltip(tooltipInsertAlt);
			
			Tooltip tooltipBtnAddTakeOff= new Tooltip();
			tooltipBtnAddTakeOff.setStyle(toolTipCss());
			tooltipBtnAddTakeOff.setText("TakeOff 버튼을 누르지 않아도 미션에서 바로 이륙하게 추가"); 
			btnAddTakeoff.setTooltip(tooltipBtnAddTakeOff);
			
			Tooltip tooltipBtnAddRTL= new Tooltip();
			tooltipBtnAddRTL.setStyle(toolTipCss());
			tooltipBtnAddRTL.setText("RTL 버튼을 누르지 않아도 미션에서 바로 RTL하게 추가"); 
			btnAddRTL.setTooltip(tooltipBtnAddRTL);
			  
			Tooltip tooltipBtnAddROI= new Tooltip();
			tooltipBtnAddROI.setStyle(toolTipCss());
			tooltipBtnAddROI.setText("드론의 헤딩을 고정"); 
			btnAddROI.setTooltip(tooltipBtnAddROI);
			  
			Tooltip tooltipBtnAddJump= new Tooltip();
			tooltipBtnAddJump.setStyle(toolTipCss());
			tooltipBtnAddJump.setText("WayPoint순서에 관계없이 지정한 위치로 이동"); 
			btnAddJump.setTooltip(tooltipBtnAddJump);
			
			Tooltip tooltipBtnAddLand= new Tooltip();
			tooltipBtnAddLand.setStyle(toolTipCss());
			tooltipBtnAddLand.setText("드론을 착륙시키고 화물 탈착"); 
			btnAddLand.setTooltip(tooltipBtnAddLand);
			  
			Tooltip tooltipBtnMissionClear= new Tooltip();
			tooltipBtnMissionClear.setStyle(toolTipCss());
			tooltipBtnMissionClear.setText("만든 미션 삭제"); 
			btnMissionClear.setTooltip(tooltipBtnMissionClear);
			  
			//Mission 펜스 button info
			Tooltip tooltipBtnFenceUpload= new Tooltip();
			tooltipBtnFenceUpload.setStyle(toolTipCss());
			tooltipBtnFenceUpload.setText("설정한 펜스를 드론에게 MAVLINK 보내기"); 
			btnFenceUpload.setTooltip(tooltipBtnFenceUpload);
			  
			Tooltip tooltipBtnFenceDownload= new Tooltip();
			tooltipBtnFenceDownload.setStyle(toolTipCss());
			tooltipBtnFenceDownload.setText("업로드 되어있는 펜스 불러오기"); 
			btnFenceDownload.setTooltip(tooltipBtnFenceDownload);
			  
			Tooltip tooltipBtnFenceEnabble= new Tooltip();
			tooltipBtnFenceEnabble.setStyle(toolTipCss());
			tooltipBtnFenceEnabble.setText("펜스 기능 활성화"); 
			btnFenceEnable.setTooltip(tooltipBtnFenceEnabble);
			  
			Tooltip tooltipBtnFenceDisable= new Tooltip();
			tooltipBtnFenceDisable.setStyle(toolTipCss());
			tooltipBtnFenceDisable.setText("펜스 기능 비활성화"); 
			btnFenceDisable.setTooltip(tooltipBtnFenceDisable);
			     
			Tooltip tooltipBtnFenceClear= new Tooltip();
			tooltipBtnFenceClear.setStyle(toolTipCss());
			tooltipBtnFenceClear.setText("만든 펜스 삭제"); 
			btnFenceClear.setTooltip(tooltipBtnFenceClear);
			  
			//NoFlyZone button info
			Tooltip tooltipInsertLat= new Tooltip();
			tooltipInsertLat.setStyle(toolTipCss());
			tooltipInsertLat.setText("비행금지구역의 중심점 위도"); 
			insertLat.setTooltip(tooltipInsertLat);
			  
			Tooltip tooltipInsertLng= new Tooltip();
			tooltipInsertLng.setStyle(toolTipCss());
			tooltipInsertLng.setText("비행금지구역의 중심점 경도"); 
			insertLng.setTooltip(tooltipInsertLng);
			  
			Tooltip tooltipInsertRad= new Tooltip();
			tooltipInsertRad.setStyle(toolTipCss());
			tooltipInsertRad.setText("비행금지구역 원의 반지름"); 
			insertRad.setTooltip(tooltipInsertRad);
			  
			Tooltip tooltipBtnNoFlyZoneAdd= new Tooltip();
			tooltipBtnNoFlyZoneAdd.setStyle(toolTipCss());
			tooltipBtnNoFlyZoneAdd.setText("입력한 비행금지 구역 만들기"); 
			btnNoFlyZoneAdd.setTooltip(tooltipBtnNoFlyZoneAdd);
			  
			Tooltip tooltipBtnNoFlyZoneEnable= new Tooltip();
			tooltipBtnNoFlyZoneEnable.setStyle(toolTipCss());
			tooltipBtnNoFlyZoneEnable.setText("비행금지구역 활성화"); 
			btnNoFlyZoneEnable.setTooltip(tooltipBtnNoFlyZoneEnable);
			  
			Tooltip tooltipBtnNoFlyZoneDisabler= new Tooltip();
			tooltipBtnNoFlyZoneDisabler.setStyle(toolTipCss());
			tooltipBtnNoFlyZoneDisabler.setText("비행금지구역 비활성화"); 
			btnNoFlyZoneDisable.setTooltip(tooltipBtnNoFlyZoneDisabler);
		} else {
			Tooltip.uninstall(imgBtnMapFix, tooltipImgBtnMapFix);
			lblFlight.setTooltip(null);
			lblDistance.setTooltip(null);
			lblTotal.setTooltip(null);
			Tooltip.uninstall(imgMissionView, tooltipImgMissionView);
			Tooltip.uninstall(imgFenceView, tooltipImgFenceView);
			Tooltip.uninstall(imgNoFlyZoneView, tooltipImgNoFlyZoneView);
			Tooltip.uninstall(imgConsoleView, tooltipImgConsoleView);
			btnMissionUpload.setTooltip(null);
			btnMissionDownload.setTooltip(null);
			btnAutoTakeoff.setTooltip(null);
			btnMissionStart.setTooltip(null);
			btnMissionStop.setTooltip(null);
			insertAlt.setTooltip(null);
			btnAddTakeoff.setTooltip(null);
			btnAddRTL.setTooltip(null);
			btnAddROI.setTooltip(null);
			btnAddJump.setTooltip(null);
			btnAddLand.setTooltip(null);
			btnMissionClear.setTooltip(null);
			btnFenceUpload.setTooltip(null);
			btnFenceDownload.setTooltip(null);
			btnFenceEnable.setTooltip(null);
			btnFenceDisable.setTooltip(null);
			btnFenceClear.setTooltip(null);
			insertLat.setTooltip(null);
			insertLng.setTooltip(null);
			insertRad.setTooltip(null);
			btnNoFlyZoneAdd.setTooltip(null);
			btnNoFlyZoneEnable.setTooltip(null);
			btnNoFlyZoneDisable.setTooltip(null);
		}
	}
   
	public String toolTipCss() {
		return "-fx-font-size: 20; -fx-font-family: D2Coding;";
	}
    
	public void consoleView(String message) {
		dateTime = new Date();
		String date = dateFormatter.format(dateTime);
		String time = timeFormatter.format(dateTime);

		Platform.runLater(() -> {
			if(!tempMessage.equals(message)) {
				consoleList.add("[" + date + "  "+ time + "] "
						+ "[" + Network.getUav().latitude + " / " + Network.getUav().longitude+ " / "  + Network.getUav().altitude + "]"
						+ " >>> " + message);
				consoleListView.getItems().clear();
				consoleListView.setItems(FXCollections.observableArrayList(consoleList));
			}
			tempMessage = message;
		});
	}
	
	public static void setHome(double latitude, double longitude, double alt) {
		Network.getUav().home(latitude, longitude, alt);
	}
}

