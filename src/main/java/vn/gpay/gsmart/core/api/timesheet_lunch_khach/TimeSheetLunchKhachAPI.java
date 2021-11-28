package vn.gpay.gsmart.core.api.timesheet_lunch_khach;

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

import vn.gpay.gsmart.core.api.timesheet_lunch.TimeSheetLunch_request;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.timesheet_lunch_khach.TimeSheetLunchKhach;
import vn.gpay.gsmart.core.timesheet_shift_type_org.ITimesheetShiftTypeOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetlunch_khach")
public class TimeSheetLunchKhachAPI {
	@Autowired
	ITimesheetShiftTypeOrgService timesheet_shift_org_Service;
	
	@RequestMapping(value = "/getby_date", method = RequestMethod.POST)
	public ResponseEntity<get_tiimesheet_lunch_khach_response> GetByDate(@RequestBody TimeSheetLunch_request entity,
			HttpServletRequest request) {
		get_tiimesheet_lunch_khach_response response = new get_tiimesheet_lunch_khach_response();
		try {
			Long orgid_link = entity.orgid_link;
			List<TimesheetShiftTypeOrg> list_shifttype_orgs = timesheet_shift_org_Service.getByOrgid_link_CaAn(orgid_link);
			
			for(TimesheetShiftTypeOrg shift_type_org : list_shifttype_orgs) {
				TimeSheetLunchKhach lunchkhach = new TimeSheetLunchKhach();
				
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_tiimesheet_lunch_khach_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_tiimesheet_lunch_khach_response>(HttpStatus.OK);
		}
	}
}
