package vn.gpay.gsmart.core.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Task_Repository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

}
