package vn.gpay.gsmart.core.pcontractproductsku;

public class PContractProductSKUBinding {
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long productid_link;
	private Long pcontract_poid_link;
	private Long skuid_link;
	private Integer pquantity_sample;//SL mau
	private Integer pquantity_porder;//SL don
	private Integer pquantity_total;//SL tong sx
	private Integer pquantity_granted;//SL da phan chuyen
	private Integer pquantity_ungranted;//SL chua phan chuyen
	private String skuName;
	private String skuCode;
	private Integer sortValue;
	private String mauSanPham;
	private String coSanPham;
	private Long sizeId;
	private Long colorId;
	private String tenSanPham;
	private String maSanPham;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
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
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Integer getSortValue() {
		return sortValue;
	}
	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
	public String getMauSanPham() {
		return mauSanPham;
	}
	public void setMauSanPham(String mauSanPham) {
		this.mauSanPham = mauSanPham;
	}
	public String getCoSanPham() {
		return coSanPham;
	}
	public void setCoSanPham(String coSanPham) {
		this.coSanPham = coSanPham;
	}
	public Long getSizeId() {
		return sizeId;
	}
	public void setSizeId(Long sizeId) {
		this.sizeId = sizeId;
	}
	public Long getColorId() {
		return colorId;
	}
	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	
}
