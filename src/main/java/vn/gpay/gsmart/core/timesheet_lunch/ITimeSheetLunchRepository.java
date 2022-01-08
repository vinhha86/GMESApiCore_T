package vn.gpay.gsmart.core.timesheet_lunch;

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
public interface ITimeSheetLunchRepository
		extends JpaRepository<TimeSheetLunch, Long>, JpaSpecificationExecutor<TimeSheetLunch> {

//	@Query("SELECT a.id, a.code, a.fullname, b.workingdate, b.shifttypeid_link, b.isworking, b.islunch "
//			+ "FROM Personel a "
//			+ "full join TimeSheetLunch b on b.personnelid_link = a.id "
//			+ "where a.orgid_link = :orgid_link "
//			+ "and (b.workingdate is null or b.workingdate = :workingdate) "
//			)
//	public List<Object[]> getForTimeSheetLunch(
//			@Param ("orgid_link")final Long orgid_link,
//			@Param ("workingdate")final Date workingdate
//			);

	@Query("SELECT a " + "FROM TimeSheetLunch a " + "inner join Personel b on a.personnelid_link = b.id "
			+ "where b.orgmanagerid_link = :orgmanagerid_link  " + "and a.workingdate = :workingdate and b.status = 0")
	public List<TimeSheetLunch> getForTimeSheetLunch(@Param("orgmanagerid_link") final Long orgmanagerid_link,
			@Param("workingdate") final Date workingdate);
	
	@Query("SELECT a " + "FROM TimeSheetLunch a "
			+ "where (a.orgmanagerid_link = :orgmanagerid_link or a.orgid_link = :orgmanagerid_link)  " + "and a.workingdate = :workingdate")
	public List<TimeSheetLunch> getForTimeSheetLunchBeforeDay(@Param("orgmanagerid_link") final Long orgmanagerid_link,
			@Param("workingdate") final Date workingdate);

	@Query("SELECT distinct a " + "FROM TimeSheetLunch a " + "inner join Personel b on a.personnelid_link = b.id "
			+ "where ((b.orgid_link = :orgmanagerid_link and a.orgid_link is null) or a.orgid_link = :orgmanagerid_link) " + "and a.workingdate = :workingdate and b.status = 0")
	public List<TimeSheetLunch> getForTimeSheetLunchByGrant(@Param("orgmanagerid_link") final Long orgmanagerid_link,
			@Param("workingdate") final Date workingdate);
	
	@Query("SELECT distinct a " + "FROM TimeSheetLunch a " + "inner join Personel b on a.personnelid_link = b.id "
			+ "where b.orgid_link = :orgid_link " + "and a.workingdate = :workingdate " + "and a.islunch is true ")
	public List<TimeSheetLunch> getForTimeSheetLunch_byOrg_Date(@Param("orgid_link") final Long orgid_link,
			@Param("workingdate") final Date workingdate);

	@Query("SELECT a " + "FROM TimeSheetLunch a " + "inner join Personel b on a.personnelid_link = b.id "
			+ "where b.orgid_link = :orgid_link " + "and a.workingdate = :workingdate ")
	public List<TimeSheetLunch> getForUpdateStatusTimeSheetLunch(@Param("orgid_link") final Long orgid_link,
			@Param("workingdate") final Date workingdate);

	@Query("SELECT a " + "FROM TimeSheetLunch a " + "where a.personnelid_link = :personnelid_link "
			+ "and a.workingdate = :workingdate " + "and a.shifttypeid_link = :shifttypeid_link ")
	public List<TimeSheetLunch> getByPersonnelDateAndShift(@Param("personnelid_link") final Long personnelid_link,
			@Param("workingdate") final Date workingdate, @Param("shifttypeid_link") final Integer shifttypeid_link);

	@Query("SELECT a " + "FROM TimeSheetLunch a " + "where a.personnelid_link = :personnelid_link "
			+ "and a.isworking = true " + "and a.workingdate >= :workingdate_start "
			+ "and a.workingdate <= :workingdate_end " + "order by a.workingdate, shifttypeid_link asc")
	public List<TimeSheetLunch> getByPersonnelDate(@Param("personnelid_link") final Long personnelid_link,
			@Param("workingdate_start") final Date workingdate_start,
			@Param("workingdate_end") final Date workingdate_end);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "inner join TimesheetShiftType c on c.id = a.shifttypeid_link "
			+ "inner join TimesheetShiftTypeOrg d on d.timesheet_shift_type_id_link = c.id "
			+ "where (b.orgid_link = :orgid_link or b.orgmanagerid_link = :orgid_link ) " 
			+ "and a.workingdate = :workingdate "
			+ "and a.status = :status "
			+ "and d.id = :timesheetShiftTypeOrg_id "
			)
	public List<TimeSheetLunch> getByConfirmStatus(
			@Param("timesheetShiftTypeOrg_id") final Long timesheetShiftTypeOrg_id,
			@Param("orgid_link") final Long orgid_link,
			@Param("workingdate") final Date workingdate,
			@Param("status") final Integer status
			);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "inner join TimesheetShiftType c on c.id = a.shifttypeid_link "
			+ "inner join TimesheetShiftTypeOrg d on d.timesheet_shift_type_id_link = c.id "
			+ "where (b.orgid_link = :orgid_link or b.orgmanagerid_link = :orgid_link ) " 
			+ "and a.workingdate = :workingdate "
			+ "and d.id in :listIds "
			)
	public List<TimeSheetLunch> getBy_multiShift(
			@Param("orgid_link") final Long orgid_link,
			@Param("workingdate") final Date workingdate,
			@Param("listIds") final List<Long> listIds
			);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "where (a.isworking = :isworking or a.isworking is null) "
			+ "and (a.islunch = :islunch or a.islunch is null) "
			)
	public List<TimeSheetLunch> getBy_isworking_islunch(
			@Param("isworking") final Boolean isworking,
			@Param("islunch") final Boolean islunch
			);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "inner join TimesheetShiftType c on c.id = a.shifttypeid_link "
			+ "inner join TimesheetShiftTypeOrg d on d.timesheet_shift_type_id_link = c.id "
			+ "where (b.orgid_link = :orgid_link or b.orgmanagerid_link = :orgid_link ) " 
			+ "and a.shifttypeid_link = :shifttypeid_link "
			+ "and a.workingdate = :workingdate "
			)
	public List<TimeSheetLunch> getByOrg_Shift(
			@Param("orgid_link") final Long orgid_link,
			@Param("shifttypeid_link") final Integer shifttypeid_link,
			@Param("workingdate") final Date workingdate
			);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "inner join TimesheetShiftType c on c.id = a.shifttypeid_link "
			+ "inner join TimesheetShiftTypeOrg d on d.timesheet_shift_type_id_link = c.id "
			+ "where (b.orgid_link = :orgid_link or b.orgmanagerid_link = :orgid_link ) " 
			+ "and a.shifttypeid_link = :shifttypeid_link "
			+ "and a.workingdate = :workingdate "
			+ "and a.is_bo_sung = true "
			)
	public List<TimeSheetLunch> getByOrg_Shift_Them(
			@Param("orgid_link") final Long orgid_link,
			@Param("shifttypeid_link") final Integer shifttypeid_link,
			@Param("workingdate") final Date workingdate
			);
	
	@Query("SELECT distinct a FROM TimeSheetLunch a " 
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "inner join TimesheetShiftType c on c.id = a.shifttypeid_link "
			+ "inner join TimesheetShiftTypeOrg d on d.timesheet_shift_type_id_link = c.id "
			+ "where (b.orgid_link = :orgid_link or b.orgmanagerid_link = :orgid_link ) " 
			+ "and a.shifttypeid_link = :shifttypeid_link "
			+ "and a.workingdate = :workingdate "
			+ "and (a.is_bo_sung = false or a.is_bo_sung is null) "
			)
	public List<TimeSheetLunch> getByOrg_Shift_DangKy(
			@Param("orgid_link") final Long orgid_link,
			@Param("shifttypeid_link") final Integer shifttypeid_link,
			@Param("workingdate") final Date workingdate
			);
}
