package vn.gpay.gsmart.core.api.stockout;
import java.util.List;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.stockout.StockOutPklist;

public class StockoutPklistResponse extends ResponseBase{
	public List<StockOutPklist> data;
}
