package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;

public class PContractPO_Shipping {
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
	private String po_vendor;
	private Long productid_link;
	private Integer po_quantity;
	private Long unitid_link;
	private Date shipdate;
	private Date matdate;
	private Float actual_quantity;
	private Date actual_shipdate;
	private Float price_cmp;
	private Float price_fob;
	private Float price_sweingtarget;
	private Float price_sweingfact;
	private Float price_add;
	private Float price_commission;
	private Float salaryfund;
	private Long currencyid_link;
	private Float exchangerate;
	private Date productiondate;
	private String packingnotice;
	private Long qcorgid_link;
	private String qcorgname;
	private Integer etm_from;
	private Integer etm_to;
	private Integer etm_avr;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private Integer productiondays;
	private Integer productiondays_ns;
	private Long orgmerchandiseid_link;
	private Long merchandiserid_link;
	private Long parentpoid_link;
	private Boolean is_tbd;
	private Float sewtarget_percent;
	private Long portfromid_link;
	private Long porttoid_link;
	private Boolean isauto_calculate;
	private Long shipmodeid_link;
	private Date date_importdata;
	private Integer plan_productivity;
	private Float plan_linerequired;
	private Integer po_typeid_link;
	private String comment;
	private String productbuyercode;
	private String portFrom;
	private String packing_method;
	private String shipmode_name;
	
	// cắt, vào chuyền, ra chuyền, đóng gói, hoàn thiện ,giao hàng
	private Integer amountcut;
	private Integer amountinputsum;
	private Integer amountoutputsum;
	private Integer amountpackstockedsum;
	private Integer amountpackedsum;
	private Integer amountgiaohang;
	
	public String getPacking_method() {
		return packing_method;
	}
	public void setPacking_method(String packing_method) {
		this.packing_method = packing_method;
	}
	public String getPortFrom() {
		return portFrom;
	}
	public void setPortFrom(String portFrom) {
		this.portFrom = portFrom;
	}
	public String getProductbuyercode() {
		return productbuyercode;
	}
	public void setProductbuyercode(String productbuyercode) {
		this.productbuyercode = productbuyercode;
	}
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
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPo_buyer() {
		return po_buyer;
	}
	public void setPo_buyer(String po_buyer) {
		this.po_buyer = po_buyer;
	}
	public String getPo_vendor() {
		return po_vendor;
	}
	public void setPo_vendor(String po_vendor) {
		this.po_vendor = po_vendor;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getPo_quantity() {
		return po_quantity;
	}
	public void setPo_quantity(Integer po_quantity) {
		this.po_quantity = po_quantity;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Date getShipdate() {
		return shipdate;
	}
	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
	}
	public Date getMatdate() {
		return matdate;
	}
	public void setMatdate(Date matdate) {
		this.matdate = matdate;
	}
	public Float getActual_quantity() {
		return actual_quantity;
	}
	public void setActual_quantity(Float actual_quantity) {
		this.actual_quantity = actual_quantity;
	}
	public Date getActual_shipdate() {
		return actual_shipdate;
	}
	public void setActual_shipdate(Date actual_shipdate) {
		this.actual_shipdate = actual_shipdate;
	}
	public Float getPrice_cmp() {
		return price_cmp;
	}
	public void setPrice_cmp(Float price_cmp) {
		this.price_cmp = price_cmp;
	}
	public Float getPrice_fob() {
		return price_fob;
	}
	public void setPrice_fob(Float price_fob) {
		this.price_fob = price_fob;
	}
	public Float getPrice_sweingtarget() {
		return price_sweingtarget;
	}
	public void setPrice_sweingtarget(Float price_sweingtarget) {
		this.price_sweingtarget = price_sweingtarget;
	}
	public Float getPrice_sweingfact() {
		return price_sweingfact;
	}
	public void setPrice_sweingfact(Float price_sweingfact) {
		this.price_sweingfact = price_sweingfact;
	}
	public Float getPrice_add() {
		return price_add;
	}
	public void setPrice_add(Float price_add) {
		this.price_add = price_add;
	}
	public Float getPrice_commission() {
		return price_commission;
	}
	public void setPrice_commission(Float price_commission) {
		this.price_commission = price_commission;
	}
	public Float getSalaryfund() {
		return salaryfund;
	}
	public void setSalaryfund(Float salaryfund) {
		this.salaryfund = salaryfund;
	}
	public Long getCurrencyid_link() {
		return currencyid_link;
	}
	public void setCurrencyid_link(Long currencyid_link) {
		this.currencyid_link = currencyid_link;
	}
	public Float getExchangerate() {
		return exchangerate;
	}
	public void setExchangerate(Float exchangerate) {
		this.exchangerate = exchangerate;
	}
	public Date getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}
	public String getPackingnotice() {
		return packingnotice;
	}
	public void setPackingnotice(String packingnotice) {
		this.packingnotice = packingnotice;
	}
	public Long getQcorgid_link() {
		return qcorgid_link;
	}
	public void setQcorgid_link(Long qcorgid_link) {
		this.qcorgid_link = qcorgid_link;
	}
	public String getQcorgname() {
		return qcorgname;
	}
	public void setQcorgname(String qcorgname) {
		this.qcorgname = qcorgname;
	}
	public Integer getEtm_from() {
		return etm_from;
	}
	public void setEtm_from(Integer etm_from) {
		this.etm_from = etm_from;
	}
	public Integer getEtm_to() {
		return etm_to;
	}
	public void setEtm_to(Integer etm_to) {
		this.etm_to = etm_to;
	}
	public Integer getEtm_avr() {
		return etm_avr;
	}
	public void setEtm_avr(Integer etm_avr) {
		this.etm_avr = etm_avr;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getProductiondays() {
		return productiondays;
	}
	public void setProductiondays(Integer productiondays) {
		this.productiondays = productiondays;
	}
	public Integer getProductiondays_ns() {
		return productiondays_ns;
	}
	public void setProductiondays_ns(Integer productiondays_ns) {
		this.productiondays_ns = productiondays_ns;
	}
	public Long getOrgmerchandiseid_link() {
		return orgmerchandiseid_link;
	}
	public void setOrgmerchandiseid_link(Long orgmerchandiseid_link) {
		this.orgmerchandiseid_link = orgmerchandiseid_link;
	}
	public Long getMerchandiserid_link() {
		return merchandiserid_link;
	}
	public void setMerchandiserid_link(Long merchandiserid_link) {
		this.merchandiserid_link = merchandiserid_link;
	}
	public Long getParentpoid_link() {
		return parentpoid_link;
	}
	public void setParentpoid_link(Long parentpoid_link) {
		this.parentpoid_link = parentpoid_link;
	}
	public Boolean getIs_tbd() {
		return is_tbd;
	}
	public void setIs_tbd(Boolean is_tbd) {
		this.is_tbd = is_tbd;
	}
	public Float getSewtarget_percent() {
		return sewtarget_percent;
	}
	public void setSewtarget_percent(Float sewtarget_percent) {
		this.sewtarget_percent = sewtarget_percent;
	}
	public Long getPortfromid_link() {
		return portfromid_link;
	}
	public void setPortfromid_link(Long portfromid_link) {
		this.portfromid_link = portfromid_link;
	}
	public Long getPorttoid_link() {
		return porttoid_link;
	}
	public void setPorttoid_link(Long porttoid_link) {
		this.porttoid_link = porttoid_link;
	}
	public Boolean getIsauto_calculate() {
		return isauto_calculate;
	}
	public void setIsauto_calculate(Boolean isauto_calculate) {
		this.isauto_calculate = isauto_calculate;
	}
	public Long getShipmodeid_link() {
		return shipmodeid_link;
	}
	public void setShipmodeid_link(Long shipmodeid_link) {
		this.shipmodeid_link = shipmodeid_link;
	}
	public Date getDate_importdata() {
		return date_importdata;
	}
	public void setDate_importdata(Date date_importdata) {
		this.date_importdata = date_importdata;
	}
	public Integer getPlan_productivity() {
		return plan_productivity;
	}
	public void setPlan_productivity(Integer plan_productivity) {
		this.plan_productivity = plan_productivity;
	}
	public Float getPlan_linerequired() {
		return plan_linerequired;
	}
	public void setPlan_linerequired(Float plan_linerequired) {
		this.plan_linerequired = plan_linerequired;
	}
	public Integer getPo_typeid_link() {
		return po_typeid_link;
	}
	public void setPo_typeid_link(Integer po_typeid_link) {
		this.po_typeid_link = po_typeid_link;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getShipmode_name() {
		return shipmode_name;
	}
	public void setShipmode_name(String shipmode_name) {
		this.shipmode_name = shipmode_name;
	}
	public Integer getAmountcut() {
		return amountcut;
	}
	public void setAmountcut(Integer amountcut) {
		this.amountcut = amountcut;
	}
	public Integer getAmountinputsum() {
		return amountinputsum;
	}
	public void setAmountinputsum(Integer amountinputsum) {
		this.amountinputsum = amountinputsum;
	}
	public Integer getAmountoutputsum() {
		return amountoutputsum;
	}
	public void setAmountoutputsum(Integer amountoutputsum) {
		this.amountoutputsum = amountoutputsum;
	}
	public Integer getAmountpackstockedsum() {
		return amountpackstockedsum;
	}
	public void setAmountpackstockedsum(Integer amountpackstockedsum) {
		this.amountpackstockedsum = amountpackstockedsum;
	}
	public Integer getAmountpackedsum() {
		return amountpackedsum;
	}
	public void setAmountpackedsum(Integer amountpackedsum) {
		this.amountpackedsum = amountpackedsum;
	}
	public Integer getAmountgiaohang() {
		return amountgiaohang;
	}
	public void setAmountgiaohang(Integer amountgiaohang) {
		this.amountgiaohang = amountgiaohang;
	}
	
}
