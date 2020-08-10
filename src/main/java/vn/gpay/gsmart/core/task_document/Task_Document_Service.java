package vn.gpay.gsmart.core.task_document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_Document_Service extends AbstractService<Task_Document> implements ITask_Document_Service {
	@Autowired ITask_Document_Repository repo;
	@Override
	protected JpaRepository<Task_Document, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
