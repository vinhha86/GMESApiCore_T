package vn.gpay.gsmart.core.stockout_order;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockout_order_service extends Operations<Stockout_order>{
	List<Stockout_order> getby_porder(Long porderid_link);
	List<Stockout_order> getby_porder_npl(Long porderid_link, Long material_skuid_link);

	List<Stockout_order> findBySearch(Date stockoutorderdate_from, Date stockoutorderdate_to);
	List<Stockout_order> findBySearch_type(Date stockoutorderdate_from, Date stockoutorderdate_to, Integer stockouttypeid_link);
}
