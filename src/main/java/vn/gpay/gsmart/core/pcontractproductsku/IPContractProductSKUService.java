package vn.gpay.gsmart.core.pcontractproductsku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProductSKUService extends Operations<PContractProductSKU> {
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link, long pcontractid_link);

	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(long skuid_link, long pcontractid_link);
	
	public List<Long> getlistvalue_by_product(long pcontractid_link, long productid_link, long attributeid_link);
	
	public List<Long> getsku_bycolor(long pcontractid_link, long productid_link, long colorid_link);

	public List<PContractProductSKU> getlistsku_bypo_and_pcontract(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link);
	public List<PContractProductSKU> getbypo_and_product(long pcontract_poid_link, long productid_link);
	
	public List<PContractProductSKU> getlistsku_bysku_and_product_PO(long skuid_link, long pcontract_poid_link, long productid_link);

	List<PContractProductSKU> getlistsku_bypo_and_pcontract_free(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link);

	List<PContractProductSKU> getbypo_and_product_free(long porderreqid_link, long pcontractid_link, long pcontract_poid_link,
			long productid_link);

	List<PContractProductSKU> getlistsku_bypcontract(long orgrootid_link, long pcontractid_link);

	List<PContractProductSKU> getPOSKU_Free_ByProduct(long productid_link, long pcontract_poid_link);
}
