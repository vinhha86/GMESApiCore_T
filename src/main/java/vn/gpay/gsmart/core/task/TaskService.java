package vn.gpay.gsmart.core.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TaskService extends AbstractService<Task> implements ITask_Service {
	@Autowired Task_Repository repo;
	@Override
	protected JpaRepository<Task, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<Task> getby_user(Long userid_link) {
		// TODO Auto-generated method stub
		return repo.getby_user(userid_link);
	}

}
