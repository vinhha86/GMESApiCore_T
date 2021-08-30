package vn.gpay.gsmart.core.porder;

import java.util.Date;

public class PorderOrigin {
	private Long id;
	private String ordercode;
	private String stylebuyer;
	private String po_buyer;
	private String buyername;
	private String vendorname;
	private Date startDatePlan;
	private Date golivedate;
	private Integer totalorder;
	private String statusName;
	private Long pcontractid_link;
	private Long productid_link;

	public Long getId() {
		return id;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public String getStylebuyer() {
		return stylebuyer;
	}

	public String getPo_buyer() {
		return po_buyer;
	}

	public String getBuyername() {
		return buyername;
	}

	public String getVendorname() {
		return vendorname;
	}

	public Date getStartDatePlan() {
		return startDatePlan;
	}

	public Date getGolivedate() {
		return golivedate;
	}

	public Integer getTotalorder() {
		return totalorder;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public void setStylebuyer(String stylebuyer) {
		this.stylebuyer = stylebuyer;
	}

	public void setPo_buyer(String po_buyer) {
		this.po_buyer = po_buyer;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public void setStartDatePlan(Date startDatePlan) {
		this.startDatePlan = startDatePlan;
	}

	public void setGolivedate(Date golivedate) {
		this.golivedate = golivedate;
	}

	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

}
