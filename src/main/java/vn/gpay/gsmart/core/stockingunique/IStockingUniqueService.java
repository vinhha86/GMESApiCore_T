package vn.gpay.gsmart.core.stockingunique;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockingUniqueService extends Operations<StockingUniqueCode> {
	public StockingUniqueCode getby_type(Integer type);
}
