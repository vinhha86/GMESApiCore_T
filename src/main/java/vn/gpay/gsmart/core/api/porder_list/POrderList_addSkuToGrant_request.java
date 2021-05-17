package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;

public class POrderList_addSkuToGrant_request extends RequestBase {
	public List<POrder_Product_SKU> porderSku;
	public Long idGrant;
	public Long idPOrder;
	public Long pcontract_poid_link;
}
