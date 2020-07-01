package vn.gpay.gsmart.core.holiday;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="holiday")
@Entity
public class Holiday implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holiday_generator")
	@SequenceGenerator(name="holiday_generator", sequenceName = "holiday_id_seq", allocationSize=1)
	protected Long id;
	
	private Integer year;
	private Date day;
	private String comment;
	private Long orgrootid_link;
	
	
	public Long getId() {
		return id;
	}
	public Integer getYear() {
		return year;
	}
	public Date getDay() {
		return day;
	}
	public String getComment() {
		return comment;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	
	
}
