package vn.gpay.gsmart.core.task_flow_status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_Flow_Status_Service extends AbstractService<Task_Flow_Status> implements ITask_Flow_Status_Service{
	@Autowired ITask_Flow_Status_Repository repo;
	
	@Override
	protected JpaRepository<Task_Flow_Status, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
