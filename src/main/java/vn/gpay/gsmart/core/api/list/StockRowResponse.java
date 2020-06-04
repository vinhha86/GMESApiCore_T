package vn.gpay.gsmart.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stock.Stockrow;

public class StockRowResponse extends ResponseBase {

	public List<Stockrow>  data = new ArrayList<>();
}	