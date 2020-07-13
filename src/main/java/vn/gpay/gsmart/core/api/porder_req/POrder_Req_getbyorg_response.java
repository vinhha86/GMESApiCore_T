package vn.gpay.gsmart.core.api.porder_req;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_req.POrder_Req;

public class POrder_Req_getbyorg_response extends ResponseBase{
	public List<POrder_Req> data = new ArrayList<POrder_Req>();
}
