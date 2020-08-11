package vn.gpay.gsmart.core.task_flow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Flow_Repository extends JpaRepository<Task_Flow, Long>, JpaSpecificationExecutor<Task_Flow> {
	@Query(value = "select c from Task_Flow c where taskid_link = :taskid_link")
	public List<Task_Flow> getby_task(
			@Param ("taskid_link")final  long taskid_link);
}
