package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockout_order_d_service extends Operations<Stockout_order_d> {
	List<Stockout_order_d> getby_Stockout_order(Long Stockout_order_id_link);
}
