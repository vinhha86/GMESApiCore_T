package vn.gpay.gsmart.core.pcontractproductbom;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractProductBom2Service extends Operations<PContractProductBom2>{
	public List<PContractProductBom2> get_pcontract_productBOMbyid(long productid_link, long pcontractid_link);
	public List<PContractProductBom2> getby_pcontract_product_material(long productid_link, long pcontractid_link, long materialid_link);
}
