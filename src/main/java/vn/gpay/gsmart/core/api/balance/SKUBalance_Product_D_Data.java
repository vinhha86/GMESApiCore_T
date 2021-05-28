package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;

public class SKUBalance_Product_D_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long p_skuid_link;
	private String p_sku_code;
	private String p_sku_name;
	private String p_sku_desc;
	private String p_sku_color;
	private String p_sku_size;
	private Integer p_amount;
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public String getP_sku_code() {
		return p_sku_code;
	}
	public void setP_sku_code(String p_sku_code) {
		this.p_sku_code = p_sku_code;
	}
	public String getP_sku_name() {
		return p_sku_name;
	}
	public void setP_sku_name(String p_sku_name) {
		this.p_sku_name = p_sku_name;
	}
	public String getP_sku_desc() {
		return p_sku_desc;
	}
	public void setP_sku_desc(String p_sku_desc) {
		this.p_sku_desc = p_sku_desc;
	}
	public Integer getP_amount() {
		return p_amount;
	}
	public void setP_amount(Integer p_amount) {
		this.p_amount = p_amount;
	}
	public String getP_sku_color() {
		return p_sku_color;
	}
	public void setP_sku_color(String p_sku_color) {
		this.p_sku_color = p_sku_color;
	}
	public String getP_sku_size() {
		return p_sku_size;
	}
	public void setP_sku_size(String p_sku_size) {
		this.p_sku_size = p_sku_size;
	}


}
