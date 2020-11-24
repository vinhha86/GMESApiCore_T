package vn.gpay.gsmart.core.porder_sewingcost;

public class POrderSewingCostBinding {
	private Long id;
	private String workingprocess_name;
	private Integer amount_complete;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkingprocess_name() {
		return workingprocess_name;
	}
	public void setWorkingprocess_name(String workingprocess_name) {
		this.workingprocess_name = workingprocess_name;
	}
	public Integer getAmount_complete() {
		return amount_complete;
	}
	public void setAmount_complete(Integer amount_complete) {
		this.amount_complete = amount_complete;
	}
	
}
