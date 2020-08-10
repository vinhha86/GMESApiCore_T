package vn.gpay.gsmart.core.task_flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_Flow_Service extends AbstractService<Task_Flow> implements ITask_Flow_Service {
	@Autowired ITask_Flow_Repository repo;
	@Override
	protected JpaRepository<Task_Flow, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
