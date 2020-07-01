package vn.gpay.gsmart.core.api.Schedule;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPI {

	
	@RequestMapping(value = "/getplan",method = RequestMethod.POST)
	public ResponseEntity<get_schedule_porder_response> GetAll(HttpServletRequest request,
			@RequestBody get_schedule_porder_request entity){
		get_schedule_porder_response response = new get_schedule_porder_response();
		
		try { 
			//Lay danh sach plan
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		
		return new ResponseEntity<get_schedule_porder_response>(response,HttpStatus.OK);
	}
}
