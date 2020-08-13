package vn.gpay.gsmart.core.task_flow_status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Flow_Status_Repository extends JpaRepository<Task_Flow_Status, Long>, JpaSpecificationExecutor<Task_Flow_Status> {

}
