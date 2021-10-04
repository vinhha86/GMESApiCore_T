package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;

public class PContractPO_Shipping {
	private Long pcontract_poid_link;
//	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
	private Long productid_link;
	private Integer po_quantity;
	private Date shipdate;
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
	private String productbuyercode_parent;

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

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	public String getProductbuyercode_parent() {
		return productbuyercode_parent;
	}

	public void setProductbuyercode_parent(String productbuyercode_parent) {
		this.productbuyercode_parent = productbuyercode_parent;
	}

}
