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
	//lấy id ca làm việc theo name
	@Query(value = "select c.id from TimesheetShiftType c where c.name LIKE :name ")
	public Long getTimesheetShiftTypeID_ByName(@Param ("name")final  String id);
	//lấy danh sách ca làm việc trong đơn vị
	@Query(value = "select c from TimesheetShiftType c inner join TimesheetShiftTypeOrg b "
			+ "on c.id = b.timesheet_shift_type_id_link "
			+ " where b.orgid_link = :orgid_link ")
	public List<TimesheetShiftType> getShift_ByIdOrgid_link(@Param ("orgid_link")final  Long orgid_link);
}
