package vn.gpay.gsmart.core.porder_balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.porder_balance_process.POrderBalanceProcess;

@Table(name="porders_balance")
@Entity
public class POrderBalance implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_balance_generator")
	@SequenceGenerator(name="porders_balance_generator", sequenceName = "porders_sewingbalance_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long porderid_link;
	private String balance_name;
	private Long prevbalanceid_link;
	private Long parentbalanceid_link;
	private Integer sortvalue;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="porderbalanceid_link",insertable=false,updatable =false)
    private List<POrderBalanceProcess> porderBalanceProcesses = new ArrayList<POrderBalanceProcess>();
	
	@Transient
	public String getWorkingprocess_name() {
		String name = "";
		for(POrderBalanceProcess porderBalanceProcess : porderBalanceProcesses) {
			if(porderBalanceProcess.getWorkingprocess_name() != null) {
				if(name.equals("")) {
					name += porderBalanceProcess.getWorkingprocess_name();
				}else {
					name += "/ " + porderBalanceProcess.getWorkingprocess_name();
				}
			}
		}
		return name;
	}
	
	@Transient
	public Integer getTimespent_standard() {
		int time = 0;
		for(POrderBalanceProcess porderBalanceProcess : porderBalanceProcesses) {
			if(porderBalanceProcess.getTimespent_standard() != null) {
				time += porderBalanceProcess.getTimespent_standard();
			}
		}
		return time;
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
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getBalance_name() {
		return balance_name;
	}
	public void setBalance_name(String balance_name) {
		this.balance_name = balance_name;
	}
	public Long getPrevbalanceid_link() {
		return prevbalanceid_link;
	}
	public void setPrevbalanceid_link(Long prevbalanceid_link) {
		this.prevbalanceid_link = prevbalanceid_link;
	}
	public Long getParentbalanceid_link() {
		return parentbalanceid_link;
	}
	public void setParentbalanceid_link(Long parentbalanceid_link) {
		this.parentbalanceid_link = parentbalanceid_link;
	}
	public Integer getSortvalue() {
		return sortvalue;
	}
	public void setSortvalue(Integer sortvalue) {
		this.sortvalue = sortvalue;
	}
	public List<POrderBalanceProcess> getPorderBalanceProcesses() {
		return porderBalanceProcesses;
	}
	public void setPorderBalanceProcesses(List<POrderBalanceProcess> porderBalanceProcesses) {
		this.porderBalanceProcesses = porderBalanceProcesses;
	}
	
	
}
