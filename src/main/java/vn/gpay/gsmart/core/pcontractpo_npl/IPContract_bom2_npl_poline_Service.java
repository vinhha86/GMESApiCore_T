package vn.gpay.gsmart.core.pcontractpo_npl;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_bom2_npl_poline_Service extends Operations<PContract_bom2_npl_poline> {
	List<PContract_bom2_npl_poline> getby_po_and_npl(Long pcontractpoid_link, Long material_skuid_link);
	public List<PContract_bom2_npl_poline> getby_pcontract_and_npl(Long pcontractid_link, Long material_skuid_link);
}
