package vn.gpay.gsmart.core.porderprocessing;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;

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
	public List<POrderProcessing>getByDateAndFactory(Date processingdate_to, Long factoryid){
		List<POrderProcessing> a = repo.getByDateAndFactory(processingdate_to, factoryid);
		return a;
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
	public List<POrderProcessing>getByBeforeDateAndOrderGrantID(Long pordergrantid_link, Date processingdate_to){
		try {
			return repo.getByBeforeDateAndOrderGrantID(pordergrantid_link, processingdate_to);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	public List<POrderProcessing>findByIdAndPDate(Long porderid_link, Long pordergrantid_link, Date processingdate){
		return repo.findByIdAndPDate(porderid_link, pordergrantid_link, processingdate);
	}
	
	@Override
	public List<POrderProcessing>findByCodeAndPDate(String ordercode, Date processingdate){
		return repo.findByCodeAndPDate(ordercode, processingdate);
	}

	@Override
	public List<POrderProcessing>getAfterDate(Long porderid_link, Long pordergrantid_link, Date processingdate_to){
		return repo.getAfterDate(porderid_link, pordergrantid_link, processingdate_to);
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

	@Override
	public List<POrderProcessing> getByOrderId_and_GrantId(Long porderid_link, Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrderId_And_GrantId(porderid_link, pordergrantid_link);
	}
	
	@Override
	public POrderGrant get_processing_bygolivedate(Long porderid_link, Long pordergrantid_link){
		POrderGrant thePorderGrant = new POrderGrant();
		List<POrderProcessing> aMIN = repo.getMINRunningByOrderId_And_GrantId(porderid_link, pordergrantid_link);
		List<POrderProcessing> aMAX = repo.getMAXRunningByOrderId_And_GrantId(porderid_link, pordergrantid_link);
		if (aMIN.size() > 0 && aMAX.size() > 0){
			thePorderGrant.setStart_date_plan(aMIN.get(0).getProcessingdate());
			thePorderGrant.setFinish_date_plan(aMAX.get(0).getProcessingdate());
			thePorderGrant.setGrantamount(null==aMAX.get(0).getAmountoutputsum()?0:aMAX.get(0).getAmountoutputsum());
			thePorderGrant.setAmountcutsum(null==aMAX.get(0).getAmountoutput()?0:aMAX.get(0).getAmountoutput());

			return thePorderGrant;
		} else {
			return null;
		}
	}

	@Override
	public List<POrderProcessing> getByOrgId(Long granttoorgid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrgId(granttoorgid_link);
	}
}
