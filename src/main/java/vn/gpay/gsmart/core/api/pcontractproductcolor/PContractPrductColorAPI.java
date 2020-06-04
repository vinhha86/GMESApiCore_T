package vn.gpay.gsmart.core.api.pcontractproductcolor;


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
import vn.gpay.gsmart.core.pcontractproductcolor.IPContractProductColorService;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontract_product_color")
public class PContractPrductColorAPI {
	@Autowired IPContractProductColorService ppcservice;
	
	@RequestMapping(value = "/getcolor_byproduct", method = RequestMethod.POST)
	public ResponseEntity<PContractColor_getbyproduct_response> AttributeValue_get(
			@RequestBody PContractColor_getbyproduct_request entity, HttpServletRequest request) {
		PContractColor_getbyproduct_response response = new PContractColor_getbyproduct_response();
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = ppcservice.getcolor_by_pcontract_and_product(orgrootid_link, pcontractid_link, productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractColor_getbyproduct_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContractColor_getbyproduct_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getcolor_update", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValue_update(
			@RequestBody PContractColor_update_request entity, HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		
		try {
			ppcservice.save(entity.data);
			
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
