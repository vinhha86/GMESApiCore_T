package vn.gpay.gsmart.core.api.stockin;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockin.StockIn;
import vn.gpay.gsmart.core.warehouse.Warehouse;

public class GetStockinByIDResponse extends ResponseBase{
	public StockIn data;
	public List<Warehouse> epcs;
}
