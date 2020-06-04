package vn.gpay.gsmart.core.porder_bom_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBOMSKU_Service extends Operations<POrderBOMSKU>{

	List<POrderBOMSKU> getByPOrderID(Long porderid_link);

	List<POrderBOMSKU_By_Product> getByPOrderID_GroupByProduct(Long porderid_link);

	List<POrderBOMSKU_By_Color> getByPOrderID_GroupByColor(Long porderid_link);

	List<POrderBOMSKU> getSKUByMaterial(Long porderid_link, Long materialid_link);

}
