package vn.gpay.gsmart.core.api.handover_sku;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.handover_sku.IHandoverSKUService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handoversku")
public class HandoverSKUAPI {
	@Autowired IHandoverSKUService handoverSkuService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<HandoverSKU_getall_response> GetAll(HttpServletRequest request ) {
		HandoverSKU_getall_response response = new HandoverSKU_getall_response();
		try {
			response.data = handoverSkuService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}
	}
}
