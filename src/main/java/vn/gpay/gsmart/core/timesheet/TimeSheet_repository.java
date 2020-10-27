package vn.gpay.gsmart.core.timesheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TimeSheet_repository extends JpaRepository<TimeSheet, Long>, JpaSpecificationExecutor<TimeSheet> {

}
