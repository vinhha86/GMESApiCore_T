package vn.gpay.gsmart.core.porder_balance_process;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBalanceProcessService extends Operations<POrderBalanceProcess>{
	public List<Long> getPOrderBalanceProcessIdByPorder(Long porderid_link);
	List<POrderBalanceProcess> getByPorderSewingcost(Long pordersewingcostid_link);
}
