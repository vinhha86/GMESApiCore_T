package vn.gpay.gsmart.core.porder;

import java.util.Date;

public class POrderOrigin {
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

}
