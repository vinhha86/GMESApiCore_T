package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Jitin_StockOutD_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orgrootid_link;
    private Long stockoutid_link;
    private String pordercode;
    private Date stockoutdate;	
    private Long skuid_link;
    private Long colorid_link;
    private Integer unitid_link;
    private Float totalorder_design;	
    private Float totalorder_tech;
    private Float widthorder;
    private Integer totalpackage;
    private String listpackage;	
    private Float totalydsorigin;
    private Integer totalpackagecheck;
    private Float totalydscheck;
    private Integer totalpackageprocessed;
    private Float totalydsprocessed;
    private Integer totalpackagestockout;
    private Float totalydsstockout;		
    private Float totalerror;		
    private Float unitprice;
    private Long p_skuid_link;
    private String extrainfo;	
    private Integer status;	
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Long sizeid_link;
	private Float totalmet_origin;
	private Float totalmet_check;
	private Float totalmet_processed;
	private Float totalmet_stockout;
	
	//Transient
	public String product_code;
	public String color_name;
	public String size_name;
	public String skucode;
	public String skuname;
	public String sku_product_code;
	public String sku_product_color;
	public String sku_product_desc;
	public String hscode;
	public String hsname;
	public String unit_name;
	
	private List<Jitin_StockOutPklist_Data>  stockout_packinglist  = new ArrayList<>();

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

	public String getPordercode() {
		return pordercode;
	}

	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}

	public Date getStockoutdate() {
		return stockoutdate;
	}

	public void setStockoutdate(Date stockoutdate) {
		this.stockoutdate = stockoutdate;
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

	public Integer getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Float getTotalorder_design() {
		return totalorder_design;
	}

	public void setTotalorder_design(Float totalorder_design) {
		this.totalorder_design = totalorder_design;
	}

	public Float getTotalorder_tech() {
		return totalorder_tech;
	}

	public void setTotalorder_tech(Float totalorder_tech) {
		this.totalorder_tech = totalorder_tech;
	}

	public Float getWidthorder() {
		return widthorder;
	}

	public void setWidthorder(Float widthorder) {
		this.widthorder = widthorder;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}

	public String getListpackage() {
		return listpackage;
	}

	public void setListpackage(String listpackage) {
		this.listpackage = listpackage;
	}

	public Float getTotalydsorigin() {
		return totalydsorigin;
	}

	public void setTotalydsorigin(Float totalydsorigin) {
		this.totalydsorigin = totalydsorigin;
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

	public Integer getTotalpackageprocessed() {
		return totalpackageprocessed;
	}

	public void setTotalpackageprocessed(Integer totalpackageprocessed) {
		this.totalpackageprocessed = totalpackageprocessed;
	}

	public Float getTotalydsprocessed() {
		return totalydsprocessed;
	}

	public void setTotalydsprocessed(Float totalydsprocessed) {
		this.totalydsprocessed = totalydsprocessed;
	}

	public Integer getTotalpackagestockout() {
		return totalpackagestockout;
	}

	public void setTotalpackagestockout(Integer totalpackagestockout) {
		this.totalpackagestockout = totalpackagestockout;
	}

	public Float getTotalydsstockout() {
		return totalydsstockout;
	}

	public void setTotalydsstockout(Float totalydsstockout) {
		this.totalydsstockout = totalydsstockout;
	}

	public Float getTotalerror() {
		return totalerror;
	}

	public void setTotalerror(Float totalerror) {
		this.totalerror = totalerror;
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

	public String getExtrainfo() {
		return extrainfo;
	}

	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getSizeid_link() {
		return sizeid_link;
	}

	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
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

	public Float getTotalmet_processed() {
		return totalmet_processed;
	}

	public void setTotalmet_processed(Float totalmet_processed) {
		this.totalmet_processed = totalmet_processed;
	}

	public Float getTotalmet_stockout() {
		return totalmet_stockout;
	}

	public void setTotalmet_stockout(Float totalmet_stockout) {
		this.totalmet_stockout = totalmet_stockout;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
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

	public String getSku_product_code() {
		return sku_product_code;
	}

	public void setSku_product_code(String sku_product_code) {
		this.sku_product_code = sku_product_code;
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

	public List<Jitin_StockOutPklist_Data> getStockout_packinglist() {
		return stockout_packinglist;
	}

	public void setStockout_packinglist(List<Jitin_StockOutPklist_Data> stockout_packinglist) {
		this.stockout_packinglist = stockout_packinglist;
	}
	
}
