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
public interface AppUserMenu_Repository extends JpaRepository<AppUserMenu, Long>, JpaSpecificationExecutor<AppUserMenu> {
	@Query(value = "select c from AppUserMenu c "
			+ "where c.userid = :userid "
			+ "and c.menuid = :menuid")
	public List<AppUserMenu> getby_menu_and_user(
			@Param ("userid")final  Long userid,
			@Param ("menuid")final  String menuid);
}
