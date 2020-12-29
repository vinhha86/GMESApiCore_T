package vn.gpay.gsmart.core.api.fabric_price;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.sku.SKU;

public class FabricPrice_getByMaterial_response extends ResponseBase{
	public List<SKU> data;
}
