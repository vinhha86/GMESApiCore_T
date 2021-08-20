package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;

public class PContractPO_Shipping {
	private Long id;
//	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
//	private String po_vendor;
	private Long productid_link;
	private Integer po_quantity;
//	private Long unitid_link;
	private Date shipdate;
//	private Date matdate;
//	private Float actual_quantity;
//	private Date actual_shipdate;
//	private Float price_cmp;
//	private Float price_fob;
//	private Float price_sweingtarget;
//	private Float price_sweingfact;
//	private Float price_add;
//	private Float price_commission;
//	private Float salaryfund;
//	private Long currencyid_link;
//	private Float exchangerate;
//	private Date productiondate;
//	private String packingnotice;
//	private Long qcorgid_link;
//	private String qcorgname;
//	private Integer etm_from;
//	private Integer etm_to;
//	private Integer etm_avr;
//	private Long usercreatedid_link;
//	private Date datecreated;
//	private Integer status;
//	private Integer productiondays;
//	private Integer productiondays_ns;
//	private Long orgmerchandiseid_link;
//	private Long merchandiserid_link;
//	private Long parentpoid_link;
//	private Boolean is_tbd;
//	private Float sewtarget_percent;
//	private Long portfromid_link;
//	private Long porttoid_link;
//	private Boolean isauto_calculate;
//	private Long shipmodeid_link;
//	private Date date_importdata;
//	private Integer plan_productivity;
//	private Float plan_linerequired;
//	private Integer po_typeid_link;
//	private String comment;
	private String productbuyercode;
	private String portFrom;
	private String packing_method;
	private String shipmode_name;
	private Boolean ismap;
	private Integer amountcut;
	private Integer amountinputsum;
	private Integer amountoutputsum;
	private Integer amountpackstockedsum;
	private Integer amountpackedsum;
	private Integer amountstockedsum;
	private Integer amountgiaohang;
	private String ordercode;
	private Integer totalpair;

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

	public Date getShipdate() {
		return shipdate;
	}

	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
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

	public Boolean getIsmap() {
		return ismap;
	}

	public void setIsmap(Boolean ismap) {
		this.ismap = ismap;
	}

	public Integer getAmountstockedsum() {
		return amountstockedsum;
	}

	public void setAmountstockedsum(Integer amountstockedsum) {
		this.amountstockedsum = amountstockedsum;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Integer getTotalpair() {
		return totalpair;
	}

	public void setTotalpair(Integer totalpair) {
		this.totalpair = totalpair;
	}

}
