package vn.gpay.gsmart.core.api.personnel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel")
public class PersonnelAPI {
	@Autowired IPersonnelType_Service personneltypeService;
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<gettype_response> getType(HttpServletRequest request ) {
		gettype_response response = new gettype_response();
		try {
			response.data = personneltypeService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}
	}
}
