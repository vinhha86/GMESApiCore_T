package vn.gpay.gsmart.core.holiday;

import java.util.Date;
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
	
	@Query(value = "select c from Holiday c where c.orgrootid_link =:orgrootid_link and year in :year")
	public List<Holiday> getby_many_year(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("year")final  List<Integer> year);
	
	@Query(value = "select distinct c.year from Holiday c where c.year >= :year")
	public List<Integer> getAllYears(@Param ("year")final  Integer year);
	
	@Query(value = "select c from Holiday c where c.day = :day and dayto = :dayto")
	public List<Holiday> getby_date(
			@Param ("day")final  Date day,
			@Param ("dayto")final  Date dayto);
	
	@Query(value = "select c from Holiday c where c.day = :day")
	public List<Holiday> getByDate(
			@Param ("day")final  Date day);
}
