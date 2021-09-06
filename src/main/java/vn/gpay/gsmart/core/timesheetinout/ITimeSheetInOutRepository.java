package vn.gpay.gsmart.core.timesheetinout;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ITimeSheetInOutRepository extends JpaRepository<TimeSheetInOut, Long>, JpaSpecificationExecutor<TimeSheetInOut>{
	@Query(value = "select c from TimeSheetInOut c where c.timerecorded  between  :todate and :fromdate order by c.timerecorded ")
	public List<TimeSheetInOut> getAll(
			@Param("todate") final Timestamp todate,
			@Param("fromdate") final Timestamp fromdate);
}
