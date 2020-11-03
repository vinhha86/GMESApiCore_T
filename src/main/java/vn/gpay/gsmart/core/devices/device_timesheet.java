package vn.gpay.gsmart.core.devices;

public class device_timesheet {
	private Long id;
	private String device_ip;
	private Integer device_port;
	private String last_download;
	public Long getId() {
		return id;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public Integer getDevice_port() {
		return device_port;
	}
	public String getLast_download() {
		return last_download;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public void setDevice_port(Integer device_port) {
		this.device_port = device_port;
	}
	public void setLast_download(String last_download) {
		this.last_download = last_download;
	}
	
	
}
