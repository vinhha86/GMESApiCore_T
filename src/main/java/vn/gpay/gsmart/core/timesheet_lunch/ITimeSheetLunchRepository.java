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

	@Query("SELECT distinct a " + "FROM TimeSheetLunch a " + "inner join Personel b on a.personnelid_link = b.id "
			+ "where b.orgid_link = :orgmanagerid_link " + "and a.workingdate = :workingdate and b.status = 0")
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
}
