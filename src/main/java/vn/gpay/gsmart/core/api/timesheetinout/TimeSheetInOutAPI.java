package vn.gpay.gsmart.core.api.timesheetinout;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.gpay.gsmart.core.timesheetinout.ITimeSheetInOutService;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetInOut;
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

			List<TimeSheetInOut> lsttimesheetinout =timesheetinoutService.getAll(entity.todate, entity.fromdate);
			
			System.out.println(lsttimesheetinout.size());
			response.data =lsttimesheetinout;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<TimeSheetInOut_load_response>(response, HttpStatus.OK);
		
	}
}
