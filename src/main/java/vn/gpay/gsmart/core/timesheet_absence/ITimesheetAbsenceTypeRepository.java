package vn.gpay.gsmart.core.timesheet_absence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITimesheetAbsenceTypeRepository extends JpaRepository<TimesheetAbsenceType, Long>, JpaSpecificationExecutor<TimesheetAbsenceType>{

}
