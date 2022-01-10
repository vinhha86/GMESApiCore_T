package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant_sku_plan.POrderGrant_SKU_Plan;

public class POrderGrant_SKU_Plan_list_response extends ResponseBase{
	public List<POrderGrant_SKU_Plan> data;
	public Map<Date, List<POrderGrant_SKU_Plan>> map;
}
