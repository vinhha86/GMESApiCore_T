package vn.gpay.gsmart.core.pcontract_po;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContract_POService extends Operations<PContract_PO> {

	List<PContract_PO> getPOByContractProduct(Long orgrootid_link, Long pcontractid_link, Long productid_link);

	List<PContract_PO> getPOByContract(Long orgrootid_link, Long pcontractid_link);
}
