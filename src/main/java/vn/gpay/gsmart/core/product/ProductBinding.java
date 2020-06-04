package vn.gpay.gsmart.core.product;

public class ProductBinding {
	private byte[] urlimage;
	private String name;
	private String code;
	private String product_typeName;
	private Integer product_type;
	private String thanhPhanVai;
	private String coKho;
	private String tenMauNPL;
	private long id;
	private String designerName;
	public byte[] getUrlimage() {
		return urlimage;
	}
	public void setUrlimage(byte[] urlimage) {
		this.urlimage = urlimage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProduct_typeName() {
		return product_typeName;
	}
	public void setProduct_typeName(String product_typeName) {
		this.product_typeName = product_typeName;
	}
	public Integer getProduct_type() {
		return product_type;
	}
	public void setProduct_type(Integer product_type) {
		this.product_type = product_type;
	}
	public String getThanhPhanVai() {
		return thanhPhanVai;
	}
	public void setThanhPhanVai(String thanhPhanVai) {
		this.thanhPhanVai = thanhPhanVai;
	}
	public String getCoKho() {
		return coKho;
	}
	public void setCoKho(String coKho) {
		this.coKho = coKho;
	}
	public String getTenMauNPL() {
		return tenMauNPL;
	}
	public void setTenMauNPL(String tenMauNPL) {
		this.tenMauNPL = tenMauNPL;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDesignerName() {
		return designerName;
	}
	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}
}
