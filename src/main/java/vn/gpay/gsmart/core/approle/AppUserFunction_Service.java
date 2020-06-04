package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class AppUserFunction_Service extends AbstractService<AppUserFunction> implements IAppUserFunction_Service {
	@Autowired AppUserFunction_Repository repo;
	@Override
	protected JpaRepository<AppUserFunction, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<AppUserFunction> getby_function_and_user(Long functionid_link, Long userid_link) {
		// TODO Auto-generated method stub
		return repo.getby_functionid_and_user(functionid_link, userid_link);
	}

}
