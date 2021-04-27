package vn.gpay.gsmart.core.pcontractpo_npl;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContractPO_npl_Service extends Operations<PContractPO_NPL> {
	List<PContractPO_NPL> getby_po_and_npl(Long pcontractpoid_link, Long material_skuid_link);
	public List<PContractPO_NPL> getby_pcontract_and_npl(Long pcontractid_link, Long material_skuid_link);
}
