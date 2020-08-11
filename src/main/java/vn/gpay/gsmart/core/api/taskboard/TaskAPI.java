package vn.gpay.gsmart.core.api.taskboard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task.TaskBinding;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.SubTask;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/task")
public class TaskAPI {
	@Autowired ITask_Service taskService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired Common commonService;
	
	@RequestMapping(value = "/getby_user",method = RequestMethod.POST)
	public ResponseEntity<getby_user_response> Product_GetAll(HttpServletRequest request ) {
		getby_user_response response = new getby_user_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Task> listTask = taskService.getby_user(user.getId());
			List<TaskBinding> list_binding = new ArrayList<TaskBinding>();
			
			for(Task task :listTask) {
				TaskBinding binding = new TaskBinding();
				binding.setDuration(task.getDuration());
				binding.setId(task.getId());
				binding.setName(task.getName());
				binding.setPercentDone(task.getPercentdone());
				binding.setResourceId(task.getUserinchargeid_link());
				binding.setState(commonService.getState(task.getStatusid_link()));
				
				List<SubTask> list_subtask = new ArrayList<SubTask>();
				List<Task_CheckList> checklists = task.getSubTasks();
				for(Task_CheckList checklist : checklists) {
					SubTask subtask = new SubTask();
					subtask.setDone(checklist.getDone());
					subtask.setId(checklist.getId());
					subtask.setName(checklist.getDescription());
					subtask.setTaskId(task.getId());
					
					list_subtask.add(subtask);
				}
				
				binding.setSubTasks(list_subtask);
				list_binding.add(binding);
			}
			
			response.data = list_binding;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_user_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_user_response>(response, HttpStatus.OK);
		}
	}
}
