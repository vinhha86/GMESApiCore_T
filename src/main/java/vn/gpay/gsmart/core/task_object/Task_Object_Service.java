package vn.gpay.gsmart.core.task_object;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.TaskObjectType_Name;

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

	@Override
	public List<Task_Object> getbyTask(Long taskid_link) {
		// TODO Auto-generated method stub
		return repo.getbytask(taskid_link);
	}

	@Override
	public List<Long> getby_pcontract_and_product(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		List<Long> list_pcontract = repo.getlistid_by_tasktype_and_objectid_link((long)TaskObjectType_Name.DonHang, pcontractid_link);
		List<Long> list_product = repo.getlistid_by_tasktype_and_objectid_link((long)TaskObjectType_Name.SanPham, productid_link);
		
		//bo nhung task ko co trong list_product
		list_pcontract.removeIf(c-> !list_product.contains(c));
		
		return list_pcontract;
	}

}
