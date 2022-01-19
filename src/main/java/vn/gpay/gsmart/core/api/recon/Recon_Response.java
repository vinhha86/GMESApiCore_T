package vn.gpay.gsmart.core.api.recon;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU_NoLink;

public class Recon_Response extends ResponseBase {
	public List<Recon_MatSKU_Data> data;
	public List<PContractProductSKU_NoLink> productsku_data;
}
