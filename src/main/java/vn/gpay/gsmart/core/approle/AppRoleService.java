package vn.gpay.gsmart.core.approle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class AppRoleService extends AbstractService<AppRole> implements IAppRoleService {
	@Autowired
	AppRole_Repository repo;
	
	@Override
	protected JpaRepository<AppRole, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
