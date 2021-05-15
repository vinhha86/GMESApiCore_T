package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.pcontractproductsku.POLineSKU;

public class add_sku_toporder_request {
	public List<POLineSKU> list_sku;
	public Long porderid_link;
	public Long productid_link;
	public Long pcontract_poid_link;
}
