package vn.gpay.gsmart.core.api.porder;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockin.StockInD;

public class porder_get_stockin_response extends ResponseBase {
	public List<StockInD> data;
	public int size;
}
