package vn.gpay.gsmart.core.pcontractproduct;

import java.util.Date;

public class PContractProductBinding {
	private Long id;
	private Long pcontractid_link;
	private Long productid_link;
	private Integer pquantity;
	private Integer amount;
	private Long orgrootid_link;
	private byte[] imgproduct;
	private String productName;
	private String productCode;
	private Date production_date;
	private Date delivery_date;
	private Float unitprice;
	private Integer producttypeid_link;
	
	public Integer getProducttypeid_link() {
		return producttypeid_link;
	}
	public void setProducttypeid_link(Integer producttypeid_link) {
		this.producttypeid_link = producttypeid_link;
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
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getPquantity() {
		return pquantity;
	}
	public void setPquantity(Integer pquantity) {
		this.pquantity = pquantity;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public byte[] getImgproduct() {
		return imgproduct;
	}
	public void setImgproduct(byte[] imgproduct) {
		this.imgproduct = imgproduct;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Date getProduction_date() {
		return production_date;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setProduction_date(Date production_date) {
		this.production_date = production_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
}
