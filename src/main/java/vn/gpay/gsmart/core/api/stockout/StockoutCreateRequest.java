package vn.gpay.gsmart.core.api.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.stockout.StockOut;

public class StockoutCreateRequest extends RequestBase{

	public List<StockOut> data;
}
