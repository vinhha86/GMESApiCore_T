package vn.gpay.gsmart.core.api.timesheet_shift_type;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.timesheet_shift_type.ITimesheetShiftTypeService;
import vn.gpay.gsmart.core.timesheet_shift_type.TimesheetShiftType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetshifttype")
public class TimesheetShiftTypeAPI {
	@Autowired ITimesheetShiftTypeService timesheetShiftTypeService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_response> timesheetshifttype_GetAll(HttpServletRequest request) {
		TimesheetShiftType_response response = new TimesheetShiftType_response();
		try {
			response.data = timesheetShiftTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimesheetShiftType_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetShiftType_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> timesheetshifttype_Create(@RequestBody TimesheetShiftType_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			
			Long id = entity.id;
			String name = entity.name.trim();
			Date timefrom = entity.timefrom;
			Date timeto = entity.timeto;
			boolean checkboxfrom = entity.checkboxfrom;
			boolean checkboxto = entity.checkboxto;
			
//			List<TimesheetShiftType> listTimesheetShiftType = timesheetShiftTypeService.getByName(name);
//			if(listTimesheetShiftType.size() > 0) {
//				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//				response.setMessage("Tên ca làm việc đã tồn tại");
//				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//			}
			
			Calendar timefromcal = Calendar.getInstance();
			timefromcal.setTime(timefrom);  
			Integer from_hour = timefromcal.get(Calendar.HOUR_OF_DAY);
			Integer from_minute = timefromcal.get(Calendar.MINUTE);
			
			Calendar timetocal = Calendar.getInstance();
			timetocal.setTime(timeto);  
			Integer to_hour = timetocal.get(Calendar.HOUR_OF_DAY);
			Integer to_minute = timetocal.get(Calendar.MINUTE);
			
			if(checkboxfrom) from_hour+=24;
			if(checkboxto) to_hour+=24;

			TimesheetShiftType timesheetShiftType = new TimesheetShiftType();
			timesheetShiftType.setId(id);
			timesheetShiftType.setName(name);
			timesheetShiftType.setFrom_hour(from_hour);
			timesheetShiftType.setFrom_minute(from_minute);
			timesheetShiftType.setTo_hour(to_hour);
			timesheetShiftType.setTo_minute(to_minute);
			timesheetShiftTypeService.save(timesheetShiftType);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> timesheetshifttype_Delete(@RequestBody TimesheetShiftType_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			for(TimesheetShiftType temp : entity.data) {
				timesheetShiftTypeService.deleteById(temp.getId());
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
}
