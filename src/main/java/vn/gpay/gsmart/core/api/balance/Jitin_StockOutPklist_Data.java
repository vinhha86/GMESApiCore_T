package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.Date;

public class Jitin_StockOutPklist_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orgrootid_link;
    private Long stockoutid_link;
    private Long stockoutdid_link;
    private Long skuid_link;
    private Integer skutypeid_link ;	
    private Long colorid_link;
    private Long unitid_link;
    private String lotnumber ;
    private Integer packageid;
	private Float ydsorigin;
    private Float ydsoriginold;		
	private Float ydscheck;
	private Float widthorigin;
    private Float ydscheckold;	
    private Float ydsprocessedold;		
    private Float widthoriginold;	
    private Float widthcheckold;	
    private Float widthprocessedold;		
    private Float totalerrorold;	
    private Float netweight;
    private Float grossweight;
    private String epc;
	private Integer rssi;		
	private Integer status;				
    private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private String extrainfo;
	private Float met_origin;
	private Float met_check;
	private Float met_processed;
	private Float widthcheck;

	//Transient
	private String color_name;
	private String skucode;
	private String skuname;
	private String hscode;
	private String hsname;
	private Integer checked;
	private String warning;
	private String unitname;
	private Integer warehousestatus;
	private String invoice;
	private Date ngayNhapKho;
	private String spaceString;
	private String stockinProductString;
	
	
	private Float met_remain;
	private Float yds_remain;
	private Float met_remain_and_check;
	private Float yds_remain_and_check;
	private String warehouseStatusString;
	private Date date_check;
	
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
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public Long getStockoutdid_link() {
		return stockoutdid_link;
	}
	public void setStockoutdid_link(Long stockoutdid_link) {
		this.stockoutdid_link = stockoutdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
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
	public Float getYdsoriginold() {
		return ydsoriginold;
	}
	public void setYdsoriginold(Float ydsoriginold) {
		this.ydsoriginold = ydsoriginold;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getWidthorigin() {
		return widthorigin;
	}
	public void setWidthorigin(Float widthorigin) {
		this.widthorigin = widthorigin;
	}
	public Float getYdscheckold() {
		return ydscheckold;
	}
	public void setYdscheckold(Float ydscheckold) {
		this.ydscheckold = ydscheckold;
	}
	public Float getYdsprocessedold() {
		return ydsprocessedold;
	}
	public void setYdsprocessedold(Float ydsprocessedold) {
		this.ydsprocessedold = ydsprocessedold;
	}
	public Float getWidthoriginold() {
		return widthoriginold;
	}
	public void setWidthoriginold(Float widthoriginold) {
		this.widthoriginold = widthoriginold;
	}
	public Float getWidthcheckold() {
		return widthcheckold;
	}
	public void setWidthcheckold(Float widthcheckold) {
		this.widthcheckold = widthcheckold;
	}
	public Float getWidthprocessedold() {
		return widthprocessedold;
	}
	public void setWidthprocessedold(Float widthprocessedold) {
		this.widthprocessedold = widthprocessedold;
	}
	public Float getTotalerrorold() {
		return totalerrorold;
	}
	public void setTotalerrorold(Float totalerrorold) {
		this.totalerrorold = totalerrorold;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
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
	public Float getMet_processed() {
		return met_processed;
	}
	public void setMet_processed(Float met_processed) {
		this.met_processed = met_processed;
	}
	public Float getWidthcheck() {
		return widthcheck;
	}
	public void setWidthcheck(Float widthcheck) {
		this.widthcheck = widthcheck;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
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
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public Integer getWarehousestatus() {
		return warehousestatus;
	}
	public void setWarehousestatus(Integer warehousestatus) {
		this.warehousestatus = warehousestatus;
	}
	public Float getMet_remain() {
		return met_remain;
	}
	public void setMet_remain(Float met_remain) {
		this.met_remain = met_remain;
	}
	public Float getYds_remain() {
		return yds_remain;
	}
	public void setYds_remain(Float yds_remain) {
		this.yds_remain = yds_remain;
	}
	public Float getMet_remain_and_check() {
		return met_remain_and_check;
	}
	public void setMet_remain_and_check(Float met_remain_and_check) {
		this.met_remain_and_check = met_remain_and_check;
	}
	public Float getYds_remain_and_check() {
		return yds_remain_and_check;
	}
	public void setYds_remain_and_check(Float yds_remain_and_check) {
		this.yds_remain_and_check = yds_remain_and_check;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Date getNgayNhapKho() {
		return ngayNhapKho;
	}
	public void setNgayNhapKho(Date ngayNhapKho) {
		this.ngayNhapKho = ngayNhapKho;
	}
	public String getSpaceString() {
		return spaceString;
	}
	public void setSpaceString(String spaceString) {
		this.spaceString = spaceString;
	}
	public String getStockinProductString() {
		return stockinProductString;
	}
	public void setStockinProductString(String stockinProductString) {
		this.stockinProductString = stockinProductString;
	}
	public String getWarehouseStatusString() {
		return warehouseStatusString;
	}
	public void setWarehouseStatusString(String warehouseStatusString) {
		this.warehouseStatusString = warehouseStatusString;
	}
	public Date getDate_check() {
		return date_check;
	}
	public void setDate_check(Date date_check) {
		this.date_check = date_check;
	}

}
