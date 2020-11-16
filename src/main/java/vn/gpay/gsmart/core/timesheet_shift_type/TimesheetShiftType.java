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
	
	
}
