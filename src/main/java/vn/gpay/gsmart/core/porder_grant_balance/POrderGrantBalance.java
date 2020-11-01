package vn.gpay.gsmart.core.porder_grant_balance;

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

import vn.gpay.gsmart.core.personel.Personel;

@Table(name="porder_grant_balance")
@Entity
public class POrderGrantBalance implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_balance_generator")
	@SequenceGenerator(name="porder_grant_balance_generator", sequenceName = "porder_grant_balance_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long pordergrantid_link;
	private Long porderbalanceid_link;
	private Long deviceid_link;
	private Long personnelid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="personnelid_link",insertable=false,updatable =false)
    private Personel personnel;
	
	@Transient
	public String getPersonnelFullName() {
		if(personnel!=null) {
			if(personnel.getFullname() != null)
				return personnel.getFullname();
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
	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}
	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}
	public Long getPorderbalanceid_link() {
		return porderbalanceid_link;
	}
	public void setPorderbalanceid_link(Long porderbalanceid_link) {
		this.porderbalanceid_link = porderbalanceid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	
	
}
