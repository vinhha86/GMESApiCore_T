package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBOMSKU_Service extends Operations<POrderBOMSKU>{

	List<POrderBOMSKU> getByPOrderID_and_type(Long porderid_link, int type);

	List<POrderBOMSKU_By_Product> getByPOrderID_GroupByProduct(Long porderid_link);

	List<POrderBOMSKU_By_Color> getByPOrderID_GroupByColor(Long porderid_link);

	List<POrderBOMSKU> getSKUByMaterial(Long porderid_link, Long materialid_link);
	List<POrderBOMSKU> getby_porder_and_color(Long porderid_link, Long colorid_link);
	List<POrderBOMSKU> getby_porder_and_material(Long porderid_link, Long materialid_link);
	List<POrderBOMSKU> getby_porder_and_material_and_color(Long porderid_link, Long materialid_link, long colorid_link);
	List<POrderBOMSKU> getby_porder_and_material_and_color_and_size_and_type(Long porderid_link, Long productid_link, Long materialid_link, long colorid_link,long sizeid_link, int type);
	List<POrderBOMSKU> getby_porder_and_material_and_sku_and_type(Long porderid_link,Long materialid_link, long skuid_link, int type);
}
