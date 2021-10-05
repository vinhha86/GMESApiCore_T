package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

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
import vn.gpay.gsmart.core.porder_grant_sku_plan.IPOrderGrant_SKU_Plan_Service;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_grant_sku_plan")
public class POrderGrant_SKU_Plan_API {
	@Autowired
	private IPOrderGrant_SKU_Plan_Service porderGrant_SKU_Plan_Service;
	
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
	
	@RequestMapping(value = "/getByPOrderGrantSku", method = RequestMethod.POST)
	public ResponseEntity<POrderGrant_SKU_Plan_list_response> getByPOrderGrantSku(@RequestBody POrderGrant_SKU_Plan_list_request entity,
			HttpServletRequest request) {
		POrderGrant_SKU_Plan_list_response response = new POrderGrant_SKU_Plan_list_response();
		try {
			Long porder_grant_skuid_link = entity.porder_grant_skuid_link;
			Date dateFrom = entity.dateFrom;
			Date dateTo = entity.dateTo;
			
			dateFrom = GPAYDateFormat.atStartOfDay(dateFrom);
			dateTo = GPAYDateFormat.atEndOfDay(dateTo);
			
			List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = 
					porderGrant_SKU_Plan_Service.getByPorderGrantSku_Date(porder_grant_skuid_link, dateFrom, dateTo);
			
			response.data = porderGrant_SKU_Plan_list;

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
