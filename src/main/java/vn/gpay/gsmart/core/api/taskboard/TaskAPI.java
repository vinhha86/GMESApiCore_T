package vn.gpay.gsmart.core.api.taskboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
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
import vn.gpay.gsmart.core.task_flow.Comment;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_flow.Task_Flow;
import vn.gpay.gsmart.core.tasktype.ITaskType_Service;
import vn.gpay.gsmart.core.tasktype.TaskType;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/task")
public class TaskAPI {
	@Autowired ITask_Service taskService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired ITask_Flow_Service commentService;
	@Autowired Common commonService;
	@Autowired ITaskType_Service tasktypeService;
	
	@RequestMapping(value = "/getby_user",method = RequestMethod.POST)
	public ResponseEntity<getby_user_response> GetByUser(HttpServletRequest request ) {
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
				binding.setDescription(task.getDescription());
				binding.setTasktypeid_link(task.getTasktypeid_link());
				binding.setCls_task(commonService.getCls(task.getTasktypeid_link()));
				
				//get checklist
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
				
				//get comment
				List<Task_Flow> list_task_flow = commentService.getby_task(task.getId());
				List<Comment> list_comment = new ArrayList<Comment>();
				for(Task_Flow flow : list_task_flow) {
					Comment comment = new Comment();
					comment.setDate(flow.getDatecreated());
					comment.setTaskId(task.getId());
					comment.setText(flow.getDescription());
					comment.setUserId(flow.getFromuserid_link());
					
					list_comment.add(comment);
				}
				
				binding.setComments(list_comment);
				
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
	
	@RequestMapping(value = "/add_comment",method = RequestMethod.POST)
	public ResponseEntity<add_comment_response> AddComment(HttpServletRequest request, @RequestBody add_comment_request entity) {
		add_comment_response response = new add_comment_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = new Date();
			Task_Flow flow = new Task_Flow();
			flow.setDatecreated(date);
			flow.setDescription(entity.text);
			flow.setFromuserid_link(user.getId());
			flow.setId(null);
			flow.setOrgrootid_link(user.getRootorgid_link());
			flow.setStatusid_link(0);
			flow.setTaskid_link(entity.taskid_link);
			
			flow = commentService.save(flow);
			
			Comment comment = new Comment();
			comment.setDate(date);
			comment.setTaskId(entity.taskid_link);
			comment.setText(flow.getDescription());
			comment.setUserId(flow.getFromuserid_link());
			
			response.data = comment;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<add_comment_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<add_comment_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/add_othertask",method = RequestMethod.POST)
	public ResponseEntity<add_othertask_response> AddOtherTask(HttpServletRequest request, @RequestBody add_othertask_request entity) {
		add_othertask_response response = new add_othertask_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long userid_link = user.getId();
			long tasktypeid_link = -1;
			
			TaskType tasktype = tasktypeService.findOne(tasktypeid_link);
			
			String taskname = tasktype.getName();
			
			Task task = new Task();
			task.setDatecreated(new Date());
			task.setId(null);
			task.setName(taskname);
			task.setOrgrootid_link(orgrootid_link);
			task.setUserinchargeid_link(userid_link);
			task.setStatusid_link(0);
			task.setPercentdone(0);
			task.setTasktypeid_link((long)-1);
			task.setUsercreatedid_link(userid_link);
			task.setDescription(entity.text);
			task = taskService.save(task);
			
			TaskBinding binding = new TaskBinding();
			binding.setDescription(task.getDescription());
			binding.setId(task.getId());
			binding.setName(task.getName());
			binding.setPercentDone(task.getPercentdone());
			binding.setResourceId(task.getUserinchargeid_link());
			binding.setState(commonService.getState(task.getStatusid_link()));
			binding.setTasktypeid_link(task.getTasktypeid_link());
			binding.setDescription(task.getDescription());
			binding.setCls_task(commonService.getCls(task.getTasktypeid_link()));
			
			response.data = binding;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<add_othertask_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<add_othertask_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/add_checklist",method = RequestMethod.POST)
	public ResponseEntity<add_checklist_response> AddCheckList(HttpServletRequest request, @RequestBody add_checklist_request entity) {
		add_checklist_response response = new add_checklist_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long userid_link = user.getId();
			Task_CheckList checklist = new Task_CheckList();
			checklist.setDatecreated(new Date());
			checklist.setDescription(entity.checklist);
			checklist.setDone(false);
			checklist.setId(null);
			checklist.setOrgrootid_link(orgrootid_link);
			checklist.setTaskid_link(entity.taskid_link);
			checklist.setUsercreatedid_link(userid_link);
			checklist = checklistService.save(checklist);
			
			response.data = checklist;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<add_checklist_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<add_checklist_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update_checklist",method = RequestMethod.POST)
	public ResponseEntity<update_checklist_done_response> UpdateCheckList(HttpServletRequest request, @RequestBody update_checklist_done_request entity) {
		update_checklist_done_response response = new update_checklist_done_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long userid_link = user.getId();
			int status = 0;
			long taskid_link = 0;
			
			for(SubTask checklist : entity.data) {
				Task_CheckList subTask = checklistService.findOne(checklist.getId());
				subTask.setDone(checklist.getDone());
				if(checklist.getDone()) {
					subTask.setDatefinished(new Date());
					subTask.setUserfinishedid_link(userid_link);
					status = 1;
					taskid_link = subTask.getTaskid_link();
				}
				checklistService.save(subTask);
			}		
			
			List<Task_CheckList> list_sub = checklistService.getby_taskid_link(taskid_link);
			list_sub.removeIf(c-> c.getDone() == true);
			if(list_sub.size() == 0) status = 2;
			
			Task task = taskService.findOne(taskid_link);
			task.setStatusid_link(status);
			if(status == 2)
				task.setDatefinished(new Date());
			task = taskService.save(task);	
			
			response.status = commonService.getState(task.getStatusid_link());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_checklist_done_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<update_checklist_done_response>(response, HttpStatus.OK);
		}
	}
}
