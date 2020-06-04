package vn.gpay.gsmart.core.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class UserMenuServiceImpl extends AbstractService<UserMenu> implements IUserMenuService{

	@Autowired UserMenuRepository repository;
	@Override
	protected JpaRepository<UserMenu, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<UserMenu> findByUserid(long userid) {
		// TODO Auto-generated method stub
		return repository.findByUserid(userid);
	}

}
