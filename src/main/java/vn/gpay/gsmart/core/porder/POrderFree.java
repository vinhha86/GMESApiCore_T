package vn.gpay.gsmart.core.porder;

import java.util.Date;

public class POrderFree {
	private Long id;
	private String po_buyer;
	private String po_vendor;
	private Integer totalorder;
	private Date shipdate;
	private Date matdate;
	private Date po_Productiondate;
	private Long pcontract_poid_link;
	private Long granttoorgid_link;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getTotalorder() {
		return totalorder;
	}
	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
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
	public Date getPo_Productiondate() {
		return po_Productiondate;
	}
	public void setPo_Productiondate(Date po_Productiondate) {
		this.po_Productiondate = po_Productiondate;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}
	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}
	
	
}
