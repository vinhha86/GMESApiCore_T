package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class StockoutOrder_create_new_request extends RequestBase{
	public List<Date> date_list;
	public Long porder_grantid_link;
}
