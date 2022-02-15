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
import vn.gpay.gsmart.core.timesheet_lunch_khach.ITimeSheetLunchKhachService;
import vn.gpay.gsmart.core.timesheet_lunch_khach.TimeSheetLunchKhach;
import vn.gpay.gsmart.core.timesheet_shift_type.ITimesheetShiftTypeService;
import vn.gpay.gsmart.core.timesheet_shift_type.TimesheetShiftType;
import vn.gpay.gsmart.core.timesheet_shift_type_org.ITimesheetShiftTypeOrgService;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;
import vn.gpay.gsmart.core.utils.Common;
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
	@Autowired
	ITimeSheetLunchKhachService lunchkhachService;
	@Autowired
	ITimeSheetLunchKhachService timeSheetLunchKhachService;
	@Autowired
	Common commonService;

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
			Long orgid_link = entity.orgid_link;
			List<Org> lst_org = new ArrayList<Org>();
			List<Personel> listPersonnel = null;			

			Date date = entity.date;
			Date dateBegin = commonService.getBeginOfDate(date);
			Date dateEnd = commonService.getEndOfDate(date);
//			Date  date_check = Common.Date_Add(dateEnd, -1);
			
			List<TimeSheetLunchBinding> list = new ArrayList<TimeSheetLunchBinding>();

			List<TimesheetShiftType> lst_timesheetshifttype = timesheetshifttypeService.findAll();

			//neu la ngay hom truoc thi load trong bang timesheet_lunch
			if(dateEnd.after(Common.Date_Add(new Date(), 0))) {
				for (GpayUserOrg userorg : list_userorg) {
					list_org_id.add(userorg.getOrgid_link());
				}
				if (!list_org_id.contains(user.getOrgid_link())) {
					list_org_id.add(user.getOrgid_link());
				}
				if (entity.orgid_link != orgrootid_link) {
					// nếu quản lý nhiều tài khoan
					if (list_org_id.size() > 1) {
//						listPersonnel = personnelService.getby_org(orgid_link, orgrootid_link);
						listPersonnel = personnelService.getTongLaoDongByDate(orgid_link, dateBegin, dateEnd);
					} else {
						// nếu có đơn vị con cụ thể
						if (user.getOrg_grant_id_link() != null) {
							lst_org = orgService.getOrgById(user.getOrg_grant_id_link());
							if (lst_org.size() != 0) {
//								listPersonnel = personnelService.getby_org(user.getOrg_grant_id_link(), orgrootid_link);
								listPersonnel = personnelService.getTongLaoDongByDate(user.getOrg_grant_id_link(),
										dateBegin, dateEnd);
							}
						} else {
//							listPersonnel = personnelService.getby_org(orgid_link, orgrootid_link);
							listPersonnel = personnelService.getTongLaoDongByDate(orgid_link, dateBegin, dateEnd);
						}
					}
				}

				// kiem tra phong ban day thuoc don vi nao - lay id cua don vi do;
				Long id_org = orgService.getParentIdById(orgid_link);
				if (id_org != null && id_org != 1) {
					orgid_link = id_org;
				}
				List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, date);
				Map<Long, TimeSheetLunchBinding> mapTmp = new HashMap<>();

				for (Personel personnel : listPersonnel) { // add personnel to map
					TimeSheetLunchBinding temp = new TimeSheetLunchBinding();
					temp.setPersonnelid_link(personnel.getId());
					temp.setPersonnelCode(personnel.getCode());
					temp.setPersonnelFullname(personnel.getFullname());
					temp.setWorkingdate(date);
					temp.setRegister_code(personnel.getRegister_code());
					temp.setOrgid_link(personnel.getOrgid_link());
					temp.setOrgmanagerid_link(personnel.getOrgmanagerid_link());
					mapTmp.put(personnel.getId(), temp);
				}

				// lấy id ca làm việc
				for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
					if (mapTmp.containsKey(timeSheetLunch.getPersonnelid_link())) {
						TimeSheetLunchBinding temp = mapTmp.get(timeSheetLunch.getPersonnelid_link());

						for (int i = 0; i < lst_timesheetshifttype.size(); i++) {
//							long id = lst_timesheetshifttype.get(i).getId();
							if (timeSheetLunch.getShifttypeid_link() == 4
//									&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 1")
							) {
								temp.setWorkingShift1(timeSheetLunch.isIsworking());
								temp.setLunchShift1(timeSheetLunch.isIslunch());
								break;
							}
							if (timeSheetLunch.getShifttypeid_link() == 5
//									&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 2")
							) {
								temp.setWorkingShift2(timeSheetLunch.isIsworking());
								temp.setLunchShift2(timeSheetLunch.isIslunch());
								break;
							}
							if (timeSheetLunch.getShifttypeid_link() == 6
//									&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 3")
							) {
								temp.setWorkingShift3(timeSheetLunch.isIsworking());
								temp.setLunchShift3(timeSheetLunch.isIslunch());
								break;
							}
							if (timeSheetLunch.getShifttypeid_link() == 7
//									&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 4")
							) {
								temp.setWorkingShift4(timeSheetLunch.isIsworking());
								temp.setLunchShift4(timeSheetLunch.isIslunch());
								break;
							}
							if (timeSheetLunch.getShifttypeid_link() == 8
//									&& lst_timesheetshifttype.get(i).getName().equals("Ca ăn 5")
							) {
								temp.setWorkingShift5(timeSheetLunch.isIsworking());
								temp.setLunchShift5(timeSheetLunch.isIslunch());
								break;
							}
						}
						
						// lay gia tri id cua ca khong an trua, set cho binding
						if(timeSheetLunch.getIs_nolunch()) {
							Long shifttypeid_link = timeSheetLunch.getShifttypeid_link().longValue();
							List<TimesheetShiftTypeOrg> timesheetShiftTypeOrg_list = timesheetshifttypeOrgService.getByOrgid_link_and_shifttypeId(
									orgid_link, shifttypeid_link);
							if(timesheetShiftTypeOrg_list.size() > 0) {
								TimesheetShiftTypeOrg timesheetShiftTypeOrg = timesheetShiftTypeOrg_list.get(0);
								temp.setNolunch_shift_idlink(timesheetShiftTypeOrg.getId());
							}
						}
						
						temp.setStatus(timeSheetLunch.getStatus());
						mapTmp.put(timeSheetLunch.getPersonnelid_link(), temp);
					}
				}
				list = new ArrayList<TimeSheetLunchBinding>(mapTmp.values());
			}
			else {
				list = new ArrayList<TimeSheetLunchBinding>();
				List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunchBeforeDate(orgid_link, date);
				for(TimeSheetLunch ts_lunch : listTimeSheetLunch) {
					Personel person = personnelService.findOne(ts_lunch.getPersonnelid_link());
					
					TimeSheetLunchBinding binding = new TimeSheetLunchBinding();
					binding.setOrgid_link(ts_lunch.getOrgid_link());
					binding.setOrgmanagerid_link(ts_lunch.getOrgmanagerid_link());
					binding.setPersonnelCode(person.getCode());
					binding.setPersonnelFullname(person.getFullname());
					binding.setPersonnelid_link(ts_lunch.getPersonnelid_link());
					binding.setRegister_code(person.getRegister_code());
					binding.setStatus(ts_lunch.getStatus());
					binding.setWorkingdate(ts_lunch.getWorkingdate());
					
					if(ts_lunch.getShifttypeid_link() == 4) {
						binding.setLunchShift1(true);
					}
					
					if(ts_lunch.getShifttypeid_link() == 5) {
						binding.setLunchShift2(true);
					}
					
					if(ts_lunch.getShifttypeid_link() == 6) {
						binding.setLunchShift3(true);
					}
					
					if(ts_lunch.getShifttypeid_link() == 7) {
						binding.setLunchShift4(true);
					}
					
					if(ts_lunch.getShifttypeid_link() == 8) {
						binding.setLunchShift5(true);
					}
					
					// lay gia tri id cua ca khong an trua, set cho binding
					if(ts_lunch.getIs_nolunch()) {
						Long shifttypeid_link = ts_lunch.getShifttypeid_link().longValue();
						List<TimesheetShiftTypeOrg> timesheetShiftTypeOrg_list = timesheetshifttypeOrgService.getByOrgid_link_and_shifttypeId(
								orgid_link, shifttypeid_link);
						if(timesheetShiftTypeOrg_list.size() > 0) {
							TimesheetShiftTypeOrg timesheetShiftTypeOrg = timesheetShiftTypeOrg_list.get(0);
							binding.setNolunch_shift_idlink(timesheetShiftTypeOrg.getId());
						}
					}
					
					list.add(binding);
				}
			}

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

				Long nolunch_shift_idlink = temp.getNolunch_shift_idlink() == null ? 0L : temp.getNolunch_shift_idlink();
				Long personnelid_link = temp.getPersonnelid_link();
				Date workingdate = temp.getWorkingdate();
				
				Integer shifttypeid_link = 0;
				boolean isWorkingShift = false;
				boolean isLunchShift = false;
				List<TimeSheetLunch> list = new ArrayList<TimeSheetLunch>();
				String name = "Ca ăn " + temp.getDataIndex();

				// lay id ca theo cột đang check
				long id_timesheetshift = timesheetshifttypeService.getTimesheetShiftTypeID_ByName(name);

				shifttypeid_link = (int) id_timesheetshift;
				isWorkingShift = temp.isWorkingShift();
				isLunchShift = temp.isLunchShift();
				list = timeSheetLunchService.getByPersonnelDateAndShift(personnelid_link, workingdate,
						shifttypeid_link);

				// save
				if (shifttypeid_link == 0) {
					continue;
				}
				
				if (list.size() > 0) {
					TimeSheetLunch timeSheetLunch = list.get(0);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					
					// lưu is_nolunch  
					// nolunch_shift_idlink gửi lên  lấy theo id bảng timesheet_shift_type_org
					// TimeSheetLunch shifttypeid_link lấy theo id bảng timesheet_shift_type
					TimesheetShiftTypeOrg timesheetShiftTypeOrg = timesheetshifttypeOrgService.findOne(nolunch_shift_idlink);
					if(timesheetShiftTypeOrg != null) {
						Long timesheet_shift_type_id_link = timesheetShiftTypeOrg.getTimesheet_shift_type_id_link();
						if(timesheet_shift_type_id_link != null) {
							if(timeSheetLunch.getShifttypeid_link().equals(timesheet_shift_type_id_link.intValue())) {
								timeSheetLunch.setIs_nolunch(true);
							}else {
								timeSheetLunch.setIs_nolunch(false);
							}
						}
					}
					//
					if(nolunch_shift_idlink.equals((long)0)) {
						timeSheetLunch.setIs_nolunch(false);
					}
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
					timeSheetLunch.setOrgid_link(temp.getOrgid_link());
					timeSheetLunch.setOrgmanagerid_link(temp.getOrgmanagerid_link());
					
					// lưu is_nolunch  
					// nolunch_shift_idlink gửi lên  lấy theo id bảng timesheet_shift_type_org
					// TimeSheetLunch shifttypeid_link lấy theo id bảng timesheet_shift_type
					TimesheetShiftTypeOrg timesheetShiftTypeOrg = timesheetshifttypeOrgService.findOne(nolunch_shift_idlink);
					if(timesheetShiftTypeOrg != null) {
						Long timesheet_shift_type_id_link = timesheetShiftTypeOrg.getTimesheet_shift_type_id_link();
						if(timesheet_shift_type_id_link != null) {
							if(timeSheetLunch.getShifttypeid_link().equals(timesheet_shift_type_id_link.intValue())) {
								timeSheetLunch.setIs_nolunch(true);
							}else {
								timeSheetLunch.setIs_nolunch(false);
							}
						}
					}
					//
					if(nolunch_shift_idlink.equals((long)0)) {
						timeSheetLunch.setIs_nolunch(false);
					}
					timeSheetLunchService.save(timeSheetLunch);
				}
			}

			// xoa nhung timsheetluch co isworking = false, islunch = false
			List<TimeSheetLunch> TimeSheetLunch_toDelete = timeSheetLunchService.getBy_isworking_islunch(false, false);
			for (TimeSheetLunch timeSheetLunch : TimeSheetLunch_toDelete) {
				timeSheetLunchService.deleteById(timeSheetLunch.getId());
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
			Date date_from = entity.date_from;
			Date date_to = entity.date_to;

			date_from = commonService.getBeginOfDate(date_from);
			date_to = commonService.getEndOfDate(date_to);
			List<Org> list_org = orgService.getorgChildrenbyOrg(orgid_link, new ArrayList<>());
			List<TongHopBaoAn> list = new ArrayList<TongHopBaoAn>();

			// Thêm đơn vị khách vào báo cáo
			Org org_khach = new Org();
			org_khach.setName("Khách");
			org_khach.setOrgtypeid_link(166);
			org_khach.setId(orgid_link);

			list_org.add(org_khach);

			// Thêm báo thêm của các tổ
			Org org_them = new Org();
			org_them.setName("Báo bổ sung");
			org_them.setOrgtypeid_link(999);
			org_them.setId((long) -1);

			list_org.add(org_them);

			for (Org org : list_org) {
				if (org.getOrgtypeid_link().equals(166)) {
					List<TimeSheetLunchKhach> listTimeSheetLunchKhach = lunchkhachService.getby_nhieungay_org(date_from, date_to,
							orgid_link);
					listTimeSheetLunchKhach.removeIf(c -> c.getAmount() == 0);

					List<TimeSheetLunchKhach> listTimeSheetLunchKhach_ca1 = new ArrayList<>(listTimeSheetLunchKhach);
					listTimeSheetLunchKhach_ca1.removeIf(c -> !c.getShifttypeid_link().equals((long) 4));

					List<TimeSheetLunchKhach> listTimeSheetLunchKhach_ca2 = new ArrayList<>(listTimeSheetLunchKhach);
					listTimeSheetLunchKhach_ca2.removeIf(c -> !c.getShifttypeid_link().equals((long) 5));

					List<TimeSheetLunchKhach> listTimeSheetLunchKhach_ca3 = new ArrayList<>(listTimeSheetLunchKhach);
					listTimeSheetLunchKhach_ca3.removeIf(c -> !c.getShifttypeid_link().equals((long) 6));

					List<TimeSheetLunchKhach> listTimeSheetLunchKhach_ca4 = new ArrayList<>(listTimeSheetLunchKhach);
					listTimeSheetLunchKhach_ca4.removeIf(c -> !c.getShifttypeid_link().equals((long) 7));

					List<TimeSheetLunchKhach> listTimeSheetLunchKhach_ca5 = new ArrayList<>(listTimeSheetLunchKhach);
					listTimeSheetLunchKhach_ca5.removeIf(c -> !c.getShifttypeid_link().equals((long) 8));

					TongHopBaoAn tonghop = new TongHopBaoAn();
					tonghop.setOrg_name(org.getName());
					tonghop.setCa1(listTimeSheetLunchKhach_ca1.size() == 0 ? 0
							: listTimeSheetLunchKhach_ca1.get(0).getAmount());
					tonghop.setCa2(listTimeSheetLunchKhach_ca2.size() == 0 ? 0
							: listTimeSheetLunchKhach_ca2.get(0).getAmount());
					tonghop.setCa3(listTimeSheetLunchKhach_ca3.size() == 0 ? 0
							: listTimeSheetLunchKhach_ca3.get(0).getAmount());
					tonghop.setCa4(listTimeSheetLunchKhach_ca4.size() == 0 ? 0
							: listTimeSheetLunchKhach_ca4.get(0).getAmount());
					tonghop.setCa5(listTimeSheetLunchKhach_ca5.size() == 0 ? 0
							: listTimeSheetLunchKhach_ca5.get(0).getAmount());
					tonghop.setOrgtypeid_link(org.getOrgtypeid_link());

					list.add(tonghop);

				} else if (org.getOrgtypeid_link().equals(999)) {

				} else {
					List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService
							.getForTimeSheetLunchByGrantManyDay(org.getId(), date_from, date_to);
					
					// chi lay nhung ai da xac nhan
					listTimeSheetLunch.removeIf(c -> !c.getStatus().equals(1));

					List<TimeSheetLunch> listca1 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
					listca1.removeIf(c -> !c.getShifttypeid_link().equals(4) || !c.isIslunch());

					List<TimeSheetLunch> listca2 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
					listca2.removeIf(c -> !c.getShifttypeid_link().equals(5) || !c.isIslunch());

					List<TimeSheetLunch> listca3 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
					listca3.removeIf(c -> !c.getShifttypeid_link().equals(6) || !c.isIslunch());

					List<TimeSheetLunch> listca4 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
					listca4.removeIf(c -> !c.getShifttypeid_link().equals(7) || !c.isIslunch());

					List<TimeSheetLunch> listca5 = new ArrayList<TimeSheetLunch>(listTimeSheetLunch);
					listca5.removeIf(c -> !c.getShifttypeid_link().equals(8) || !c.isIslunch());

					TongHopBaoAn tonghop = new TongHopBaoAn();
					tonghop.setOrg_name(org.getName());
					tonghop.setCa1(listca1.size());
					tonghop.setCa2(listca2.size());
					tonghop.setCa3(listca3.size());
					tonghop.setCa4(listca4.size());
					tonghop.setCa5(listca5.size());
					tonghop.setOrgtypeid_link(org.getOrgtypeid_link());

					list.add(tonghop);
				}
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
			Date date = new Date();
			List<Long> selectIds = entity.selectIds;
			Long orgid_link = entity.orgid_link;
			Date workingdate = entity.workingdate;
			String comment = entity.comment;
			Long shifttypeid_link = entity.shifttypeid_link;

			Org org = orgService.findOne(orgid_link);
			List<TimeSheetLunch> listTimeSheetLunch_select = new ArrayList<TimeSheetLunch>();

			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
//				listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, workingdate);
				if (selectIds.size() > 0) {
					listTimeSheetLunch_select = timeSheetLunchService.getBy_multiShift(orgid_link, workingdate,
							selectIds);
				}
			} else if (!org.getId().equals((long) 1)) {
//				listTimeSheetLunch = timeSheetLunchService.getForUpdateStatusTimeSheetLunch(orgid_link, workingdate);
				if (selectIds.size() > 0) {
					listTimeSheetLunch_select = timeSheetLunchService.getBy_multiShift(orgid_link, workingdate,
							selectIds);
				}
			}

			// getForUpdateStatusTimeSheetLunch

			for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch_select) {
				if (timeSheetLunch.getStatus().equals(0)) {
					if (shifttypeid_link != null) {
						if (timeSheetLunch.getShifttypeid_link().equals(shifttypeid_link.intValue())) {
							timeSheetLunch.setComment(comment);
							timeSheetLunch.setIs_bo_sung(true);
						}
					}

					timeSheetLunch.setStatus(1);
					timeSheetLunch.setTime_approve(date);
					timeSheetLunchService.save(timeSheetLunch);
				}

			}

//			for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch_unselect) {
//				timeSheetLunch.setStatus(0);
//				timeSheetLunch.setTime_approve(null);
//				timeSheetLunchService.save(timeSheetLunch);
//			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

//	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
//	public ResponseEntity<ResponseBase> updateStatus(@RequestBody TimeSheetLunch_updateStatus_request entity,
//			HttpServletRequest request) {
//		ResponseBase response = new ResponseBase();
//		try {
//			Long orgid_link = entity.orgid_link;
//			Date workingdate = entity.workingdate;
//			Integer status = entity.status;
//
//			Org org = orgService.findOne(orgid_link);
//			List<TimeSheetLunch> listTimeSheetLunch = new ArrayList<TimeSheetLunch>();
//			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
//				listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, workingdate);
//			} else {
//				listTimeSheetLunch = timeSheetLunchService.getForUpdateStatusTimeSheetLunch(orgid_link, workingdate);
//			}
//
//			// getForUpdateStatusTimeSheetLunch
//
//			for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
//				timeSheetLunch.setStatus(status);
//				timeSheetLunchService.save(timeSheetLunch);
//			}
//
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
//		}
//	}

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

			List<TimeSheetLunch_Binding> timeSheetLunch_Binding_list = new ArrayList<TimeSheetLunch_Binding>();

			Org org = orgService.findOne(orgid_link);
			// lấy danh sách nghỉ trong ngày
			List<TimesheetAbsence> timesheetAbsence_list = new ArrayList<TimesheetAbsence>();
			if (org.getOrgtypeid_link().equals(OrgType.ORG_TYPE_XUONGSX)) {
				timesheetAbsence_list = timesheetAbsenceService.getByOrgAndDate(orgid_link, date);
			} else {
				timesheetAbsence_list = timesheetAbsenceService.GetByOrgPhongBanAndDate(orgid_link, date);
			}

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

//						String name = timesheetShiftTypeOrg.getName();
						Long id = timesheetShiftTypeOrg.getTimesheet_shift_type_id_link();
//						System.out.println(timesheetShiftTypeOrg.getTimesheet_shift_type_id_link());
//						System.out.println(name);
						if (id.equals((long) 4)
//								&& name.equals("Ca ăn 1")
						) {
							newBinding.setLunchShift(1);
						}
						if (id.equals((long) 5)
//								&& name.equals("Ca ăn 2")
						) {
							newBinding.setLunchShift(2);
						}
						if (id.equals((long) 6)
//								&& name.equals("Ca ăn 3")
						) {
							newBinding.setLunchShift(3);
						}
						if (id.equals((long) 7)
//								&& name.equals("Ca ăn 4")
						) {
							newBinding.setLunchShift(4);
						}
						if (id.equals((long) 8)
//								&& name.equals("Ca ăn 5")
						) {
							newBinding.setLunchShift(5);
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

	@RequestMapping(value = "/getForTimeSheetLunch_Mobile", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_Binding_response> getForTimeSheetLunch_Mobile(
			@RequestBody TimeSheetLunch_request entity, HttpServletRequest request) {
		TimeSheetLunch_Binding_response response = new TimeSheetLunch_Binding_response();
		try {
			Long orgid_link = entity.orgid_link; // id phan xuong
			Date date = entity.date; // ngay

			List<TimeSheetLunch_Binding> result = new ArrayList<TimeSheetLunch_Binding>();

			// lay danh sach ca cua phan xuong
			List<TimesheetShiftTypeOrg> shift_list = timesheetshifttypeOrgService.getByOrgid_link_CaAn(orgid_link);

			for (TimesheetShiftTypeOrg shift : shift_list) {
				TimeSheetLunch_Binding newTimeSheetLunch_Binding = new TimeSheetLunch_Binding();
				String name = shift.getName() + " ";
				name += shift.getFrom_hour() < 10 ? "0" + shift.getFrom_hour() : shift.getFrom_hour();
				name += ":";
				name += shift.getFrom_minute() < 10 ? "0" + shift.getFrom_minute() : shift.getFrom_minute();
				name += " - ";
				name += shift.getTo_hour() < 10 ? "0" + shift.getTo_hour() : shift.getTo_hour();
				name += ":";
				name += shift.getTo_minute() < 10 ? "0" + shift.getTo_minute() : shift.getTo_minute();
				newTimeSheetLunch_Binding.setCaName(name);

//				System.out.println(name);

				// sl

//				List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getByOrg_Shift(orgid_link,
//						shift.getTimesheet_shift_type_id_link().intValue(), date);
				List<TimeSheetLunch> listTimeSheetLunch_DangKy = timeSheetLunchService.getByOrg_Shift_DangKy(orgid_link,
						shift.getTimesheet_shift_type_id_link().intValue(), date);
				List<TimeSheetLunch> listTimeSheetLunch_Them = timeSheetLunchService.getByOrg_Shift_Them(orgid_link,
						shift.getTimesheet_shift_type_id_link().intValue(), date);
				newTimeSheetLunch_Binding.setSoDangKy(listTimeSheetLunch_DangKy.size());
				newTimeSheetLunch_Binding.setSoThem(listTimeSheetLunch_Them.size());
				
				// Tinh so khach - // orgid_link, shifttype_orgid_link, date
				Long shifttype_orgid_link = shift.getId();
				List<TimeSheetLunchKhach> TimeSheetLunchKhach_list = timeSheetLunchKhachService.getbyCa_ngay_org(shifttype_orgid_link, date, orgid_link);
				Integer khach_amount = 0;
				if(TimeSheetLunchKhach_list.size() > 0) {
					khach_amount = TimeSheetLunchKhach_list.get(0).getAmount() == null ? 0 : TimeSheetLunchKhach_list.get(0).getAmount();
				}
//				System.out.println("-----");
//				System.out.println(TimeSheetLunchKhach_list.size());
//				System.out.println(shifttype_orgid_link);
//				System.out.println(date);
//				System.out.println(orgid_link);
//				System.out.println(khach_amount);
				
				newTimeSheetLunch_Binding.setSoKhach(khach_amount);
				newTimeSheetLunch_Binding.setSoTong(listTimeSheetLunch_DangKy.size() + listTimeSheetLunch_Them.size() + khach_amount);

				newTimeSheetLunch_Binding.setTimesheet_shift_type_id_link(shift.getTimesheet_shift_type_id_link());
				newTimeSheetLunch_Binding.setTimesheet_shift_type_org_id_link(shift.getId());

				result.add(newTimeSheetLunch_Binding);
			}

			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetLunch_Binding_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetLunch_Binding_response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getForTimeSheetLunch_Mobile_Detail", method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_Binding_response> getForTimeSheetLunch_Mobile_Detail(
			@RequestBody TimeSheetLunch_request entity, HttpServletRequest request) {
		TimeSheetLunch_Binding_response response = new TimeSheetLunch_Binding_response();
		try {
			Long orgid_link = entity.orgid_link; // id phan xuong
			Date date = entity.date; // ngay
			Long timesheet_shift_type_id_link = entity.timesheet_shift_type_id_link;
//			Long timesheet_shift_type_org_id_link = entity.timesheet_shift_type_org_id_link;

			List<TimeSheetLunch_Binding> result = new ArrayList<TimeSheetLunch_Binding>();
			List<Org> org_list = orgService.getorgChildrenbyOrg(orgid_link, new ArrayList<>());

			// Thêm đơn vị khách vào báo cáo
			Org org_khach = new Org();
			org_khach.setName("Khách");
			org_khach.setCode("Khách");
			org_khach.setOrgtypeid_link(166);
			org_khach.setId(orgid_link);

			org_list.add(org_khach);

			for (Org org : org_list) {
				TimeSheetLunch_Binding newTimeSheetLunch_Binding = new TimeSheetLunch_Binding();
				newTimeSheetLunch_Binding.setOrgCode(org.getCode());
				newTimeSheetLunch_Binding.setOrgName(org.getName());
				newTimeSheetLunch_Binding.setOrgType(org.getOrgtypeid_link());
				if (org.getOrgtypeid_link().equals(166)) {
					List<TimeSheetLunchKhach> list_lunch_khach = lunchkhachService
							.getbyCa_ngay_org(timesheet_shift_type_id_link, date, orgid_link);
					if (list_lunch_khach.size() > 0) {
						newTimeSheetLunch_Binding.setSoDangKy(list_lunch_khach.get(0).getAmount());
						newTimeSheetLunch_Binding.setSoTong(list_lunch_khach.get(0).getAmount());
					}

				} else {

					List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getByOrg_Shift(org.getId(),
							timesheet_shift_type_id_link.intValue(), date);
					List<TimeSheetLunch> listTimeSheetLunch_DangKy = timeSheetLunchService
							.getByOrg_Shift_DangKy(org.getId(), timesheet_shift_type_id_link.intValue(), date);
					List<TimeSheetLunch> listTimeSheetLunch_Them = timeSheetLunchService
							.getByOrg_Shift_Them(org.getId(), timesheet_shift_type_id_link.intValue(), date);
					newTimeSheetLunch_Binding.setSoDangKy(listTimeSheetLunch_DangKy.size());
					newTimeSheetLunch_Binding.setSoThem(listTimeSheetLunch_Them.size());
					newTimeSheetLunch_Binding.setSoTong(listTimeSheetLunch.size());

				}
				result.add(newTimeSheetLunch_Binding);
			}

			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TimeSheetLunch_Binding_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<TimeSheetLunch_Binding_response>(HttpStatus.OK);
		}
	}

//	@RequestMapping(value = "/getForTimeSheetLunch_Mobile_old", method = RequestMethod.POST)
//	public ResponseEntity<TimeSheetLunch_Binding_response> getForTimeSheetLunch_Mobile_old(
//			@RequestBody TimeSheetLunch_request entity, HttpServletRequest request) {
//		TimeSheetLunch_Binding_response response = new TimeSheetLunch_Binding_response();
//		try {
//			Long orgid_link = entity.orgid_link; // id phan xuong
//			Date date = entity.date; // ngay
//
//			List<TimeSheetLunch_Binding> result = new ArrayList<TimeSheetLunch_Binding>();
//
//			// lay danh sach cac to cua phan xuong
////			List<Org> org_list = orgService.getByParentId_for_TimeSheetLunchMobile(orgid_link);
//			List<Org> org_list = orgService.getorgChildrenbyOrg(orgid_link, new ArrayList<>());
//
////			System.out.println(org_list.size());
//
//			for (Org org : org_list) {
//				TimeSheetLunch_Binding newTimeSheetLunch_Binding = new TimeSheetLunch_Binding();
//				newTimeSheetLunch_Binding.setOrgId(org.getId());
//				newTimeSheetLunch_Binding.setOrgCode(org.getCode());
//				newTimeSheetLunch_Binding.setOrgName(org.getName());
//				newTimeSheetLunch_Binding.setOrgType(org.getOrgtypeid_link());
//
//				Integer sumCa1 = 0, sumCa2 = 0, sumCa3 = 0, sumCa4 = 0, sumCa5 = 0;
//				List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService
//						.getForTimeSheetLunch_byOrg_Date(org.getId(), date);
//				for (TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
//					Integer shifttypeid_link = timeSheetLunch.getShifttypeid_link();
//					TimesheetShiftType timesheetShiftType = timesheetshifttypeService.findOne(shifttypeid_link);
//					String shiftName = timesheetShiftType.getName();
//					Long id = timesheetShiftType.getId();
//					if (id.equals((long) 4) 
////							&& shiftName.equals("Ca ăn 1")
//					) {
//						sumCa1++;
//					}
//					if (id.equals((long) 5)
////							&& shiftName.equals("Ca ăn 2")
//					) {
//						sumCa2++;
//					}
//					if (id.equals((long) 6)
////							&& shiftName.equals("Ca ăn 3")
//					) {
//						sumCa3++;
//					}
//					if (id.equals((long) 7)
////							&& shiftName.equals("Ca ăn 4")
//					) {
//						sumCa4++;
//					}
//					if (id.equals((long) 8)
////							&& shiftName.equals("Ca ăn 5")
//					) {
//						sumCa5++;
//					}
//				}
//
//				newTimeSheetLunch_Binding.setSumCa1(sumCa1);
//				newTimeSheetLunch_Binding.setSumCa2(sumCa2);
//				newTimeSheetLunch_Binding.setSumCa3(sumCa3);
//				newTimeSheetLunch_Binding.setSumCa4(sumCa4);
//				newTimeSheetLunch_Binding.setSumCa5(sumCa5);
//				result.add(newTimeSheetLunch_Binding);
//			}
//
//			response.data = result;
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<TimeSheetLunch_Binding_response>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//			return new ResponseEntity<TimeSheetLunch_Binding_response>(HttpStatus.OK);
//		}
//	}
}
