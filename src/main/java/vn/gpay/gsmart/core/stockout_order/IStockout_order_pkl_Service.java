package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockout_order_pkl_Service extends Operations<Stockout_order_pkl>{
	List<Stockout_order_pkl> getby_detail(Long stockout_orderdid_link);
	List<Stockout_order_pkl> getByEpc_stockout_order(Long stockoutorderid_link, String epc);
}
