package vn.gpay.gsmart.core.api.porder;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;


public class POrder_Create_request {
	public String ordercode;
	public Date orderdate;
	public Long id;
	
	public Long pcontractid_link;
	public Long productid_link;
	public Long granttoorg_link;
	public List<PContractProductSKU> sku;
}
