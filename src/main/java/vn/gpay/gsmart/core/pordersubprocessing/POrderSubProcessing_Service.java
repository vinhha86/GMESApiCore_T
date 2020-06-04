package vn.gpay.gsmart.core.pordersubprocessing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderSubProcessing_Service extends AbstractService<POrderSubProcessing> implements IPOrderSubProcessing_Service {
	@Autowired IPOrderSubProcessing_Repository repo;
	@Override
	protected JpaRepository<POrderSubProcessing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<POrderSubProcessing>findByOrderCode(String ordercode){
		return repo.findByOrderCode(ordercode);
	}
	
	@Override
	public List<POrderSubProcessing>findByProcessID(String ordercode, Long workingprocessid_link){
		return repo.findByProcessID(ordercode, workingprocessid_link);
	}

	@Override
	public List<POrderSubProcessing>findByProcessID(Long porderid_link, Long workingprocessid_link){
		return repo.findByProcessID(porderid_link, workingprocessid_link);
	}
	
	@Override
	public List<POrderSubProcessing>findByOrderID(Long porderid_link){
		return repo.findByOrderID(porderid_link);
	}
}
