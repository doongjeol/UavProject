package gcs.mission;

import java.io.Serializable;


public class NoFlyZonePoint implements Serializable {
	public int no;
	public double lat;
	public double lng;
	public double rad;
	public static NoFlyZonePoint instance;
	
	public NoFlyZonePoint() {
		instance = this;
	}
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
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
	public double getRad() {
		return rad;
	}
	public void setRad(double rad) {
		this.rad = rad;
	}
	
	public double ifNoflyzone(double Ox, double Oy, double Wx1, double Wy1, double Wx2, double Wy2) {
		//1번WP = (Wx1,Wy1) 다음WP(Wx2,Wy2) noflyzone(Ox,Oy)
		Ox= Ox* 111189.57696002942;
		Oy= Oy* 88799.53629131494;

		Wx1= Wx1* 111189.57696002942;
		Wy1= Wy1* 88799.53629131494;
		
		Wx2= Wx2* 111189.57696002942;
		Wy2= Wy2* 88799.53629131494;
		
		double m = (Wy1 -Wy2) /(Wx1 -Wx2);
		double n = Wy1 - m* Wx1;
		
		double a = m;
		double b = -1;
		double c = n;
		
		// 2. 점과 직선의 거리를 구하는 식
		double d = Math.abs(a*Ox + b*Oy + c) / Math.sqrt(Math.pow(a, 2)+ Math.pow(b, 2));
				
		System.out.println("d: "+ d);
		return d;
	}
}
