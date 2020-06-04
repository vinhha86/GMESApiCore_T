package vn.gpay.gsmart.core.pcontractproduct;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProductService extends Operations<PContractProduct>{
	public List<PContractProduct> get_by_product_and_pcontract(long orgrootid_link, long productid_link, long pcontractid_link);

	List<Long> get_by_orgcustomer(Long orgrootid_link, Long orgcustomerid_link);
}
