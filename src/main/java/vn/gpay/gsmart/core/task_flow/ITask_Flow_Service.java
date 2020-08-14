package vn.gpay.gsmart.core.task_flow;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_Flow_Service extends Operations<Task_Flow> {
	List<Task_Flow> getby_task(long taskid_link);
	List<Task_Flow> get_lasttest_by_userto(long userto_id_link, long taskid_link);
}
