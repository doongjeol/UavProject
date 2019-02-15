package gcs.mission;

import java.util.ArrayList;
import java.util.Arrays;

import gcs.network.Network;
import javafx.scene.control.TableView;

public class MissionCalculator {
	@SuppressWarnings("unused")
	private static String[] distKind = {"waypoint", "takeoff", "rtl", "land"}; // 거리 배열에 영향을 주는 kind
	private static String[] notDistKind = {"roi", "jump"}; // 거리 배열에 영향을 주지 않는 kind
	public static double[] distArray; // distArray[i]: i-1번에서 i번 WayPoint 까지의 예상이동거리*
	public static double[] cuDistArray; // cuDistArray[i]: 0번에서 i번 WayPoint 까지의 예상이동거리*
	public static double[] propCuDistArray; // propCuDistArray[i]: 전체거리대비 0번에서 i번 WayPoint 까지의 거리 비율
	public static String[] kindArray; // kindArray[i]: i번 WayPoint의 kind*
	public static ArrayList<WayPoint> uavTravelList;
	public static double totalDistance;
	public static int baseIndex; // 남은거리 계산용
	public static int currentSeq;	
	public static int restDistance;
	
	// 3차원 거리 계산 메소드
	public static double distance3D(double lat1, double lat2, double lon1,
	        double lon2, double alt1, double alt2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = alt1 - alt2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	
	public static double distance3D(WayPoint currentWp, WayPoint prevWp) {
		return distance3D(currentWp.getLatitude(), prevWp.getLatitude(),
				currentWp.getLongitude(), prevWp.getLongitude(), currentWp.getAltitude(), prevWp.getAltitude());
	}
	
	public static double distance3D(double latitude, double longitude, double altitude, WayPoint prevWp) {
		return distance3D(latitude, prevWp.getLatitude(),
				longitude, prevWp.getLongitude(), altitude, prevWp.getAltitude());
	}
	
	public static void missionDistance2(TableView<WayPoint> missionTableView) {
		System.out.println("MissionCalculator.missionDistance2 Start");
		totalDistance = 0;
		uavTravelList = new ArrayList<WayPoint>();
		WayPoint startPoint = new WayPoint(0, "waypoint",
				Network.getUav().latitude,Network.getUav().longitude,
				Network.getUav().altitude, 0, 0);
		uavTravelList.add(startPoint);
		int tableViewSize = missionTableView.getItems().size();
		if(tableViewSize == 0) {
			MissionController.instance.lblTotal.setText("0m");
			return;
		}
		
		for(int missionIndex = 0; missionIndex<tableViewSize; missionIndex++) {
			WayPoint wp = missionTableView.getItems().get(missionIndex);
			if(!Arrays.asList(notDistKind).contains(wp.getKind()))
				uavTravelList.add(wp);
			else if(wp.getKind().equals("jump")) {
				int no = wp.getNo();
				int jump = wp.getJump();
				int rep = wp.getRepeat();
				if(jump < no-1) {
					for(int i=0; i<rep; i++) {
						for(int j=jump; j<no; j++) {
							WayPoint addedWp = missionTableView.getItems().get(j-1);
							if(!Arrays.asList(notDistKind).contains(addedWp.getKind()))
								uavTravelList.add(addedWp);
						}
					}
				} else if(no+1 < jump) {
					missionIndex = jump-2;
				}
			} else {
				// Do Nothing
			}
		}
		WayPoint lastPoint = missionTableView.getItems().get(tableViewSize-1);
		uavTravelList.add(lastPoint); // 미션 종료 후, seq번호가 0번으로 초기화되므로...
		
		WayPoint prevWp = uavTravelList.get(0);
		WayPoint currentWp;
		distArray = new double[uavTravelList.size()];
		cuDistArray = new double[uavTravelList.size()];
		propCuDistArray = new double[uavTravelList.size()];
		for(int i=0; i<uavTravelList.size(); i++) {
			currentWp = uavTravelList.get(i);
			distArray[i] = distance3D(currentWp, prevWp);
			totalDistance += distArray[i];
			cuDistArray[i] = totalDistance;
			//System.out.println(currentWp.toString() + "  distance[prevWp~currentWp]: " + distArray[i] + "  cuDist: " + cuDistArray[i]);
			prevWp = currentWp;
		}
		for(int i=0; i<uavTravelList.size(); i++) {
			propCuDistArray[i] = cuDistArray[i]*100/totalDistance;
		}
		
		MissionController.instance.lblTotal.setText(String.valueOf(Math.round(totalDistance))+"m");
		System.out.println(Math.round(totalDistance));
		System.out.println("MissionCalculator.missionDistance2 End");
	}
	
	public static double restDistance3D() throws Exception {
		if(baseIndex == 0) return 0;
		WayPoint nextWp = uavTravelList.get(baseIndex);
		restDistance = (int)(totalDistance - cuDistArray[baseIndex] + distance3D(Network.getUav().latitude, Network.getUav().longitude,Network.getUav().altitude, nextWp));
		return restDistance;
	}
}
