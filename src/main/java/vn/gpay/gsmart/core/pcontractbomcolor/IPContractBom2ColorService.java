package vn.gpay.gsmart.core.pcontractbomcolor;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContractBom2ColorService extends Operations<PContractBom2Color> {
	public List<PContractBom2Color> getall_material_in_productBOMColor(long pcontractid_link, long productid_link, long colorid_link, long materialid_link);
	public List<PContractBom2Color> getcolor_bymaterial_in_productBOMColor(long pcontractid_link, long productid_link, long materialid_link);
	public List<PContractBom2Color> getall_byproduct(long pcontractid_link, long productid_link);
	List<PContractBom2Color> getall_bypcontract(long orgrootid_link, long pcontractid_link);
}
