package vn.gpay.gsmart.core.api.timesheet;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.timesheet.ITimeSheet_Service;
import vn.gpay.gsmart.core.timesheet_lunch.ITimeSheetLunchService;
import vn.gpay.gsmart.core.timesheet_sum.ITimeSheet_Sum_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetsum")
public class TimeSheet_SumAPI {
	@Autowired
	ITimeSheet_Service timesheetService;
	@Autowired
	ITimeSheetLunchService timesheet_lunchService;
	@Autowired
	ITimeSheet_Sum_Service timesheet_sumService;
	@Autowired
	IPersonnel_Service personnelService;

	@RequestMapping(value = "/timesheet_sum_byorg", method = RequestMethod.POST)
	public ResponseEntity<timesheet_sum_response> timesheet_sum_byorg(HttpServletRequest request,
			@RequestBody timesheet_sum_byorg_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		timesheet_sum_response response = new timesheet_sum_response();
		try {
			response.data = timesheet_sumService.getall_bymanageorg(entity.orgid_link, entity.year, entity.month);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<timesheet_sum_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<timesheet_sum_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/timesheet_sum_calculate", method = RequestMethod.POST)
	public ResponseEntity<timesheet_sum_response> timesheet_sum_calculate(HttpServletRequest request,
			@RequestBody timesheet_sum_byorg_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		timesheet_sum_response response = new timesheet_sum_response();
		try {
			// 1. Lay danh sach nhan su cua don vi quan ly (orgmanagerid_link)
			List<Personel> ls_Personnel = personnelService.getby_org(entity.orgid_link, orgrootid_link);
			CountDownLatch latch = new CountDownLatch(ls_Personnel.size());
//			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//			System.out.println("Start:" + formatter.format(new Date()));
			for (Personel personnel : ls_Personnel) {
				TimeSheet_Personnel sal_personnel = new TimeSheet_Personnel(personnel, entity.year, entity.month,
						entity.orgid_link, timesheetService, timesheet_lunchService, timesheet_sumService, latch);
				sal_personnel.start();
			}
			latch.await();
//			System.out.println("End:" + formatter.format(new Date()));
			response.data = timesheet_sumService.getall_bymanageorg(entity.orgid_link, entity.year, entity.month);
//			System.out.println(response.data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<timesheet_sum_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<timesheet_sum_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
