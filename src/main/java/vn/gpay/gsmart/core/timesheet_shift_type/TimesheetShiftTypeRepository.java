package vn.gpay.gsmart.core.timesheet_shift_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface TimesheetShiftTypeRepository extends JpaRepository<TimesheetShiftType, Long>, JpaSpecificationExecutor<TimesheetShiftType>{
	@Query(value = "select c from TimesheetShiftType c where c.name = :name ")
	public List<TimesheetShiftType> getByName(@Param ("name")final  String name);
	
	@Query(value = "select c from TimesheetShiftType c where c.id = 1 ")
	public List<TimesheetShiftType>getShift1ForAbsence();
	//lay cao theo don vi
	@Query(value = "select c from TimesheetShiftType c where c.orgid_link = :orgid_link ")
	public List<TimesheetShiftType> getByOrgid_link(@Param ("orgid_link")final  Long orgid_link);
}
