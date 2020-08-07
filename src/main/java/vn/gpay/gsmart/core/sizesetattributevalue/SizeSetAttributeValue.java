package vn.gpay.gsmart.core.sizesetattributevalue;

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

@Table(name="sizeset_attrvalue")
@Entity
public class SizeSetAttributeValue implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sizeset_attrvalue_generator")
	@SequenceGenerator(name="sizeset_attrvalue_generator", sequenceName = "sizeset_attrvalue_id_seq", allocationSize=1)
	private Long id;
	
	@Transient
	private Boolean isDefault;
	
	private Long sizesetid_link;
	
	private Long attributevalueid_link;
	
	private Long attributeid_link;
	
	private Long orgrootid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="attributeid_link",insertable=false,updatable =false)
    private Attribute attribute;
	
	@Transient
	public String getAttributeName() {
		if(attribute!=null) {
			return attribute.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="attributevalueid_link",insertable=false,updatable =false)
    private Attributevalue attributevalue;
	
	@Transient
	public Boolean getIsdefaultvalue() {
		if(attributevalue!=null) {
			return attributevalue.getIsdefault();
		}
		return true;
	}
	
	@Transient
	public String getAttributeValueName() {
		if(attributevalue!=null) {
			if(attributevalue.getAttributeid_link() == attributeid_link)
			 return attributevalue.getValue();
		}
		return "";
	}
	
	@Transient
	public Integer getAttributeValueSortValue() {
		Integer sortVal = 0;
		if(attributevalue!=null) {
			return attributevalue.getSortvalue();
		}
		return sortVal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Long getSizesetid_link() {
		return sizesetid_link;
	}

	public void setSizesetid_link(Long sizesetid_link) {
		this.sizesetid_link = sizesetid_link;
	}

	public Long getAttributevalueid_link() {
		return attributevalueid_link;
	}

	public void setAttributevalueid_link(Long attributevalueid_link) {
		this.attributevalueid_link = attributevalueid_link;
	}

	public Long getAttributeid_link() {
		return attributeid_link;
	}

	public void setAttributeid_link(Long attributeid_link) {
		this.attributeid_link = attributeid_link;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
	public Attributevalue getAttributevalue() {
		return attributevalue;
	}
	
	
}
