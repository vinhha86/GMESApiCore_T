package vn.gpay.gsmart.core.timesheet_sum_col_type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TimeSheet_Sum_ColType_Repo extends JpaRepository<TimeSheet_Sum_ColType, Long>, JpaSpecificationExecutor<TimeSheet_Sum_ColType> {

}
