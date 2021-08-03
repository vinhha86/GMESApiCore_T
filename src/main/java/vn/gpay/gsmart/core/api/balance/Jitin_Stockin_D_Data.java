package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
public class Jitin_Stockin_D_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orgrootid_link;
    private Long stockinid_link;
	private String skucode;
    private Long skuid_link;
	private Integer skutypeid_link;
	private Integer porder_year;
    private Long colorid_link;
	private Long sizeid_link;
    private Integer unitid_link;
    private Integer totalpackage;
    private Float totalydsorigin;
    private Float foc;
    private Integer totalpackagecheck;
    private Float totalydscheck;
    private Float unitprice;
    private Long p_skuid_link;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
    private Integer status;
	private Integer totalpackage_order;
    private Float netweight;
    private Float grossweight;
    private Float m3;
    private Float totalmet_origin;
    private Float totalmet_check;
	
    //Transient
    private String skuname;
    private String hscode;
    private String hsname;
    private String unit_name;
    private String color_name;
    private Long sku_product_id;
    private String sku_product_code;
    private String sku_product_desc;
    private String sku_product_color;
    private String size_name;
    private String stockinDLot;
    private Boolean isPklistInStore;

    private List<Jitin_Stockin_PkList_Data> stockin_packinglist = new ArrayList<>();
    @JsonIgnoreProperties
    private List<Jitin_StockinLot>  stockin_lot  = new ArrayList<Jitin_StockinLot>();

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

	public String getSkucode() {
		return skucode;
	}

	public void setSkucode(String skucode) {
		this.skucode = skucode;
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

	public Integer getPorder_year() {
		return porder_year;
	}

	public void setPorder_year(Integer porder_year) {
		this.porder_year = porder_year;
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

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}

	public Float getTotalydsorigin() {
		return totalydsorigin;
	}

	public void setTotalydsorigin(Float totalydsorigin) {
		this.totalydsorigin = totalydsorigin;
	}

	public Float getFoc() {
		return foc;
	}

	public void setFoc(Float foc) {
		this.foc = foc;
	}

	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}

	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}

	public Float getTotalydscheck() {
		return totalydscheck;
	}

	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Long getP_skuid_link() {
		return p_skuid_link;
	}

	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
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

	public Integer getTotalpackage_order() {
		return totalpackage_order;
	}

	public void setTotalpackage_order(Integer totalpackage_order) {
		this.totalpackage_order = totalpackage_order;
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

	public Float getTotalmet_origin() {
		return totalmet_origin;
	}

	public void setTotalmet_origin(Float totalmet_origin) {
		this.totalmet_origin = totalmet_origin;
	}

	public Float getTotalmet_check() {
		return totalmet_check;
	}

	public void setTotalmet_check(Float totalmet_check) {
		this.totalmet_check = totalmet_check;
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

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getColor_name() {
		return color_name;
	}

	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}

	public String getSku_product_code() {
		return sku_product_code;
	}

	public void setSku_product_code(String sku_product_code) {
		this.sku_product_code = sku_product_code;
	}

	public String getSize_name() {
		return size_name;
	}

	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}

	public List<Jitin_Stockin_PkList_Data> getStockin_packinglist() {
		return stockin_packinglist;
	}

	public void setStockin_packinglist(List<Jitin_Stockin_PkList_Data> stockin_packinglist) {
		this.stockin_packinglist = stockin_packinglist;
	}

	public Long getSku_product_id() {
		return sku_product_id;
	}

	public void setSku_product_id(Long sku_product_id) {
		this.sku_product_id = sku_product_id;
	}

	public String getSku_product_desc() {
		return sku_product_desc;
	}

	public void setSku_product_desc(String sku_product_desc) {
		this.sku_product_desc = sku_product_desc;
	}

	public String getStockinDLot() {
		return stockinDLot;
	}

	public void setStockinDLot(String stockinDLot) {
		this.stockinDLot = stockinDLot;
	}



	public List<Jitin_StockinLot> getStockin_lot() {
		return stockin_lot;
	}

	public void setStockin_lot(List<Jitin_StockinLot> stockin_lot) {
		this.stockin_lot = stockin_lot;
	}

	public String getSku_product_color() {
		return sku_product_color;
	}

	public void setSku_product_color(String sku_product_color) {
		this.sku_product_color = sku_product_color;
	}

	public Boolean getIsPklistInStore() {
		return isPklistInStore;
	}

	public void setIsPklistInStore(Boolean isPklistInStore) {
		this.isPklistInStore = isPklistInStore;
	}
    
}
