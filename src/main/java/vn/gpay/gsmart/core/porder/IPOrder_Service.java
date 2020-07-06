package vn.gpay.gsmart.core.porder;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_Service extends Operations<POrder> {

	List<POrder> getByContract(Long pcontractid_link);
	List<POrder> getByContractAndProduct(Long pcontractid_link, Long productid_link);
	List<POrder> getByStatus(Integer status);
	Integer getMaxPriority();
	List<POrder> getFilter(String ordercode, Integer status, Long granttoorgid_link, String collection, String season,
			Integer salaryyear, Integer salarymonth, Date processingdate_from, Date processingdate_to);
	
	List<POrder> get_by_org(long orgid_link);
	List<POrder> getByContractAndPO(Long pcontractid_link, Long pcontract_poid_link);
	Long savePOrder(POrder porder, String po_code);
	List<POrder> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,Boolean isReqPorder);

}
