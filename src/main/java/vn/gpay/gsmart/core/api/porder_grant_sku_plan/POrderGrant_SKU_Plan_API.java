package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.porder_grant_sku_plan.POrderGrant_SKU_Plan;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrantBinding;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_grant_sku_plan.IPOrderGrant_SKU_Plan_Service;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_grant_sku_plan")
public class POrderGrant_SKU_Plan_API {
	@Autowired
	private IPOrderGrant_SKU_Plan_Service porderGrant_SKU_Plan_Service;
	@Autowired
	private IPOrderGrant_SKUService porderGrant_SKU_Service;
	@Autowired
	private IPOrderGrant_Service porderGrant_Service;
	
	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_SKU_Plan_list_response> findAll(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrant_SKU_Plan_list_response response = new POrderGrant_SKU_Plan_list_response();
		try {

			response.data = porderGrant_SKU_Plan_Service.findAll();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getByPOrderGrant", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_SKU_Plan_list_response> getByPOrderGrant(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrant_SKU_Plan_list_response response = new POrderGrant_SKU_Plan_list_response();
		try {
			Long porder_grantid_link = entity.porder_grantid_link;
			Date dateFrom = entity.dateFrom;
			Date dateTo = entity.dateTo;
			
			dateFrom = GPAYDateFormat.atStartOfDay(dateFrom);
			dateTo = GPAYDateFormat.atEndOfDay(dateTo);
			
			LocalDate start = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			List<POrderGrant_SKU> porderGrant_SKU_list = 
					porderGrant_SKU_Service.getPOrderGrant_SKU(porder_grantid_link);
			List<POrderGrant_SKU_Plan> result = new ArrayList<POrderGrant_SKU_Plan>();
			for(POrderGrant_SKU porderGrant_SKU : porderGrant_SKU_list) {
				for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
					Date dateObj = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
					List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_singleDate_list =
							porderGrant_SKU_Plan_Service.getByPOrderGrant_SKU_Date(porderGrant_SKU.getId(), dateObj);
					if(porderGrant_SKU_Plan_singleDate_list.size() == 0) {
						POrderGrant_SKU_Plan porderGrant_SKU_Plan = new POrderGrant_SKU_Plan();
						porderGrant_SKU_Plan.setId(null);
						porderGrant_SKU_Plan.setAmount(0);
						porderGrant_SKU_Plan.setPorder_grant_skuid_link(porderGrant_SKU.getId());
						porderGrant_SKU_Plan.setDate(dateObj);
						porderGrant_SKU_Plan_Service.save(porderGrant_SKU_Plan);
					}
				}
				
				List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = 
						porderGrant_SKU_Plan_Service.getByPOrderGrant_SKU_Date(porderGrant_SKU.getId(), dateFrom, dateTo);
				
				for(POrderGrant_SKU_Plan porderGrant_SKU_Plan : porderGrant_SKU_Plan_list) {
					porderGrant_SKU_Plan.setSkuCode(porderGrant_SKU.getSku_product_code());
					porderGrant_SKU_Plan.setMauSanPham(porderGrant_SKU.getMauSanPham());
					porderGrant_SKU_Plan.setCoSanPham(porderGrant_SKU.getCoSanPham());
					porderGrant_SKU_Plan.setPorderGrant_SKU_grantamount(porderGrant_SKU.getGrantamount());
				}

				result.addAll(porderGrant_SKU_Plan_list);
			}
			
			response.data = result;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/porder_grant_sku_plan_update", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_SKU_Plan_list_response> porder_grant_sku_plan_update(@RequestBody POrderGrant_SKU_Plan_update_request entity,
			HttpServletRequest request) {
		POrderGrant_SKU_Plan_list_response response = new POrderGrant_SKU_Plan_list_response();
		try {
			POrderGrant_SKU_Plan porderGrant_SKU_Plan = entity.data;
			if(porderGrant_SKU_Plan != null) {
				Long porder_grant_skuid_link = porderGrant_SKU_Plan.getPorder_grant_skuid_link();
				POrderGrant_SKU porderGrant_SKU = porderGrant_SKU_Service.findOne(porder_grant_skuid_link);
				Integer grantamount = porderGrant_SKU.getGrantamount();
				
				List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = 
						porderGrant_SKU_Plan_Service.getByPOrderGrant_SKU_NotId(porder_grant_skuid_link, porderGrant_SKU_Plan.getId());
				Integer currentTotal = 0;
				Integer amount = porderGrant_SKU_Plan.getAmount() == null ? 0 : porderGrant_SKU_Plan.getAmount();
				for(POrderGrant_SKU_Plan item : porderGrant_SKU_Plan_list) {
					currentTotal += item.getAmount() == null ? 0 : item.getAmount();
				}
				if(currentTotal + amount > grantamount) {
					response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
					response.setMessage("Lỗi: Số lượng tổng vượt quá số lượng phân cho lệnh.");
					return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.OK);
				}else {
					porderGrant_SKU_Plan_Service.save(porderGrant_SKU_Plan);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrant_SKU_Plan_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getFor_KeHoachVaoChuyen", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_list_response> getFor_KeHoachVaoChuyen(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrant_list_response response = new POrderGrant_list_response();
		try {
//			Long pcontractid_link = entity.pcontractid_link;
//			Long porderid_link = entity.porderid_link;
			Long porder_grantid_link = entity.porder_grantid_link;
			
			List<POrderGrant> result = new ArrayList<POrderGrant>();
			if(porder_grantid_link != null) {
				POrderGrant pordergrant = porderGrant_Service.findOne(porder_grantid_link);
				result.add(pordergrant);
			}
			
			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrant_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrant_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getDateFor_KeHoachVaoChuyen", method = RequestMethod.POST)
	public ResponseEntity<POrderGrantBinding_list_response> getDateFor_KeHoachVaoChuyen(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrantBinding_list_response response = new POrderGrantBinding_list_response();
		try {
//			Long pcontractid_link = entity.pcontractid_link;
//			Long porderid_link = entity.porderid_link;
			Long porder_grantid_link = entity.porder_grantid_link;
			
			Date dateBegin = new Date();
			Date dateEnd = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateBegin);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateBegin = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, +6);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateEnd = cal.getTime();
			
//			System.out.println(dateBegin);
//			System.out.println(dateEnd);
			
			Map<Date, POrderGrantBinding> map = new HashMap<Date, POrderGrantBinding>();
			
			if(porder_grantid_link != null) {
				POrderGrant pordergrant = porderGrant_Service.findOne(porder_grantid_link);
				List<POrderGrant_SKU> porderGrant_SKU_list = porderGrant_SKU_Service.getPOrderGrant_SKU(pordergrant.getId());
				
				for(POrderGrant_SKU porderGrant_SKU : porderGrant_SKU_list) {
					List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = porderGrant_SKU_Plan_Service.getByPOrderGrant_SKU_Date(
							porderGrant_SKU.getId(), dateBegin, dateEnd);
					
					for(POrderGrant_SKU_Plan porderGrant_SKU_Plan : porderGrant_SKU_Plan_list) {
						Boolean is_ordered = porderGrant_SKU_Plan.getIs_ordered();
						if(is_ordered == null) {
							is_ordered = false;
						}
						if(map.containsKey(porderGrant_SKU_Plan.getDate())) {
							//
							POrderGrantBinding porderGrantBinding = map.get(porderGrant_SKU_Plan.getDate());
							porderGrantBinding.setAmount(porderGrantBinding.getAmount() + porderGrant_SKU_Plan.getAmount());
//							porderGrantBinding.setIs_ordered(is_ordered);
							map.put(porderGrant_SKU_Plan.getDate(), porderGrantBinding);
						}else {
							//
							POrderGrantBinding newPOrderGrantBinding = new POrderGrantBinding();
							newPOrderGrantBinding.setPordergrantid_link(pordergrant.getId());
							newPOrderGrantBinding.setDate(porderGrant_SKU_Plan.getDate());
							newPOrderGrantBinding.setAmount(porderGrant_SKU_Plan.getAmount());
							newPOrderGrantBinding.setIs_ordered(is_ordered);
							map.put(porderGrant_SKU_Plan.getDate(), newPOrderGrantBinding);
						}
					}
				}
			}
			
			// Thêm ngày nếu thiếu (db không có ngày này)
			LocalDate start = dateBegin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
				Date dateObj = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
				if(!map.containsKey(dateObj)) {
					POrderGrantBinding newPOrderGrantBinding = new POrderGrantBinding();
					newPOrderGrantBinding.setPordergrantid_link(porder_grantid_link);
					newPOrderGrantBinding.setDate(dateObj);
					newPOrderGrantBinding.setAmount(0);
					newPOrderGrantBinding.setIs_ordered(false);
					map.put(dateObj, newPOrderGrantBinding);
				}
			}
			
			
			// sort
			Map<Date, POrderGrantBinding> mapResult = new HashMap<Date, POrderGrantBinding>();
			SortedSet<Date> keys = new TreeSet<>(map.keySet());
			for (Date key : keys) { 
				POrderGrantBinding value = map.get(key);
			   // do something
				mapResult.put(key, value);
			}
			
			List<POrderGrantBinding> dataResult = new ArrayList<POrderGrantBinding>(mapResult.values());
			Comparator<POrderGrantBinding> compareByDate = (POrderGrantBinding a1, POrderGrantBinding a2) -> a1.getDate().compareTo(a2.getDate());
			Collections.sort(dataResult, compareByDate);
			
			response.dataMap = mapResult;
			response.data = dataResult;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrantBinding_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrantBinding_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getDateFor_KeHoachVaoChuyen_ChuaYeuCau", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_SKU_Plan_date_list_response> getDateFor_KeHoachVaoChuyen_ChuaYeuCau(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrant_SKU_Plan_date_list_response response = new POrderGrant_SKU_Plan_date_list_response();
		try {
			Long porder_grantid_link = entity.porder_grantid_link;
			
			Date dateBegin = new Date();
			Date dateEnd = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateBegin);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateBegin = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, +6);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateEnd = cal.getTime();
			
			List<Date> date_list = porderGrant_SKU_Plan_Service.getDate_ChuaYeuCau(porder_grantid_link, dateBegin, dateEnd);
//			for(Date date : date_list) {
//				System.out.println(date);
//			}
			
			response.data = date_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGrant_SKU_Plan_date_list_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderGrant_SKU_Plan_date_list_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
