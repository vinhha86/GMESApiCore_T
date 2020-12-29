package vn.gpay.gsmart.core.api.fabric_price;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class FabricPrice_getByMaterial_request extends RequestBase{
	public List<Long> materialid_link_list; // skuid_link
	public Long currencyid_link; // currencyid_link của pcontract_po
	public Long unitid_link; // kg hay met
}
