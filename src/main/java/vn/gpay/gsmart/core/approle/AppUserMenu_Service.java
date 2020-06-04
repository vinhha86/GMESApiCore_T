package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class AppUserMenu_Service extends AbstractService<AppUserMenu> implements IAppUserMenu_Service{
	@Autowired
	AppUserMenu_Repository repo;
	@Override
	protected JpaRepository<AppUserMenu, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<AppUserMenu> getuser_menu_by_menuid_and_userid(String menuid_link, long userid) {
		// TODO Auto-generated method stub
		return repo.getby_menu_and_user(userid, menuid_link);
	}

}
