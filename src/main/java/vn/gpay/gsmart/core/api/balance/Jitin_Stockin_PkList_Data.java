package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.Date;

public class Jitin_Stockin_PkList_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orgrootid_link;
    private Long stockinid_link;
    private Long stockindid_link;
    private Long skuid_link;
    private Long colorid_link;
	private Long sizeid_link;
    private Integer unitid_link;
    private String lotnumber ;
    private Integer packageid;
	private Float ydsorigin;
	private Float ydscheck;
    private Float width;
    private Float netweight;
    private Float grossweight;
    private Float m3;
    private String epc;
	private Integer skutypeid_link;	
    private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
    private Integer status;
    private String comment;
    private Float width_check;
	private Float met_origin;
	private Float met_check;
	private String barcode;
	
	//Transient
	private String skucode;
	private String skuname;
	private String hscode;
	private String hsname;
	private String checked;
	private String unitname;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getStockinid_link() {
		return stockinid_link;
	}
	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}
	public Long getStockindid_link() {
		return stockindid_link;
	}
	public void setStockindid_link(Long stockindid_link) {
		this.stockindid_link = stockindid_link;
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
	public Long getSizeid_link() {
		return sizeid_link;
	}
	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
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
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Float getWidth_check() {
		return width_check;
	}
	public void setWidth_check(Float width_check) {
		this.width_check = width_check;
	}
	public Float getMet_origin() {
		return met_origin;
	}
	public void setMet_origin(Float met_origin) {
		this.met_origin = met_origin;
	}
	public Float getMet_check() {
		return met_check;
	}
	public void setMet_check(Float met_check) {
		this.met_check = met_check;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public String getSkuname() {
		return skuname;
	}
	public void setSkuname(String skuname) {
		this.skuname = skuname;
	}
	public String getHscode() {
		return hscode;
	}
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
	public String getHsname() {
		return hsname;
	}
	public void setHsname(String hsname) {
		this.hsname = hsname;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	
	
}
