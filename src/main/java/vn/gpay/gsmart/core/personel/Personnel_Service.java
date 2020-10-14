package vn.gpay.gsmart.core.personel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Personnel_Service extends AbstractService<Personel> implements IPersonnel_Service {
	@Autowired Personnel_repository repo;
	@Override
	protected JpaRepository<Personel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
