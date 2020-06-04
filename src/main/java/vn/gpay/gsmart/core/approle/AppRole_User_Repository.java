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
public interface AppRole_User_Repository extends JpaRepository<AppRole_User, Long>, JpaSpecificationExecutor<AppRole_User> {
	@Query(value = "select c from AppRole_User c "
			+ "where c.user_id = :user_id "
			+ "and (role_id =:role_id or 0 =:role_id)")
	public List<AppRole_User> getby_role_and_user(
			@Param ("role_id")final  Long role_id,
			@Param ("user_id")final  Long user_id);
}
