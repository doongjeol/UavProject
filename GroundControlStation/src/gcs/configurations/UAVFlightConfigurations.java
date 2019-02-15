package gcs.configurations;

import java.io.Serializable;

public class UAVFlightConfigurations implements Serializable  {
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
}
