package vn.gpay.gsmart.core.devices;

import java.io.Serializable;


public class DeviceLocal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String devicename;
	
	private String deviceid;
	
	private int devicetype;
	
	private int state;
	
	private int minago;

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public int getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(int devicetype) {
		this.devicetype = devicetype;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMinago() {
		return minago;
	}

	public void setMinago(int minago) {
		this.minago = minago;
	}
}
