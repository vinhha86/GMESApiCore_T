package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.Date;

public class Jitin_Invoice_PkList_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Long id;
    private Long orgrootid_link;
    private Long invoiceid_link;
    private Long invoicedid_link;
    private Long skuid_link;
    private Long colorid_link;
    private String lotnumber ;
    private Integer packageid;
	private Float ydsorigin;
	private Float ydscheck;
	private Float met_origin;
    private Float m3;
    private Float width;
    private Float netweight;
    private Float grossweight;
    private String epc;
	private Integer rssi;	
    private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	
	private String sizenumber;
	
	private String skucode;
	private String skuname;
	private String hscode;
	private String hsname;
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
	public Long getInvoiceid_link() {
		return invoiceid_link;
	}
	public void setInvoiceid_link(Long invoiceid_link) {
		this.invoiceid_link = invoiceid_link;
	}
	public Long getInvoicedid_link() {
		return invoicedid_link;
	}
	public void setInvoicedid_link(Long invoicedid_link) {
		this.invoicedid_link = invoicedid_link;
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
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
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
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
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
	public String getSizenumber() {
		return sizenumber;
	}
	public void setSizenumber(String sizenumber) {
		this.sizenumber = sizenumber;
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
	public Float getMet_origin() {
		return met_origin;
	}
	public void setMet_origin(Float met_origin) {
		this.met_origin = met_origin;
	}
	

}
