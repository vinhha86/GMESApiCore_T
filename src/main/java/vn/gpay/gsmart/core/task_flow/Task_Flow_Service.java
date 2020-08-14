package vn.gpay.gsmart.core.task_flow;

import java.util.List;

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
	@Override
	public List<Task_Flow> getby_task(long taskid_link) {
		// TODO Auto-generated method stub
		return repo.getby_task(taskid_link);
	}
	@Override
	public List<Task_Flow> get_lasttest_by_userto(long userto_id_link, long taskid_link) {
		// TODO Auto-generated method stub
		return repo.getby_task_and_userto(taskid_link, userto_id_link);
	}

}
