package vn.gpay.gsmart.core.timesheet_absence;

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

import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="timesheet_absence")
@Entity
public class TimesheetAbsence implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_absence_generator")
	@SequenceGenerator(name="timesheet_absence_generator", sequenceName = "timesheet_absence_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long personnelid_link;
	private Date absencedate_from;
	private Date absencedate_to;
	private String absence_reason;
	private Long absencetypeid_link;
	private Long usercreatedid_link;
	private Date timecreate;
	private Long userapproveid_link;
	private Date timeapprove;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="personnelid_link",insertable=false,updatable =false)
	private Personel personnel;
	
	@Transient
	public String getPersonnelOrgname() {
		if(personnel!=null) {
			if(personnel.getOrgname() != null)
				return personnel.getOrgname();
		}
		return "";
	}
	
	@Transient
	public String getPersonnelOrgManagename() {
		if(personnel!=null) {
			if(personnel.getOrgManageName() != null)
				return personnel.getOrgManageName();
		}
		return "";
	}
	
	@Transient
	public String getPersonnelCode() {
		if(personnel!=null) {
			if(personnel.getCode() != null)
				return personnel.getCode();
		}
		return "";
	}
	
	@Transient
	public String getPersonnelFullname() {
		if(personnel!=null) {
			if(personnel.getFullname() != null)
				return personnel.getFullname();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="absencetypeid_link",insertable=false,updatable =false)
	private TimesheetAbsenceType timesheetAbsenceType;
	
	@Transient
	public String getTimesheetAbsenceType() {
		if(timesheetAbsenceType!=null) {
			if(timesheetAbsenceType.getName() != null)
				return timesheetAbsenceType.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="userapproveid_link",insertable=false,updatable =false)
	private GpayUser userApprove;
	
	@Transient
	public String getUserApprove() {
		if(userApprove!=null) {
			if(userApprove.getFullname() != null)
				return userApprove.getFullname();
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
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
	}
	public Date getAbsencedate_from() {
		return absencedate_from;
	}
	public void setAbsencedate_from(Date absencedate_from) {
		this.absencedate_from = absencedate_from;
	}
	public Date getAbsencedate_to() {
		return absencedate_to;
	}
	public void setAbsencedate_to(Date absencedate_to) {
		this.absencedate_to = absencedate_to;
	}
	public String getAbsence_reason() {
		return absence_reason;
	}
	public void setAbsence_reason(String absence_reason) {
		this.absence_reason = absence_reason;
	}
	public Long getAbsencetypeid_link() {
		return absencetypeid_link;
	}
	public void setAbsencetypeid_link(Long absencetypeid_link) {
		this.absencetypeid_link = absencetypeid_link;
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
	public Long getUserapproveid_link() {
		return userapproveid_link;
	}
	public void setUserapproveid_link(Long userapproveid_link) {
		this.userapproveid_link = userapproveid_link;
	}
	public Date getTimeapprove() {
		return timeapprove;
	}
	public void setTimeapprove(Date timeapprove) {
		this.timeapprove = timeapprove;
	}
	
}
