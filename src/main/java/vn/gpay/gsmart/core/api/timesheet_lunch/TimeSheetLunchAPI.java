package vn.gpay.gsmart.core.api.timesheet_lunch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.timesheet_absence.ITimesheetAbsenceService;
import vn.gpay.gsmart.core.timesheet_absence.TimesheetAbsence;
import vn.gpay.gsmart.core.timesheet_lunch.ITimeSheetLunchService;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunch;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunchBinding;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunch_Binding;
import vn.gpay.gsmart.core.timesheet_lunch.TongHopBaoAn;
import vn.gpay.gsmart.core.timesheet_shift_type.ITimesheetShiftTypeService;
import vn.gpay.gsmart.core.timesheet_shift_type.TimesheetShiftType;
import vn.gpay.gsmart.core.timesheet_shift_type_org.ITimesheetShiftTypeOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetlunch")
public class TimeSheetLunchAPI {

	@Autowired
	private ITimeSheetLunchService timeSheetLunchService;
	@Autowired
	private IPersonnel_Service personnelService;
	@Autowired
	private ITimesheetShiftTypeService timesheetshifttypeService;
	@Autowired
	private ITimesheetShiftTypeOrgService timesheetshifttypeOrgService;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGpayUserOrgService userOrgService;
	@Autowired
	private ITimesheetAbsenceService timesheetAbsenceService;

//	@RequestMapping(value = "/getForTimeSheetLunch",method = RequestMethod.POST)
//	public ResponseEntity<TimeSheetLunch_response> getForTimeSheetLunch(HttpServletRequest request) {
//		TimeSheetLunch_response response = new TimeSheetLunch_response();
//		try {
////			Calendar cal = new GregorianCalendar();
////			cal.add(Calendar.DAY_OF_MONTH, -20);
////			Date twentyDaysAgo = cal.getTime();
//			Date today = new Date();
//			Long l = (long) 175;
//			
////			List<TimeSheetLunchBinding> list = timeSheetLunchService.getForTimeSheetLunch(l, today);
//			System.out.println(list.size());
//			
//			response.data = list;
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
//			return new ResponseEntity<TimeSheetLunch_response>(response,HttpStatus.OK);
//		}catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());			
//		    return new ResponseEntity<TimeSheetLunch_response>(HttpStatus.OK);
//		}    			
//	}

	@RequestMapping(value = "/getForTimeSheetLunch", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_response> getForTimeSheetLunch(@RequestBody TimeSheetLunch_request entity,
			HttpServletRequest request) {
		TimeSheetLunch_response response = new TimeSheetLunch_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> list_org_id = new ArrayList<Long>();
			List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
					OrgType.ORG_TYPE_FACTORY);
			Long orgrootid_link = user.getRootorgid_link();
			List<Org> lst_org = new ArrayList<Org>();
			List<Personel> listPersonnel = null;

//			Calendar cal = new GregorianCalendar();
//			cal.add(Calendar.DAY_OF_MONTH, -20);
//			Date twentyDaysAgo = cal.getTime();
			Date date = entity.date;
			Long orgid_link = entity.orgid_link;
			for (GpayUserOrg userorg : list_userorg) {
				list_org_id.add(userorg.getOrgid_link());
			}
			if (!list_org_id.contains(user.getOrgid_link())) {
				list_org_id.add(user.getOrgid_link());
			}
			List<TimeSheetLunchBinding> list = new ArrayList<TimeSheetLunchBinding>();
			if (entity.orgid_link != orgrootid_link) {
				// nếu quản lý nhiều tài khảon
				if (list_org_id.size() > 1) {
					listPersonnel = personnelService.getby_org(orgid_link, orgrootid_link);
				} else {
					// nếu có đơn vị con cụ thể
					if (user.getOrg_grant_id_link() != null) {
						lst_org = orgService.getOrgById(user.getOrg_grant_id_link());
						if (lst_org.size() != 0) {
							listPersonnel = personnelService.getby_org(user.getOrg_grant_id_link(), orgrootid_link);
						}
					} else {
						listPersonnel = personnelService.getby_org(orgid_link, orgrootid_link);
					}
				}
			}

			// List<Personel> listPersonnel = personnelService.getby_org(orgid_link,
			// orgrootid_link);
//			System.out.println(listPersonnel.size());

			// kieerm tra phong ban day thuoc don vi nao - lay id cua don vi do;
			Long id_org = orgService.getParentIdById(orgid_link);
			if (id_org != null && id_org != 1) {
				orgid_link = id_org;
			}
			List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, date);
//			System.out.println(today);
//			System.out.println(listTimeSheetLunch.size());
			Map<Long, TimeSheetLunchBinding> mapTmp = new HashMap<>();

			// set status co editable hay khong
			Integer status = 0;
			if (listTimeSheetLunch.size() > 0) {
				status = listTimeSheetLunch.get(0).getStatus();
			}

			for (Personel personnel : listPersonnel) { // add personnel to map
				TimeSheetLunchBinding temp = new TimeSheetLunchBinding();
				temp.setPersonnelid_link(personnel.getId());
				temp.setPersonnelCode(personnel.getCode());
				temp.setPersonnelFullname(personnel.getFullname());
				temp.setWorkingdate(date);
//				temp.setLunchShift1(true);
//				temp.setLunchShift2(true);
//				temp.setLunchShift3(true);
//				temp.setWorkingShift1(true);
//				temp.setWorkingShift2(true);
//				temp.setWorkingShift3(true);
				temp.setStatus(status);
				temp.setRegister_code(personnel.getRegister_code());
				mapTmp.put(personnel.getId(), temp);
			}

			// lấy id ca làm việc
			List<TimesheetShiftType> lst_timesheetshifttype = timesheetshifttypeService.findAll();
			for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
				if (mapTmp.containsKey(timeSheetLunch.getPersonnelid_link())) {
					TimeSheetLunchBinding temp = mapTmp.get(timeSheetLunch.getPersonnelid_link());

					for (int i = 0; i < lst_timesheetshifttype.size(); i++) {
						long id = lst_timesheetshifttype.get(i).getId();
						if (timeSheetLunch.getShifttypeid_link() == id
								&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 1")) {
							temp.setWorkingShift1(timeSheetLunch.isIsworking());
							temp.setLunchShift1(timeSheetLunch.isIslunch());
							break;
						}
						if (timeSheetLunch.getShifttypeid_link() == id
								&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 2")) {
							temp.setWorkingShift2(timeSheetLunch.isIsworking());
							temp.setLunchShift2(timeSheetLunch.isIslunch());
							break;
						}
						if (timeSheetLunch.getShifttypeid_link() == id
								&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 3")) {
							temp.setWorkingShift3(timeSheetLunch.isIsworking());
							temp.setLunchShift3(timeSheetLunch.isIslunch());
							break;
						}
						if (timeSheetLunch.getShifttypeid_link() == id
								&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 4")) {
							temp.setWorkingShift4(timeSheetLunch.isIsworking());
							temp.setLunchShift4(timeSheetLunch.isIslunch());
							break;
						}
//						switch (timeSheetLunch.getShifttypeid_link().toString()) {
//						case "1":
//							System.out.println("here 111");
//							temp.setWorkingShift1(timeSheetLunch.isIsworking());
//							temp.setLunchShift1(timeSheetLunch.isIslunch());
//							break;
//						case "2":
////							System.out.println("here 222");
//							temp.setWorkingShift2(timeSheetLunch.isIsworking());
//							temp.setLunchShift2(timeSheetLunch.isIslunch());
//							break;
//						case "3":
////							System.out.println("here 333");
//							temp.setWorkingShift3(timeSheetLunch.isIsworking());
//							temp.setLunchShift3(timeSheetLunch.isIslunch());
//							break;
//						}
					}
					mapTmp.put(timeSheetLunch.getPersonnelid_link(), temp);
				}
			}
			list = new ArrayList<TimeSheetLunchBinding>(mapTmp.values());
			response.data = list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetLunch_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetLunch_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> save(@RequestBody TimeSheetLunch_save_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();

			List<TimeSheetLunchBinding> listTimeSheetLunchBinding = entity.data;
			Date now = new Date();

			for (TimeSheetLunchBinding temp : listTimeSheetLunchBinding) {
				// TimeSheetLunchBinding temp = entity.data;
//				String dataIndex = temp.getDataIndex();

				Long personnelid_link = temp.getPersonnelid_link();
				Date workingdate = temp.getWorkingdate();

				Integer shifttypeid_link = 0;
				boolean isWorkingShift = false;
				boolean isLunchShift = false;
				List<TimeSheetLunch> list = new ArrayList<TimeSheetLunch>();
				String name = "Ca ăn " + temp.getDataIndex();

				// lay id ca theo cột đang check
				long id_tiemsheetshift = timesheetshifttypeService.getTimesheetShiftTypeID_ByName(name);
//				System.out.println(name);

				shifttypeid_link = (int) id_tiemsheetshift;
				isWorkingShift = temp.isWorkingShift();
				isLunchShift = temp.isLunchShift();
				list = timeSheetLunchService.getByPersonnelDateAndShift(personnelid_link, workingdate,
						shifttypeid_link);

//				if (dataIndex.equals("workingShift2") || dataIndex.equals("lunchShift2")) {
//					shifttypeid_link = 2;
//					isWorkingShift = temp.isWorkingShift2();
//					isLunchShift = temp.isLunchShift2();
//					list = timeSheetLunchService.getByPersonnelDateAndShift(personnelid_link, workingdate,
//							shifttypeid_link);
//				}
//				if (dataIndex.equals("workingShift3") || dataIndex.equals("lunchShift3")) {
//					shifttypeid_link = 3;
//					isWorkingShift = temp.isWorkingShift3();
//					isLunchShift = temp.isLunchShift3();
//					list = timeSheetLunchService.getByPersonnelDateAndShift(personnelid_link, workingdate,
//							shifttypeid_link);
//				}
				// save
				if (shifttypeid_link == 0) {
					continue;
				}
				if (list.size() > 0) {
					TimeSheetLunch timeSheetLunch = list.get(0);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				} else {
					TimeSheetLunch timeSheetLunch = new TimeSheetLunch();
					timeSheetLunch.setId(0L);
					timeSheetLunch.setOrgrootid_link(orgrootid_link);
					timeSheetLunch.setPersonnelid_link(personnelid_link);
					timeSheetLunch.setShifttypeid_link(shifttypeid_link);
					timeSheetLunch.setUsercreatedid_link(user.getId());
					timeSheetLunch.setTimecreated(now);
					timeSheetLunch.setWorkingdate(workingdate);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunch.setStatus(0);
					timeSheetLunchService.save(timeSheetLunch);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/tonghopbaoan", method = RequestMethod.POST)
	public ResponseEntity<get_TongHopBaoAn_response> TongHopBaoAn(@RequestBody get_tonghopbaoan_request entity,
			HttpServletRequest request) {
		get_TongHopBaoAn_response response = new get_TongHopBaoAn_response();
		try {
			Long orgid_link = entity.orgid_link;
			Date date = entity.date;

			List<Org> list_org = orgService.getorgChildrenbyOrg(orgid_link, new ArrayList<>());
			List<TongHopBaoAn> list = new ArrayList<TongHopBaoAn>();

			for (Org org : list_org) {
				List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunchByGrant(org.getId(),
						date);
				List<TimeSheetLunch> listca1 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
				listca1.removeIf(c -> !c.getShifttypeid_link().equals(4));

				List<TimeSheetLunch> listca2 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
				listca2.removeIf(c -> !c.getShifttypeid_link().equals(5));

				List<TimeSheetLunch> listca3 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
				listca3.removeIf(c -> !c.getShifttypeid_link().equals(6));

				List<TimeSheetLunch> listca4 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
				listca4.removeIf(c -> !c.getShifttypeid_link().equals(7));

				TongHopBaoAn tonghop = new TongHopBaoAn();
				tonghop.setOrg_name(org.getName());
				tonghop.setCa1(listca1.size());
				tonghop.setCa2(listca2.size());
				tonghop.setCa3(listca3.size());
				tonghop.setCa4(listca4.size());

				list.add(tonghop);
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_TongHopBaoAn_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_TongHopBaoAn_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updateStatus(@RequestBody TimeSheetLunch_updateStatus_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			Long orgid_link = entity.orgid_link;
			Date workingdate = entity.workingdate;
			Integer status = entity.status;

			Org org = orgService.findOne(orgid_link);
			List<TimeSheetLunch> listTimeSheetLunch = new ArrayList<TimeSheetLunch>();
			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
				listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, workingdate);
			} else {
				listTimeSheetLunch = timeSheetLunchService.getForUpdateStatusTimeSheetLunch(orgid_link, workingdate);
			}

			// getForUpdateStatusTimeSheetLunch

//			System.out.println(orgid_link);
//			System.out.println(workingdate);
//			System.out.println(listTimeSheetLunch.size());

			for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
				timeSheetLunch.setStatus(status);
				timeSheetLunchService.save(timeSheetLunch);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/isconfirm", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_isconfirm_response> isconfirm(@RequestBody TimeSheetLunch_request entity,
			HttpServletRequest request) {
		TimeSheetLunch_isconfirm_response response = new TimeSheetLunch_isconfirm_response();
		try {
			Long orgid_link = entity.orgid_link;
			Date date = entity.date;

			Org org = orgService.findOne(orgid_link);
			List<TimeSheetLunch> listTimeSheetLunch = new ArrayList<TimeSheetLunch>();
			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
				listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, date);
			} else {
				listTimeSheetLunch = timeSheetLunchService.getForUpdateStatusTimeSheetLunch(orgid_link, date);
			}

//			List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, date);

			if (listTimeSheetLunch.size() > 0) {
				TimeSheetLunch temp = listTimeSheetLunch.get(0);
				if (temp.getStatus() == 0) {
					response.isConfirm = false;
				}
				if (temp.getStatus() == 1) {
					response.isConfirm = true;
				}
			} else {
				response.isConfirm = false;
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetLunch_isconfirm_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetLunch_isconfirm_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getListCheckCaAnAuto", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_Binding_response> getListCheckCaAnAuto(
			@RequestBody TimeSheetLunch_request entity, HttpServletRequest request) {
		TimeSheetLunch_Binding_response response = new TimeSheetLunch_Binding_response();
		try {
			Long orgid_link = entity.orgid_link;
			Date date = entity.date;
			List<TimesheetShiftTypeOrg> listCa = entity.listCa;

//			System.out.println(orgid_link);
//			System.out.println(date);
//			System.out.println(listCa.size());

			List<TimeSheetLunch_Binding> timeSheetLunch_Binding_list = new ArrayList<TimeSheetLunch_Binding>();

			Org org = orgService.findOne(orgid_link);
			// lấy danh sách nghỉ trong ngày
			List<TimesheetAbsence> timesheetAbsence_list = new ArrayList<TimesheetAbsence>();
			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
//				System.out.println("top");
				timesheetAbsence_list = timesheetAbsenceService.getByOrgAndDate(orgid_link, date);
			} else {
//				System.out.println("bot");
				timesheetAbsence_list = timesheetAbsenceService.GetByOrgPhongBanAndDate(orgid_link, date);
			}

//			System.out.println(timesheetAbsence_list.size());
//			System.out.println(listCa.size());

			for (TimesheetAbsence timesheetAbsence : timesheetAbsence_list) {
				Date date_abs_from = timesheetAbsence.getAbsencedate_from();
				Date date_abs_to = timesheetAbsence.getAbsencedate_to();

				for (TimesheetShiftTypeOrg timesheetShiftTypeOrg : listCa) {
					timesheetShiftTypeOrg = timesheetshifttypeOrgService.findOne(timesheetShiftTypeOrg.getId());

					Integer fromHour = timesheetShiftTypeOrg.getFrom_hour();
					Integer fromMinute = timesheetShiftTypeOrg.getFrom_minute();
					Integer toHour = timesheetShiftTypeOrg.getTo_hour();
					Integer toMinute = timesheetShiftTypeOrg.getTo_minute();
					Boolean is_atnight = timesheetShiftTypeOrg.getIs_atnight();

					// (StartA <= EndB) and (EndA >= StartB) -> overlap
					Calendar cal = Calendar.getInstance();
					Date caFrom = date;
					cal.setTime(caFrom);
					cal.set(Calendar.HOUR_OF_DAY, fromHour);
					cal.set(Calendar.MINUTE, fromMinute);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					caFrom = cal.getTime();

					Date caTo = date;
					cal.setTime(caTo);
					if (is_atnight != null) {
						if (is_atnight) {
							cal.add(Calendar.DATE, 1);
						}
					}
					cal.set(Calendar.HOUR_OF_DAY, toHour);
					cal.set(Calendar.MINUTE, toMinute);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					caTo = cal.getTime();

//					System.out.println("-----");
//					System.out.println(date_abs_from);
//					System.out.println(date_abs_to);
//					System.out.println(caFrom);
//					System.out.println(caTo);
//					System.out.println(date_abs_from.before(caTo));
//					System.out.println(caFrom.before(date_abs_to));

					if (date_abs_from.before(caTo) && caFrom.before(date_abs_to)) {
						TimeSheetLunch_Binding newBinding = new TimeSheetLunch_Binding();
						newBinding.setPersonnelid_link(timesheetAbsence.getPersonnelid_link());
						newBinding.setIsCheck(true);

						String name = timesheetShiftTypeOrg.getName();
//						System.out.println(timesheetShiftTypeOrg.getTimesheet_shift_type_id_link());
//						System.out.println(name);
						if (name.equals("Ca ăn 1")) {
							newBinding.setLunchShift(1);
						}
						if (name.equals("Ca ăn 2")) {
							newBinding.setLunchShift(2);
						}
						if (name.equals("Ca ăn 3")) {
							newBinding.setLunchShift(3);
						}
						if (name.equals("Ca ăn 4")) {
							newBinding.setLunchShift(4);
						}
						timeSheetLunch_Binding_list.add(newBinding);
					}
				}
//				TimeSheetLunch_Binding a = new TimeSheetLunch_Binding();
			}

			response.data = timeSheetLunch_Binding_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetLunch_Binding_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetLunch_Binding_response>(HttpStatus.OK);
		}
	}
}
