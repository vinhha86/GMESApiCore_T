package vn.gpay.gsmart.core.api.pcontract_po;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;

public class get_sku_by_line_response extends ResponseBase{
	public List<PContractProductSKU> data;
}
