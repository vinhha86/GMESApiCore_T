package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.stockout_order.Stockout_order;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_d;

public class create_order_request {
	public Stockout_order data;
	public List<Stockout_order_d> detail;
}
