package vn.gpay.gsmart.core.api.timesheet_shift_type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type.ITimesheetShiftTypeService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetshifttype")
public class TimesheetShiftTypeAPI {
	@Autowired
	ITimesheetShiftTypeService timesheetShiftTypeService;
	@Autowired 
	IOrgService orgService;
	// lay tất cả các ca làm việc
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_load_response> timesheetshifttype_GetAll(HttpServletRequest request) {
		TimesheetShiftType_load_response response = new TimesheetShiftType_load_response();
		try {
			response.data = timesheetShiftTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<TimesheetShiftType_load_response>(response, HttpStatus.OK);

	}

	// lay tất cả các ca làm việc chưa có trong đơn vị quản lý
	@RequestMapping(value = "/getbyorgid_link", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_load_response> getTimesheetshifttype_ByIdOrg(
			@RequestBody Timesheetshift_load_byIdOrg entity) {
		TimesheetShiftType_load_response response = new TimesheetShiftType_load_response();
		try {
			response.data = timesheetShiftTypeService.getTimesheetShiftType_ByIdOrgid_link(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<TimesheetShiftType_load_response>(response, HttpStatus.OK);

	}

	// thêm mới ca làm việc
	@RequestMapping(value = "/add_timesheetshifttype", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> add_timesheetshifttype(@RequestBody TimesheetShiftType_add_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			timesheetShiftTypeService.save(entity.data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}

	// xóa ca làm việc
	@RequestMapping(value = "/delete_timesheetshifttype", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete_timesheetshifttype(
			@RequestBody TimesheetShiftType_delete_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			timesheetShiftTypeService.delete(entity.data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}

	// lay tất cả các ca làm việc có trong đơn vị quản lý
	@RequestMapping(value = "/getshiftbyorgid_link", method = RequestMethod.POST)
	public ResponseEntity<TimesheetShiftType_load_response> getshift_ByIdOrgid_link(
			@RequestBody Timesheetshift_load_byIdOrg entity) {
		TimesheetShiftType_load_response response = new TimesheetShiftType_load_response();
		try {
			//nếu id là tổ thì kiểm tra tổ đấy thuộc đơn vị vào lấy id đơn vị lên
			long id = entity.id;
			Long id_org = orgService.getById(entity.id);
			//nếu có id và id khác 1.( 1 là id của công ty)
			if(id_org != null && id_org != 1) {
				id= id_org;
			}
			response.data = timesheetShiftTypeService.getShift_ByIdOrgid_link(id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<TimesheetShiftType_load_response>(response, HttpStatus.OK);

	}
}
