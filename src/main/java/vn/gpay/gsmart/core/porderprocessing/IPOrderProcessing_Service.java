package vn.gpay.gsmart.core.porderprocessing;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
public interface IPOrderProcessing_Service extends Operations<POrderProcessing>{

	public List<POrderProcessing> getLatest_All();

	public List<POrderProcessing> getAfterDate(Long porderid_link, Long pordergrantid_link, Date processingdate_to);
	
	public List<POrderProcessing> getBeforeDate(Long porderid_link, Long pordergrantid_link, Date processingdate);

	public List<POrderProcessing> findByCodeAndPDate(String ordercode, Date processingdate);

	public List<POrderProcessing> findByIdAndPDate(Long porderid_link, Long pordergrantid_link, Date processingdate);

	public List<POrderProcessing> getByDateAndStatus(Date processingdate_to, Integer status);

	public List<POrderProcessing> getByBeforeDateAndOrderCode(String ordercode, Date processingdate_to);

	public List<POrderProcessing> getByDate(Date processingdate_to);

	public List<POrderProcessing> getByDate_Cutting(Date processingdate_to);

	public List<POrderProcessing> getPProcess_Cutting(Date processingdate_to, Long porderid_link);

	public List<POrderProcessing> getByDateAndOrderCode(String ordercode, Date processingdate_to);

	public List<POrderProcessing> getProductionStartDate(String ordercode);

	List<POrderProcessing> getByBeforeDateAndOrderID(Long porderid_link, Date processingdate_to);

	void deleteByOrderID(Long porderid_link);

	List<POrderProcessing> getBySalaryMonth(Integer salaryyear, Integer salarymonth);

	List<POrderProcessing> getByOrderId(Long porderid_link);
	
	List<POrderProcessing> getByOrderId_and_GrantId(Long porderid_link, Long pordergrantid_link);

	List<POrderProcessing> getByDateAndFactory(Date processingdate_to, Long factoryid);

	List<POrderProcessing> getByBeforeDateAndOrderGrantID(Long pordergrantid_link, Date processingdate_to);

	POrderGrant get_processing_bygolivedate(Long porderid_link, Long pordergrantid_link);
	
	List<POrderProcessing>getByOrgId(Long granttoorgid_link);
	
	public List<POrderProcessing>getByPOrderAndPOrderGrant(Long porderid_link,Long pordergrantid_link);	
	
	public List<POrderProcessing>getByPOrderAndPOrderGrantAndMaxDate(Long porderid_link,Long pordergrantid_link);	
	
	List<POrderProcessing> getby_pcontratpo(Long pcontract_poid_link);

	public List<POrderProcessingBinding>getAmountOutputForChart(Date dateFrom, Date dateTo, Long dayDifference);
	
	public List<POrderProcessingBinding>getAmountPackStockedForChart(Date tenDaysAgo, Date today);
	
	public List<POrderProcessing>getByPOrderAndLineAndDate (Long porderid_link, Long orgid_to_link, Date receive_date);

}
