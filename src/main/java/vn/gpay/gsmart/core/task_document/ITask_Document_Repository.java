package vn.gpay.gsmart.core.task_document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ITask_Document_Repository extends JpaRepository<Task_Document, Long>, JpaSpecificationExecutor<Task_Document> {

}
