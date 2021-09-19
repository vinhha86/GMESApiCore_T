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
public interface ITimesheetAbsenceRepository
		extends JpaRepository<TimesheetAbsence, Long>, JpaSpecificationExecutor<TimesheetAbsence> {
	@Query(value = "select c from TimesheetAbsence c " + "inner join Personel b on b.id = c.personnelid_link "
			+ "where (b.orgmanagerid_link = :orgFactory or :orgFactory = 0) "
			+ "and lower(b.code) like lower(concat('%',:personnelCode,'%')) "
			+ "and lower(b.fullname) like lower(concat('%',:personnelName,'%')) "
			+ "and (CAST(:datefrom AS date) IS NULL or c.absencedate_from >= :datefrom) "
			+ "and (CAST(:dateto AS date) IS NULL or c.absencedate_to <= :dateto) "
//			+ "and (c.absencedate_from >= :datefrom) "
//			+ "and (c.absencedate_to <= :dateto) "
			+ "and (c.absencetypeid_link = :timeSheetAbsenceType or :timeSheetAbsenceType = 0) ")
	public List<TimesheetAbsence> getbypaging(@Param("orgFactory") final Long orgFactory,
			@Param("personnelCode") final String personnelCode, @Param("personnelName") final String personnelName,
			@Param("datefrom") final Date datefrom, @Param("dateto") final Date dateto,
			@Param("timeSheetAbsenceType") final Long timeSheetAbsenceType);

//	WHERE (Column1 = @Var1 OR @Var1 IS NULL)
//	AND (Column2 = @Var2 OR @Var2 IS NULL)
//	+ "and lower(c.po_buyer) like lower(concat('%',:po_buyer,'%')) "




	@Query(value = "select count(c.id) from TimesheetAbsence c "
			+ " inner join Personel b on c.personnelid_link = b.id "
			+ " where b.orgid_link = :Org_grant_id_link and absencedate_from <= :today and absencedate_to >= :today")
	public int getbyOrg_grant_id_link_Today(@Param("Org_grant_id_link") final Long Org_grant_id_link,
			@Param("today") final Date today);

	
	//lây danh sách theo đơn vị - của tài khoản quản lý 
	@Query(value="select c from TimesheetAbsence c "
			+ " inner join Personel b on c.personnelid_link = b.id " 
			+ " where b.orgmanagerid_link = :org_id "
			+ " and (b.orgmanagerid_link = :orgFactory or :orgFactory = 0) "
			+ " and lower(b.code) like lower(concat('%',:personnelCode,'%')) "
			+ " and lower(b.fullname) like lower(concat('%',:personnelName,'%')) "
			+ " and (c.absencetypeid_link = :timeSheetAbsenceType or :timeSheetAbsenceType = 0) "
			+ "	and  c.absencedate_from >= :datefrom "
			+ " and  c.absencedate_to <= :dateto")
	public List<TimesheetAbsence> getbyOrgid(
			@Param ("orgFactory")final Long orgFactory,
			@Param ("org_id")final Long org_id,
			@Param ("datefrom")final Date datefrom,
			@Param ("dateto")final Date dateto,
			@Param ("personnelCode")final String personnelCode,
			@Param ("personnelName")final String personnelName,
			@Param ("timeSheetAbsenceType")final Long timeSheetAbsenceType);
	//lấy danh sách theo tổ - của tài khoản quản lý
	@Query(value="select c from TimesheetAbsence c "
			+ " inner join Personel b on c.personnelid_link = b.id "
			+ " where b.orgid_link = :Org_grant_id_link "
			+ " and lower(b.code) like lower(concat('%',:personnelCode,'%')) "
			+ " and lower(b.fullname) like lower(concat('%',:personnelName,'%')) "
			+ " and (c.absencetypeid_link = :timeSheetAbsenceType or :timeSheetAbsenceType = 0) "
			+ "	and  c.absencedate_from >= :datefrom "
			+ " and  c.absencedate_to <= :dateto")
	public List<TimesheetAbsence> getbyOrg_grant_id_link(
			@Param ("Org_grant_id_link")final Long Org_grant_id_link,
			@Param ("datefrom")final Date datefrom,
			@Param ("dateto")final Date dateto,
			@Param ("personnelCode")final String personnelCode,
			@Param ("personnelName")final String personnelName,
			@Param ("timeSheetAbsenceType")final Long timeSheetAbsenceType);
	
	//lấy tất cả danh sách báo nghỉ theo ngày
	@Query(value="select c from TimesheetAbsence c "
			+ " inner join Personel b on c.personnelid_link = b.id "
			+ " where   c.absencedate_from >= :datefrom "
			+ " and lower(b.code) like lower(concat('%',:personnelCode,'%')) "
			+ " and lower(b.fullname) like lower(concat('%',:personnelName,'%')) "
			+ " and (c.absencetypeid_link = :timeSheetAbsenceType or :timeSheetAbsenceType = 0) "
			+ " and (b.orgmanagerid_link = :orgFactory or :orgFactory = 0) "
			+ " and  c.absencedate_to <= :dateto")
	public List<TimesheetAbsence> getAllbydate(
			@Param ("orgFactory")final Long orgFactory,
			@Param ("datefrom")final Date datefrom,
			@Param ("dateto")final Date dateto,
			@Param ("personnelCode")final String personnelCode,
			@Param ("personnelName")final String personnelName,
			@Param ("timeSheetAbsenceType")final Long timeSheetAbsenceType);

}
