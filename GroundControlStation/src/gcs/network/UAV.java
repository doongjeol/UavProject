package gcs.network;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import gcs.appmain.AppMainController;
import gcs.configurations.ConfigurationsController;
import gcs.configurations.UAVFlightConfigurations;
import gcs.ctrlstat.CtrlstatController;
import gcs.mission.FencePoint;
import gcs.mission.MissionController;
import gcs.mission.WayPoint;
import javafx.application.Platform;

public class UAV implements Cloneable {
	
	public String autopilotVersion;
	public String systemStatus;
	public String statusText;
	public String tempStatus = "";
	public String mode;
	public double batteryVoltage;
	public double batteryCurrent;
	public double batteryLevel;
	public double roll;
	public double pitch;
	public double yaw;
	public boolean armed;
	public double latitude;
	public double longitude;
	public double altitude;
	public double altitudeAbs;	
	public double heading;
	public double airSpeed;
	public double groundSpeed;
	public double verticalSpeed;
	public double homeLat;
	public double homeLng;
	public int gpsTime;
	public double gpsHdop;
	public double gpsVdop;
	public int gpsFixType;
	public int gpsSatellites;
	public double rangeFinderDistance;
	public double rangeFinderVoltage;
	public double opticalFlowQuality;
	public int wpDist;
	public boolean uploadStatus = false;
	
	public double rpiCpuLoad;
	public double rpiRamLoad;
	public double rpiNetUpSpeed;
	public double rpiNetDownSpeed;
	
	public boolean settingsStatus = false;
	public double rtl_alt;
	public double land_speed;
	public int rng_fnd;
	public int fence_enable;
	public int fence_action;
	public double fence_alt_max;
	public double fence_margin;
	public double batt_capacity;
	public double batt_low_volt;
	public double wpnav_radius;
	public double wpnav_speed;
	public double wpnav_dn;
	public double wpnav_up;
	public double wpnav_accel;
	public double wpnav_accel_z;
	private double seq_alt;
	
	public int nextWaypointNo;
	public List<WayPoint> wayPoints;
	
	public double fenceEnable;
	public double fenceType;
	public double fenceAction;
	public double fenceRadius;
	public double fenceAltMax;
	public double fenceMargin;
	public double fenceTotal;
	public List<FencePoint> fencePoints;
	
	public boolean connected;
	private MqttClient mqttClient;
	public boolean gcsConnect = true;
	private boolean missionStatus = false;
	public boolean cargoStatus = false;
	//DB용
	public int autoflag;
	
	public List<WayPoint> tList = new ArrayList<>();
	
	public UAV() {
	}
	
	public void connect() {
		Thread thread = new Thread() {
			@Override
			public void run() {			
				try {
					mqttClient = new MqttClient("tcp://" + Network.mqttIp + ":" + Network.mqttPort, MqttClient.generateClientId(), null);
					mqttClient.setCallback(new MqttCallback() {
						String strJson;
						@Override
						public void messageArrived(String topic, MqttMessage message) throws Exception {
							strJson = new String(message.getPayload());
//							System.out.println(strJson);
							dataParsing(strJson);
							connected = true;
						}
						@Override
						public void deliveryComplete(IMqttDeliveryToken token) {
						}
						@Override
						public void connectionLost(Throwable e) {
							UAV.this.disconnect();
							e.printStackTrace();
						}
					});
					MqttConnectOptions mco = new MqttConnectOptions();
					mco.setConnectionTimeout(3);
					mqttClient.connect(mco);
					mqttClient.subscribe(Network.uavPubTopic);
					gcsHeartBeat();
				} catch (Exception e) {
					UAV.this.disconnect();
					//e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public void disconnect() {
		try {
			CtrlstatController.instance.setStatus(new UAV());
			ConfigurationsController.instance.setStatus(new UAV());
			mqttClient.disconnect();
			mqttClient.close();
		} catch (Exception e) {
		}
		connected = false;
	}

	private void dataParsing(String strJson) {
		try {
			JSONObject jsonObject = new JSONObject(strJson);
			autopilotVersion = jsonObject.getString("autopilot_version");
			systemStatus = jsonObject.getString("system_status");
			statusText = jsonObject.getString("statustext");
			//if(!(statusText.equals("")))System.out.println(statusText);
			mode = jsonObject.getString("mode");
				//SITL에서 가끔 battery_level 을 보내지 않아
				//시뮬레이션에서 가끔 NullPointerException 발생
			try {batteryVoltage = jsonObject.getDouble("battery_voltage");} catch(Exception e) {}
			try {batteryCurrent = jsonObject.getDouble("battery_current");} catch(Exception e) {}
			try {batteryLevel = jsonObject.getDouble("battery_level");} catch(Exception e) {}
			roll = jsonObject.getDouble("roll");
			pitch = jsonObject.getDouble("pitch");
			yaw = jsonObject.getDouble("yaw");
			armed = jsonObject.getBoolean("armed");
			latitude = jsonObject.getDouble("latitude");
			longitude = jsonObject.getDouble("longitude");
			altitude = jsonObject.getDouble("altitude");
			altitudeAbs = jsonObject.getDouble("altitude_abs");
			heading = jsonObject.getDouble("heading");
			airSpeed = jsonObject.getDouble("airspeed");
			groundSpeed = jsonObject.getDouble("groundspeed");
			verticalSpeed = jsonObject.getDouble("verticalspeed");
			homeLat = jsonObject.getDouble("homeLat");
			homeLng = jsonObject.getDouble("homeLng");
			gpsTime = jsonObject.getInt("gps_time");
			gpsHdop = jsonObject.getDouble("gps_hdop");
			gpsVdop = jsonObject.getDouble("gps_vdop");
			gpsFixType = jsonObject.getInt("gps_fix_type");
			gpsSatellites = jsonObject.getInt("gps_satellites_visible");
			cargoStatus = jsonObject.getBoolean("luggage_status");
			
			tempStatus = "";
			
			try { rangeFinderVoltage = jsonObject.getDouble("rangefinder_voltage");
			} catch(Exception e) { rangeFinderVoltage = 0; }
			try { rangeFinderDistance = jsonObject.getDouble("rangefinder_distance");
			} catch(Exception e) { rangeFinderDistance = 0; }
			try { opticalFlowQuality = jsonObject.getDouble("optical_flow_quality");
			} catch(Exception e) { opticalFlowQuality = 0; }
			
			try {
				rpiCpuLoad = jsonObject.getDouble("cpu_usage");
				rpiRamLoad = jsonObject.getDouble("ram_usage");
				rpiNetUpSpeed = jsonObject.getDouble("up_speed");
				rpiNetDownSpeed = jsonObject.getDouble("down_speed");
			} catch(Exception e) {
				rpiCpuLoad = -1;
				rpiRamLoad = -1;
				rpiNetUpSpeed = -1;
				rpiNetDownSpeed = -1;
			}
			
			try {
				rtl_alt = jsonObject.getDouble("rtl_alt");
				land_speed = jsonObject.getDouble("land_speed");
				rng_fnd = jsonObject.getInt("rng_fnd");
				fence_enable = jsonObject.getInt("fence_enable");
				fence_action = jsonObject.getInt("fence_action");
				fence_alt_max = jsonObject.getDouble("fence_alt_max");
				fence_margin = jsonObject.getDouble("fence_margin");
				batt_capacity = jsonObject.getDouble("batt_capacity");
				batt_low_volt = jsonObject.getDouble("batt_low_volt");
				wpnav_radius = jsonObject.getDouble("wpnav_radius");
				wpnav_speed = jsonObject.getDouble("wpnav_speed");
				wpnav_dn = jsonObject.getDouble("wpnav_dn");
				wpnav_up = jsonObject.getDouble("wpnav_up");
				wpnav_accel = jsonObject.getDouble("wpnav_accel");
				wpnav_accel_z = jsonObject.getDouble("wpnav_accel_z");
				settingsStatus = true;
			} catch(Exception e) {}
			
			nextWaypointNo = jsonObject.getInt("next_waypoint_no");
			
			JSONArray jsonArrayWayPoints = jsonObject.getJSONArray("waypoints");
			List<WayPoint> listWayPoint = new ArrayList<WayPoint>();
			for(int i=0; i<jsonArrayWayPoints.length(); i++) {
				JSONObject jo = jsonArrayWayPoints.getJSONObject(i);
				WayPoint wp = new WayPoint();
				wp.no = i+1;
				wp.kind = jo.getString("kind");
				if(wp.kind.equals("takeoff")) {
					wp.altitude = jo.getDouble("alt");
				} else if(wp.kind.equals("waypoint")) {
					wp.latitude = jo.getDouble("lat");
					wp.longitude = jo.getDouble("lng");
					wp.altitude = jo.getDouble("alt");
				} else if(wp.kind.equals("rtl")) {
					
				} else if(wp.kind.equals("jump")) {
					wp.jump = jo.getInt("jump");
					wp.repeat = jo.getInt("repeat");
					wp.latitude = listWayPoint.get(wp.jump-1).latitude;
					wp.longitude = listWayPoint.get(wp.jump-1).longitude;
				} else if(wp.kind.equals("roi")) {
					wp.latitude = jo.getDouble("lat");
					wp.longitude = jo.getDouble("lng");
				} else if(wp.kind.equals("land")) {
					wp.latitude = jo.getDouble("lat");
					wp.longitude = jo.getDouble("lng");
				}
				listWayPoint.add(wp);
			}
			wayPoints = listWayPoint;
			
			JSONObject jsonObjectFenceInfo =  jsonObject.getJSONObject("fence_info");
			fenceEnable = jsonObjectFenceInfo.getDouble("fence_enable");
			try {
				fenceType = jsonObjectFenceInfo.getDouble("fence_type");
				fenceAction = jsonObjectFenceInfo.getDouble("fence_action");
				fenceRadius = jsonObjectFenceInfo.getDouble("fence_radius");
				fenceAltMax = jsonObjectFenceInfo.getDouble("fence_alt_max");
				fenceMargin = jsonObjectFenceInfo.getDouble("fence_margin");
				fenceTotal = jsonObjectFenceInfo.getDouble("fence_total");
				JSONArray jsonArrayFencePoints = jsonObjectFenceInfo.getJSONArray("fence_points");
				List<FencePoint> listFencePoint = new ArrayList<FencePoint>();
				for(int i=0; i<jsonArrayFencePoints.length(); i++) {
					JSONObject jo = jsonArrayFencePoints.getJSONObject(i);
					FencePoint fp = new FencePoint();
					fp.no = jo.getInt("no");
					fp.count = jo.getInt("count");
					fp.lat = jo.getDouble("lat");
					fp.lng = jo.getDouble("lng");
					listFencePoint.add(fp);
				}
				fencePoints = listFencePoint;
			} catch(Exception e) {
				fenceType = 0;
				fenceAction = 0;
				fenceRadius = 0;
				fenceAltMax = 0;
				fenceMargin = 0;
				fenceTotal = 0;
				fencePoints = new ArrayList<FencePoint>();
			}
			
			AppMainController.instance.viewStatus((UAV)this.clone());
			
			if(statusText.equals("Reached command #" + (MissionController.instance.landNo-1))) {
				uploadStatus = true;
				System.out.println("uploadStatus true로 설정");
			} 
			
			// !armed &&
			if(!armed && uploadStatus && tList != null && MissionController.instance.autoStatus) {
				uploadStatus = false;
				
				for(WayPoint t: tList) {
					System.out.println(t.toString());
				}
				
				if(cargoStatus) {
					Network.getUav().luggageDettach();
				}
				
				List<WayPoint> list = new ArrayList<WayPoint>();
				System.out.println("새로운 미션 생성>>> landNo: " + MissionController.instance.landNo);
				for(int i = 0; i < tList.size(); i++) {
					WayPoint wapoint = tList.get(i);
					if(i+1 > MissionController.instance.landNo) {
						list.add(wapoint);
					}
				}
				
				if(list.get(0).altitude == 0) {
					seq_alt = Integer.parseInt(MissionController.instance.insertAlt.getText());
				} else {
					seq_alt = list.get(0).altitude;
				}
				
				WayPoint waypoint = new WayPoint(1, "takeoff", Network.getUav().latitude, Network.getUav().longitude, seq_alt, 0, 0); 
				list.add(0, waypoint);
				
				MissionController.instance.setMissionTableViewItems(list);
				MissionController.instance.setMission(list);
				missionUpload(list);
				
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							boolean flag = false;
							while(true) {
								Thread.sleep(7000);
								while(!armed) {
									flag = true;
									arm();

									Thread.sleep(1000);
									missionStatus = true;
								}
								if(flag) break;
							}
							//takeoff(Integer.parseInt(MissionController.instance.insertAlt.getText()));
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
			
			if(missionStatus) {
				missionStart();
				Platform.runLater(()->MissionController.instance.missionStatus = true);
				if(!CtrlstatController.instance.timerStatus) CtrlstatController.instance.secToHHMMSS();
				missionStatus = false;
			}
			
			if(statusText.equals("Arming motors")) {
				tempStatus = "arm";
				MissionController.instance.consoleView("UAV Armed...");
			} else if(statusText.equals("Disarming motors")) {
				tempStatus = "disarm";
				MissionController.instance.consoleView("UAV Disarmed...");
			} else if(statusText.equals("Reached command #" + (MissionController.instance.landNo-1))) {
				tempStatus = "land";
				MissionController.instance.consoleView("UAV Land...");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void send(String strJson) {
		if(connected) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						MqttMessage message = new MqttMessage(strJson.getBytes());
						mqttClient.publish(Network.uavSubTopic, message);
					} catch(Exception e) {
//						e.printStackTrace();
//						UAV.this.disconnect();
					} 
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}	
	
	public void arm() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "arm");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void disarm() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "disarm");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void changeAlt(int height) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "changeAlt");
		jsonObject.put("height", height);
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void takeoff(int height) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "takeoff");
		jsonObject.put("height", height);
		String strJson = jsonObject.toString();
		send(strJson);
	}	
	
	public void rtl() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "rtl");
		String strJson = jsonObject.toString();
		send(strJson);	
	}	
	
	public void land() {	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "land");
		String strJson = jsonObject.toString();
		send(strJson);
	}	
	
	public void gotoStart(double latitude, double longitude, double altitude) {	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "goto");
		jsonObject.put("latitude", latitude);
		jsonObject.put("longitude", longitude);
		jsonObject.put("altitude", altitude);
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void home(double latitude, double longitude, double altitude) {	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "home");
		jsonObject.put("latitude", latitude);
		jsonObject.put("longitude", longitude);
		jsonObject.put("altitude", altitude);
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionUpload(List<WayPoint> list) {
		// Land 이후 미션은 드론에 전달하지 않도록 하여 land 지점에서 disarm하도록 함.
		// 대회 미션 수행을 위한 임시 수정(단, 미션 다운로드 시, gcs의 미션테이블 내 미션과 다른 문제 발생)
		boolean check = false; 
		tList.clear();
		for(WayPoint t: list) {
			tList.add(t);
		}
		JSONObject root = new JSONObject();
		root.put("command", "mission_upload");
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		for(WayPoint wp : list) {
			System.out.println("UAV :" + wp.toString());
			JSONObject jo = new JSONObject();
			if(wp.kind.equals("land")) {
				MissionController.instance.landNo = wp.no;
			}
			if(check == false) {
				jo.put("kind", wp.kind);
				jo.put("lat", wp.latitude);
				jo.put("lng", wp.longitude);
				jo.put("alt", wp.altitude);
				jo.put("jump", wp.jump);
				jo.put("repeat", wp.repeat);
				jsonArray1.put(jo);
				if(wp.kind.equals("land")) {
					check = true;
				}
			} else {
				jo.put("kind", wp.kind);
				jo.put("lat", wp.latitude);
				jo.put("lng", wp.longitude);
				jo.put("alt", wp.altitude);
				jo.put("jump", wp.jump);
				jo.put("repeat", wp.repeat);
				jsonArray2.put(jo);
			}
			
		}
		root.put("waypoints", jsonArray1);
		String strJson = root.toString();
		send(strJson);
	}
	
	public void missionDownload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_download");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionStart() {
		System.out.println("미션 START");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_start");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionStop() {	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_stop");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionClear() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_clear");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionSave() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_save");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void missionRead() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_read");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void takeoffAdd() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "takeoff_add");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	public void returnAdd() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "return_add");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	public void roiAdd() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "roi_add");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	public void jumpAdd() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "jump_add");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	
	public void fenceEnable() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_enable");
		String strJson = jsonObject.toString();
		send(strJson);
	}	
	
	public void fenceDisable() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_disable");
		String strJson = jsonObject.toString();
		send(strJson);
	}	
	
	public void fenceUpload(String jsonFencePoints) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_upload");
		jsonObject.put("fence_type", 4); //4:polygon, 7:All=polygon+radius+alt_max
		jsonObject.put("fence_action", 1); //RTL
		jsonObject.put("fence_radius", 500);
		jsonObject.put("fence_alt_max", 100);
		jsonObject.put("fence_margin", 5);
		JSONArray jsonArrayFencePoints = new JSONArray(jsonFencePoints);
		jsonObject.put("fence_total", jsonArrayFencePoints.length());
		jsonObject.put("points", jsonArrayFencePoints);
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void fenceDownload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_download");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void fenceClear() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_clear");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void move(double velocityX, double velocityY, double velocityZ, double duration) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "move");
		jsonObject.put("velocity_x", velocityX);
		jsonObject.put("velocity_y", velocityY);
		jsonObject.put("velocity_z", velocityZ);
		jsonObject.put("duration", duration);
		String strJson = jsonObject.toString();
		send(strJson);
	}

	public void changeHeading(double heading) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "change_heading");
		jsonObject.put("heading", heading);
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void settingsUpload(UAVFlightConfigurations set) {
		JSONObject settings = new JSONObject();
		settings.put("command", "settings_upload");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("rtl_alt", set.rtl_alt);
		jo.put("land_speed", set.land_speed);
		jo.put("rng_fnd", set.rng_fnd);
		jo.put("fence_enable", set.fence_enable);
		jo.put("fence_action", set.fence_action);
		jo.put("fence_alt_max", set.fence_alt_max);
		jo.put("fence_margin", set.fence_margin);
		jo.put("batt_capacity", set.batt_capacity);
		jo.put("batt_low_volt", set.batt_low_volt);
		jo.put("wpnav_radius", set.wpnav_radius);
		jo.put("wpnav_speed", set.wpnav_speed);
		jo.put("wpnav_dn", set.wpnav_dn);
		jo.put("wpnav_up", set.wpnav_up);
		jo.put("wpnav_accel", set.wpnav_accel);
		jo.put("wpnav_accel_z", set.wpnav_accel_z);
		jsonArray.put(jo);
		
		settings.put("settings", jsonArray);
		String strJson = settings.toString();
		send(strJson);
	}
	public void gcsHeartBeat() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("command", "gcs_connect");
						jsonObject.put("status", "true");
						String strJson = jsonObject.toString();
						send(strJson);
						Thread.sleep(1000);
					}catch(Exception e) {
						
					}
				}
			}
		};
		thread.start();
	}
	
	public void settingsDownload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "settings_download");
		String strJson = jsonObject.toString();
		send(strJson);
	}
	
	public void luggageAttach() {
		System.out.println("luggageAttach");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "luggage_attach");
		String strJson = jsonObject.toString();
		send(strJson);
		MissionController.instance.consoleView("화물 부착...");
	}
	
	public void luggageDettach() {
		System.out.println("luggageDettach");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "luggage_dettach");
		String strJson = jsonObject.toString();
		send(strJson);
		MissionController.instance.consoleView("화물 탈착...");
	}
}