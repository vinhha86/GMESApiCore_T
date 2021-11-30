package vn.gpay.gsmart.core.api.timesheet_lunch_khach;

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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.timesheet_lunch_khach.ITimeSheetLunchKhachService;
import vn.gpay.gsmart.core.timesheet_lunch_khach.TimeSheetLunchKhach;
import vn.gpay.gsmart.core.timesheet_shift_type_org.ITimesheetShiftTypeOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetlunch_khach")
public class TimeSheetLunchKhachAPI {
	@Autowired
	ITimesheetShiftTypeOrgService timesheet_shift_org_Service;
	@Autowired
	ITimeSheetLunchKhachService lunchkhachService;

	@RequestMapping(value = "/getby_date", method = RequestMethod.POST)
	public ResponseEntity<get_tiimesheet_lunch_khach_response> GetByDate(
			@RequestBody get_timesheet_lunch_khach_request entity, HttpServletRequest request) {
		get_tiimesheet_lunch_khach_response response = new get_tiimesheet_lunch_khach_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = entity.orgid_link;
			List<TimesheetShiftTypeOrg> list_shifttype_orgs = timesheet_shift_org_Service
					.getByOrgid_link_CaAn(orgid_link);
			Date day = entity.ngay;

			List<TimeSheetLunchKhach> list_khach = new ArrayList<TimeSheetLunchKhach>();

			for (TimesheetShiftTypeOrg shift_type_org : list_shifttype_orgs) {
				Long shifttypeid_link = shift_type_org.getId();

				List<TimeSheetLunchKhach> list_lunchkhach = lunchkhachService.getbyCa_ngay_org(shifttypeid_link, day,
						orgid_link);
				if (list_lunchkhach.size() > 0) {
					list_khach.add(list_lunchkhach.get(0));
				} else {
					TimeSheetLunchKhach lunch_khach_new = new TimeSheetLunchKhach();
					lunch_khach_new.setAmount(0);
					lunch_khach_new.setDay(day);
					lunch_khach_new.setId(null);
					lunch_khach_new.setOrgid_link(orgid_link);
					lunch_khach_new.setOrgrootid_link(user.getRootorgid_link());
					lunch_khach_new.setShifttype_orgid_link(shifttypeid_link);
					lunch_khach_new.setStatus(0);
					lunch_khach_new.setUsercreatedid_link(user.getId());
					lunch_khach_new.setTimecreated(new Date());
					lunch_khach_new = lunchkhachService.save(lunch_khach_new);
					lunch_khach_new.shifttypeorg = shift_type_org;

					list_khach.add(lunch_khach_new);
				}
			}

			response.data = list_khach;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_tiimesheet_lunch_khach_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_tiimesheet_lunch_khach_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(@RequestBody update_timesheet_shiftlunch_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			Long id = entity.id;
			Integer amount = entity.amount;
			TimeSheetLunchKhach lunchkhach = lunchkhachService.findOne(id);
			lunchkhach.setAmount(amount);
			lunchkhachService.save(lunchkhach);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}
}
