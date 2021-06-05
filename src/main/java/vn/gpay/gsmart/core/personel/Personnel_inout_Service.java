package vn.gpay.gsmart.core.personel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_inout_Service extends AbstractService<Personnel_inout> implements IPersonnel_inout_Service {
	@Autowired Personnel_inout_repository repo;
	@Override
	protected JpaRepository<Personnel_inout, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
