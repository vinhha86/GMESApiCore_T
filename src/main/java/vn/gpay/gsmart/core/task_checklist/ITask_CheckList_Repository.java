package vn.gpay.gsmart.core.task_checklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_CheckList_Repository extends JpaRepository<Task_CheckList, Long>, JpaSpecificationExecutor<Task_CheckList> {

}
