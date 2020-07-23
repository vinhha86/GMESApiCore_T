package vn.gpay.gsmart.core.api.pcontractconfigamount;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractconfigamount.IConfigAmountService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/configamount")
public class ConfigAmountAPI {
	@Autowired IConfigAmountService configamountService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<ConfigAmount_getall_response> GetAll(HttpServletRequest request ) {
		ConfigAmount_getall_response response = new ConfigAmount_getall_response();
		try {
			
			response.data = configamountService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ConfigAmount_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ConfigAmount_getall_response>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> save(@RequestBody ConfigAmount_save_request entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			configamountService.save(entity.data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete(@RequestBody ConfigAmount_delete_request entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			configamountService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
}
