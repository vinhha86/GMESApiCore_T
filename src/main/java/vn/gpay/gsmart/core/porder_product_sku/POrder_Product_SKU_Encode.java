package vn.gpay.gsmart.core.porder_product_sku;

public class POrder_Product_SKU_Encode {
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_total;
	private Integer pquantity_encode;
	private String color_name;
	private String size_name;
	private String skucode;
	private String skuname;
	private String product_code;
	private Integer porderyear;
	private String pordercode;
	
	
	
	
	
	public String getPordercode() {
		return pordercode;
	}
	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}
	public Integer getPorderyear() {
		return porderyear;
	}
	public void setPorderyear(Integer porderyear) {
		this.porderyear = porderyear;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSkuname() {
		return skuname;
	}
	public void setSkuname(String skuname) {
		this.skuname = skuname;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public String getColor_name() {
		return color_name;
	}
	public String getSize_name() {
		return size_name;
	}
	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}
	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Integer getPquantity_total() {
		return pquantity_total;
	}
	public Integer getPquantity_encode() {
		return pquantity_encode;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setPquantity_total(Integer pquantity_total) {
		this.pquantity_total = pquantity_total;
	}
	public void setPquantity_encode(Integer pquantity_encode) {
		this.pquantity_encode = pquantity_encode;
	}
	
	
}
