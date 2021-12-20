package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

public class Jitin_Stockout_order_pkl_Data implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long orgid_link;
	private Long stockoutorderid_link;
	private Long stockoutorderdid_link;
	private Long skuid_link;
	private Long colorid_link;
	private String lotnumber;
	private Integer packageid;
	private Float ydsorigin;
	private Float ydscheck;
	private Float width;
	private Float netweight;
	private Float grossweight;
	private String epc;
	private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private String spaceepc_link;
	private Integer status;
	private Float met;
	private Float metorigin;
	private Float metcheck;
	private Float width_yds_check;
	private Float width_yds;
	private Float width_met_check;
	private Float width_met; // width_yds_check, width_yds, width_met_check, width_met
	
	//Transient
	private String material_product_code;
	private String color_name;
	private String unit_name;
	private String spaceString;
	private Integer warehouseStatus;
	private String warehouseStatusString;
	private Float warehouse_check_met_check;
	private Float warehouse_check_width_check;
	private Float warehouse_check_met_err;
	private Date date_check;
	private Boolean isInStock;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public Long getStockoutorderdid_link() {
		return stockoutorderdid_link;
	}
	public void setStockoutorderdid_link(Long stockoutorderdid_link) {
		this.stockoutorderdid_link = stockoutorderdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	public Float getNetweight() {
		return netweight;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Date getEncryptdatetime() {
		return encryptdatetime;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}
	public Float getMet() {
		return met;
	}
	public void setMet(Float met) {
		this.met = met;
	}
	public Float getMetorigin() {
		return metorigin;
	}
	public void setMetorigin(Float metorigin) {
		this.metorigin = metorigin;
	}
	public Float getMetcheck() {
		return metcheck;
	}
	public void setMetcheck(Float metcheck) {
		this.metcheck = metcheck;
	}
	public Float getWidth_yds_check() {
		return width_yds_check;
	}
	public void setWidth_yds_check(Float width_yds_check) {
		this.width_yds_check = width_yds_check;
	}
	public Float getWidth_yds() {
		return width_yds;
	}
	public void setWidth_yds(Float width_yds) {
		this.width_yds = width_yds;
	}
	public Float getWidth_met_check() {
		return width_met_check;
	}
	public void setWidth_met_check(Float width_met_check) {
		this.width_met_check = width_met_check;
	}
	public Float getWidth_met() {
		return width_met;
	}
	public void setWidth_met(Float width_met) {
		this.width_met = width_met;
	}
	public String getMaterial_product_code() {
		return material_product_code;
	}
	public void setMaterial_product_code(String material_product_code) {
		this.material_product_code = material_product_code;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getSpaceString() {
		return spaceString;
	}
	public void setSpaceString(String spaceString) {
		this.spaceString = spaceString;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getWarehouseStatus() {
		return warehouseStatus;
	}
	public void setWarehouseStatus(Integer warehouseStatus) {
		this.warehouseStatus = warehouseStatus;
	}
	public String getWarehouseStatusString() {
		return warehouseStatusString;
	}
	public void setWarehouseStatusString(String warehouseStatusString) {
		this.warehouseStatusString = warehouseStatusString;
	}
	public Float getWarehouse_check_met_check() {
		return warehouse_check_met_check;
	}
	public void setWarehouse_check_met_check(Float warehouse_check_met_check) {
		this.warehouse_check_met_check = warehouse_check_met_check;
	}
	public Float getWarehouse_check_width_check() {
		return warehouse_check_width_check;
	}
	public void setWarehouse_check_width_check(Float warehouse_check_width_check) {
		this.warehouse_check_width_check = warehouse_check_width_check;
	}
	public Float getWarehouse_check_met_err() {
		return warehouse_check_met_err;
	}
	public void setWarehouse_check_met_err(Float warehouse_check_met_err) {
		this.warehouse_check_met_err = warehouse_check_met_err;
	}
	public Date getDate_check() {
		return date_check;
	}
	public void setDate_check(Date date_check) {
		this.date_check = date_check;
	}
	public Boolean getIsInStock() {
		return isInStock;
	}
	public void setIsInStock(Boolean isInStock) {
		this.isInStock = isInStock;
	}
	
}
