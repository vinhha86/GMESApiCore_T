package vn.gpay.gsmart.core.porderprocessing;

import java.io.Serializable;

public class TVSOrgStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long granttoorgid_link;
	
	private String orgname;
	
	private Integer orderamount=0;
	private Integer amountcutsum;
	private Integer amountinputsum;
	private Integer amountoutputsum;
	private float percentcomplete;
	
	private String orderlist;
	
	private Integer status;

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(String orderlist) {
		this.orderlist = orderlist;
	}

	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}

	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void addOrder(String order){
		if (null != orderlist)
			orderlist += "; " + order;
		else
			orderlist = order;
		
		orderamount++;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderamount() {
		return orderamount;
	}

	public void setOrderamount(Integer orderamount) {
		this.orderamount = orderamount;
	}

	public Integer getAmountcutsum() {
		return null == amountcutsum?0:amountcutsum;
	}

	public void setAmountcutsum(Integer amountcutsum) {
		this.amountcutsum = amountcutsum;
	}

	public Integer getAmountinputsum() {
		return null == amountinputsum?0:amountinputsum;
	}

	public void setAmountinputsum(Integer amountinputsum) {
		this.amountinputsum = amountinputsum;
	}

	public Integer getAmountoutputsum() {
		return null==amountoutputsum?0:amountoutputsum;
	}

	public void setAmountoutputsum(Integer amountoutputsum) {
		this.amountoutputsum = amountoutputsum;
	}

	public float getPercentcomplete() {
		if (amountcutsum != 0){
			percentcomplete = (amountoutputsum/amountcutsum)*100;
		} else percentcomplete = 0;
		return percentcomplete;
		
	}

	public void setPercentcomplete(float percentcomplete) {
		this.percentcomplete = percentcomplete;
	}
	
	
}
