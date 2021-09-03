package vn.gpay.gsmart.core.salary;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Table(name="org_sal_basic")
@Entity
public class OrgSal_Basic implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sal_basic_generator")
	@SequenceGenerator(name="org_sal_basic_generator", sequenceName = "org_sal_basic_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long orgid_link;
	private Integer sal_basic;
	private Integer sal_min;
	private Integer workingdays;
	private Integer costpersecond;
	private Float overtime_normal;
	private Float overtime_weekend;
	private Float overtime_holiday;
	private Float overtime_night;
	private Date date_cal_sal;
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
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public Integer getSal_basic() {
		return sal_basic;
	}
	public void setSal_basic(Integer sal_basic) {
		this.sal_basic = sal_basic;
	}
	public Integer getSal_min() {
		return sal_min;
	}
	public void setSal_min(Integer sal_min) {
		this.sal_min = sal_min;
	}
	public Integer getWorkingdays() {
		return workingdays;
	}
	public void setWorkingdays(Integer workingdays) {
		this.workingdays = workingdays;
	}
	public Float getOvertime_normal() {
		return overtime_normal;
	}
	public void setOvertime_normal(Float overtime_normal) {
		this.overtime_normal = overtime_normal;
	}
	public Float getOvertime_weekend() {
		return overtime_weekend;
	}
	public void setOvertime_weekend(Float overtime_weekend) {
		this.overtime_weekend = overtime_weekend;
	}
	public Float getOvertime_holiday() {
		return overtime_holiday;
	}
	public void setOvertime_holiday(Float overtime_holiday) {
		this.overtime_holiday = overtime_holiday;
	}
	public Float getOvertime_night() {
		return overtime_night;
	}
	public void setOvertime_night(Float overtime_night) {
		this.overtime_night = overtime_night;
	}
	public Integer getCostpersecond() {
		return costpersecond;
	}
	public void setCostpersecond(Integer costpersecond) {
		this.costpersecond = costpersecond;
	}
	public Date getDate_cal_sal() {
		return date_cal_sal;
	}
	public void setDate_cal_sal(Date date_cal_sal) {
		this.date_cal_sal = date_cal_sal;
	}
	
}
