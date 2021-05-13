package vn.gpay.gsmart.core.porders_poline;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_POLine_Service extends Operations<POrder_POLine> {
	List<Long> get_porderid_by_line(Long pcontract_poid_link);
}
