package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockout_order_coloramount_Service extends Operations<Stockout_order_coloramount>{
	public List<Stockout_order_coloramount> getby_stockout_Order(Long stockoutorderid_link);
	public List<Stockout_order_coloramount> getby_stockoutorder_and_sku(Long stockoutorderid_link, Long skuid_link);
}
