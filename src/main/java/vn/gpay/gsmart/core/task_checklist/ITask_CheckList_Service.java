package vn.gpay.gsmart.core.task_checklist;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITask_CheckList_Service extends Operations<Task_CheckList> {
	public List<Task_CheckList> getby_taskid_link(Long taskid_link);
	public List<Task_CheckList> getby_taskid_link_and_typechecklist(Long taskid_link, Long tasktype_checklits_id_link);
}
