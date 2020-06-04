package vn.gpay.gsmart.core.api.stockin;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.stockin.StockIn;

public class StockinCreateRequest extends RequestBase{
	public List<StockIn> data;
}
