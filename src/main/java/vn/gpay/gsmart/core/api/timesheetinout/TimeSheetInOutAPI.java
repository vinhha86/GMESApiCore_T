package vn.gpay.gsmart.core.api.timesheetinout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.timesheetinout.ITimeSheetInOutService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetinout")
public class TimeSheetInOutAPI {
	@Autowired ITimeSheetInOutService timesheetinoutService;
	//lấy tất cả  danh sách 
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetInOut_load_response> timesheetinout_GetAll(@RequestBody TimeSheetInOut_load_request entity) {
		TimeSheetInOut_load_response response = new TimeSheetInOut_load_response();
		try {
			response.data = timesheetinoutService.getAll(entity.todate, entity.fromdate);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<TimeSheetInOut_load_response>(response, HttpStatus.OK);
		
	}
}
