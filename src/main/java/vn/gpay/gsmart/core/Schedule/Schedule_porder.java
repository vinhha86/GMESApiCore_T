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
	private Integer totalpackage;
	private Long parentid_origin;
	public long porder_grantid_link;
	private Integer status;
	private long productid_link;
	private long pcontract_poid_link;
	private long pcontractid_link;
	private long porderid_link;
	private String productbuyercode;
	

	@JsonProperty("Cls")
	private String Cls;
	
	private long id_origin;
	
	private int duration;
	private int productivity;
	private int productivity_po;
	private int productivity_porder;
	
	
		
	public int getProductivity_po() {
		return productivity_po;
	}
	public void setProductivity_po(int productivity_po) {
		this.productivity_po = productivity_po;
	}
	public int getProductivity_porder() {
		return productivity_porder;
	}
	public void setProductivity_porder(int productivity_porder) {
		this.productivity_porder = productivity_porder;
	}
	public Long getParentid_origin() {
		return parentid_origin;
	}
	public void setParentid_origin(Long parentid_origin) {
		this.parentid_origin = parentid_origin;
	}
	public String getPordercode() {
		return pordercode;
	}
	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}
	
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
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public long getPorder_grantid_link() {
		return porder_grantid_link;
	}
	public void setPorder_grantid_link(long porder_grantid_link) {
		this.porder_grantid_link = porder_grantid_link;
	}
	public long getProductid_link() {
		return productid_link;
	}
	public long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setProductid_link(long productid_link) {
		this.productid_link = productid_link;
	}
	public void setPcontract_poid_link(long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getProductbuyercode() {
		return productbuyercode;
	}
	public void setProductbuyercode(String productbuyercode) {
		this.productbuyercode = productbuyercode;
	}
	
	
}
