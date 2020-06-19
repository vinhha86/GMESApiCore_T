package vn.gpay.gsmart.core.pcontractbomsku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContractBOM2SKUService extends Operations<PContractBOM2SKU>{
	public List<PContractBOM2SKU> getall_material_in_productBOMSKU(long pcontractid_link, long productid_link, long sizeid_link, long colorid_link, long materialid_link);
	public List<PContractBOM2SKU> getcolor_bymaterial_in_productBOMSKU(long pcontractid_link, long productid_link, long materialid_link);
	public long getskuid_link_by_color_and_size(long colorid_link, long sizeid_link, long productid_link);
	public List<PContractBOM2SKU> getmaterial_bycolorid_link(long pcontractid_link, long productid_link, long colorid_link, long materialid_link);
	public List<Long> getsize_bycolor(long pcontractid_link, long productid_link, long colorid_link);
	List<PContractBOM2SKU> getMaterials_BySKUId(Long skuid_link);
}	
