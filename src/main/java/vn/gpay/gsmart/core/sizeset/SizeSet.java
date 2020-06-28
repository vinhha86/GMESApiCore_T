package vn.gpay.gsmart.core.sizeset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.sizesetattributevalue.SizeSetAttributeValue;

@Table(name="sizeset")
@Entity
public class SizeSet implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sizeset_generator")
	@SequenceGenerator(name="sizeset_generator", sequenceName = "sizeset_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private String name;
	private String comment;
	private Long usercreatedid_link;
	private Date timecreate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="sizesetid_link",insertable=false,updatable =false)
    private List<SizeSetAttributeValue> list;
	
	@Transient
	public String getAttrValues() {
		String values = "";
		if(list.size()!=0) {
			for(SizeSetAttributeValue sizeSetAttributeValue : list) {
				if(values.equals("")) {
					values+=sizeSetAttributeValue.getAttributeValueName();
				}else {
					values+=", " + sizeSetAttributeValue.getAttributeValueName();
				}
			}
		}
		return values;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
}
