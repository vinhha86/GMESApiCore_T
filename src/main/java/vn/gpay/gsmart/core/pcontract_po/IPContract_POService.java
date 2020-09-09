package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContract_POService extends Operations<PContract_PO> {

	List<PContract_PO> getPOByContractProduct(Long orgrootid_link, Long pcontractid_link, Long productid_link, Long userid_link, Long orgid_link);

	List<PContract_PO> getPOByContract(Long orgrootid_link, Long pcontractid_link);

	List<PContract_PO> getPO_LeafOnly(Long orgrootid_link, Long pcontractid_link, Long productid_link, Long userid_link, Long orgid_link);

	List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Date shipdate);

	List<PContract_PO> getPOByContractAndProduct(Long pcontractid_link, Long productid_link);

	List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link);
	
	List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link);
}
