package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout_order.Stockout_order;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_coloramount;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_d;

public class getby_id_response extends ResponseBase {
	public Stockout_order data;
	public List<Stockout_order_d> detail;
	public List<Stockout_order_coloramount> color;
}
