package vn.gpay.gsmart.core.pcontract_po_shipping;

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

@Table(name="pcontract_po_shipping_d")
@Entity
public class PContract_PO_Shipping_D implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_shipping_d_generator")
	@SequenceGenerator(name="pcontract_po_shipping_d_generator", sequenceName = "pcontract_po_shipping_d_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontract_po_shippingid_link;
	private Long skuid_link;
	private Integer amount;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getSkucode() {
		if(sku != null) {
			return sku.getCode();
		}
		return "";
	}	
	@Transient
	public String getMauSanPham() {
		if(sku != null) {
			return sku.getMauSanPham();
		}
		return "";
	}	
	@Transient
	public String getCoSanPham() {
		if(sku != null) {
			return sku.getCoSanPham();
		}
		return "";
	}
	
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
	public Long getPcontract_po_shippingid_link() {
		return pcontract_po_shippingid_link;
	}
	public void setPcontract_po_shippingid_link(Long pcontract_po_shippingid_link) {
		this.pcontract_po_shippingid_link = pcontract_po_shippingid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
