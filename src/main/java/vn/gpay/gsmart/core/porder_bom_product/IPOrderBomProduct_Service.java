package vn.gpay.gsmart.core.porder_bom_product;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBomProduct_Service extends Operations<POrderBomProduct> {
	List<POrderBomProduct> getby_porder(Long porderid_link);
	List<POrderBomProduct> getby_porder_and_type(Long porderid_link, int type_from, int type_to);
	List<POrderBomProduct> getby_porder_and_material(Long porderid_link, Long material_skuid_link);
}
