package vn.gpay.gsmart.core.porderprocessingns;

import java.util.Date;

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
}
