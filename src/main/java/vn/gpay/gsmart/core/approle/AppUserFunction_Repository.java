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
public interface AppUserFunction_Repository extends JpaRepository<AppUserFunction, Long>, JpaSpecificationExecutor<AppUserFunction>{
	@Query(value = "select c from AppUserFunction c "
			+ "where c.functionid_link = :functionid_link "
			+ "and userid_link = :userid_link")
	public List<AppUserFunction> getby_functionid_and_user(
			@Param ("functionid_link")final  Long functionid_link,
			@Param ("userid_link")final  Long userid_link);
}
