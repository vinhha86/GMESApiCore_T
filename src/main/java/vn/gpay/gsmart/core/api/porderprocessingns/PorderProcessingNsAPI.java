package vn.gpay.gsmart.core.api.porderprocessingns;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.porderprocessingns.IPorderProcessingNsService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderprocessingns")
public class PorderProcessingNsAPI {
	@Autowired IPorderProcessingNsService porderProcessingNsService;
	
	@RequestMapping(value = "/getAll",method = RequestMethod.POST)
	public ResponseEntity<PorderProcessingNs_response> getAll(HttpServletRequest request ) {
		PorderProcessingNs_response response = new PorderProcessingNs_response();
		try {
			
			response.data = porderProcessingNsService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PorderProcessingNs_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PorderProcessingNs_response>(response, HttpStatus.BAD_REQUEST);
		}
	}    
}
