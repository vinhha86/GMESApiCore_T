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

	List<POrder> get_free_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link, String PO_code,
			Long orgbuyerid_link, Long orgvendorid_link, List<Long> vendors, List<Long> buyers);

	List<POrder> get_free_bygolivedate_groupby_product(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			String PO_code, Long orgbuyerid_link, Long orgvendorid_link);

	List<POrder> getByPOrder_Req(Long pcontract_poid_link, Long porderreqid_link);

	POrder get_oneby_po_price(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link,
			long productid_link, long sizesetid_link);

	public List<POrder> getPOrderListBySearch(String style, Long buyerid, Long vendorid, Long factoryid, Long status,
			Long granttoorgid_link);

	POrder get_oneby_po_org_product(long orgrootid_link, long granttoorgid_link, long pcontract_poid_link,
			long productid_link);

	List<POrder> getByContractAndPO_Granted(Long pcontractid_link, Long pcontract_poid_link);

	List<POrder> getByPOAndProduct(Long pcontract_poid_link, Long productid_link);

	POrder getById(Long id);

	public List<POrder> get_by_code(String ordercode, long orgrootid_link);

	public List<POrder> getPOrderByOrdercode(String ordercode, Long granttoorgid_link);

	public List<POrder> getPOrderByExactOrdercode(String ordercode);

	public List<POrderBinding> getForNotInProductionChart();

	public List<POrderBinding> getPOrderStatusChart();

	POrder createPOrder(POrder_Req porder_req, GpayUser user);

	List<POrder> getby_offer(Long pcontract_poid_link, Long productid_link, Long orgid_link);

	public List<POrder> getPOrderBySearch(Long buyerid, Long vendorid, Long factoryid, String pobuyer,
			String stylebuyer, String contractcode, List<Integer> statuses, Long granttoorgid_link, Date golivedatefrom,
			Date golivedateto);

//	public List<POrder> getPOrderBySearch(Long buyerid, Long vendorid, Long factoryid, String pobuyer,
//			String stylebuyer, String contractcode, Long granttoorgid_link, Date golivedatefrom, Date golivedateto);

	List<POrder> getby_offer_and_orgs(Long pcontract_poid_link, List<Long> orgs);

	List<POrder> getPOrderByOrderCodeAndProductBuyerCode(Long granttoorgid_link, String ordercode, String buyercode);

	List<POrder> getPorderByOrdercodeAndOrg(String ordercode, Long granttoorgid_link);

	List<POrder> getByPcontractPO(Long pcontract_poid_link);

	Integer getTotalOrder(Long porderid_link);
}
