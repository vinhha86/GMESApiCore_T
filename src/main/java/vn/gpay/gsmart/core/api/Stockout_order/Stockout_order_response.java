package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout_order.Stockout_order;

public class Stockout_order_response extends ResponseBase{
	public List<Stockout_order> data;
	public long totalCount;
}
