package vn.gpay.gsmart.core.porderprocessingns;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PorderProcessingNsService extends AbstractService<PorderProcessingNs> implements IPorderProcessingNsService{

	@Autowired IPorderProcessingNsRepository repo;
	
	@Override
	protected JpaRepository<PorderProcessingNs, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public Integer getTotalWTime_ByPorder(Long pordergrantid_link, Long personnelid_link, Date date_from, Date date_to){
		return repo.getTotalWTime_ByPorder(pordergrantid_link, personnelid_link, date_from, date_to);
	}

	@Override
	public List<PorderProcessingNs> getByPersonnelDateAndShift(Long porderid_link, Long pordergrantid_link,
			Long personnelid_link, Date processingdate, Integer shifttypeid_link) {
		return repo.getByPersonnelDateAndShift(porderid_link, pordergrantid_link, personnelid_link, processingdate, shifttypeid_link);
	}

	@Override
	public List<PorderProcessingNs> getByPersonnelDateAndShiftAndPOrderSewingCost(Long porderid_link,
			Long pordergrantid_link, Long personnelid_link, Date processingdate, Integer shifttypeid_link,
			Long pordersewingcostid_link) {
		// TODO Auto-generated method stub
		return repo.getByPersonnelDateAndShiftAndPOrderSewingCost(porderid_link, pordergrantid_link, personnelid_link, processingdate, shifttypeid_link, pordersewingcostid_link);
	}
}
