package vn.gpay.gsmart.core.porder_bom_sku;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="porder_bom_sku")
@Entity
public class POrderBOMSKU implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_bom_sku_generator")
	@SequenceGenerator(name="porder_bom_sku_generator", sequenceName = "porder_bom_sku_id_seq", allocationSize=1)
	private Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "pcontractid_link")
	private Long pcontractid_link;
	
	@Column(name = "porderid_link")
	private Long porderid_link;
	
	@Column(name = "productid_link")
	private Long productid_link;
	
	@Column(name = "skuid_link")
	private Long skuid_link;
	
	@Column(name = "materialid_link")
	private Long materialid_link;
	
	@Column(name = "amount")
	private Float amount;
	
	@Column(name = "lost_ratio")
	private Float lost_ratio;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "createduserid_link")
	private Long createduserid_link;
	
	@Column(name = "createddate")
	private Date createddate;
	
	@Column(name = "productcolor_name")
	private String productcolor_name;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
    private SKU material;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;

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


	public Long getPorderid_link() {
		return porderid_link;
	}


	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
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


	public Long getMaterialid_link() {
		return materialid_link;
	}


	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}


	public Float getAmount() {
		return amount;
	}


	public void setAmount(Float amount) {
		this.amount = amount;
	}


	public Float getLost_ratio() {
		return lost_ratio;
	}


	public void setLost_ratio(Float lost_ratio) {
		this.lost_ratio = lost_ratio;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getCreateduserid_link() {
		return createduserid_link;
	}


	public void setCreateduserid_link(Long createduserid_link) {
		this.createduserid_link = createduserid_link;
	}


	public Date getCreateddate() {
		return createddate;
	}


	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}


	public String getProductcolor_name() {
		return productcolor_name;
	}


	public void setProductcolor_name(String productcolor_name) {
		this.productcolor_name = productcolor_name;
	}
	
	
}
