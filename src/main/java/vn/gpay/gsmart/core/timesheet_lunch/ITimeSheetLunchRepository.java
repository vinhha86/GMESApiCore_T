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
public interface ITimeSheetLunchRepository extends JpaRepository<TimeSheetLunch, Long>, JpaSpecificationExecutor<TimeSheetLunch>{
	
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
	
	@Query("SELECT a "
			+ "FROM TimeSheetLunch a "
			+ "inner join Personel b on a.personnelid_link = b.id "
			+ "where b.orgid_link = :orgid_link "
			+ "and a.workingdate = :workingdate "
			)
	public List<TimeSheetLunch> getForTimeSheetLunch(
			@Param ("orgid_link")final Long orgid_link,
			@Param ("workingdate")final Date workingdate
			);
	
	@Query("SELECT a "
			+ "FROM TimeSheetLunch a "
			+ "where a.personnelid_link = :personnelid_link "
			+ "and a.workingdate = :workingdate "
			+ "and a.shifttypeid_link = :shifttypeid_link "
			)
	public List<TimeSheetLunch> getByPersonnelDateAndShift(
			@Param ("personnelid_link")final Long personnelid_link,
			@Param ("workingdate")final Date workingdate,
			@Param ("shifttypeid_link")final Integer shifttypeid_link
			);
}
