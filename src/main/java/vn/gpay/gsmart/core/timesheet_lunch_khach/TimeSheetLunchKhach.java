package vn.gpay.gsmart.core.timesheet_lunch_khach;

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

import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;

@Table(name = "timesheet_lunch_khach")
@Entity
public class TimeSheetLunchKhach implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_lunch_generator")
	@SequenceGenerator(name = "timesheet_lunch_generator", sequenceName = "timesheet_lunch_id_seq", allocationSize = 1)
	private Long id;
	private Long orgrootid_link;
	private Long orgid_link;
	private Long shifttype_orgid_link;
	private Integer status;
	private Long usercreatedid_link;
	private Date timecreated;
	private Date day;
	private Date time_approve;
	private Integer amount;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "shifttype_orgid_link", insertable = false, updatable = false)
	private TimesheetShiftTypeOrg shifttype;
	
	@Transient
	public Long getShifttypeid_link() {
		if(shifttype!=null) {
			return shifttype.getTimesheet_shift_type_id_link();
		}
		return null;
	}
	@Transient
	public TimesheetShiftTypeOrg shifttypeorg;

	@Transient
	public Integer getShift_from_hour() {
		if (shifttype != null)
			return shifttype.getFrom_hour();
		return null;
	}

	@Transient
	public Integer getShift_from_minute() {
		if (shifttype != null)
			return shifttype.getFrom_minute();
		return null;
	}

	@Transient
	public Integer getShift_to_hour() {
		if (shifttype != null)
			return shifttype.getTo_hour();
		return null;
	}

	@Transient
	public Integer getShift_to_minute() {
		if (shifttype != null)
			return shifttype.getTo_minute();
		return null;
	}

	@Transient
	public Boolean getShift_is_atnight() {
		if (shifttype != null)
			return shifttype.getIs_atnight();
		return null;
	}

	@Transient
	public Boolean getShift_is_default() {
		if (shifttype != null)
			return shifttype.getIs_default();
		return null;
	}

	@Transient
	public String getshifttype_name() {
		if (shifttype != null) {
			String from_hour = (shifttype.getFrom_hour() + "").length() < 2 ? "0" + shifttype.getFrom_hour()
					: shifttype.getFrom_hour() + "";
			String from_minute = (shifttype.getFrom_minute() + "").length() < 2 ? "0" + shifttype.getFrom_minute()
					: shifttype.getFrom_minute() + "";
			String to_hour = (shifttype.getTo_hour() + "").length() < 2 ? "0" + shifttype.getTo_hour()
					: shifttype.getTo_hour() + "";
			String to_minute = (shifttype.getTo_minute() + "").length() < 2 ? "0" + shifttype.getTo_minute()
					: shifttype.getTo_minute() + "";
			return shifttype.getName() + " " + from_hour + ":" + from_minute + "-" + to_hour + ":" + to_minute;
		} else if (shifttypeorg != null) {
			String from_hour = (shifttypeorg.getFrom_hour() + "").length() < 2 ? "0" + shifttypeorg.getFrom_hour()
					: shifttypeorg.getFrom_hour() + "";
			String from_minute = (shifttypeorg.getFrom_minute() + "").length() < 2 ? "0" + shifttypeorg.getFrom_minute()
					: shifttypeorg.getFrom_minute() + "";
			String to_hour = (shifttypeorg.getTo_hour() + "").length() < 2 ? "0" + shifttypeorg.getTo_hour()
					: shifttypeorg.getTo_hour() + "";
			String to_minute = (shifttypeorg.getTo_minute() + "").length() < 2 ? "0" + shifttypeorg.getTo_minute()
					: shifttypeorg.getTo_minute() + "";
			return shifttypeorg.getName() + " " + from_hour + ":" + from_minute + "-" + to_hour + ":" + to_minute;
		}
		return "";
	}

	@Transient
	public Integer getShift_lunch_minute() {
		if (shifttype != null)
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

	public Date getTime_approve() {
		return time_approve;
	}

	public void setTime_approve(Date time_approve) {
		this.time_approve = time_approve;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getShifttype_orgid_link() {
		return shifttype_orgid_link;
	}

	public void setShifttype_orgid_link(Long shifttype_orgid_link) {
		this.shifttype_orgid_link = shifttype_orgid_link;
	}
}
