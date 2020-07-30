package vn.gpay.gsmart.core.porder_bom_product;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderBomProduct_Service extends Operations<POrderBomProduct> {
	List<POrderBomProduct> getby_porder(Long porderid_link);
}
