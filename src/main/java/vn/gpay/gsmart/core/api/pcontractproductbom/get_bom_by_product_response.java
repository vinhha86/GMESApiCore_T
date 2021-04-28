package vn.gpay.gsmart.core.api.pcontractproductbom;

import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.base.ResponseBase;

public class get_bom_by_product_response extends ResponseBase{
	public List<Map<String, String>> data;
	public boolean isbomdone;
}
