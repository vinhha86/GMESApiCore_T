package vn.gpay.gsmart.core.porder;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.security.GpayUser;

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
	
	public List<POrder> getPOrderListBySearch(String style, Long buyerid, Long vendorid, Long factoryid, Long status, Long granttoorgid_link);
	POrder get_oneby_po_org_product(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link,
			long productid_link);
	List<POrder> getByContractAndPO_Granted(Long pcontractid_link, Long pcontract_poid_link);
	List<POrder> getByPOAndProduct(Long pcontract_poid_link, Long productid_link);
	POrder getById(Long id);
	public List<POrder> get_by_code(String ordercode, long orgrootid_link);
	public List<POrder> getPOrderByOrdercode(String ordercode);
	public List<POrder> getPOrderByExactOrdercode(String ordercode);
	
	public List<POrderBinding> getForNotInProductionChart();
	POrder createPOrder(POrder_Req porder_req, GpayUser user);
	
	public List<POrder> getPOrderBySearch(
			Long buyerid, Long vendorid, Long factoryid, 
			String pobuyer, String stylebuyer, 
			List<Integer> statuses, Long granttoorgid_link);
	public List<POrder> getPOrderBySearch(
			Long buyerid, Long vendorid, Long factoryid, 
			String pobuyer, String stylebuyer, Long granttoorgid_link);
}
