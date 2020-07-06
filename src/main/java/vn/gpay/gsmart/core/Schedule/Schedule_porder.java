package vn.gpay.gsmart.core.Schedule;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule_porder {
	@JsonProperty("ResourceId")
	private long ResourceId;
	
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("StartDate")
	private Date StartDate;
	
	@JsonProperty("EndDate")
	private Date EndDate;
	
	private String mahang;
	
	private String vendorname;
	private String buyername;
	private String pordercode;
		
	public String getPordercode() {
		return pordercode;
	}
	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}
	@JsonProperty("Cls")
	private String Cls;
	
	private long id_origin;
	
	private int duration;
	private int productivity;
	
	public long getResourceId() {
		return ResourceId;
	}
	public String getName() {
		return Name;
	}
	public Date getStartDate() {
		return StartDate;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public void setResourceId(long resourceId) {
		ResourceId = resourceId;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public String getMahang() {
		return mahang;
	}
	public String getCls() {
		return Cls;
	}
	public void setMahang(String mahang) {
		this.mahang = mahang;
	}
	public void setCls(String cls) {
		Cls = cls;
	}
	public long getId_origin() {
		return id_origin;
	}
	public void setId_origin(long id_origin) {
		this.id_origin = id_origin;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getProductivity() {
		return productivity;
	}
	public void setProductivity(int productivity) {
		this.productivity = productivity;
	}
	public String getVendorname() {
		return vendorname;
	}
	public String getBuyername() {
		return buyername;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
	
	
}
