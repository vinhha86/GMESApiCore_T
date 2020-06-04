package vn.gpay.gsmart.core.api.porder;

import java.util.Date;

public class POrderFilterRequest{
	private String ordercode;
	private String orderstatus = "";
	private Long granttoorgid_link;
	private String collection;
	private String season;
	private Integer salaryyear;
	private Integer salarymonth;
	private Date processingdate_from;
	private Date processingdate_to;
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}

	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}
	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}
	public Integer getSalaryyear() {
		return salaryyear;
	}
	public void setSalaryyear(Integer salaryyear) {
		this.salaryyear = salaryyear;
	}
	public Integer getSalarymonth() {
		return salarymonth;
	}
	public void setSalarymonth(Integer salarymonth) {
		this.salarymonth = salarymonth;
	}
	public Date getProcessingdate_from() {
		return processingdate_from;
	}
	public void setProcessingdate_from(Date processingdate_from) {
		this.processingdate_from = processingdate_from;
	}
	public Date getProcessingdate_to() {
		return processingdate_to;
	}
	public void setProcessingdate_to(Date processingdate_to) {
		this.processingdate_to = processingdate_to;
	}
}
