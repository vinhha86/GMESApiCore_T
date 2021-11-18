package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant.POrderGrantBinding;

public class POrderGrantBinding_list_response extends ResponseBase{
	public List<POrderGrantBinding> data;
	public Map<Date, POrderGrantBinding> dataMap;
}
