package vn.gpay.gsmart.core.api.category;

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
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.category.IPortService;
import vn.gpay.gsmart.core.category.Port;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/categoty")
public class PortAPI {

	@Autowired IPortService portService;
	
	@RequestMapping(value = "/getPortAll",method = RequestMethod.POST)
	public ResponseEntity<?> GetPortAll(HttpServletRequest request ) {
		PortResponse response = new PortResponse();
		try {
			response.data = portService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PortResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getPortById",method = RequestMethod.POST)
	public ResponseEntity<GetPortById_response> GetPortByid(@RequestBody GetPortById_request entity, HttpServletRequest request ) {//@RequestParam("id") 
		GetPortById_response response = new GetPortById_response();
		try {
			response.data = portService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetPortById_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<GetPortById_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createPort",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreatePort(@RequestBody Port_create_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		Port_create_response response = new Port_create_response();
		try {
			Port port = entity.data;
			port = portService.save(port);
			response.id = port.getId();
			response.port = port;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deletePort",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeletePort(@RequestBody Port_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			portService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
}
