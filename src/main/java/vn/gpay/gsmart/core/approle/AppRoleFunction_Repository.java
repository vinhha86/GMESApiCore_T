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
public interface AppRoleFunction_Repository extends JpaRepository<AppRoleFunction, Long>, JpaSpecificationExecutor<AppRoleFunction> {
	@Query(value = "select c.functionid_link from AppRoleFunction c "
			+ "where c.roleid_link = :roleid_link")
	public List<Long> getfuntion_byrole(
			@Param ("roleid_link")final  Long roleid_link);
	
	@Query(value = "select c from AppRoleFunction c "
			+ "where c.roleid_link = :roleid_link "
			+ "and c.functionid_link = :functionid_link")
	public List<AppRoleFunction> getbyfuntion_and_byrole(
			@Param ("roleid_link")final  Long roleid_link,
			@Param ("functionid_link")final  Long functionid_link);
}
