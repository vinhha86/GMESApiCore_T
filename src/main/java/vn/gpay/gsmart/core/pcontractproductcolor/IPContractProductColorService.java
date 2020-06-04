package vn.gpay.gsmart.core.pcontractproductcolor;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProductColorService extends Operations<PContractProductColor> {
	public List<PContractProductColor> getcolor_by_pcontract_and_product(long orgrootid_link, long pcontractid_link, long productid_link);
	public List<Long> getcolorid_by_pcontract_and_product(long orgrootid_link, long pcontractid_link, long productid_link);
}
