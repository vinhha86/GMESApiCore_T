package vn.gpay.gsmart.core.pcontratproductsku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProductSKUService extends Operations<PContractProductSKU> {
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link, long pcontractid_link);

	public List<PContractProductSKU> getlistsku_bysku_and_pcontract(long skuid_link, long pcontractid_link);
	
	public List<Long> getlistvalue_by_product(long pcontractid_link, long productid_link, long attributeid_link);
	
	public List<Long> getsku_bycolor(long pcontractid_link, long productid_link, long colorid_link);

	List<PContractProductSKU> getlistsku_bypo_and_pcontract(long orgrootid_link, long pcontract_poid_link,
			long pcontractid_link);
	
}
