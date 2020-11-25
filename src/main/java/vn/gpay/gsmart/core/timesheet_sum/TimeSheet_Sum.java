package vn.gpay.gsmart.core.timesheet_sum;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="timesheet_sum")
@Entity
public class TimeSheet_Sum implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_sum_generator")
	@SequenceGenerator(name="timesheet_sum_generator", sequenceName = "timesheet_sum_id_seq", allocationSize=1)
	private Long id;
	private Long personnelid_link;
	private Integer year;
	private Integer month;
	private Date fromdate;
	private Date todate;
	private Integer sumcolid_link;
	private Integer sumcoltypeid_link;
	private Float sumvalue;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonnelid_link() {
		return personnelid_link;
	}
	public void setPersonnelid_link(Long personnelid_link) {
		this.personnelid_link = personnelid_link;
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
	public Date getFromdate() {
		return fromdate;
	}
	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}
	public Date getTodate() {
		return todate;
	}
	public void setTodate(Date todate) {
		this.todate = todate;
	}
	public Integer getSumcolid_link() {
		return sumcolid_link;
	}
	public void setSumcolid_link(Integer sumcolid_link) {
		this.sumcolid_link = sumcolid_link;
	}
	public Integer getSumcoltypeid_link() {
		return sumcoltypeid_link;
	}
	public void setSumcoltypeid_link(Integer sumcoltypeid_link) {
		this.sumcoltypeid_link = sumcoltypeid_link;
	}
	public Float getSumvalue() {
		return sumvalue;
	}
	public void setSumvalue(Float sumvalue) {
		this.sumvalue = sumvalue;
	}
	
	
}