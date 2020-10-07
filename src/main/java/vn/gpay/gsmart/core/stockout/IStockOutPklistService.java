package vn.gpay.gsmart.core.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockOutPklistService extends Operations<StockOutPklist>{

	public List<StockOutPklist> inv_getbyid(long stockoutid_link);
}
