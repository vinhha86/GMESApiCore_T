package vn.gpay.gsmart.core.timesheet_shift_type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="timesheet_shift_type")
@Entity
public class TimesheetShiftType implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_shift_type_generator")
	@SequenceGenerator(name="timesheet_shift_type_generator", sequenceName = "timesheet_shift_type_id_seq", allocationSize=1)
	private Long id;
	private String name;
	private Integer from_hour;
	private Integer from_minute;
	private Integer to_hour;
	private Integer to_minute;
	private Boolean is_atnight;
	private Boolean is_default;
	private Integer lunch_minute;
	private Long orgid_link;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFrom_hour() {
		return from_hour;
	}
	public void setFrom_hour(Integer from_hour) {
		this.from_hour = from_hour;
	}
	public Integer getFrom_minute() {
		return from_minute;
	}
	public void setFrom_minute(Integer from_minute) {
		this.from_minute = from_minute;
	}
	public Integer getTo_hour() {
		return to_hour;
	}
	public void setTo_hour(Integer to_hour) {
		this.to_hour = to_hour;
	}
	public Integer getTo_minute() {
		return to_minute;
	}
	public void setTo_minute(Integer to_minute) {
		this.to_minute = to_minute;
	}
	public Boolean getIs_atnight() {
		return is_atnight;
	}
	public void setIs_atnight(Boolean is_atnight) {
		this.is_atnight = is_atnight;
	}
	public Boolean getIs_default() {
		return is_default;
	}
	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}
	public Integer getLunch_minute() {
		return lunch_minute;
	}
	public void setLunch_minute(Integer lunch_minute) {
		this.lunch_minute = lunch_minute;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	
	
}
