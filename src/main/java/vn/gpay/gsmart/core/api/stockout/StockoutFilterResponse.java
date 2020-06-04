package vn.gpay.gsmart.core.api.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockOut;

public class StockoutFilterResponse extends ResponseBase{
	public List<StockOut> data;
}
