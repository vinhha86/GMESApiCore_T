package vn.gpay.gsmart.core.fabric_price;

import java.io.Serializable;

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

import vn.gpay.gsmart.core.sku.SKU;

@Table(name="fabric_price")
@Entity
public class FabricPrice implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fob_price_generator")
	@SequenceGenerator(name="fob_price_generator", sequenceName = "fob_price_id_seq", allocationSize=1)
	protected Long id;
	
	private Long materialid_link;
	private Float price_per_kg;
	private Float m_per_kg;
	private Float price_per_m;
	private Long currencyid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
	private SKU material;
	
	@Transient
	public String getProducttype_name() {
		if(material != null) {
			if(material.getProducttype_name() != null) {
				return material.getProducttype_name();
			}
		}
		return "Khác";
	}
	
	@Transient
	public String getMaterialCode() {
		if(material != null) {
			if(material.getCode() != null) {
				return material.getCode();
			}
		}
		return "";
	}
	
	@Transient
	public String getColor_name() {
		if(material != null) {
			if(material.getColor_name() != null) {
				return material.getColor_name();
			}
		}
		return "";
	}
	
	@Transient
	public String getSize_name() {
		if(material != null) {
			if(material.getSize_name() != null) {
				return material.getSize_name();
			}
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMaterialid_link() {
		return materialid_link;
	}
	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}
	public Float getPrice_per_kg() {
		return price_per_kg;
	}
	public void setPrice_per_kg(Float price_per_kg) {
		this.price_per_kg = price_per_kg;
	}
	public Float getM_per_kg() {
		return m_per_kg;
	}
	public void setM_per_kg(Float m_per_kg) {
		this.m_per_kg = m_per_kg;
	}
	public Float getPrice_per_m() {
		return price_per_m;
	}
	public void setPrice_per_m(Float price_per_m) {
		this.price_per_m = price_per_m;
	}
	public Long getCurrencyid_link() {
		return currencyid_link;
	}
	public void setCurrencyid_link(Long currencyid_link) {
		this.currencyid_link = currencyid_link;
	}
}
