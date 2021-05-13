package vn.gpay.gsmart.core.api.porder_bom;

import java.util.List;
import java.util.Map;

import vn.gpay.gsmart.core.base.ResponseBase;

public class getbom2sku_by_porder_response extends ResponseBase {
	public List<Map<String, String>> data;
	public boolean isbomdone;
}
