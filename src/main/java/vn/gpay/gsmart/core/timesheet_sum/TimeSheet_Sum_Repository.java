package vn.gpay.gsmart.core.timesheet_sum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TimeSheet_Sum_Repository extends JpaRepository<TimeSheet_Sum, Long>, JpaSpecificationExecutor<TimeSheet_Sum> {
	@Query(value = "select c from TimeSheet_Sum c where"
			+ " c.personnelid_link = :personnelid_link"
			+ " and c.year = :year"
			+ " and c.month = :month"
			+ " and c.sumcolid_link = :sumcolid_link"
			)
	public List<TimeSheet_Sum> getByKey(
			@Param ("personnelid_link")final  Long personnelid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month,
			@Param ("sumcolid_link")final  Integer sumcolid_link
			);
	
	@Query(value = "select c from TimeSheet_Sum c inner join Personel d on c.personnelid_link = d.id "
			+ "where d.orgmanagerid_link = :orgid_link"
			+ " and c.year = :year and c.month = :month")
	public List<TimeSheet_Sum> getall_bymanageorg(@Param ("orgid_link")final  Long orgid_link,
			@Param ("year")final  Integer year,
			@Param ("month")final  Integer month);
}
