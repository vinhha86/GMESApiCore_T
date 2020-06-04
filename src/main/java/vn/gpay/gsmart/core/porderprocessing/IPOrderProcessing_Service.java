package vn.gpay.gsmart.core.porderprocessing;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
public interface IPOrderProcessing_Service extends Operations<POrderProcessing>{

	public List<POrderProcessing> getLatest_All();

	public List<POrderProcessing> getAfterDate(Long porderid, Date processingdate_to);

	public List<POrderProcessing> findByCodeAndPDate(String ordercode, Date processingdate);

	public List<POrderProcessing> findByIdAndPDate(Long porderid, Date processingdate);

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

}
