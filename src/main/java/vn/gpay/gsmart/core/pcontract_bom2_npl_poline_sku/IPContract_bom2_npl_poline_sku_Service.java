package vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_bom2_npl_poline_sku_Service extends Operations<PContract_bom2_npl_poline_sku> {
	public List<PContract_bom2_npl_poline_sku> getby_po_and_sku(Long orgrootid_link, Long pcontract_poid_link, Long product_skuid_link, Long material_skuid_link);
	public List<PContract_bom2_npl_poline_sku> getby_po(Long orgrootid_link, Long pcontract_poid_link, Long material_skuid_link, Long productid_link);
	public List<PContract_bom2_npl_poline_sku> getone_rec(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Long pcontract_poid_link, Long product_skuid_link, Long material_skuid_link);
}
