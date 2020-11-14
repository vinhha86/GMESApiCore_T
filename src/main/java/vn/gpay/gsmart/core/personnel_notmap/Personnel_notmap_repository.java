package vn.gpay.gsmart.core.personnel_notmap;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Personnel_notmap_repository extends JpaRepository<Personnel_notmap, Long>,JpaSpecificationExecutor<Personnel_notmap> {
	@Query("SELECT c FROM Personnel_notmap c "
			+ "where c.register_code = :register_code  ")
	public List<Personnel_notmap> getby_registercode(
			@Param ("register_code")final String register_code);
}
