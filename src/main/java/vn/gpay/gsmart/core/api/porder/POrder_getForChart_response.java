package vn.gpay.gsmart.core.api.porder;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.POrderBinding;

public class POrder_getForChart_response extends ResponseBase{
	public List<POrderBinding> data;
}
