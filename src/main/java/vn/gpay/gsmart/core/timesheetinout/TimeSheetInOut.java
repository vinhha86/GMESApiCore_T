package vn.gpay.gsmart.core.timesheetinout;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

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

@Table(name="timesheet_inout")
@Entity
public class TimeSheetInOut implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_inout_generator")
	@SequenceGenerator(name="timesheet_inout_generator", sequenceName = "timesheet_inout_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long deviceid_link;
	private Timestamp timerecorded;
	private Boolean ishand_record;
	private Long zoneid_link;
	private String register_code;
	private Integer year;
	private Integer  month;
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
	public Long getDeviceid_link() {
		return deviceid_link;
	}
	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
	}
	
	public Boolean getIshand_record() {
		return ishand_record;
	}
	public void setIshand_record(Boolean ishand_record) {
		this.ishand_record = ishand_record;
	}
	public Long getZoneid_link() {
		return zoneid_link;
	}
	public void setZoneid_link(Long zoneid_link) {
		this.zoneid_link = zoneid_link;
	}
	public String getRegister_code() {
		return register_code;
	}
	public void setRegister_code(String register_code) {
		this.register_code = register_code;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="register_code",insertable=false,updatable =false)
    private Personel personel;
	
	@Transient
	public String getcode() {
		if(personel!=null) {
			return personel.getCode();
		}
		return "";
	}
	@Transient
	public String getname() {
		if(personel!=null) {
			return personel.getFullname();
		}
		return "";
	}
	public Timestamp getTimerecorded() {
		return timerecorded;
	}
	public void setTimerecorded(Timestamp timerecorded) {
		this.timerecorded = timerecorded;
	}
}
