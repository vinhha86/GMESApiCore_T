package vn.gpay.gsmart.core.task_grant;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="task_grant")
@Entity
public class Task_Grant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_grant_generator")
	@SequenceGenerator(name="task_grant_generator", sequenceName = "task_grant_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long orgid_link;
	private Long tasktypeid_link;
	private Long buyerid_link;
	private Long vendorid_link;
	private Long userid_link;
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public Long getTasktypeid_link() {
		return tasktypeid_link;
	}
	public Long getBuyerid_link() {
		return buyerid_link;
	}
	public Long getVendorid_link() {
		return vendorid_link;
	}
	public Long getUserid_link() {
		return userid_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public void setTasktypeid_link(Long tasktypeid_link) {
		this.tasktypeid_link = tasktypeid_link;
	}
	public void setBuyerid_link(Long buyerid_link) {
		this.buyerid_link = buyerid_link;
	}
	public void setVendorid_link(Long vendorid_link) {
		this.vendorid_link = vendorid_link;
	}
	public void setUserid_link(Long userid_link) {
		this.userid_link = userid_link;
	}
	
	
}
