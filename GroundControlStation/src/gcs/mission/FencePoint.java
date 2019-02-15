package gcs.mission;

import java.io.Serializable;

public class FencePoint implements Serializable {
	public String kind;
	public int no;
	public int count;
	public double lat;
	public double lng;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
}
