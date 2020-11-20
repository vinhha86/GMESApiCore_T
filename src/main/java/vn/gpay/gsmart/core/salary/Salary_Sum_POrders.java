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

import vn.gpay.gsmart.core.porder_grant.POrderGrant;

@Table(name="salary_sum_porders")
@Entity
public class Salary_Sum_POrders implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salary_sum_porders_generator")
	@SequenceGenerator(name="salary_sum_porders_generator", sequenceName = "salary_sum_porders_id_seq", allocationSize=1)
	private Long id;
	
	private Integer year;
	private Integer month;
	private Long porderid_link;
	private Long pordergrantid_link;
	private Long orgid_link;
	private Integer amountstocked;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pordergrantid_link",insertable=false,updatable =false)
    private POrderGrant pordergrant;

	@Transient
	public String getPordercode() {
		if(pordergrant != null) {
			return pordergrant.getpordercode();
		}
		return "";
	}
	@Transient
	public String getProductcode() {
		if(pordergrant != null) {
			return pordergrant.getProductcode();
		}
		return "";
	}
	@Transient
	public String getPo_buyer() {
		if(pordergrant != null) {
			return pordergrant.getPo_buyer();
		}
		return "";
	}
	@Transient
	public String getBuyername() {
		if(pordergrant != null) {
			return pordergrant.getBuyername();
		}
		return "";
	}
	@Transient
	public String getVendorname() {
		if(pordergrant != null) {
			return pordergrant.getVendorname();
		}
		return "";
	}
	@Transient
	public Date getStart_date_plan() {
		if(pordergrant != null) {
			return pordergrant.getStart_date_plan();
		}
		return null;
	}
	@Transient
	public Date getFinish_date_plan() {
		if(pordergrant != null) {
			return pordergrant.getFinish_date_plan();
		}
		return null;
	}
	@Transient
	public Integer getGrantamount(){
		if(pordergrant != null) {
			return pordergrant.getGrantamount();
		}
		return 0;
	}
	@Transient
	public Integer getStatus(){
		if(pordergrant != null) {
			return pordergrant.getStatus();
		}
		return 0;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}

	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}

	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Integer getAmountstocked() {
		return amountstocked;
	}

	public void setAmountstocked(Integer amountstocked) {
		this.amountstocked = amountstocked;
	}

	public POrderGrant getPordergrant() {
		return pordergrant;
	}

	public void setPordergrant(POrderGrant pordergrant) {
		this.pordergrant = pordergrant;
	}

}
