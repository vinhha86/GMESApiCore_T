package vn.gpay.gsmart.core.cutplan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="cutplan")
@Entity
public class CutPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cutplan_generator")
	@SequenceGenerator(name="cutplan_generator", sequenceName = "cutplan_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private Long skuid_link;
	private Long cutplanrowid_link;
	private Long createduserid_link;
	private Date createddate;
	
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Long getCutplanrowid_link() {
		return cutplanrowid_link;
	}
	public Long getCreateduserid_link() {
		return createduserid_link;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setCutplanrowid_link(Long cutplanrowid_link) {
		this.cutplanrowid_link = cutplanrowid_link;
	}
	public void setCreateduserid_link(Long createduserid_link) {
		this.createduserid_link = createduserid_link;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	
	
}
