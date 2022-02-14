package vn.gpay.gsmart.core.timesheet_lunch;

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
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;

@Table(name="timesheet_lunch")
@Entity
public class TimeSheetLunch implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_lunch_generator")
	@SequenceGenerator(name="timesheet_lunch_generator", sequenceName = "timesheet_lunch_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long personnelid_link;
	private Integer shifttypeid_link;
	private Integer status;
	private Long usercreatedid_link;
	private Date timecreated;
	private Date workingdate;
	private boolean isworking;
	private boolean islunch;
	private Date time_approve;
	private String comment;
	private Boolean is_bo_sung;
	private Long orgid_link;
	private Long orgmanagerid_link;
	private Boolean is_nolunch;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="shifttypeid_link",insertable=false,updatable =false)
    private TimesheetShiftTypeOrg shifttype;
	
	
	@Transient
	public Integer getShift_from_hour() {
		if(shifttype != null)
			return shifttype.getFrom_hour();
		return null;
	}
	@Transient
	public Integer getShift_from_minute() {
		if(shifttype != null)
			return shifttype.getFrom_minute();
		return null;
	}
	@Transient
	public Integer getShift_to_hour() {
		if(shifttype != null)
			return shifttype.getTo_hour();
		return null;
	}
	@Transient
	public Integer getShift_to_minute() {
		if(shifttype != null)
			return shifttype.getTo_minute();
		return null;
	}
	@Transient
	public Boolean getShift_is_atnight() {
		if(shifttype != null)
			return shifttype.getIs_atnight();
		return null;
	}
	@Transient
	public Boolean getShift_is_default() {
		if(shifttype != null)
			return shifttype.getIs_default();
		return null;
	}
	@Transient
	public Integer getShift_lunch_minute() {
		if(shifttype != null)
			return shifttype.getLunch_minute();
		return null;
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
	public Integer getShifttypeid_link() {
		return shifttypeid_link;
	}
	public void setShifttypeid_link(Integer shifttypeid_link) {
		this.shifttypeid_link = shifttypeid_link;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}
	public Date getWorkingdate() {
		return workingdate;
	}
	public void setWorkingdate(Date workingdate) {
		this.workingdate = workingdate;
	}
	public boolean isIsworking() {
		return isworking;
	}
	public void setIsworking(boolean isworking) {
		this.isworking = isworking;
	}
	public boolean isIslunch() {
		return islunch;
	}
	public void setIslunch(boolean islunch) {
		this.islunch = islunch;
	}
	public Date getTime_approve() {
		return time_approve;
	}
	public void setTime_approve(Date time_approve) {
		this.time_approve = time_approve;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Boolean getIs_bo_sung() {
		return is_bo_sung;
	}
	public void setIs_bo_sung(Boolean is_bo_sung) {
		this.is_bo_sung = is_bo_sung;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public Long getOrgmanagerid_link() {
		return orgmanagerid_link;
	}
	public void setOrgmanagerid_link(Long orgmanagerid_link) {
		this.orgmanagerid_link = orgmanagerid_link;
	}
	public Boolean getIs_nolunch() {
		return is_nolunch == null ? false : is_nolunch;
	}
	public void setIs_nolunch(Boolean is_nolunch) {
		this.is_nolunch = is_nolunch;
	}
	
	
}
