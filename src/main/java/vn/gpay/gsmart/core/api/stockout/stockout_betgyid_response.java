package vn.gpay.gsmart.core.api.stockout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockOut;
import vn.gpay.gsmart.core.warehouse.Warehouse;

public class stockout_betgyid_response extends ResponseBase {
	public List<Warehouse> listepc;
	public StockOut data;
}
