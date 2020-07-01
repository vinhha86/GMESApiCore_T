package vn.gpay.gsmart.core.holiday;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Holiday_repository extends JpaRepository<Holiday, Long>,JpaSpecificationExecutor<Holiday> {
	@Query(value = "select c from Holiday c where c.orgrootid_link =:orgrootid_link and year = :year")
	public List<Holiday> getby_year(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("year")final  Integer year);
}
