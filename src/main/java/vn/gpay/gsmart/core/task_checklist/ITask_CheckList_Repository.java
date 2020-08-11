package vn.gpay.gsmart.core.task_checklist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_CheckList_Repository extends JpaRepository<Task_CheckList, Long>, JpaSpecificationExecutor<Task_CheckList> {
	@Query(value = "select c from Task_CheckList c where taskid_link = :taskid_link")
	public List<Task_CheckList> getby_taskid_link(
			@Param ("taskid_link")final  long taskid_link);
}
