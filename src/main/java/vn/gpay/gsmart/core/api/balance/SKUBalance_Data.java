package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SKUBalance_Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long mat_skuid_link;
	private String mat_sku_code;
	private String mat_sku_name;
	private String mat_sku_desc;
	private Long mat_sku_color_id;
	
	private Integer mat_sku_product_total;
	
	private String mat_sku_color_name;
	private String mat_sku_size_name;
	private String mat_sku_unit_name;
	private String mat_sku_product_typename;
	private Integer mat_sku_product_typeid_link;
	
	private Float mat_sku_bom_amount;
	private Float mat_sku_bom_lostratio;
	private Float mat_sku_demand;
	private Float mat_sku_invoice;
	private Date mat_sku_invoice_date;
	private Float mat_sku_stockin;
	private Float mat_sku_stockout_order;
	private Float mat_sku_stockout;
	private Float mat_sku_dif;
	private Integer mat_sku_product;
	
	
	
	private List<SKUBalance_Product_D_Data> product_d = new ArrayList<SKUBalance_Product_D_Data>();
	
	public Long getMat_skuid_link() {
		return mat_skuid_link;
	}
	public void setMat_skuid_link(Long mat_skuid_link) {
		this.mat_skuid_link = mat_skuid_link;
	}
	public String getMat_sku_code() {
		return mat_sku_code;
	}
	public void setMat_sku_code(String mat_sku_code) {
		this.mat_sku_code = mat_sku_code;
	}
	public String getMat_sku_name() {
		return mat_sku_name;
	}
	public void setMat_sku_name(String mat_sku_name) {
		this.mat_sku_name = mat_sku_name;
	}
	public Long getMat_sku_color_id() {
		return mat_sku_color_id;
	}
	public void setMat_sku_color_id(Long mat_sku_color_id) {
		this.mat_sku_color_id = mat_sku_color_id;
	}
	public String getMat_sku_color_name() {
		return mat_sku_color_name;
	}
	public void setMat_sku_color_name(String mat_sku_color_name) {
		this.mat_sku_color_name = mat_sku_color_name;
	}
	public String getMat_sku_size_name() {
		return mat_sku_size_name;
	}
	public void setMat_sku_size_name(String mat_sku_size_name) {
		this.mat_sku_size_name = mat_sku_size_name;
	}
	public String getMat_sku_unit_name() {
		return mat_sku_unit_name;
	}
	public void setMat_sku_unit_name(String mat_sku_unit_name) {
		this.mat_sku_unit_name = mat_sku_unit_name;
	}
	public Float getMat_sku_bom_amount() {
		return mat_sku_bom_amount;
	}
	public void setMat_sku_bom_amount(Float mat_sku_bom_amount) {
		this.mat_sku_bom_amount = mat_sku_bom_amount;
	}
	public Float getMat_sku_bom_lostratio() {
		return mat_sku_bom_lostratio;
	}
	public void setMat_sku_bom_lostratio(Float mat_sku_bom_lostratio) {
		this.mat_sku_bom_lostratio = mat_sku_bom_lostratio;
	}
	public Float getMat_sku_demand() {
		return mat_sku_demand;
	}
	public void setMat_sku_demand(Float mat_sku_demand) {
		this.mat_sku_demand = mat_sku_demand;
	}
	public Float getMat_sku_invoice() {
		return mat_sku_invoice;
	}
	public void setMat_sku_invoice(Float mat_sku_invoice) {
		this.mat_sku_invoice = mat_sku_invoice;
	}
	public Date getMat_sku_invoice_date() {
		return mat_sku_invoice_date;
	}
	public void setMat_sku_invoice_date(Date mat_sku_invoice_date) {
		this.mat_sku_invoice_date = mat_sku_invoice_date;
	}
	public Float getMat_sku_stockin() {
		return mat_sku_stockin;
	}
	public void setMat_sku_stockin(Float mat_sku_stockin) {
		this.mat_sku_stockin = mat_sku_stockin;
	}
	public Float getMat_sku_stockout() {
		return mat_sku_stockout;
	}
	public void setMat_sku_stockout(Float mat_sku_stockout) {
		this.mat_sku_stockout = mat_sku_stockout;
	}
	public Float getMat_sku_dif() {
		return mat_sku_dif;
	}
	public void setMat_sku_dif(Float mat_sku_dif) {
		this.mat_sku_dif = mat_sku_dif;
	}
	public String getMat_sku_product_typename() {
		return mat_sku_product_typename;
	}
	public void setMat_sku_product_typename(String mat_sku_product_typename) {
		this.mat_sku_product_typename = mat_sku_product_typename;
	}
	public String getMat_sku_desc() {
		return mat_sku_desc;
	}
	public void setMat_sku_desc(String mat_sku_desc) {
		this.mat_sku_desc = mat_sku_desc;
	}
	public Integer getMat_sku_product_total() {
		return mat_sku_product_total;
	}
	public void setMat_sku_product_total(Integer mat_sku_product_total) {
		this.mat_sku_product_total = mat_sku_product_total;
	}
	public Float getMat_sku_stockout_order() {
		return mat_sku_stockout_order;
	}
	public void setMat_sku_stockout_order(Float mat_sku_stockout_order) {
		this.mat_sku_stockout_order = mat_sku_stockout_order;
	}
	public List<SKUBalance_Product_D_Data> getProduct_d() {
		return product_d;
	}
	public void setProduct_d(List<SKUBalance_Product_D_Data> product_d) {
		this.product_d = product_d;
	}
	public Integer getMat_sku_product() {
		return mat_sku_product;
	}
	public void setMat_sku_product(Integer mat_sku_product) {
		this.mat_sku_product = mat_sku_product;
	}
	public Integer getMat_sku_product_typeid_link() {
		return mat_sku_product_typeid_link;
	}
	public void setMat_sku_product_typeid_link(Integer mat_sku_product_typeid_link) {
		this.mat_sku_product_typeid_link = mat_sku_product_typeid_link;
	}

}
