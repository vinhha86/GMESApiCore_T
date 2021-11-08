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
public class TimesheetShiftType implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_shift_type_generator")
	@SequenceGenerator(name="timesheet_shift_type_generator", sequenceName = "timesheet_shift_type_id_seq", allocationSize=1)
	private Long id;
	private String name;
	private String code;
	private Boolean is_ca_an;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getIs_ca_an() {
		return is_ca_an;
	}
	public void setIs_ca_an(Boolean is_ca_an) {
		this.is_ca_an = is_ca_an;
	}
	
}
