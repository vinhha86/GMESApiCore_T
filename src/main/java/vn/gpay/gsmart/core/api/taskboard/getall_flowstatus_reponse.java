package vn.gpay.gsmart.core.api.taskboard;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.task_flow_status.Task_Flow_Status;

public class getall_flowstatus_reponse extends ResponseBase{
	public List<Task_Flow_Status> data;
}
