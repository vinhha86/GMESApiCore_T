package vn.gpay.gsmart.core.api.task_grant;

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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.task_grant.ITask_Grant_Service;
import vn.gpay.gsmart.core.task_grant.Task_Grant;
import vn.gpay.gsmart.core.tasktype.ITaskType_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/taskgrant")
public class Task_GrantAPI {
	@Autowired ITask_Grant_Service taskGrantService;
	@Autowired ITaskType_Service taskTypeService;
	
	@RequestMapping(value = "/getalltaskgrant", method = RequestMethod.POST)
	public ResponseEntity<Task_Grant_getall_response> GetAllTaskGrant(HttpServletRequest request) {
		Task_Grant_getall_response response = new Task_Grant_getall_response();
		try {
			response.data = taskGrantService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Task_Grant_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Task_Grant_getall_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/getalltasktype", method = RequestMethod.POST)
	public ResponseEntity<Task_Type_getall_response> GetAllTaskType(HttpServletRequest request) {
		Task_Type_getall_response response = new Task_Type_getall_response();
		try {
			response.data = taskTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Task_Type_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Task_Type_getall_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Save(@RequestBody Task_Grant_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			Task_Grant taskgrant = entity.data;
			
			if(taskgrant.getId() == null || taskgrant.getId() == 0) {
				// Thêm mới
				List<Task_Grant> list = taskGrantService.getby_tasktype_and_org(taskgrant.getTasktypeid_link(), taskgrant.getOrgid_link());
				if(list.size() == 0) {
					// lưu
					GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					taskgrant.setOrgrootid_link(user.getRootorgid_link());
					taskGrantService.save(taskgrant);
					response.setMessage("Lưu thành công");
				}else {
					// ko lưu
					response.setMessage("Đã có người phụ trách");
				}
			}else {
				// Sửa
				Task_Grant taskToEdit = taskGrantService.findOne(taskgrant.getId());
				taskToEdit.setUserid_link(taskgrant.getUserid_link());
				taskGrantService.save(taskToEdit);
				response.setMessage("Thay đổi người phụ trách thành công");
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody Task_Grant_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			taskGrantService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
