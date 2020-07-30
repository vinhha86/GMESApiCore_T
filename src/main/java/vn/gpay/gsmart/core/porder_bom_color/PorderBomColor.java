package vn.gpay.gsmart.core.porder_bom_color;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="porder_bom_color")
@Entity
public class PorderBomColor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_bom_color_generator")
	@SequenceGenerator(name="porder_bom_color_generator", sequenceName = "porder_bom_color_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long porderid_link;
	private Long productid_link;
	private Long pcontractid_link;
	private Long colorid_link;
	private Long materialid_link;
	private Float amount;
	private String description;
	private Long createduserid_link;
	private Date createddate;
	private Long unitid_link;
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
	public Long getColorid_link() {
		return colorid_link;
	}
	public Long getMaterialid_link() {
		return materialid_link;
	}
	public Float getAmount() {
		return amount;
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
	public Long getUnitid_link() {
		return unitid_link;
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
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
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
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	
	
}
