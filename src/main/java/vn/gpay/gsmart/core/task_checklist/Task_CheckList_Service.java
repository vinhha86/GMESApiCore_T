package vn.gpay.gsmart.core.task_checklist;

import java.util.List;

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
	@Override
	public List<Task_CheckList> getby_taskid_link(Long taskid_link) {
		// TODO Auto-generated method stub
		return repo.getby_taskid_link(taskid_link);
	}
	@Override
	public List<Task_CheckList> getby_taskid_link_and_typechecklist(Long taskid_link, Long tasktype_checklist_id_link) {
		// TODO Auto-generated method stub
		return repo.getby_taskid_link_and_tasktype_checlist_id_link(taskid_link, tasktype_checklist_id_link);
	}

}
