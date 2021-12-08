package vn.gpay.gsmart.core.porder_bom_sku;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "porder_bom_sku_loaiphoi")
@Entity
public class porder_bom_sku_loaiphoi implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_bom_sku_loaiphoi_generator")
	@SequenceGenerator(name = "porder_bom_sku_loaiphoi_generator", sequenceName = "porder_bom_sku_loaiphoi_id_seq", allocationSize = 1)
	private Long id;

	private Long pcontractid_link;

	private Long productid_link;

	private Long skuid_link;

	private Long material_skuid_link;

	private Float amount;

	private String loaiphoi;

	public Long getId() {
		return id;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}

	public Float getAmount() {
		return amount;
	}

	public String getLoaiphoi() {
		return loaiphoi;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public void setLoaiphoi(String loaiphoi) {
		this.loaiphoi = loaiphoi;
	}

}
