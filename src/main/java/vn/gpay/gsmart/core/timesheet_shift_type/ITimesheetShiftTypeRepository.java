package vn.gpay.gsmart.core.timesheet_shift_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ITimesheetShiftTypeRepository extends JpaRepository<TimesheetShiftType, Long>, JpaSpecificationExecutor<TimesheetShiftType>{
	//lay danh sách ca chưa có trong đơn vị quản 
	@Query(value = "select c from TimesheetShiftType c where c.id not in "
			+ " (select timesheet_shift_type_id_link from TimesheetShiftTypeOrg  where orgid_link = :id ) ")
	public List<TimesheetShiftType> getTimesheetShiftType_ByIdOrgid_link(@Param ("id")final  Long id);
}
