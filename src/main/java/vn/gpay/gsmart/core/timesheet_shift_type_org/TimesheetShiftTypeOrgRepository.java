package vn.gpay.gsmart.core.timesheet_shift_type_org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface TimesheetShiftTypeOrgRepository extends JpaRepository<TimesheetShiftTypeOrg, Long>, JpaSpecificationExecutor<TimesheetShiftTypeOrg>{
//	@Query(value = "select c from TimesheetShiftTypeOrg c where c.name = :name ")
//	public List<TimesheetShiftTypeOrg> getByName(@Param ("name")final  String name);
//	
	@Query(value = "select c from TimesheetShiftTypeOrg c where c.id = 1 ")
	public List<TimesheetShiftTypeOrg>getShift1ForAbsence();
	//lay cao theo don vi
	@Query(value = "select c from TimesheetShiftTypeOrg c where c.orgid_link = :orgid_link ")
	public List<TimesheetShiftTypeOrg> getByOrgid_link(@Param ("orgid_link")final  Long orgid_link);
}
