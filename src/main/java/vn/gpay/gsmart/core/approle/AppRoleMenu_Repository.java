package vn.gpay.gsmart.core.approle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional
public interface AppRoleMenu_Repository extends JpaRepository<AppRoleMenu, Long>, JpaSpecificationExecutor<AppRoleMenu> {
	@Query(value = "select c.menuid_link from AppRoleMenu c where c.roleid_link = :roleid_link ")
	public List<String> getmenuid_byRole(@Param ("roleid_link")final  Long roleid_link);
	
	@Query(value = "select c.menuid_link from AppRoleMenu c where c.roleid_link = :roleid_link ")
	public List<String> getmenuid_inRole(@Param ("roleid_link")final  Long roleid_link);
	
	
	@Query(value = "select c from AppRoleMenu c "
			+ "where c.roleid_link = :roleid_link "
			+ "and c.menuid_link = :menuid_link")
	public List<AppRoleMenu> getRoleMenu_bymenuid_and_byRole(
			@Param ("roleid_link")final  Long roleid_link,
			@Param ("menuid_link")final  String menuid_link);
}
