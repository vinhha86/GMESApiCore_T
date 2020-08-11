package vn.gpay.gsmart.core.tasktype_checklist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskType_CheckList_Repository extends JpaRepository<TaskType_CheckList, Long>, JpaSpecificationExecutor<TaskType_CheckList>{
	@Query(value = "select c from TaskType_CheckList c where tasktypeid_link = :tasktypeid_link")
	public List<TaskType_CheckList> getby_tasktype(
			@Param ("tasktypeid_link")final  int tasktypeid_link);
}
