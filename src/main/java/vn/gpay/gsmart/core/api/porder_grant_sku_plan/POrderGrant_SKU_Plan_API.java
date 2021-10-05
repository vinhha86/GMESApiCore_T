package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

import vn.gpay.gsmart.core.porder_grant_sku_plan.POrderGrant_SKU_Plan;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
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
			
//			System.out.println("---------------------");
//			System.out.println(porder_grantid_link);
//			System.out.println(dateFrom);
//			System.out.println(dateTo);
			
			dateFrom = GPAYDateFormat.atStartOfDay(dateFrom);
			dateTo = GPAYDateFormat.atEndOfDay(dateTo);
			
//			System.out.println("---------------------");
//			System.out.println(dateFrom);
//			System.out.println(dateTo);
			
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
				}
				// test
				
//				Date newdate = new GregorianCalendar(2021, Calendar.OCTOBER, 13).getTime();
//				POrderGrant_SKU_Plan newPOrderGrant_SKU_Plan = new POrderGrant_SKU_Plan();
//				newPOrderGrant_SKU_Plan.setAmount(0);
//				newPOrderGrant_SKU_Plan.setDate(newdate);
//				newPOrderGrant_SKU_Plan.setPorder_grant_skuid_link(porderGrant_SKU.getId());
//				newPOrderGrant_SKU_Plan.setSkuCode(porderGrant_SKU.getSku_product_code());
//				newPOrderGrant_SKU_Plan.setMauSanPham(porderGrant_SKU.getMauSanPham());
//				newPOrderGrant_SKU_Plan.setCoSanPham(porderGrant_SKU.getCoSanPham());
//				porderGrant_SKU_Plan_list.add(newPOrderGrant_SKU_Plan);

				result.addAll(porderGrant_SKU_Plan_list);
			}
			
//			List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = 
//					porderGrant_SKU_Plan_Service.getByPOrderGrant_Date(porder_grantid_link, dateFrom, dateTo);
			
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
}
