package vn.gpay.gsmart.core.porder_grant_sku_plan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;

@Table(name="porder_grant_sku_plan")
@Entity
public class POrderGrant_SKU_Plan implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_sku_plan_generator")
	@SequenceGenerator(name="porder_grant_sku_plan_generator", sequenceName = "porder_grant_sku_plan_id_seq", allocationSize=1)
	protected Long id;

	private Long porder_grant_skuid_link;
	private Date date;
	private Integer amount;
	
	@Transient
	public String skuCode;
	@Transient
	public String mauSanPham;
	@Transient
	public String coSanPham;
	@Transient
	public Integer porderGrant_SKU_grantamount;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porder_grant_skuid_link",insertable=false,updatable =false)
	private POrderGrant_SKU porderGrant_SKU;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPorder_grant_skuid_link() {
		return porder_grant_skuid_link;
	}
	public void setPorder_grant_skuid_link(Long porder_grant_skuid_link) {
		this.porder_grant_skuid_link = porder_grant_skuid_link;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
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
	public Integer getPorderGrant_SKU_grantamount() {
		return porderGrant_SKU_grantamount;
	}
	public void setPorderGrant_SKU_grantamount(Integer porderGrant_SKU_grantamount) {
		this.porderGrant_SKU_grantamount = porderGrant_SKU_grantamount;
	}
	
}
