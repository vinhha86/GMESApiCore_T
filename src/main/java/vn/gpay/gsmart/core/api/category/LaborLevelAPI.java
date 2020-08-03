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
import vn.gpay.gsmart.core.category.ILaborLevelService;
import vn.gpay.gsmart.core.category.LaborLevel;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/laborlevel")
public class LaborLevelAPI {
	@Autowired ILaborLevelService laborLevelService;
	
	@RequestMapping(value = "/getalllaborlevel",method = RequestMethod.POST)
	public ResponseEntity<LaborLevelResponse> GetAllLaborLevel(HttpServletRequest request ) {
		LaborLevelResponse response = new LaborLevelResponse();
		try {
			response.data = laborLevelService.findAllByOrderByIdAsc();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<LaborLevelResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<LaborLevelResponse>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/createLaborLevel",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateLaborLevel(@RequestBody LaborLevelRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			LaborLevel ll = entity.data;
			if(ll.getId() == null || ll.getId() == 0) {
				ll.setOrgrootid_link(orgrootid_link);
			}else {
				LaborLevel oldll = laborLevelService.findOne(ll.getId());
				ll.setOrgrootid_link(oldll.getOrgrootid_link());
			}
			laborLevelService.save(ll);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/deleteLaborLevel",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteLaborLevel(@RequestBody LaborLevelRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			laborLevelService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
}
