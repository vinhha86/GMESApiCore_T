package vn.gpay.gsmart.core.porder_grant_timesheet;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="porder_grant_timesheet")
@Entity
public class POrderGrantTimesheet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_timesheet_generator")
	@SequenceGenerator(name="porder_grant_timesheet_generator", sequenceName = "porder_grant_timesheet_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long pordergrantid_link;
	private Long porderbalanceid_link;
	private Long personnelid_link;
	private Date time_out;
	private Date time_in;
	
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
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Date getTime_out() {
		return time_out;
	}
	public void setTime_out(Date time_out) {
		this.time_out = time_out;
	}
	public Date getTime_in() {
		return time_in;
	}
	public void setTime_in(Date time_in) {
		this.time_in = time_in;
	}
	
	

}
