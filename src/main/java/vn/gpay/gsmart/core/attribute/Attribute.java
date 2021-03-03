package vn.gpay.gsmart.core.attribute;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Table(name="attribute")
@Entity
public class Attribute implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attribute_generator")
	@SequenceGenerator(name="attribute_generator", sequenceName = "attribute_id_seq", allocationSize=1)
	private Long id;
	
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name ="name")
	private String name;
	
	@Column(name ="description")
	private String description;
	
	@Column(name ="categoryid_link")
	private Long categoryid_link;
	
	@Column(name ="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name ="isproduct")
	private Boolean isproduct;
	
	@Column(name ="ismaterial")
	private Boolean ismaterial;
	
	@Column(name ="issewingtrims")
	private Boolean issewingtrims;
	
	@Column(name ="ispackingtrims")
	private Boolean ispackingtrims;
	
	private Boolean isthread;
	
	private Integer sortvalue;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="attributeid_link", referencedColumnName="id")	
    private List<Attributevalue> attvalues = new ArrayList<Attributevalue>();
	
    
	public List<Attributevalue> getAttvalues() {
		return attvalues;
	}

	public void setAttvalues(List<Attributevalue> attvalues) {
		this.attvalues = attvalues;
	}

	public Boolean getIsthread() {
		return isthread;
	}

	public void setIsthread(Boolean isthread) {
		this.isthread = isthread;
	}

	//Danh sach cac value dc chon trong cau lenh tim kiem Sku
	@Transient
	private String selectedids;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryid_link() {
		return categoryid_link;
	}

	public void setCategoryid_link(Long categoryid_link) {
		this.categoryid_link = categoryid_link;
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

	public Boolean getIsproduct() {
		return isproduct;
	}

	public void setIsproduct(Boolean isproduct) {
		this.isproduct = isproduct;
	}

	public Boolean getIsmaterial() {
		return ismaterial;
	}

	public void setIsmaterial(Boolean ismaterial) {
		this.ismaterial = ismaterial;
	}

	public Boolean getIssewingtrims() {
		return issewingtrims;
	}

	public void setIssewingtrims(Boolean issewingtrims) {
		this.issewingtrims = issewingtrims;
	}

	public Boolean getIspackingtrims() {
		return ispackingtrims;
	}

	public void setIspackingtrims(Boolean ispackingtrims) {
		this.ispackingtrims = ispackingtrims;
	}

	public String getSelectedids() {
		return selectedids;
	}

	public void setSelectedids(String selectedids) {
		this.selectedids = selectedids;
	}

	public Integer getSortvalue() {
		return sortvalue;
	}

	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}
	
}
