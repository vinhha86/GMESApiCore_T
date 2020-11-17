package vn.gpay.gsmart.core.salary;

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

@Table(name="salary_sum")
@Entity
public class Salary_Sum implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_sum_generator")
	@SequenceGenerator(name="salary_sum_generator", sequenceName = "salary_sum_id_seq", allocationSize=1)
	private Long id;
	
	private Long personnelid_link;
	private Integer year;
	private Integer month;
	private Date fromdate;
	private Date todate;
	private Integer sumcolid_link;
	private Integer sumcoltypeid_link;
	private Float sumvalue;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sumcolid_link",insertable=false,updatable =false)
    private Salary_Sum_Col sumcol;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sumcoltypeid_link",insertable=false,updatable =false)
    private Salary_Sum_Col_Type sumcoltype;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="personnelid_link",insertable=false,updatable =false)
    private Personel personnel;
	
	@Transient
	public String getPersonel_fullname() {
		if(personnel != null) {
			return personnel.getFullname();
		}
		return "";
	}
	
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
