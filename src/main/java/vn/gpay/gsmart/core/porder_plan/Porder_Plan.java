package vn.gpay.gsmart.core.porder_plan;

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

import vn.gpay.gsmart.core.porder.POrder;

@Table(name="porder_plan")
@Entity
public class Porder_Plan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_plan_generator")
	@SequenceGenerator(name="porder_plan_generator", sequenceName = "porder_plan_id_seq", allocationSize=1)
	private Long id;
	
	private Long porderid_link;
	private Date plan_date_start;
	private Date plan_date_end;
	private Long unitid_link;
	private Long skuid_link;
	private Float plan_amount;
	private String comment;
	private Long usercreatedid_link;
	private Date timecreate;
	private Integer plan_type;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	public Long getId() {
		return id;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Float getPlan_amount() {
		return plan_amount;
	}
	public String getComment() {
		return comment;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public Integer getPlan_type() {
		return plan_type;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setPlan_amount(Float plan_amount) {
		this.plan_amount = plan_amount;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public void setPlan_type(Integer plan_type) {
		this.plan_type = plan_type;
	}
	public Date getPlan_date_start() {
		return plan_date_start;
	}
	public void setPlan_date_start(Date plan_date_start) {
		this.plan_date_start = plan_date_start;
	}
	public Date getPlan_date_end() {
		return plan_date_end;
	}
	public void setPlan_date_end(Date plan_date_end) {
		this.plan_date_end = plan_date_end;
	}
	
	@Transient
	public String getPContract_code() {
		if(porder != null) {
			return porder.getContractcode();
		}
		return "";
	}
}
