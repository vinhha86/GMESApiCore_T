package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockout_order_service extends Operations<Stockout_order>{
	List<Stockout_order> getby_porder(Long porderid_link);
}
