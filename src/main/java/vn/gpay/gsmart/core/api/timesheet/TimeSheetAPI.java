package vn.gpay.gsmart.core.api.timesheet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.timesheet.TimeSheet;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheet")
public class TimeSheetAPI {
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<create_timesheet_response> Create( HttpServletRequest request ) {
		create_timesheet_response response = new create_timesheet_response();
		try {
//			List<TimeSheet> list_record = entity.data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_timesheet_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<create_timesheet_response>(response, HttpStatus.OK);
		}
	}
}
