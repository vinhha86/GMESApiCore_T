package vn.gpay.gsmart.core.pcontractproductsku;

public class POLineSKU {
	private Long pcontractid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_ungranted;//SL mau
	private Integer pquantity_granted;//SL don
	private Integer pquantity_production;
	private Integer pquantity_sample;
	private String mauSanPham;
	private String coSanPham;
	
	
	
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
	
	

	public Integer getPquantity_lenhsx() {
		return pquantity_lenhsx;
	}

	public void setPquantity_lenhsx(Integer pquantity_lenhsx) {
		this.pquantity_lenhsx = pquantity_lenhsx;
	}

	public String getMauSanPham() {
		return mauSanPham;
	}

	public String getCoSanPham() {
		return coSanPham;
	}

	public void setMauSanPham(String mauSanPham) {
		this.mauSanPham = mauSanPham;
	}

	public void setCoSanPham(String coSanPham) {
		this.coSanPham = coSanPham;
	}

	public Integer getPquantity_granted() {
		return pquantity_granted;
	}

	public void setPquantity_granted(Integer pquantity_granted) {
		this.pquantity_granted = pquantity_granted;
	}

	public Integer getPquantity_ungranted() {
		return pquantity_ungranted;
	}

	public void setPquantity_ungranted(Integer pquantity_ungranted) {
		this.pquantity_ungranted = pquantity_ungranted;
	}

	public Integer getPquantity_production() {
		return pquantity_production;
	}

	public void setPquantity_production(Integer pquantity_production) {
		this.pquantity_production = pquantity_production;
	}

	public Integer getPquantity_sample() {
		return pquantity_sample;
	}

	public void setPquantity_sample(Integer pquantity_sample) {
		this.pquantity_sample = pquantity_sample;
	}
	
	
}
