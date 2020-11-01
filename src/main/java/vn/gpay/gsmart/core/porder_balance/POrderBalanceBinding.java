package vn.gpay.gsmart.core.porder_balance;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.porder_balance_process.POrderBalanceProcess;

public class POrderBalanceBinding {
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private String balance_name;
	private Long prevbalanceid_link;
	private Long parentbalanceid_link;
	private Integer sortvalue;
	
	private String workingprocess_name;
	private Integer timespent_standard;
	
	private Long personnelId;
	private String personnelFullName;
	
	private List<POrderBalanceProcess> porderBalanceProcesses = new ArrayList<POrderBalanceProcess>();
	
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
	public String getPersonnelFullName() {
		return personnelFullName;
	}
	public void setPersonnelFullName(String personelFullName) {
		this.personnelFullName = personelFullName;
	}
	public Long getPersonnelId() {
		return personnelId;
	}
	public void setPersonnelId(Long personelId) {
		this.personnelId = personelId;
	}
	public String getWorkingprocess_name() {
		return workingprocess_name;
	}
	public void setWorkingprocess_name(String workingprocess_name) {
		this.workingprocess_name = workingprocess_name;
	}
	public Integer getTimespent_standard() {
		return timespent_standard;
	}
	public void setTimespent_standard(Integer timespent_standard) {
		this.timespent_standard = timespent_standard;
	}
	public List<POrderBalanceProcess> getPorderBalanceProcesses() {
		return porderBalanceProcesses;
	}
	public void setPorderBalanceProcesses(List<POrderBalanceProcess> porderBalanceProcesses) {
		this.porderBalanceProcesses = porderBalanceProcesses;
	}
	
}
