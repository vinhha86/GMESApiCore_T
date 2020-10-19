package vn.gpay.gsmart.core.api.personnel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel")
public class PersonnelAPI {
	@Autowired IPersonnelType_Service personneltypeService;
	@Autowired IPersonnel_Service personService;
	
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
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<create_personnel_response> Create(HttpServletRequest request, @RequestBody create_personnel_request entity ) {
		create_personnel_response response = new create_personnel_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Personel person = entity.data;
			if(person.getId() == null) {
				person.setOrgrootid_link(orgrootid_link);
			}
			person = personService.save(person);
			
			response.id = person.getId();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<create_personnel_response>(response,HttpStatus.OK);
		}
	}
}
