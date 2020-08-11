package vn.gpay.gsmart.core.task_grant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_Grant_Service extends AbstractService<Task_Grant> implements ITask_Grant_Service {
	@Autowired ITask_Grant_Repository repo;
	
	@Override
	protected JpaRepository<Task_Grant, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Task_Grant> getby_tasktype_and_org(long tasktypeid_link, Long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getby_tasktype_and_org(tasktypeid_link, orgid_link);
	}

}
