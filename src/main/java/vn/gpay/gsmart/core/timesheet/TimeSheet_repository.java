package vn.gpay.gsmart.core.timesheet;

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
public interface TimeSheet_repository extends JpaRepository<TimeSheet, Long>, JpaSpecificationExecutor<TimeSheet> {


	@Query(value = "SELECT count(distinct(a.register_code)), date(a.timerecorded) "
			+ "from timesheet_inout a "
//			+ "where date(a.timerecorded) <= '2020-10-27' and date(a.timerecorded) >= '2020-10-18' "
//			+ "where date(a.timerecorded) <= :today and date(a.timerecorded) >= :tenDaysAgo "
			+ "group by date(a.timerecorded) "
			+ "order by date(a.timerecorded) desc "
			+ "limit 10 "
			, nativeQuery = true)
	public List<Object[]> getForRegisterCodeCountChart(
			@Param ("tenDaysAgo")final Date tenDaysAgo, 
			@Param ("today")final Date today);
	
	
	@Query(value = "SELECT c from timesheet_inout c where"
			+ " c.timerecorded >= :datefrom"
			+ " c.timerecorded <= :dateto"
			+ " order by c.timerecorded asc")
	public List<TimeSheet> getByTime(
			@Param ("register_code")final String register_code, 
			@Param ("datefrom")final Date datefrom,
			@Param ("dateto")final Date dateto);
	
}
