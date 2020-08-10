package vn.gpay.gsmart.core.task_flow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Flow_Repository extends JpaRepository<Task_Flow, Long>, JpaSpecificationExecutor<Task_Flow> {

}
