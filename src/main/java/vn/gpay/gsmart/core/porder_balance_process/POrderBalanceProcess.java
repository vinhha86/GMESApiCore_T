package vn.gpay.gsmart.core.porder_balance_process;

import java.io.Serializable;

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

import vn.gpay.gsmart.core.porder_balance.POrderBalance;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCost;

@Table(name="porders_balance_process")
@Entity
public class POrderBalanceProcess implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_balance_process_generator")
	@SequenceGenerator(name="porders_balance_process_generator", sequenceName = "porders_balance_process_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long porderbalanceid_link;
	private Long pordersewingcostid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderbalanceid_link",insertable=false,updatable =false)
	private POrderBalance porderBalance;
	
	@Transient
	public String getBalance_name() {
		if(porderBalance!=null)
			return porderBalance.getBalance_name();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pordersewingcostid_link",insertable=false,updatable =false)
	private POrderSewingCost porderSewingCost;
	
	@Transient
	public String getWorkingprocess_name() {
		if(porderSewingCost!=null)
			return porderSewingCost.getWorkingprocess_name();
		return "";
	}
	
	@Transient
	public Integer getTimespent_standard() {
		Integer time = 0;
		if(porderSewingCost!=null) {
			if(porderSewingCost.getTimespent_standard() != null)
				time = porderSewingCost.getTimespent_standard();
		}
		return time;
	}
	
	@Transient
	public String getDevice_name() {
		if(porderSewingCost!=null) {
			if(porderSewingCost.getDevice_name() != null)
				return porderSewingCost.getDevice_name();
		}
		return "";
	}
	
	@Transient
	public String getLaborlevel_name() {
		if(porderSewingCost!=null) {
			if(porderSewingCost.getLaborlevel_name() != null)
				return porderSewingCost.getLaborlevel_name();
		}
		return "";
	}
	
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
	public Long getPorderbalanceid_link() {
		return porderbalanceid_link;
	}
	public void setPorderbalanceid_link(Long porderbalanceid_link) {
		this.porderbalanceid_link = porderbalanceid_link;
	}
	public Long getPordersewingcostid_link() {
		return pordersewingcostid_link;
	}
	public void setPordersewingcostid_link(Long pordersewingcostid_link) {
		this.pordersewingcostid_link = pordersewingcostid_link;
	}
}
