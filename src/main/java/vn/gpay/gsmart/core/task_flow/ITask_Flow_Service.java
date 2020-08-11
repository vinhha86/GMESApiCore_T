package vn.gpay.gsmart.core.task_flow;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_Flow_Service extends Operations<Task_Flow> {
	List<Task_Flow> getby_task(long taskid_link);
}
