package vn.gpay.gsmart.core.porderprocessing;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderProcessing_Service extends AbstractService<POrderProcessing> implements IPOrderProcessing_Service {
	@Autowired IPOrderProcessing_Repository repo;
	@Override
	protected JpaRepository<POrderProcessing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderProcessing>getLatest_All(){
		return repo.getLatest_All();
	}

	@Override
	public List<POrderProcessing>getByDate(Date processingdate_to){
		return repo.getByDate(processingdate_to);
	}
	
	@Override
	public List<POrderProcessing>getBySalaryMonth(Integer salaryyear, Integer salarymonth){
		return repo.getBySalaryMonth(salaryyear, salarymonth);
	}
	
	
	@Override
	public List<POrderProcessing>getByDate_Cutting(Date processingdate_to){
		return repo.getByDate_Cutting(processingdate_to);
	}
	
	@Override
	public List<POrderProcessing>getPProcess_Cutting(Date processingdate_to, Long porderid_link){
		return repo.getPProcess_Cutting(processingdate_to, porderid_link);
	}

	@Override
	public List<POrderProcessing>getByDateAndOrderCode(String ordercode, Date processingdate_to){
		return repo.getByDateAndOrderCode(ordercode, processingdate_to);
	}

	@Override
	public List<POrderProcessing> getProductionStartDate(String ordercode){
		return repo.getProductionStartDate(ordercode);
	}

	@Override
	public List<POrderProcessing>getByBeforeDateAndOrderCode(String ordercode, Date processingdate_to){
		return repo.getByBeforeDateAndOrderCode(ordercode, processingdate_to);
	}

	@Override
	public List<POrderProcessing>getByBeforeDateAndOrderID(Long porderid_link, Date processingdate_to){
		return repo.getByBeforeDateAndOrderID(porderid_link, processingdate_to);
	}

	@Override
	public List<POrderProcessing>getByDateAndStatus(Date processingdate_to, Integer status){
		return repo.getByDateAndStatus(processingdate_to, status);
	}

	@Override
	public List<POrderProcessing>findByIdAndPDate(Long porderid, Date processingdate){
		return repo.findByIdAndPDate(porderid, processingdate);
	}
	
	@Override
	public List<POrderProcessing>findByCodeAndPDate(String ordercode, Date processingdate){
		return repo.findByCodeAndPDate(ordercode, processingdate);
	}

	@Override
	public List<POrderProcessing>getAfterDate(Long porderid, Date processingdate_to){
		return repo.getAfterDate(porderid, processingdate_to);
	}
	
	@Override
	public void deleteByOrderID(final Long porderid_link){
		for(POrderProcessing porder: repo.getByOrderId(porderid_link)){
			repo.delete(porder);
		}
	}
	
	@Override
	public List<POrderProcessing>getByOrderId(Long porderid_link){
		return repo.getByOrderId(porderid_link);
	}
}
