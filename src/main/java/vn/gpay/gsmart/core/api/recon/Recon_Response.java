package vn.gpay.gsmart.core.api.recon;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;

public class Recon_Response extends ResponseBase {
	public List<Recon_MatSKU_Data> data;
	public List<Recon_ProductSKU_Data> productsku_data;
}
