package vn.gpay.gsmart.core.pcontractbomcolor;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractBOMColorService extends Operations<PContractBOMColor> {
	public List<PContractBOMColor> getall_material_in_productBOMColor(long pcontractid_link, long productid_link, long colorid_link, long materialid_link);
	public List<PContractBOMColor> getcolor_bymaterial_in_productBOMColor(long pcontractid_link, long productid_link, long materialid_link);
	public List<PContractBOMColor> getall_byproduct(long pcontractid_link, long productid_link);
}
