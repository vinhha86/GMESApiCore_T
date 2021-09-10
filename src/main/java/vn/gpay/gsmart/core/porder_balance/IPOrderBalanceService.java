package vn.gpay.gsmart.core.porder_balance;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBalanceService  extends Operations<POrderBalance>{
	public List<POrderBalance> getByPorder(Long porderid_link);
	List<POrderBalance> getByPOrderAndPOrderSewingCost(Long porderid_link, Long pordersewingcostid_link);
	List<POrderBalance> getByBalanceName_POrder(String balance_name, Long porderid_link);
}
