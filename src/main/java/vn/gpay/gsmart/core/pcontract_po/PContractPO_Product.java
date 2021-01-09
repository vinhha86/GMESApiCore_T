package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;

public class PContractPO_Product {
	private Long pcontract_poid_link;
	private Long productid_link;
	private Long granttoorgid_link;
	private Date shipdate;
	private String buyername;
	private String vendorname;
	private String product_buyername;
	private String po_buyer;
	private Integer quantity;
	private String orgname;
	
	
	
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPo_buyer() {
		return po_buyer;
	}
	public void setPo_buyer(String po_buyer) {
		this.po_buyer = po_buyer;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getProduct_buyername() {
		return product_buyername;
	}
	public void setProduct_buyername(String product_buyername) {
		this.product_buyername = product_buyername;
	}
	public Date getShipdate() {
		return shipdate;
	}
	public void setShipdate(Date shipDate) {
		shipdate = shipDate;
	}
	public String getBuyername() {
		return buyername;
	}
	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}
	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}
	
	
}
