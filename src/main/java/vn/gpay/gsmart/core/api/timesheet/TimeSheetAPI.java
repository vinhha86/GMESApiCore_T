package vn.gpay.gsmart.core.api.timesheet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.devices.IDevicesService;
import vn.gpay.gsmart.core.devices.device_timesheet;
import vn.gpay.gsmart.core.timesheet.ITimeSheet_Service;
import vn.gpay.gsmart.core.timesheet.TimeSheet;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheet")
public class TimeSheetAPI {
	@Autowired ITimeSheet_Service timesheetService;
	@Autowired IDevicesService deviceService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<create_timesheet_response> Create( HttpServletRequest request, @RequestBody create_timesheet_request entity ) {
		create_timesheet_response response = new create_timesheet_response();
		try {
			List<TimeSheet> list_record = entity.data;
			for (TimeSheet timeSheet : list_record) {
				timesheetService.save(timeSheet);
				
				//Kiem tra user co trong db chua ko thi them vao
			}
			
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_timesheet_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<create_timesheet_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getlist_device",method = RequestMethod.POST)
	public ResponseEntity<get_device_timesheet_response> GetListDevice( HttpServletRequest request, @RequestBody get_device_timesheet_request entity ) {
		get_device_timesheet_response response = new get_device_timesheet_response();
		try {
			Long orgrootid_link = entity.orgrootid_link;
			Long devicegroupid_link = (long)11;
			
			String pattern = "yyyy/MM/dd HH:mm:ss";
			DateFormat df = new SimpleDateFormat(pattern);
			//Lay danh sach user chua co trong db
			List<Devices> list_device = deviceService.getdevice_bygroup_and_orgroot(orgrootid_link, devicegroupid_link);
			List<device_timesheet> list_device_ts = new ArrayList<device_timesheet>();
			for(Devices device : list_device) {
				device_timesheet timesheet = new device_timesheet();
				timesheet.setDevice_ip(device.getIp());
				timesheet.setDevice_port(device.getPort());
				timesheet.setId(device.getId());
				timesheet.setLast_download(device.getLasttimeupdate() == null ? "" : df.format(device.getLasttimeupdate()));
				list_device_ts.add(timesheet);
			}
			response.data = list_device_ts;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_device_timesheet_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<get_device_timesheet_response>(response, HttpStatus.OK);
		}
	}
}