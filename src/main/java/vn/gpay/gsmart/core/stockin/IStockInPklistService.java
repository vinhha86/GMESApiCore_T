package vn.gpay.gsmart.core.stockin;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockInPklistService extends Operations<StockInPklist>{

	public List<StockInPklist> inv_getbyid(long stockoutid_link);
}
