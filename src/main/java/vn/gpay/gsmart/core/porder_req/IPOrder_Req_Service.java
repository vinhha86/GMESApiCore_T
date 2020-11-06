package vn.gpay.gsmart.core.porder_req;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_Req_Service extends Operations<POrder_Req> {

	List<POrder_Req> getByContract(Long pcontractid_link);
	List<POrder_Req> getByContractAndProduct(Long pcontractid_link, Long productid_link);
	List<POrder_Req> getByStatus(Integer status);
	List<POrder_Req> getFilter(String ordercode, Integer status, Long granttoorgid_link, String collection, String season,
			Integer salaryyear, Integer salarymonth, Date processingdate_from, Date processingdate_to);
	
	List<POrder_Req> get_by_org(long orgid_link);
	List<POrder_Req> get_req_granted(long orgid_link);
	List<POrder_Req> getByContractAndPO(Long pcontractid_link, Long pcontract_poid_link);
	List<POrder_Req> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link);
	Long savePOrder_Req(POrder_Req porder_req);
	List<POrder_Req> getByPO(Long pcontract_poid_link);
	List<POrder_Req> getByPO_is_calculate(Long pcontract_poid_link);
	List<POrder_Req> getByOrg_PO_Product(Long pcontract_poid_link, long productid_link, long granttoorgid_link);
	List<POrder_Req> getByPOAndProduct(Long pcontract_poid_link, Long productid_link);
	List<POrder_Req> getByContractAndPO_and_Org(Long pcontractid_link, Long pcontract_poid_link, Long granttoorgid_link, Long productid_link);
}
