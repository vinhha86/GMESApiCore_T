package vn.gpay.gsmart.core.timesheet_lunch;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
}
