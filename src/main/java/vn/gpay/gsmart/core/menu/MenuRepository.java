package vn.gpay.gsmart.core.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MenuRepository extends JpaRepository<Menu, String>{

	@Query(value="select a from Menu a "
			+ "inner join AppRoleMenu b on a.id = b.menuid_link "
			+ "inner join AppRole_User c on b.roleid_link = c.role_id "
			+ "where c.user_id = :userid order by index desc")
	public List<Menu> findByUserid(@Param ("userid")final long userid);
	
	@Query(value="select a from Menu a"
			+ " where parent_id = :parent_id")
	public List<Menu> getListMenu_byParent(@Param ("parent_id")final String parent_id);
	
	@Query(value="select a from Menu a "
			+ "inner join AppRoleMenu b on a.id = b.menuid_link "
			+ " where roleid_link = :roleid_link")
	public List<Menu> getmenu_inroleid(@Param ("roleid_link")final Long roleid_link);
	
	//lay ds menu
	@Query(value="select a from Menu a")
	public List<Menu> getListMenu();
}
