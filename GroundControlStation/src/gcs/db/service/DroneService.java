package gcs.db.service;

import java.sql.Connection;
import java.sql.DriverManager;

import gcs.db.dao.DroneDao;
import gcs.network.UAV;

public class DroneService {
	private static DroneService instance;
	public static DroneService getInstance() {
		if(instance == null) {
			instance = new DroneService();
		}
		return instance;
	}
	
	public void write(UAV uav){
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:orcl",
					"hr",
					"iot12345");
			DroneDao droneDao = DroneDao.getInstance();
			
			Integer autoflag = droneDao.insert(uav, conn);
			
			if(autoflag != null) {
				//System.out.println("DroneService >> 드론의 데이터가 저장되었습니다");
			} else {
				System.out.println("DroneService >> 드론의 데이터가 저장되지 않았습니다");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{conn.close();} catch(Exception e) {}
		}		
	}
}
