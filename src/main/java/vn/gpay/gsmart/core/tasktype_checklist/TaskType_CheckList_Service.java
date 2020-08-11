package vn.gpay.gsmart.core.tasktype_checklist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TaskType_CheckList_Service extends AbstractService<TaskType_CheckList> implements ITaskType_CheckList_Service {
	@Autowired TaskType_CheckList_Repository repo;
	@Override
	protected JpaRepository<TaskType_CheckList, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<TaskType_CheckList> getby_tasktype(long tasktypeid_link) {
		// TODO Auto-generated method stub
		return repo.getby_tasktype(tasktypeid_link);
	}

}
