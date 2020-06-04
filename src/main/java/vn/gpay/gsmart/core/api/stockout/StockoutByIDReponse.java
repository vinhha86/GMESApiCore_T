package vn.gpay.gsmart.core.api.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockOut;
import vn.gpay.gsmart.core.stockout.StockOutPklist;

public class StockoutByIDReponse extends ResponseBase{
	public StockOut data;
	public List<StockOutPklist> epcs;
}
