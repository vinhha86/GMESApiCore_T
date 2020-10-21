package vn.gpay.gsmart.core.api.position;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.position.IPosition_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/position")
public class PositionAPI {
	@Autowired IPosition_Service positionService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<getall_position_response> getType(HttpServletRequest request ) {
		getall_position_response response = new getall_position_response();
		try {
			response.data = positionService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getall_position_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getall_position_response>(response,HttpStatus.OK);
		}
	}
}
