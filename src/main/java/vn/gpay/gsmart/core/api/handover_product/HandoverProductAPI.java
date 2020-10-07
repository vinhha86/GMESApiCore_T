package vn.gpay.gsmart.core.api.handover_product;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.handover_product.IHandoverProductService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handoverproduct")
public class HandoverProductAPI {
	@Autowired IHandoverProductService handoverProductService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<HandoverProduct_getall_response> GetAll(HttpServletRequest request ) {
		HandoverProduct_getall_response response = new HandoverProduct_getall_response();
		try {
			response.data = handoverProductService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}
	}
}
