package vn.gpay.gsmart.core.api.stockin;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockin.StockIn;

public class StockInResponse extends ResponseBase{
	public List<StockIn> data;
	public long totalCount;
}
