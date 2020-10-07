package vn.gpay.gsmart.core.api.vattype;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.vat_type.IVatTypeService;

@RestController
@RequestMapping("/api/v1/vattype")
public class VatTypeAPI {
	@Autowired IVatTypeService vattypeService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<?> CustomerGetbyCode(HttpServletRequest request ) {
		VatType_getall_response response = new VatType_getall_response();
		try {
			response.data= vattypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<VatType_getall_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
