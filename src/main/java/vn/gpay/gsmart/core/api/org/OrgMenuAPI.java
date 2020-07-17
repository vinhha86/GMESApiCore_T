package vn.gpay.gsmart.core.api.org;

import java.util.List;

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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.org.OrgTree;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/orgmenu")
public class OrgMenuAPI {
	
	
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/orgmenu_tree",method = RequestMethod.POST)
	public ResponseEntity<?> OrgMenuTree(HttpServletRequest request ) {
		try {
			OrgMenuTreeResponse response = new OrgMenuTreeResponse();
			List<Org> menu = orgService.findOrgByTypeForMenuOrg();
			List<OrgTree> children = orgService.createTree(menu);
//			System.out.println(menu.size());
			response.children=children;
			return new ResponseEntity<OrgMenuTreeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/orgall",method = RequestMethod.POST)
	public ResponseEntity<?> OrgAll(HttpServletRequest request ) {
		try {
			OrgResponse response = new OrgResponse();
			List<Org> menu = orgService.findOrgByTypeForMenuOrg();
			response.data=menu;
			return new ResponseEntity<OrgResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/createOrg",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateOrg(@RequestBody Org_create_Request entity, HttpServletRequest request ) {//@RequestParam("type") 
		Org_create_Response response = new Org_create_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = entity.data;
			if(org.getId()==null || org.getId()==0) {
				org.setOrgrootid_link(user.getRootorgid_link());
			}else {
				Org _org =  orgService.findOne(org.getId());
				org.setOrgrootid_link(_org.getOrgrootid_link());
			}
			org = orgService.save(org);
			response.id = org.getId();
			response.org = org;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteOrg",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteOrg(@RequestBody Org_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			orgService.deleteById(entity.id);
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
