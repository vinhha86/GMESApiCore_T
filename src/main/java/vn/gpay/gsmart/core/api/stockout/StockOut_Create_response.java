package vn.gpay.gsmart.core.api.stockout;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockOutPklist;

public class StockOut_Create_response extends ResponseBase {
	public long id;
	public List<StockOutPklist> epc_err = new ArrayList<StockOutPklist>();
}
