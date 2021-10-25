package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UnitRepository extends JpaRepository<Unit, Long> {
	@Query(value = "select c from Unit c where lower(c.code) =lower(:name)")
	public List<Unit> getByName(@Param("name") final String name);
}
