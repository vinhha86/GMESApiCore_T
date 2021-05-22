package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Jitin_Stockout_order_d_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long orgrootid_link;
	private Long stockoutorderid_link;
	private Long material_skuid_link;
	private Long colorid_link;
	private Long unitid_link;
	private Integer totalpackage;
	private Float totalyds;
	private Integer totalpackagecheck;
	private Float totalydscheck;
	private Float unitprice;
	private Long p_skuid_link;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Float totalmet;
	private Float totalmetcheck;
	private String data_spaces; //  danh sách khoang
	
	//Transient
	private String unitName;
	private String materialCode;
	private String materialName;
	private String tenMauNPL;
	private String coKho;
	private String skuname;
	private String skucode;
	private String sku_product_color;
	private String sku_product_desc;
	private String color_name;
	private String size_name;
	private String unitname;
	private List<Jitin_Stockout_order_pkl_Data>  stockout_order_pkl  = new ArrayList<Jitin_Stockout_order_pkl_Data>();
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
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}
	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
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
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
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
	public Float getTotalmet() {
		return totalmet;
	}
	public void setTotalmet(Float totalmet) {
		this.totalmet = totalmet;
	}
	public Float getTotalmetcheck() {
		return totalmetcheck;
	}
	public void setTotalmetcheck(Float totalmetcheck) {
		this.totalmetcheck = totalmetcheck;
	}
	public String getData_spaces() {
		return data_spaces;
	}
	public void setData_spaces(String data_spaces) {
		this.data_spaces = data_spaces;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getTenMauNPL() {
		return tenMauNPL;
	}
	public void setTenMauNPL(String tenMauNPL) {
		this.tenMauNPL = tenMauNPL;
	}
	public String getCoKho() {
		return coKho;
	}
	public void setCoKho(String coKho) {
		this.coKho = coKho;
	}
	public String getSkuname() {
		return skuname;
	}
	public void setSkuname(String skuname) {
		this.skuname = skuname;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public String getSku_product_color() {
		return sku_product_color;
	}
	public void setSku_product_color(String sku_product_color) {
		this.sku_product_color = sku_product_color;
	}
	public String getSku_product_desc() {
		return sku_product_desc;
	}
	public void setSku_product_desc(String sku_product_desc) {
		this.sku_product_desc = sku_product_desc;
	}
	public String getColor_name() {
		return color_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public String getSize_name() {
		return size_name;
	}
	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public List<Jitin_Stockout_order_pkl_Data> getStockout_order_pkl() {
		return stockout_order_pkl;
	}
	public void setStockout_order_pkl(List<Jitin_Stockout_order_pkl_Data> stockout_order_pkl) {
		this.stockout_order_pkl = stockout_order_pkl;
	}

	
}
