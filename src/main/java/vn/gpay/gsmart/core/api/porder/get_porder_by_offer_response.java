package vn.gpay.gsmart.core.api.porder;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.POrderFree;

public class get_porder_by_offer_response extends ResponseBase{
	public List<POrderFree> data;
}
