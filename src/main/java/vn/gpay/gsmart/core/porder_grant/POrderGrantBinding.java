package vn.gpay.gsmart.core.porder_grant;

import java.util.Date;

public class POrderGrantBinding {
	private Long pordergrantid_link;
	private Date date;
	private Integer amount;
	private Boolean is_ordered;
	
	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}
	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Boolean getIs_ordered() {
		return is_ordered;
	}
	public void setIs_ordered(Boolean is_ordered) {
		this.is_ordered = is_ordered;
	}
}
