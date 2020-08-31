package vn.gpay.gsmart.core.api.pcontract_po;

import java.util.List;

import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.porder_req.POrder_Req;

public class PContract_pocreate_request  {
	public PContract_PO data;
	public List<POrder_Req> po_orders;
	public long pcontractid_link;
}
