package vn.gpay.gsmart.core.sku;

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

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.attributevalue.Attributevalue;

@Table(name="sku_attribute_value")
@Entity
public class SKU_Attribute_Value implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skuattvalue_generator")
	@SequenceGenerator(name="skuattvalue_generator", sequenceName = "sku_attribute_value_id_seq", allocationSize=1)
	private Long id;
	
	private Long skuid_link;
	
	private Long attributevalueid_link;
	
	private Long attributeid_link;
	
	private String extvalue;
	
	private Long usercreateid_link;
	
	private Date timecreate;
	
	private Long Orgrootid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU skuName;
	
	@Transient
	public String getSkuName() {
		if(skuName!=null) {
			return skuName.getName();
		}
		return "";
	}
	
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
	public int getSort_Size() {
		if(attributevalue!=null)
			return attributevalue.getSortvalue();
		return 0;
	}
	@Transient
	public String getAttributeValueName() {
		if(attributevalue!=null) {
			return attributevalue.getValue();
		}
		return "";
	}
	
	@Transient
	public Boolean getIsdefaultvalue() {
		if(attributevalue != null)
			return attributevalue.getIsdefault();
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
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

	public String getExtvalue() {
		return extvalue;
	}

	public void setExtvalue(String extvalue) {
		this.extvalue = extvalue;
	}

	public Long getUsercreateid_link() {
		return usercreateid_link;
	}

	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}

	public Long getOrgrootid_link() {
		return Orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		Orgrootid_link = orgrootid_link;
	}

	public Attributevalue getAttributevalue() {
		return attributevalue;
	}

	public void setAttributevalue(Attributevalue attributevalue) {
		this.attributevalue = attributevalue;
	}
}
