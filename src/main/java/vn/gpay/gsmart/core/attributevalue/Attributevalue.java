package vn.gpay.gsmart.core.attributevalue;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="attribute_value")
@Entity
public class Attributevalue implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attributevalue_generator")
	@SequenceGenerator(name="attributevalue_generator", sequenceName = "attribute_value_id_seq", allocationSize=1)
	private Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "attributeid_link")
	private Long attributeid_link;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "datatype")
	private int datatype;
	
	@Column(name = "description")
	private String description;
		
	@Column(name = "usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name = "timecreate")
	private Date timecreate;
	
	private Boolean isdefault;	
	private Integer sortvalue;	
	

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

	public Long getAttributeid_link() {
		return attributeid_link;
	}

	public void setAttributeid_link(Long attributeid_link) {
		this.attributeid_link = attributeid_link;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getDatatype() {
		return datatype;
	}

	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public Integer getSortvalue() {
		return sortvalue;
	}

	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}

	
}
