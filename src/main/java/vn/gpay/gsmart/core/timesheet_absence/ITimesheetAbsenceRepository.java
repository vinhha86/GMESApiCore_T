package vn.gpay.gsmart.core.timesheet_absence;

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
public interface ITimesheetAbsenceRepository extends JpaRepository<TimesheetAbsence, Long>, JpaSpecificationExecutor<TimesheetAbsence>{
	@Query(value = "select c from TimesheetAbsence c "
			+ "inner join Personel b on b.id = c.personnelid_link "
			+ "where (b.orgmanagerid_link = :orgFactory or :orgFactory = 0) "
			+ "and lower(b.code) like lower(concat('%',:personnelCode,'%')) "
			+ "and lower(b.fullname) like lower(concat('%',:personnelName,'%')) "
			+ "and (CAST(:datefrom AS date) IS NULL or c.absencedate_from >= :datefrom) "
			+ "and (CAST(:dateto AS date) IS NULL or c.absencedate_to <= :dateto) "
//			+ "and (c.absencedate_from >= :datefrom) "
//			+ "and (c.absencedate_to <= :dateto) "
			+ "and (c.absencetypeid_link = :timeSheetAbsenceType or :timeSheetAbsenceType = 0) "
			)
	public List<TimesheetAbsence> getbypaging(
			@Param ("orgFactory")final Long orgFactory,
			@Param ("personnelCode")final String personnelCode,
			@Param ("personnelName")final String personnelName,
			@Param ("datefrom")final Date datefrom,
			@Param ("dateto")final Date dateto,
			@Param ("timeSheetAbsenceType")final Long timeSheetAbsenceType
			);
	
//	WHERE (Column1 = @Var1 OR @Var1 IS NULL)
//	AND (Column2 = @Var2 OR @Var2 IS NULL)
//	+ "and lower(c.po_buyer) like lower(concat('%',:po_buyer,'%')) "
}