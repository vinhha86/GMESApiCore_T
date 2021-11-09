package vn.gpay.gsmart.core.api.timesheet_absence;

import java.util.ArrayList;
import java.util.Calendar;
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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.timesheet_absence.ITimesheetAbsenceService;
import vn.gpay.gsmart.core.timesheet_absence.ITimesheetAbsenceTypeService;
import vn.gpay.gsmart.core.timesheet_absence.TimesheetAbsence;
import vn.gpay.gsmart.core.timesheet_absence.TimesheetAbsenceType;
import vn.gpay.gsmart.core.timesheet_absence.TimesheetAbsence_Binding;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetabsence")
public class TimesheetAbsenceAPI {
	@Autowired
	ITimesheetAbsenceService timesheetAbsenceService;
	@Autowired
	ITimesheetAbsenceTypeService timesheetAbsenceTypeService;
	@Autowired
	IPersonnel_Service personnelService;
	@Autowired
	IOrgService orgService;
	@Autowired
	IGpayUserOrgService userOrgService;

	@RequestMapping(value = "/getAllTimeSheetAbsence", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsence_response> getAllTimeSheetAbsence(
			@RequestBody TimeSheetAbsence_request entity, HttpServletRequest request) {
		TimeSheetAbsence_response response = new TimeSheetAbsence_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Long orgrootid_link = user.getRootorgid_link();
			// : lấy danh sách những đơn vị mà user đấy quản lý
			List<Long> list_org_id = new ArrayList<Long>();
			// List<TimesheetAbsence> lst_timesheetab = new ArrayList<TimesheetAbsence>();
			List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
					OrgType.ORG_TYPE_FACTORY);
			// danh sách ID những đơn vị user quản lý
			for (GpayUserOrg userorg : list_userorg) {
				list_org_id.add(userorg.getOrgid_link());
			}

			// lấy danh sách báo nghỉ theo đơn vị của tài khoản quản lý
//			if (user.getOrgid_link() != 1) {
//				//nếu quản lý nhiều đơn vị
//				if(list_org_id.size()>1) {
//					for (int i = 0; i < list_org_id.size(); i++) {
//						List<TimesheetAbsence> lstlst_timesheetabsence = timesheetAbsenceService.getbyOrgid(list_org_id.get(i),entity.datefrom, entity.dateto);
//						lst_timesheetab.addAll(lstlst_timesheetabsence);
//					}
//					response.data = lst_timesheetab;
//				}else {
//					// lấy ds báo nghỉ theo tổ của tài khoản quản lý (nếu có)
//					if (user.getOrg_grant_id_link() != null) {
//						response.data = timesheetAbsenceService.getbyOrg_grant_id_link(user.getOrg_grant_id_link(),entity.datefrom, entity.dateto);
//					}
//				}
//			} else {
//				response.data = timesheetAbsenceService.getAllbydate(entity.datefrom, entity.dateto);
//			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsence_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity<TimeSheetAbsence_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getbypaging", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsence_response> ContractBuyerGetpage(
			@RequestBody TimeSheetAbsence_getbypaging_request entity, HttpServletRequest request) {
		TimeSheetAbsence_response response = new TimeSheetAbsence_response();
		try {
			List<TimesheetAbsence> lstlst_timesheetabsence = timesheetAbsenceService.getbyOrgid(entity.orgFactory,
					entity.orgFactory, entity.datefrom, entity.dateto, entity.personnelCode, entity.personnelName,
					entity.timeSheetAbsenceType);

			response.data = lstlst_timesheetabsence;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsence_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetAbsence_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getbyorg_and_date", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsence_response> GetByOrgAndDate(@RequestBody getByOrgAndDate_request entity,
			HttpServletRequest request) {
		TimeSheetAbsence_response response = new TimeSheetAbsence_response();
		try {
			long orgid_link = entity.orgid_link;
			Date date = entity.date;

			response.data = timesheetAbsenceService.getByOrgAndDate(orgid_link, date);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsence_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetAbsence_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getAllTimeSheetAbsenceType", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsenceType_response> getAllTimeSheetAbsenceType(
			@RequestBody TimeSheetAbsence_request entity, HttpServletRequest request) {
		TimeSheetAbsenceType_response response = new TimeSheetAbsenceType_response();
		try {
			// GpayUser user = (GpayUser)
			// SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			response.data = timesheetAbsenceTypeService.findAll();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsenceType_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetAbsenceType_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getOne", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsence_getOne_response> getOne(@RequestBody TimeSheetAbsence_getOne_request entity,
			HttpServletRequest request) {
		TimeSheetAbsence_getOne_response response = new TimeSheetAbsence_getOne_response();
		try {

			TimesheetAbsence timesheetAbsence = timesheetAbsenceService.findOne(entity.id);
			Personel personnel = personnelService.findOne(timesheetAbsence.getPersonnelid_link());

			String timefrom = "";
			String timeto = "";

			Date timefromDate = timesheetAbsence.getAbsencedate_from();
			Date timetoDate = timesheetAbsence.getAbsencedate_to();

			Calendar calFrom = Calendar.getInstance();
			calFrom.setTime(timefromDate);

			String timefrom_hour = String.valueOf(calFrom.get(Calendar.HOUR_OF_DAY));
			String timefrom_minute = String.valueOf(calFrom.get(Calendar.MINUTE));
			if (calFrom.get(Calendar.MINUTE) < 10) {
				timefrom_minute = "0" + calFrom.get(Calendar.MINUTE);
			}
			if (calFrom.get(Calendar.HOUR_OF_DAY) < 10) {
				timefrom_hour = "0" + calFrom.get(Calendar.HOUR_OF_DAY);
			}
			timefrom = timefrom_hour + ":" + timefrom_minute;

			//

			Calendar calTo = Calendar.getInstance();
			calTo.setTime(timetoDate);
			String timeto_hour = String.valueOf(calTo.get(Calendar.HOUR_OF_DAY));
			String timeto_minute = String.valueOf(calTo.get(Calendar.MINUTE));
			if (calTo.get(Calendar.MINUTE) < 10) {

				timeto_minute = "0" + calTo.get(Calendar.MINUTE);
			}
			if (calTo.get(Calendar.HOUR_OF_DAY) < 10) {
				timeto_hour = "0" + calTo.get(Calendar.HOUR_OF_DAY);
			}
			timeto = timeto_hour + ":" + timeto_minute;

			response.orgProductionLineId = personnel.getOrgid_link();
			response.orgFactoryId = personnel.getOrgmanagerid_link();
			response.absencedate_from = timefromDate;
			response.absencedate_to = timetoDate;
			response.absence_reason = timesheetAbsence.getAbsence_reason();
			response.absencetypeid_link = timesheetAbsence.getAbsencetypeid_link();
			response.personnelfullname = personnel.getFullname();
			response.personnelid_link = personnel.getId();
			response.timefrom = timefrom;
			response.timeto = timeto;

			if (timesheetAbsence.getUserapproveid_link() == null) {
				response.isConfirm = false;
			} else {
				response.isConfirm = true;
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsence_getOne_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetAbsence_getOne_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetAbsenceType_response> save(@RequestBody TimeSheetAbsence_save_request entity,
			HttpServletRequest request) {
		TimeSheetAbsenceType_response response = new TimeSheetAbsenceType_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();

			Long id = entity.id;
			Long personnelid_link = entity.personnelid_link;
			// kiểm tra xem mã nhân viên và tên NV có trong DB không
			Personel personel_Code = personnelService.findOne(personnelid_link);
			if (personel_Code != null) {
				if (!personel_Code.getFullname().equals(entity.personnelfullname) && entity.personnelfullname != null) {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Mã nhân viên hoặc tên không trùng khớp!");
					return new ResponseEntity<TimeSheetAbsenceType_response>(response, HttpStatus.OK);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã nhân viên hoặc tên không trùng khớp!");
				return new ResponseEntity<TimeSheetAbsenceType_response>(response, HttpStatus.OK);
			}

			Long absencetypeid_link = entity.absencetypeid_link;
			String absence_reason = entity.absence_reason;
			Date absencedate_from = entity.absencedate_from;
			Date absencedate_to = entity.absencedate_to;
			Date timefrom = entity.timefrom;
			Date timeto = entity.timeto;

			Calendar calDateFrom = Calendar.getInstance();
			calDateFrom.setTime(absencedate_from);
			Calendar calHourFrom = Calendar.getInstance();
			calHourFrom.setTime(timefrom);
			Calendar calDateFromCombine = Calendar.getInstance();
			calDateFromCombine.set(Calendar.YEAR, calDateFrom.get(Calendar.YEAR));
			calDateFromCombine.set(Calendar.MONTH, calDateFrom.get(Calendar.MONTH));
			calDateFromCombine.set(Calendar.DAY_OF_MONTH, calDateFrom.get(Calendar.DAY_OF_MONTH));
			calDateFromCombine.set(Calendar.HOUR_OF_DAY, calHourFrom.get(Calendar.HOUR_OF_DAY));
			calDateFromCombine.set(Calendar.MINUTE, calHourFrom.get(Calendar.MINUTE));
			calDateFromCombine.set(Calendar.SECOND, calHourFrom.get(Calendar.SECOND));
			calDateFromCombine.set(Calendar.MILLISECOND, 0);
			Date dateFrom = calDateFromCombine.getTime();

			Calendar calDateTo = Calendar.getInstance();
			calDateTo.setTime(absencedate_to);
			Calendar calHourTo = Calendar.getInstance();
			calHourTo.setTime(timeto);
			Calendar calDateToCombine = Calendar.getInstance();
			calDateToCombine.set(Calendar.YEAR, calDateTo.get(Calendar.YEAR));
			calDateToCombine.set(Calendar.MONTH, calDateTo.get(Calendar.MONTH));
			calDateToCombine.set(Calendar.DAY_OF_MONTH, calDateTo.get(Calendar.DAY_OF_MONTH));
			calDateToCombine.set(Calendar.HOUR_OF_DAY, calHourTo.get(Calendar.HOUR_OF_DAY));
			calDateToCombine.set(Calendar.MINUTE, calHourTo.get(Calendar.MINUTE));
			calDateToCombine.set(Calendar.SECOND, calHourTo.get(Calendar.SECOND));
			calDateToCombine.set(Calendar.MILLISECOND, 0);
			Date dateTo = calDateToCombine.getTime();

			TimesheetAbsence timesheetAbsence = new TimesheetAbsence();
			timesheetAbsence.setId(id);
			timesheetAbsence.setOrgrootid_link(orgrootid_link);
			timesheetAbsence.setPersonnelid_link(personnelid_link);
			timesheetAbsence.setAbsencedate_from(dateFrom);
			timesheetAbsence.setAbsencedate_to(dateTo);
			timesheetAbsence.setAbsence_reason(absence_reason);
			timesheetAbsence.setAbsencetypeid_link(absencetypeid_link);
			timesheetAbsence.setUsercreatedid_link(user.getId());
			timesheetAbsence.setTimecreate(new Date());
			timesheetAbsenceService.save(timesheetAbsence);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetAbsenceType_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetAbsenceType_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> confirm(@RequestBody TimeSheetAbsence_getOne_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Long id = entity.id;
			TimesheetAbsence timesheetAbsence = timesheetAbsenceService.findOne(id);
			timesheetAbsence.setUserapproveid_link(user.getId());
			timesheetAbsence.setTimeapprove(new Date());
			timesheetAbsenceService.save(timesheetAbsence);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete(@RequestBody TimeSheetAbsence_getOne_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Long id = entity.id;
			timesheetAbsenceService.deleteById(id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	// them loai nghi viec
	@RequestMapping(value = "/add_timesheetabsencetype", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> add_TimeSheetAbsenceType(@RequestBody TimeSheetAbsenceType_add_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TimesheetAbsenceType data = entity.data;
			timesheetAbsenceTypeService.save(data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	// xoa loai nghi viec
	@RequestMapping(value = "/delete_timesheetabsencetype", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete_TimeSheetAbsenceType(
			@RequestBody TimeSheetAbsenceType_delete_request entity, HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			TimesheetAbsenceType data = entity.data;
			timesheetAbsenceTypeService.delete(data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/getForBaoCaoNS", method = RequestMethod.POST)
	public ResponseEntity<TimesheetAbsence_BaoCaoNS_repsonse> getForBaoCaoNS(
			@RequestBody getByOrgAndDate_request entity, HttpServletRequest request) {
		TimesheetAbsence_BaoCaoNS_repsonse response = new TimesheetAbsence_BaoCaoNS_repsonse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = entity.date;
			Date dateBegin = GPAYDateFormat.atStartOfDay(date);
			Date dateEnd = GPAYDateFormat.atEndOfDay(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateBegin);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateBegin = cal.getTime();
			cal.setTime(dateEnd);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateEnd = cal.getTime();
			
			List<TimesheetAbsence_Binding> result = new ArrayList<TimesheetAbsence_Binding>();
			
			//  lấy danh sách đơn vị
			List<Org> org_list = new ArrayList<Org>();
			if (user.getOrgid_link().equals(user.getRootorgid_link())) {
				org_list = orgService.findOrgByType(user.getRootorgid_link(), 0, OrgType.ORG_TYPE_FACTORY);
			} else {
				List<GpayUserOrg> list_userorg = userOrgService.getall_byuser_andtype(user.getId(),
						OrgType.ORG_TYPE_FACTORY);
				List<Long> list_org = new ArrayList<Long>();
				for (GpayUserOrg gpayUserOrg : list_userorg) {
					list_org.add(gpayUserOrg.getOrgid_link());
				}
				if (!list_org.contains(user.getOrgid_link())) {
					list_org.add(user.getOrgid_link());
				}
				for (Long id : list_org) {
					org_list.add(orgService.findOne(id));
				}
			}
			
			// Lấy thông tin sl nghỉ cho từng đơn vị
			for(Org org : org_list) {
				TimesheetAbsence_Binding newTimesheetAbsence_Binding = new TimesheetAbsence_Binding();
				newTimesheetAbsence_Binding.setOrgid_link(org.getId());
				newTimesheetAbsence_Binding.setOrgName(org.getName());
				newTimesheetAbsence_Binding.setOrgCode(org.getCode());
				
				// timesheetAbsenceService
				// (StartA <= EndB) and (EndA >= StartB) -> overlap
				Long orgmanagerid_link = org.getId();
				// tongLaoDong
				List<Personel> personel_list = personnelService.getTongLaoDongByDate(orgmanagerid_link, dateBegin, dateEnd);
				Integer tongLaoDong = personel_list.size();
				newTimesheetAbsence_Binding.setTongLaoDong(tongLaoDong);
				
				// nghiPhep (type 5)
				List<TimesheetAbsence> timesheetAbsence_list = timesheetAbsenceService.getNghiPhepTheoNgay(orgmanagerid_link, dateBegin, dateEnd, (long)5);
				Integer nghiPhep = timesheetAbsence_list.size();
				newTimesheetAbsence_Binding.setNghiPhep(nghiPhep);
				
				// nghi1phan2 
				// trong khoảng 7h30 đến 1h30 hoặc sau 1h30 đến hết 17h
				Calendar calendar = Calendar.getInstance();
				Date caFrom1 = date;
				calendar.setTime(caFrom1);
				calendar.set(Calendar.HOUR_OF_DAY, 7);
				calendar.set(Calendar.MINUTE, 30);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				caFrom1 = calendar.getTime();
				Date caTo1 = date;
				calendar.setTime(caTo1);
				calendar.set(Calendar.HOUR_OF_DAY, 13);
				calendar.set(Calendar.MINUTE, 30);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				caTo1 = calendar.getTime();
				Date caFrom2 = date;
				calendar.setTime(caFrom2);
				calendar.set(Calendar.HOUR_OF_DAY, 13);
				calendar.set(Calendar.MINUTE, 30);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 1);
				caFrom2 = calendar.getTime();
				Date caTo2 = date;
				calendar.setTime(caTo2);
				calendar.set(Calendar.HOUR_OF_DAY, 17);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				caTo2 = calendar.getTime();
				
				timesheetAbsence_list = timesheetAbsenceService.getNghi1Phan2TheoNgay(orgmanagerid_link, caFrom1, caTo1, caFrom2, caTo2);
				Integer nghi1phan2 = timesheetAbsence_list.size();
				newTimesheetAbsence_Binding.setNghi1phan2(nghi1phan2);
				
				// nghiKhongPhep (type 14)
				timesheetAbsence_list = timesheetAbsenceService.getNghiPhepTheoNgay(orgmanagerid_link, dateBegin, dateEnd, (long)14);
				Integer nghiKhongPhep = timesheetAbsence_list.size();
				newTimesheetAbsence_Binding.setNghiKhongPhep(nghiKhongPhep);
				
				// nghiCachLy (type 15)
				timesheetAbsence_list = timesheetAbsenceService.getNghiPhepTheoNgay(orgmanagerid_link, dateBegin, dateEnd, (long)15);
				Integer nghiCachLy = timesheetAbsence_list.size();
				newTimesheetAbsence_Binding.setNghiCachLy(nghiCachLy);
				
				// soCoMat = tongLaoDong - soTongNghi + nghi1phan2
				personel_list = personnelService.getTongLaoDongNghiByDate(orgmanagerid_link, dateBegin, dateEnd);
				Integer soTongNghi = personel_list.size();
				Integer soCoMat = tongLaoDong - soTongNghi + nghi1phan2;
				newTimesheetAbsence_Binding.setSoCoMat(soCoMat);
				
//				if(personel_list.size() > 0) {
//					for(Personel p : personel_list) {
//						System.out.println(p.getFullname());
//					}
//					System.out.println(tongLaoDong);
//					System.out.println(soTongNghi);
//					System.out.println(nghi1phan2);
//					System.out.println(dateBegin);
//					System.out.println(dateEnd);
//				}
				
				
				//
				result.add(newTimesheetAbsence_Binding);
			}
			
			// response
			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimesheetAbsence_BaoCaoNS_repsonse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimesheetAbsence_BaoCaoNS_repsonse>(HttpStatus.OK);
		}
	}
}
