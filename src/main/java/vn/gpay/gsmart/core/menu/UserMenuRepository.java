package vn.gpay.gsmart.core.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserMenuRepository extends JpaRepository<UserMenu, Long>{

	@Query(value = "Select a from UserMenu a where a.userid=:userid")
	public List<UserMenu>findByUserid(@Param ("userid")final Long userid);
}
