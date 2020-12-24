package vn.gpay.gsmart.core.pcontract_price;

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

@Table(name="pcontract_price_d_sku")
@Entity
public class PContract_Price_D_SKU implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_price_d_sku_generator")
	@SequenceGenerator(name="pcontract_price_d_sku_generator", sequenceName = "pcontract_price_d_sku_id_seq", allocationSize=1)
	private Long id;
	
	private Long pcontractprice_d_id_link;
	private Long materialid_link;
	private Integer amount;
	private Float unitprice;
	private Float totalprice;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="materialid_link",insertable=false,updatable =false)
	private SKU material;
	
	@Transient
	public String getMaterialCode() {
		if(material != null) {
			if(material.getCode() != null) {
				return material.getCode();
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
	public Long getPcontractprice_d_id_link() {
		return pcontractprice_d_id_link;
	}
	public void setPcontractprice_d_id_link(Long pcontractprice_d_id_link) {
		this.pcontractprice_d_id_link = pcontractprice_d_id_link;
	}
	public Long getMaterialid_link() {
		return materialid_link;
	}
	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public Float getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Float totalprice) {
		this.totalprice = totalprice;
	}
}
