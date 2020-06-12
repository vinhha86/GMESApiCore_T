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
import vn.gpay.gsmart.core.category.IUnitService;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/unit")
public class UnitAPI {
	@Autowired IUnitService unitservice;
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<Unit_getall_response> GetAllUnit(HttpServletRequest request ) {
		Unit_getall_response response = new Unit_getall_response();
		try {
			response.data = unitservice.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Unit_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Unit_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getUnitById",method = RequestMethod.POST)
	public ResponseEntity<GetUnitById_response> GetUnitByid(@RequestBody GetUnitById_request entity, HttpServletRequest request ) {//@RequestParam("id") 
		GetUnitById_response response = new GetUnitById_response();
		try {
			response.data = unitservice.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetUnitById_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<GetUnitById_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createUnit",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateUnit(@RequestBody Unit_create_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Unit unit = entity.data;
			if(unit.getId()==null || unit.getId()==0) {
				unit.setOrgrootid_link(user.getRootorgid_link());
			}else {
				Unit _unit =  unitservice.findOne(unit.getId());
				unit.setOrgrootid_link(_unit.getOrgrootid_link());
			}
			unitservice.save(unit);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteUnit",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteUnit(@RequestBody Unit_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			unitservice.deleteById(entity.id);
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
