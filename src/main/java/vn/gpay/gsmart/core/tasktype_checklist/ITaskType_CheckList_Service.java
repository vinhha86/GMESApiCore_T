package vn.gpay.gsmart.core.tasktype_checklist;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITaskType_CheckList_Service extends Operations<TaskType_CheckList>{
	List<TaskType_CheckList> getby_tasktype(long tasktypeid_link);
}
