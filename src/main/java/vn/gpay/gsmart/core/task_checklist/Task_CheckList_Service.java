package vn.gpay.gsmart.core.task_checklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_CheckList_Service extends AbstractService<Task_CheckList> implements ITask_CheckList_Service {
	@Autowired ITask_CheckList_Repository repo;
	@Override
	protected JpaRepository<Task_CheckList, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
