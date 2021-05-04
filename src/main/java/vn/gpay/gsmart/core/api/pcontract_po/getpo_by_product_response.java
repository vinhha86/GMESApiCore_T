package vn.gpay.gsmart.core.api.pcontract_po;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;

public class getpo_by_product_response extends ResponseBase{
	public List<PContract_PO> data;
	public List<PContract_bom2_npl_poline> poline;
}
