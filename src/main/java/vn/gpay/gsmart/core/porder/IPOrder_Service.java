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
	POrder savePOrder(POrder porder, String po_code);
	List<POrder> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,String PO_code,Long orgbuyerid_link,Long orgvendorid_link);
	List<POrder> getByPOrder_Req(Long pcontract_poid_link, Long porderreqid_link);
	POrder get_oneby_po_price(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link, long productid_link, long sizesetid_link);
	
	public List<POrder> getPOrderListBySearch(String style, Long buyerid, Long vendorid, Date orderdatefrom, Date orderdateto, Long status, Long granttoorgid_link);
	POrder get_oneby_po_org_product(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link,
			long productid_link);
	List<POrder> getByContractAndPO_Granted(Long pcontractid_link, Long pcontract_poid_link);
	List<POrder> getByPOAndProduct(Long pcontract_poid_link, Long productid_link);
	POrder getById(Long id);

}
