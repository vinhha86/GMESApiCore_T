package vn.gpay.gsmart.core.api.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout_type.StockoutType;

public class Stockout_gettype_response extends ResponseBase {
	public List<StockoutType> data;
}
