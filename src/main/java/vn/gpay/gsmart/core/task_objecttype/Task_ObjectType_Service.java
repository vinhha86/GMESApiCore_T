package vn.gpay.gsmart.core.task_objecttype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Task_ObjectType_Service extends AbstractService<Task_ObjectType> implements ITask_ObjectType_Service {
	@Autowired ITask_ObjectType_Repository repo;
	
	@Override
	protected JpaRepository<Task_ObjectType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
