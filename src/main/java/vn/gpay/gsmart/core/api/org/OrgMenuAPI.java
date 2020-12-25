package vn.gpay.gsmart.core.api.org;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.org.IOrg_AutoID_Service;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.org.OrgTree;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/orgmenu")
public class OrgMenuAPI {
	
	
	@Autowired IOrgService orgService;
	@Autowired IOrg_AutoID_Service orgAutoidService;
	@Autowired IPOrderGrant_Service porderGrantService;
	@Autowired IPOrderProcessing_Service pprocessService;
	
	@RequestMapping(value = "/orgmenu_tree",method = RequestMethod.POST)
	public ResponseEntity<?> OrgMenuTree(HttpServletRequest request ) {
		try {
			OrgMenuTreeResponse response = new OrgMenuTreeResponse();
			List<Org> menu = orgService.findOrgByTypeForMenuOrg();
			List<OrgTree> children = orgService.createTree(menu);
//			System.out.println(menu.size());
			response.children=children;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgMenuTreeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/orgmenu_tree_bytype",method = RequestMethod.POST)
	public ResponseEntity<?> OrgMenuTree(@RequestBody Org_getByOrgTypeString_request entity,HttpServletRequest request ) {
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String[] listtype = entity.orgtypestring.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : listtype) {
				list.add(string.trim());
			}
			
			OrgMenuTreeResponse response = new OrgMenuTreeResponse();
			List<Org> menu = orgService.findOrgByOrgTypeString(list,user.getRootorgid_link());
			menu.add(orgService.findOne(user.getRootorgid_link()));
			List<OrgTree> children = orgService.createTree(menu);
//			System.out.println(menu.size());
			response.children=children;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<OrgMenuTreeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invcheckdevice_orgmenu_tree",method = RequestMethod.POST)
	public ResponseEntity<?> InvCheckDeviceOrgMenuTree(HttpServletRequest request ) {
		try {
			OrgMenuTreeResponse response = new OrgMenuTreeResponse();
			List<Org> menu = orgService.findOrgByTypeForInvCheckDeviceMenuOrg();
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
	
	@RequestMapping(value = "/org_create_quick",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> org_create_quick(@RequestBody Org_create_Request entity, HttpServletRequest request ) {//@RequestParam("type") 
		Org_create_Response response = new Org_create_Response();
		try {
			Org org = entity.data;
			
			String code = org.getCode().trim();
			String name = org.getName().trim();
			Integer type = org.getOrgtypeid_link();
			
			List<Org> listOrg = orgService.getByCodeAndType(code, type);
			if(listOrg.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Mã đã tồn tại");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			listOrg = orgService.getByNameAndType(name, type);
			if(listOrg.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Tên đã tồn tại");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			org.setCode(code);
			org.setName(name);
			orgService.save(org);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/duplicate",method = RequestMethod.POST)
	public ResponseEntity<Org_create_Response> Duplicate(@RequestBody Org_create_Request entity, HttpServletRequest request ) {//@RequestParam("type") 
		Org_create_Response response = new Org_create_Response();
		try {
			Org org = entity.data;
			Org parent = orgService.findOne(org.getParentid_link());
			List<String> result = orgAutoidService.getLastID(parent.getCode());
			org.setId(0L);
			org.setCode(result.get(0));
			org.setName(result.get(1));
			org = orgService.save(org);
			
			response.id = org.getId();
			response.org = org;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Org_create_Response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Org_create_Response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createproductionline",method = RequestMethod.POST)
	public ResponseEntity<Org_create_Response> CreateProductionLine(@RequestBody Org_create_Request entity, HttpServletRequest request ) {//@RequestParam("type") 
		Org_create_Response response = new Org_create_Response();
		try {
			Org parent = entity.data;
			parent = orgService.findOne(parent.getId());
			List<String> result = orgAutoidService.getLastID(parent.getCode());
			
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = new Org();
			org.setId(0L);
			org.setOrgrootid_link(user.getRootorgid_link());
			org.setOrgtypeid_link(14);
			org.setParentid_link(parent.getId());
			org.setCode(result.get(0));
			org.setName(result.get(1));
			org.setStatus(1);
				
			org = orgService.save(org);
			response.id = org.getId();
			response.org = org;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Org_create_Response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Org_create_Response>(response,HttpStatus.OK);
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
	
	@RequestMapping(value = "/deleteProductionLine",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteProductionLine(@RequestBody Org_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
//			orgService.deleteById(entity.id);
			List<POrderGrant> listgrant = porderGrantService.getByOrgId(entity.id);
			List<POrderProcessing> listprocessing = pprocessService.getByOrgId(entity.id);
			if(listgrant.size() == 0 && listprocessing.size() == 0) {
				orgService.deleteById(entity.id);
				response.setMessage("Xoá thành công");
			}else {
				response.setMessage("Tổ chuyền đang hoạt động");
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}

}
