package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LaborLevelRepository extends JpaRepository<LaborLevel, Long>{
	public List<LaborLevel> findAllByOrderByIdAsc();
	
	@Query("SELECT c FROM LaborLevel c "
			+ "where trim(lower(c.name)) = trim(lower(:name)) "
			)
	public List<LaborLevel> findByName(
			@Param ("name")final String name
			);
}
