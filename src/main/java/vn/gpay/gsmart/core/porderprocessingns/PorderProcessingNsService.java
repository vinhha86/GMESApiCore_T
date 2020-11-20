package vn.gpay.gsmart.core.porderprocessingns;

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

}
