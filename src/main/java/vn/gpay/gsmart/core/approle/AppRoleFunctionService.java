package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class AppRoleFunctionService extends AbstractService<AppRoleFunction> implements IAppRoleFunctionService {
	@Autowired
	AppRoleFunction_Repository repo;
	@Override
	protected JpaRepository<AppRoleFunction, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Long> getfunction_byrole(Long roleid_link) {
		// TODO Auto-generated method stub
		return repo.getfuntion_byrole(roleid_link);
	}
	@Override
	public AppRoleFunction getby_role_and_function(Long roleid_link, Long functionid_link) {
		// TODO Auto-generated method stub
		List<AppRoleFunction> list = repo.getbyfuntion_and_byrole(roleid_link, functionid_link);
		if(list.size() > 0)
			return list.get(0);
		return null;
	}

}
