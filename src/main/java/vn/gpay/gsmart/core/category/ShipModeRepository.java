package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ShipModeRepository extends JpaRepository<ShipMode, Long> {
	@Query(value = "select c from ShipMode c "
			+ "where trim(lower(replace(c.name,' ',''))) = trim(lower(replace(:name, ' ','')))")
	public List<ShipMode> getby_name(
			@Param ("name")final  String name);
}
