package vn.gpay.gsmart.core.tasktype;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITaskType_Repository extends JpaRepository<TaskType, Long>, JpaSpecificationExecutor<TaskType> {
	
}
