package vn.gpay.gsmart.core.timesheet_lunch_khach;

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
public interface ITimeSheetLunchLKhachRepository
		extends JpaRepository<TimeSheetLunchKhach, Long>, JpaSpecificationExecutor<TimeSheetLunchKhach> {

	@Query("SELECT a "
			+ "FROM TimeSheetLunchKhach a inner join TimesheetShiftTypeOrg b on a.shifttype_orgid_link = b.id "
			+ "where a.orgid_link = :orgid_link  "
			+ "and a.day = :day and b.timesheet_shift_type_id_link = :shifttypeid_link")
	public List<TimeSheetLunchKhach> getByDate_Ca_Org(@Param("shifttypeid_link") final Long shifttypeid_link,
			@Param("day") final Date day, @Param("orgid_link") final Long orgid_link);

	@Query("SELECT a " + "FROM TimeSheetLunchKhach a " + "where a.orgid_link = :orgid_link  " + "and a.day = :day")
	public List<TimeSheetLunchKhach> getByDate_Org(@Param("day") final Date day,
			@Param("orgid_link") final Long orgid_link);
}
