package vn.gpay.gsmart.core.api.stockout;

import java.util.List;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockoutReport;

public class StockoutForCheckReportResponse extends ResponseBase{
	public List<StockoutReport> data;
}
