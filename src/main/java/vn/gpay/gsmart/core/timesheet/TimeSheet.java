package vn.gpay.gsmart.core.timesheet;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="timesheet_inout")
@Entity
public class TimeSheet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_inout_generator")
	@SequenceGenerator(name="timesheet_inout_generator", sequenceName = "timesheet_inout_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long personnelid_link;
	private Long deviceid_link;
	private Date timerecorded;
	private Boolean ishand_record;
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
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	public Date getTimerecorded() {
		return timerecorded;
	}
	public void setTimerecorded(Date timerecorded) {
		this.timerecorded = timerecorded;
	}
	public Boolean getIshand_record() {
		return ishand_record;
	}
	public void setIshand_record(Boolean ishand_record) {
		this.ishand_record = ishand_record;
	}
	
	
}
