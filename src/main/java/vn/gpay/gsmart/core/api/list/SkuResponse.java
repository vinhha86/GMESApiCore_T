package vn.gpay.gsmart.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.sku.SKU;

public class SkuResponse extends ResponseBase {

	public List<SKU>  data = new ArrayList<>();
}	
