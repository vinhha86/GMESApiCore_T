package vn.gpay.gsmart.core.timesheet_absence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ITimesheetAbsenceRepository extends JpaRepository<TimesheetAbsence, Long>, JpaSpecificationExecutor<TimesheetAbsence>{

}
