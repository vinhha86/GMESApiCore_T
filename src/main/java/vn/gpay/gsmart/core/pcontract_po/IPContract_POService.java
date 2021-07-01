package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IPContract_POService extends Operations<PContract_PO> {

	List<PContract_PO> getPOByContractProduct(Long orgrootid_link, Long pcontractid_link, Long productid_link, Long userid_link, Long orgid_link, Integer potype);

	List<PContract_PO> getPOByContract(Long orgrootid_link, Long pcontractid_link);

//	List<PContract_PO> getPO_LeafOnly(Long orgrootid_link, Long pcontractid_link, Long productid_link, Long userid_link, Long orgid_link);

	List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Date shipdate);

	List<PContract_PO> getPOByContractAndProduct(Long pcontractid_link, Long productid_link);

	List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link, Long productid_link);
	
	List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link, Long productid_link);
	
	List<PContract_PO> getPcontractPoByPContractAndPOBuyer(Long pcontractid_link, String po_buyer, String buyercode);
	
	List<PContract_PO> getone_by_template_set(String PO_No, Date ShipDate, long productid_link, long shipmodeid_link, long pcontractid_link);
	List<PContract_PO> check_exist_po(Date ShipDate, long productid_link, long shipmodeid_link, long pcontractid_link, String po_buyer);
	List<PContract_PO> check_exist_line(Date ShipDate, long productid_link, long pcontractid_link, long parentid_link);
	List<PContract_PO> get_by_parentid(Long pcontractpo_parentid_link);
	List<PContract_PO> check_exist_po_children(String PO_No, Date Shipdate, Long shipmodeid_link, Long pcontractid_link, Long parentid_link);
	List<PContract_PO> check_exist_PONo(String PO_No,Long pcontractid_link);

	List<PContract_PO> getBySearch(String po_code, List<Long> orgs);
	List<PContract_PO> getBySearch_andType(String po_code, List<Long> orgs, int po_type);
	List<Long> getpcontract_BySearch(String po_code, List<Long> orgs);
	List<PContract_POBinding> getForMarketTypeChart();
	
	List<PContract_PO> getPO_Offer_Accept_ByPContract_AndOrg(Long pcontractid_link, Long productid_link, List<Long>  list_orgid_link);
	List<PContract_PO> getby_porder(Long porderid_link);
	List<PContract_PO> get_by_parent_and_type(Long pcontractpo_parentid_link, int po_typeid_link);
	List<PContract_PO> get_by_parent_and_type_and_MauSP(Long pcontractpo_parentid_link, int po_typeid_link, Long mausanphamid_link);
	
	List<PContract_PO> getall_offers_by_org(List<Long> orgid_link);
	List<PContract_PO> getby_pcontract_and_type(Long pcontractid_link, List<Integer> type);
	List<PContract_PO> getby_pcontract_and_type_andproduct(Long pcontractid_link, List<Integer> type, Long productid_link);
	List<PContractPO_Shipping> get_po_shipping(List<Long> orgs, int po_type, Date shipdate_from, Date shipdate_to, Long orgrootid_link);
	List<PContract_PO> getbycode_and_type_and_product(String po_no, int type, Long pcontractid_link, Long productid_link);
	List<PContract_PO> getpo_notin_list(List<String> list_po, int type, Long pcontractid_link);
	List<PContract_PO> getpo_byid(Long pcontractpoid_link);
}
