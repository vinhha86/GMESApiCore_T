package vn.gpay.gsmart.core.handover;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverService  extends AbstractService<Handover> implements IHandoverService{

	@Autowired HandoverRepository repo;
	
	@Override
	protected JpaRepository<Handover, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Handover> getByType(Long handovertypeid_link) {
		// TODO Auto-generated method stub
		return repo.getByType(handovertypeid_link);
	}

}
