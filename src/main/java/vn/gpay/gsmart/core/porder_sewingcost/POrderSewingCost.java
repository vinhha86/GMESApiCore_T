package vn.gpay.gsmart.core.porder_sewingcost;

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

import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

@Table(name="porders_sewingcost")
@Entity
public class POrderSewingCost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_sewingcost_generator")
	@SequenceGenerator(name="porders_sewingcost_generator", sequenceName = "porders_sewingcost_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private Long workingprocessid_link;
	private Float cost;
	private Integer amount;
	private Float totalcost;
	private Long usercreatedid_link;
	private Date datecreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="workingprocessid_link",insertable=false,updatable =false)
    private WorkingProcess workingprocess;
	
	@Transient
	public String getWorkingprocess_name() {
		if(workingprocess != null) {
			return workingprocess.getName();
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public Long getWorkingprocessid_link() {
		return workingprocessid_link;
	}
	public Float getCost() {
		return cost;
	}
	public Integer getAmount() {
		return amount;
	}
	public Float getTotalcost() {
		return totalcost;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public void setWorkingprocessid_link(Long workingprocessid_link) {
		this.workingprocessid_link = workingprocessid_link;
	}
	public void setCost(Float cost) {
		this.cost = cost;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public void setTotalcost(Float totalcost) {
		this.totalcost = totalcost;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	
	
}
