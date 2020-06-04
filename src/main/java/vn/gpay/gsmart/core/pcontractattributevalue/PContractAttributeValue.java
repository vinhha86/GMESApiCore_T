package vn.gpay.gsmart.core.pcontractattributevalue;


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

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.attributevalue.Attributevalue;

@Table(name="pcontract_product_atribute_value")
@Entity
public class PContractAttributeValue implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_attvalue_generator")
	@SequenceGenerator(name="pcontract_attvalue_generator", sequenceName = "pcontract_product_atribute_value_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long productid_link;
	private Long attributeid_link;
	private Long attributevalueid_link;
	private Long pcontractid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="attributeid_link",insertable=false,updatable =false)
    private Attribute attribute;
	
	@Transient
	public String getAttributeName() {
		if(attribute != null)
			return attribute.getName();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="attributevalueid_link",insertable=false,updatable =false)
    private Attributevalue attributevalue;
	
	@Transient
	public String getAttributevalueName() {
		if(attributevalue != null)
			return attributevalue.getValue();
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

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
}
