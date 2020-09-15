package vn.gpay.gsmart.core.api.Schedule;

import java.util.List;

import vn.gpay.gsmart.core.Schedule.Schedule_porder;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;

public class break_porder_response extends ResponseBase {
	public Schedule_porder old_data;
	public Schedule_porder new_data;
	public String mes;
	public List<POrderGrant_SKU> sku;
}
