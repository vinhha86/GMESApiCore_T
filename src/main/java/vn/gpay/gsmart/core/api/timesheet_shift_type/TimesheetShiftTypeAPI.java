package vn.gpay.gsmart.core.api.timesheet_shift_type;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	@RequestMapping(value = "/getbyorgid_link", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_response> timesheetshifttype_GetByorgid_link(@RequestBody TimesheetShiftType_load_byorgid_link entity,HttpServletRequest request) {
		TimesheetShiftType_response response = new TimesheetShiftType_response();
		try {
			response.data = timesheetShiftTypeService.getByOrgid_link(entity.orgid_link);
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
			Long orgid_link  =entity.orgid_link;
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
			
			timesheetShiftType.setOrgid_link(orgid_link);
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
	
	@RequestMapping(value = "/getShift1ForAbsence", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_getShift1ForAbsence_response> getShift1ForAbsence(HttpServletRequest request) {
		TimesheetShiftType_getShift1ForAbsence_response response = new TimesheetShiftType_getShift1ForAbsence_response();

		try {
			List<TimesheetShiftType> list = timesheetShiftTypeService.getShift1ForAbsence();
			if(list.size() > 0) {
				TimesheetShiftType temp = list.get(0);
				Integer fromHour = temp.getFrom_hour();
				Integer fromMinute = temp.getFrom_minute();
				Integer toHour = temp.getTo_hour();
				Integer toMinute = temp.getTo_minute();
				
				String timeFrom = "";
				String timeTo = "";
				if(fromMinute < 10) {
					timeFrom = timeFrom + fromHour + ":0" + fromMinute;
				}else {
					timeFrom = timeFrom + fromHour + ":" + fromMinute;
				}
				if(toMinute < 10) {
					timeTo = timeTo + toHour + ":0" + toMinute;
				}else {
					timeTo = timeTo + toHour + ":" + toMinute;
				}
				
				response.timeFrom = timeFrom;
				response.timeTo = timeTo;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}else {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Không tìm thấy ca 1 (id 1)");
			}
			return new ResponseEntity<TimesheetShiftType_getShift1ForAbsence_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetShiftType_getShift1ForAbsence_response>(response, HttpStatus.OK);
		}
	}
}
