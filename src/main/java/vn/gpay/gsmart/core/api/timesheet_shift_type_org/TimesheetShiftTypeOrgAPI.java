package vn.gpay.gsmart.core.api.timesheet_shift_type_org;

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
import vn.gpay.gsmart.core.timesheet_shift_type_org.ITimesheetShiftTypeOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetshifttypeorg")
public class TimesheetShiftTypeOrgAPI {
	@Autowired ITimesheetShiftTypeOrgService timesheetShiftTypeOrgService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftTypeOrg_response> timesheetshifttype_GetAll(HttpServletRequest request) {
		TimesheetShiftTypeOrg_response response = new TimesheetShiftTypeOrg_response();
		try {
			response.data = timesheetShiftTypeOrgService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimesheetShiftTypeOrg_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetShiftTypeOrg_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/getbyorgid_link", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftTypeOrg_response> timesheetshifttype_GetByorgid_link(@RequestBody TimesheetShiftTypeOrg_load_byorgid_link entity,HttpServletRequest request) {
		TimesheetShiftTypeOrg_response response = new TimesheetShiftTypeOrg_response();
		try {
			response.data = timesheetShiftTypeOrgService.getByOrgid_link(entity.orgid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimesheetShiftTypeOrg_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetShiftTypeOrg_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> timesheetshifttype_Create(@RequestBody TimesheetShiftTypeOrg_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			Long timesheet_shift_type_id_link = entity.timesheet_shift_type_id_link;
			Long id = entity.id;
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

			TimesheetShiftTypeOrg timesheetShiftType = new TimesheetShiftTypeOrg();
			timesheetShiftType.setId(id);
			timesheetShiftType.setTimesheet_shift_type_id_link(timesheet_shift_type_id_link);
			timesheetShiftType.setFrom_hour(from_hour);
			timesheetShiftType.setFrom_minute(from_minute);
			timesheetShiftType.setTo_hour(to_hour);
			timesheetShiftType.setTo_minute(to_minute);
			
			timesheetShiftType.setOrgid_link(orgid_link);
			timesheetShiftTypeOrgService.save(timesheetShiftType);
			
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
	public ResponseEntity<ResponseBase> timesheetshifttype_Delete(@RequestBody TimesheetShiftTypeOrg_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			
				timesheetShiftTypeOrgService.deleteById(entity.id);
			

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
	public ResponseEntity<TimesheetShiftTypeOrg_getShift1ForAbsence_response> getShift1ForAbsence(HttpServletRequest request) {
		TimesheetShiftTypeOrg_getShift1ForAbsence_response response = new TimesheetShiftTypeOrg_getShift1ForAbsence_response();

		try {
			List<TimesheetShiftTypeOrg> list = timesheetShiftTypeOrgService.getShift1ForAbsence();
			if(list.size() > 0) {
				TimesheetShiftTypeOrg temp = list.get(0);
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
			return new ResponseEntity<TimesheetShiftTypeOrg_getShift1ForAbsence_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetShiftTypeOrg_getShift1ForAbsence_response>(response, HttpStatus.OK);
		}
	}
}
