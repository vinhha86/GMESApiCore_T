package vn.gpay.gsmart.core.sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface ISKU_Service extends Operations<SKU> {
	public List<SKU> getlist_byProduct(Long productid_link);

	List<SKU> getSKU_MainMaterial(String code);

	List<SKU> getSKU_ByType(String code, Integer producttypeid_link);

	SKU getSKU_byCode(String code, long orgrootid_link);
}
