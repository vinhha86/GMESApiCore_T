package vn.gpay.gsmart.core.tasktype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TaskType_Service extends AbstractService<TaskType> implements ITaskType_Service {
	@Autowired ITaskType_Repository repo;
	@Override
	protected JpaRepository<TaskType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
