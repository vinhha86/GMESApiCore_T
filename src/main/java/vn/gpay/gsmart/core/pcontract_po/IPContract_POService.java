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

	List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link, Long productid_link);
	
	List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link, Long productid_link);
	
	List<PContract_PO> getPcontractPoByPContractAndPOBuyer(Long pcontractid_link, String po_buyer, String buyercode);
	
	List<PContract_PO> getone_by_template_set(String PO_No, Date ShipDate, long productid_link, long shipmodeid_link, long pcontractid_link);
	List<PContract_PO> check_exist_po(String PO_No, Date ShipDate, long productid_link, long shipmodeid_link, long pcontractid_link, float vendor_targer);
	List<PContract_PO> get_by_parentid(Long pcontractpo_parentid_link);
}
