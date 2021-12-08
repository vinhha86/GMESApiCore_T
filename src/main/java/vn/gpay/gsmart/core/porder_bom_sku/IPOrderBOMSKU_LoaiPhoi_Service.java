package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBOMSKU_LoaiPhoi_Service extends Operations<porder_bom_sku_loaiphoi> {
	public List<porder_bom_sku_loaiphoi> getbypcontract_product_material_sku_loaiphoi(Long pcontractid_link,
			Long productid_link, Long material_skuid_link, Long skuid_link, String loaiphoi);

	public List<porder_bom_sku_loaiphoi> getbypcontract_product_material_sku(Long pcontractid_link, Long productid_link,
			Long material_skuid_link, Long skuid_link);

}
