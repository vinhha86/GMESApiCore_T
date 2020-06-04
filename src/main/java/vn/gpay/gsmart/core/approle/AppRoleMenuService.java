package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class AppRoleMenuService extends AbstractService<AppRoleMenu> implements IAppRoleMenuService {
	@Autowired
	AppRoleMenu_Repository repo;
	@Override
	protected JpaRepository<AppRoleMenu, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<String> getmenuid_byrole(Long roleid_link) {
		// TODO Auto-generated method stub
		return repo.getmenuid_byRole(roleid_link);
	}
	@Override
	public AppRoleMenu getRoleMenu_by_MenuAndRole(String menuid_link, long roleid_link) {
		// TODO Auto-generated method stub
		List<AppRoleMenu> list = repo.getRoleMenu_bymenuid_and_byRole(roleid_link, menuid_link);
		if(list.size() >0 )
			return list.get(0);
		return null;
	}

}
