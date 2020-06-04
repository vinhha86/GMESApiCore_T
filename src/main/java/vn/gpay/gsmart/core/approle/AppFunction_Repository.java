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
public interface AppFunction_Repository extends JpaRepository<AppFunction, Long>, JpaSpecificationExecutor<AppFunction> {
	@Query(value = "select c from AppFunction c "
			+ "where c.menuid_link = :menuid_link")
	public List<AppFunction> getAppFunction_byMenu(
			@Param ("menuid_link")final  String menuid_link);
	
	@Query(value = "select c from AppFunction c "
			+ "inner join AppRoleFunction d on c.id = d.functionid_link "
			+ "where c.menuid_link = :menuid_link and d.roleid_link = :roleid_link")
	public List<AppFunction> getAppFunction_inMenu(
			@Param ("menuid_link")final  String menuid_link,
			@Param ("roleid_link")final  Long roleid_link);
}
