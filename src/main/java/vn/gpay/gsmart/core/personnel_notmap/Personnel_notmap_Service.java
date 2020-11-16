package vn.gpay.gsmart.core.personnel_notmap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_notmap_Service extends AbstractService<Personnel_notmap> implements IPersonnel_notmap_Service {
	@Autowired Personnel_notmap_repository repo;
	@Override
	protected JpaRepository<Personnel_notmap, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Personnel_notmap> getby_registercode(String code) {
		// TODO Auto-generated method stub
		return repo.getby_registercode(code);
	}

}
