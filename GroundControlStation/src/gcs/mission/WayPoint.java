package gcs.mission;

import java.io.Serializable;

import javafx.scene.control.Button;

public class WayPoint implements Serializable {
		public int no;
		public String kind;
		public double latitude;
		public double longitude;
		public double altitude;
		public int jump;
		public int repeat;
		
		public WayPoint() {
			
		}
		
		public WayPoint(int no, String kind, double latitude, double longitude, double altitude, int jump, int repeat) {
			super();
			this.no = no;
			this.kind = kind;
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
			this.jump = jump;
			this.repeat = repeat;
		}
		
		public int getJump() { 
			return jump;
		}
		public void setJump(int jump) {
			this.jump = jump;
		}
		public int getRepeat() {
			return repeat;
		}
		public void setRepeat(int repeat) {
			this.repeat = repeat;
		}
		//TableView Item을 위해 Getter/Setter 필요
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public String getKind() {
			return kind;
		}
		public void setKind(String kind) {
			this.kind = kind;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getAltitude() {
			return altitude;
		}
		public void setAltitude(double altitude) {
			this.altitude = altitude;
		}
		
		@Override
		public String toString() {
			return "WayPoint [no=" + no + ", kind=" + kind + ", latitude=" + latitude + ", longitude=" + longitude
					+ ", altitude=" + altitude + ", jump=" + jump + ", repeat=" + repeat + "]";
		}
		
	}
