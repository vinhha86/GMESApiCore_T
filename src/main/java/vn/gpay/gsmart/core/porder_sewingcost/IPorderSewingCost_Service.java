package vn.gpay.gsmart.core.porder_sewingcost;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPorderSewingCost_Service extends Operations<POrderSewingCost> {
	List<POrderSewingCost> getby_porder_and_workingprocess(Long porderid_link, Long workingprocessid_link);
}