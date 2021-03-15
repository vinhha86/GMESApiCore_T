package vn.gpay.gsmart.core.pcontractproductsku;

public class POLineSKU {
	private Long pcontractid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_sample;//SL mau
	private Integer pquantity_porder;//SL don
	private Integer pquantity_total;//SL tong sx
	
	private Integer pquantity_lenhsx ;//SL da tao lenh sx

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

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Integer getPquantity_sample() {
		return pquantity_sample;
	}

	public void setPquantity_sample(Integer pquantity_sample) {
		this.pquantity_sample = pquantity_sample;
	}

	public Integer getPquantity_porder() {
		return pquantity_porder;
	}

	public void setPquantity_porder(Integer pquantity_porder) {
		this.pquantity_porder = pquantity_porder;
	}

	public Integer getPquantity_total() {
		return pquantity_total;
	}

	public void setPquantity_total(Integer pquantity_total) {
		this.pquantity_total = pquantity_total;
	}

	public Integer getPquantity_lenhsx() {
		return pquantity_lenhsx;
	}

	public void setPquantity_lenhsx(Integer pquantity_lenhsx) {
		this.pquantity_lenhsx = pquantity_lenhsx;
	}
	
	
}
