package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LaborLevelRepository extends JpaRepository<LaborLevel, Long>{
	public List<LaborLevel> findAllByOrderByIdAsc();
}
