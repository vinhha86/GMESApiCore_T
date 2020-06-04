package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class AppRole_User_Service extends AbstractService<AppRole_User> implements IAppRole_User_Service {
	@Autowired
	AppRole_User_Repository repo;
	@Override
	protected JpaRepository<AppRole_User, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<AppRole_User> getby_user_and_role(Long userid, Long roleid_link) {
		// TODO Auto-generated method stub
		return repo.getby_role_and_user(roleid_link, userid);
	}

}
