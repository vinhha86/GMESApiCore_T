package vn.gpay.gsmart.core.api.pcontract_po;

import java.util.List;

import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
//import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.porder.POrder;

public class PContract_pocreate_request  {
	public PContract_PO data;
	public List<POrder> po_orders;
	public long pcontractid_link;
}
