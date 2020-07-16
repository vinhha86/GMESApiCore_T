package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.POrder;

public class POrderList_getlist_response extends ResponseBase{
	public List<POrder> data;
}
