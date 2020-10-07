package vn.gpay.gsmart.core.api.handover;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.handover.HandoverType;
import vn.gpay.gsmart.core.handover.IHandoverTypeService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handovertype")
public class HandoverTypeAPI {
	@Autowired IHandoverTypeService handoverTypeService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(@RequestBody HandoverType_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			HandoverType ht = entity.data;
			handoverTypeService.save(ht);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<HandoverType_getall_response> GetAll(HttpServletRequest request ) {
		HandoverType_getall_response response = new HandoverType_getall_response();
		try {
			response.data = handoverTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverType_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverType_getall_response>(response,HttpStatus.OK);
		}
	}
}
