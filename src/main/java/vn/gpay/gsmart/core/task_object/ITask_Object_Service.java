package vn.gpay.gsmart.core.task_object;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_Object_Service extends Operations<Task_Object> {
	List<Task_Object> getbyObjectType_and_objectid_link(Long objecttypeid_link, Long objectid_link);
	List<Task_Object> getbyTask(Long taskid_link);
	List<Task_Object> getby_pcontract_and_product(Long pcontractid_link,Long productid_link);
}
