package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class AppFunctionService extends AbstractService<AppFunction> implements IAppFunctionService{
	@Autowired
	AppFunction_Repository repo;
	@Override	
	protected JpaRepository<AppFunction, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<AppFunction> getAppFunction_byMenu(String menuid_link) {
		// TODO Auto-generated method stub
		return repo.getAppFunction_byMenu(menuid_link);
	}
	@Override
	public List<AppFunction> getAppFunction_inmenu(String menuid_link , long roleid_link) {
		// TODO Auto-generated method stub
		return repo.getAppFunction_inMenu(menuid_link, roleid_link);
	}

}
