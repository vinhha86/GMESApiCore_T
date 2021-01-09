package vn.gpay.gsmart.core.api.porder_req;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_po.PContractPO_Product;

public class get_poline_product_byorg_response extends ResponseBase {
	public List<PContractPO_Product> data;
}
