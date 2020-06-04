package vn.gpay.gsmart.core.pcontract_price;

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

import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="pcontract_price_attribute_value")
@Entity
public class PContract_Price_AttrValue implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_generator")
	@SequenceGenerator(name="pcontract_price_attribute_value_generator", sequenceName = "pcontract_price_attribute_value_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long pcontract_priceid_link;
	private Long attributeid_link;
	private Long attributevalueid_link;
	private Float price;
	private Float cost;
	private Boolean isfob;
	private Long usercreatedid_link;
	private Date datecreated;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Long getPcontract_priceid_link() {
		return pcontract_priceid_link;
	}

	public void setPcontract_priceid_link(Long pcontract_priceid_link) {
		this.pcontract_priceid_link = pcontract_priceid_link;
	}

	public Long getAttributeid_link() {
		return attributeid_link;
	}

	public void setAttributeid_link(Long attributeid_link) {
		this.attributeid_link = attributeid_link;
	}

	public Long getAttributevalueid_link() {
		return attributevalueid_link;
	}

	public void setAttributevalueid_link(Long attributevalueid_link) {
		this.attributevalueid_link = attributevalueid_link;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Boolean getIsfob() {
		return isfob;
	}

	public void setIsfob(Boolean isfob) {
		this.isfob = isfob;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

}
