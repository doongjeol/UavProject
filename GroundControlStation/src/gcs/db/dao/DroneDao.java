package gcs.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import gcs.mission.WayPoint;
import gcs.network.UAV;

public class DroneDao{
	WayPoint waypoint = new WayPoint();
	private static DroneDao instance;
	public static DroneDao getInstance() {
		if(instance == null) {
			instance = new DroneDao();
		}
		return instance;
	}
	
	public Integer insert(UAV uav, Connection conn) throws Exception {
		Integer pk = null;
		
		String sql = ""+
			"insert into drone "+
			"(basetime, autoflag, wpnum, gpstime, latitude, longitude, altitude) "+
			"values "+
			"(?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"autoflag"});
		pstmt.setString(1, getCurrentTime());
		pstmt.setInt(2,uav.autoflag);
		pstmt.setInt(3,uav.nextWaypointNo);
		pstmt.setInt(4,uav.gpsTime);
		pstmt.setDouble(5,uav.latitude);
		pstmt.setDouble(6,uav.longitude);
		pstmt.setDouble(7,uav.altitude);
		int row = pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		if(rs.next()) {
			pk = rs.getInt(1);
		}
		rs.close();
		pstmt.close();
		
		return pk;		
	}
	
	public String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SS");
		String str = sdf.format(new Date());
		return str;
	}
	
}