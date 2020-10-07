package vn.gpay.gsmart.core.porder_product;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_Product_Service extends Operations<POrder_Product> {
	public List<POrder_Product> get_product_inporder(Long orgrootid_link, Long porderid_link);
}
