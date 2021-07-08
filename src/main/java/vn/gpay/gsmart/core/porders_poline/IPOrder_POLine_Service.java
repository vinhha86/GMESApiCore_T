package vn.gpay.gsmart.core.porders_poline;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
import vn.gpay.gsmart.core.porder.POrder;

public interface IPOrder_POLine_Service extends Operations<POrder_POLine> {
	List<Long> get_porderid_by_line(Long pcontract_poid_link);
	List<POrder> getporder_by_po(Long pcontract_poid_link);
	List<POrder_POLine> get_porderline_by_po(Long pcontract_poid_link);
}
