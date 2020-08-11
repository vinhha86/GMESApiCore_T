package vn.gpay.gsmart.core.task_grant;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_Grant_Service extends Operations<Task_Grant>{
	List<Task_Grant> getby_tasktype_and_org(long tasktypeid_link, Long orgid_link);
}
