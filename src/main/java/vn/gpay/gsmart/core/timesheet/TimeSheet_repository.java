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


//	@Query(value = "SELECT count(distinct(a.register_code)), date(a.timerecorded) "
//			+ "from timesheet_inout a "
////			+ "where date(a.timerecorded) <= '2020-10-27' and date(a.timerecorded) >= '2020-10-18' "
////			+ "where date(a.timerecorded) <= :today and date(a.timerecorded) >= :tenDaysAgo "
//			+ "group by date(a.timerecorded) "
//			+ "order by date(a.timerecorded) desc "
//			+ "limit 10 "
//			, nativeQuery = true)
//	public List<Object[]> getForRegisterCodeCountChart(
//			@Param ("tenDaysAgo")final Date tenDaysAgo, 
//			@Param ("today")final Date today);
	
	@Query(value = "SELECT count(distinct a.register_code), year(a.timerecorded), month(a.timerecorded), day(a.timerecorded), "
			+ "b.orgmanagerid_link "
			+ "from TimeSheet a "
			+ "inner join Personel b on a.register_code = b.code "
			+ "inner join Org c on b.orgmanagerid_link = c.id "
			+ "where a.timerecorded > :tenDaysAgo and a.timerecorded < :today "
			+ "group by year(a.timerecorded), month(a.timerecorded), day(a.timerecorded), b.orgmanagerid_link "
			+ "order by b.orgmanagerid_link "
			)
	public List<Object[]> getForRegisterCodeCountChart(
			@Param ("tenDaysAgo")final Date tenDaysAgo, 
			@Param ("today")final Date today
			);
	
	
//	SELECT count(*),
//	EXTRACT(year FROM timerecorded) as year, EXTRACT(month FROM timerecorded) as month,EXTRACT(day FROM timerecorded) as day,
//	c.name
//		FROM gsmart_inv.timesheet_inout a
//		inner join gsmart_inv.personnel b on a.register_code = b.code
//		inner join gsmart_inv.org c on c.id = b.orgmanagerid_link
//		where timerecorded > '2020-08-20' and timerecorded < '2020-08-25'
//		group by EXTRACT(year FROM timerecorded), EXTRACT(month FROM timerecorded),EXTRACT(day FROM timerecorded),
//		c.name
//		order by c.name
//		;
	
}
