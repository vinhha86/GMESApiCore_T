package vn.gpay.gsmart.core.api.gantt;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO_Gantt;

public class gantt_getbydate_porder_po_response extends ResponseBase {
	public List<PContract_PO_Gantt> children;
}
