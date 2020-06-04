package vn.gpay.gsmart.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stock.Stockspace;

public class StockSpaceResponse extends ResponseBase {

	public List<Stockspace>  data = new ArrayList<>();
}	