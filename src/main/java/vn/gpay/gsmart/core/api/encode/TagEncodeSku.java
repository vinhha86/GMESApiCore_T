package vn.gpay.gsmart.core.api.encode;

public class TagEncodeSku {

	private String epc;
	
	private String oldepc;
	
	private String tid;
	
	private String sku;
	
	private Long deviceid_link;

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getOldepc() {
		return oldepc;
	}

	public void setOldepc(String oldepc) {
		this.oldepc = oldepc;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getDeviceid_link() {
		return deviceid_link;
	}

	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	
	
}
