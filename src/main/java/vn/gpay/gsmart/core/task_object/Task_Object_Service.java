package vn.gpay.gsmart.core.task_object;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_Object_Service extends AbstractService<Task_Object> implements ITask_Object_Service {
	@Autowired ITask_Object_Repository repo;
	
	@Override
	protected JpaRepository<Task_Object, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Task_Object> getbyObjectType_and_objectid_link(Long objecttypeid_link, Long objectid_link) {
		// TODO Auto-generated method stub
		return repo.getby_tasktype_and_objectid_link(objecttypeid_link, objectid_link);
	}

}
