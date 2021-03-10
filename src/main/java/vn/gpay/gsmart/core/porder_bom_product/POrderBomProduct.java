package vn.gpay.gsmart.core.porder_bom_product;

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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="porder_bom_product")
@Entity
public class POrderBomProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_bom_product_generator")
	@SequenceGenerator(name="porder_bom_product_generator", sequenceName = "porder_bom_product_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long porderid_link;
	private Long productid_link;
	private Long pcontractid_link;
	private Long materialid_link;
	private Long unitid_link;
	private Float amount;
	private Float lost_ratio;
	private String description;
	private Long createduserid_link;
	private Date createddate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getCoKho() {
		if(sku !=null) {
			return sku.getCoSanPham();
		}
		return "";
	}
	
	@Transient
	public String getMaterialName() {
		if(sku !=null) {
			return sku.getName();
		}
		return "";
	}
	
	@Transient
	public Long getColorid_link() {
		if(sku !=null) {
			return sku.getColorid_link();
		}
		return (long)0;
	}
	
	@Transient
	public String getMaterialCode() {
		if(sku !=null) {
			return sku.getCode();
		}
		return "";
	}
	
	@Transient
	public int getProduct_type() {
		if(sku != null) {
			return (int)sku.getProducttypeid_link();
		}
		return 0;
	}
	
	@Transient
	public String getProduct_typeName() {
		if(sku != null) {
			return sku.getProducttype_name();
		}
		return "";
	}
	
	@Transient
	public String getTenMauNPL() {
		if(sku !=null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	
	@Transient
	public String getThanhPhanVai() {
		if(sku !=null) {
			return sku.getThanhPhanVai();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getUnitName() {
		if(unit !=null) {
			return unit.getCode();
		}
		return "";
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
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public Long getMaterialid_link() {
		return materialid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public Float getAmount() {
		return amount;
	}
	public Float getLost_ratio() {
		return lost_ratio;
	}
	public String getDescription() {
		return description;
	}
	public Long getCreateduserid_link() {
		return createduserid_link;
	}
	public Date getCreateddate() {
		return createddate;
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
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public void setLost_ratio(Float lost_ratio) {
		this.lost_ratio = lost_ratio;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCreateduserid_link(Long createduserid_link) {
		this.createduserid_link = createduserid_link;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	
	
}
