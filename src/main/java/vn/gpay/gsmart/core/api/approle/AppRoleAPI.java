package vn.gpay.gsmart.core.api.approle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.approle.AppFunction;
import vn.gpay.gsmart.core.approle.AppRole;
import vn.gpay.gsmart.core.approle.AppRoleFunction;
import vn.gpay.gsmart.core.approle.AppRoleMenu;
import vn.gpay.gsmart.core.approle.AppRole_User;
import vn.gpay.gsmart.core.approle.IAppFunctionService;
import vn.gpay.gsmart.core.approle.IAppRoleFunctionService;
import vn.gpay.gsmart.core.approle.IAppRoleMenuService;
import vn.gpay.gsmart.core.approle.IAppRoleService;
import vn.gpay.gsmart.core.approle.IAppRole_User_Service;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.menu.IMenuService;
import vn.gpay.gsmart.core.menu.Menu;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/approle")
public class AppRoleAPI {
	@Autowired
	IAppRoleService approleService;
	@Autowired
	IAppRoleMenuService rolemenuService;
	@Autowired
	IMenuService menuService;
	@Autowired
	IAppRoleFunctionService approlefunctionService;
	@Autowired
	IAppFunctionService appfunctionService;
	@Autowired
	IAppRole_User_Service approleuserService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<AppRole_create_response> Create(@RequestBody AppRole_create_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		AppRole_create_response response = new AppRole_create_response();
		try {
			AppRole role = entity.data;
			role = approleService.save(role);
			response.data = role;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AppRole_create_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<AppRole_create_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getlist", method = RequestMethod.POST)
	public ResponseEntity<AppRole_getlist_response> GetLists(@RequestBody AppRole_create_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		AppRole_getlist_response response = new AppRole_getlist_response();
		try {
			response.data = approleService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AppRole_getlist_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<AppRole_getlist_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getlist_byuserid", method = RequestMethod.POST)
	public ResponseEntity<AppRole_getlist_response> GetLists_byuserid(@RequestBody AppRole_getbyuser_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		AppRole_getlist_response response = new AppRole_getlist_response();
		try {
			long userid = entity.userid;
			
			List<AppRole> listapp_role = approleService.findAll();
			List<AppRole_User> listrole_user = approleuserService.getby_user_and_role(userid, (long)0);
			for(AppRole approle: listapp_role) {
				long roleid = approle.getId();
				for(AppRole_User roleuser : listrole_user) {
					long roleid_link = roleuser.getRole_id();
					if(roleid == roleid_link) {
						approle.setChecked(true);
					}
				}
			}
			response.data = listapp_role;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AppRole_getlist_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<AppRole_getlist_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody AppRole_delete_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			approleService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create_role_menu", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create_role_menu(@RequestBody AppRole_CreateaRoleMenu_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			String menuid_link = entity.menuid_link;
			long roleid_link = entity.roleid_link;
			
			if(entity.checked) {
				AppRoleMenu rolemenu = new AppRoleMenu();
				rolemenu.setId(null);
				rolemenu.setMenuid_link(menuid_link);
				rolemenu.setRoleid_link(roleid_link);
				rolemenuService.save(rolemenu);
				
				Menu menu = menuService.findOne(menuid_link);
				if(menu.getParent_id() != null && menu.getParent_id() != "") {
					AppRoleMenu menuparent = rolemenuService.getRoleMenu_by_MenuAndRole(menu.getParent_id(), roleid_link);
					if (menuparent == null) {
						menuparent = new AppRoleMenu();
						menuparent.setId(null);
						menuparent.setMenuid_link(menu.getParent_id());
						menuparent.setRoleid_link(roleid_link);
						rolemenuService.save(menuparent);
					}
				}
;			}
			else {
				AppRoleMenu rolemenu = rolemenuService.getRoleMenu_by_MenuAndRole(menuid_link, roleid_link);
				if(rolemenu!=null)
					rolemenuService.delete(rolemenu);
				
				List<Menu> listmenu = menuService.getby_parentid(menuid_link);
				for(Menu menu : listmenu) {
					AppRoleMenu rolemenu_children = rolemenuService.getRoleMenu_by_MenuAndRole(menu.getId(), roleid_link);
					if(rolemenu_children!=null)
						rolemenuService.delete(rolemenu_children);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create_role_function", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create_role_function(@RequestBody AppRole_create_function_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			long roleid_link = entity.roleid_link;
			long functionid_link = entity.functionid_link;
			boolean isreadonly = entity.isreadonly;
			
			
			if(entity.checked) {
				AppRoleFunction approleFunction = approlefunctionService.getby_role_and_function(roleid_link, functionid_link);
				if(approleFunction == null) {
					approleFunction = new AppRoleFunction();
					approleFunction.setFunctionid_link(functionid_link);
					approleFunction.setId(null);
					approleFunction.setIshidden(false);
					approleFunction.setIsreadonly(isreadonly);
					approleFunction.setRoleid_link(roleid_link);
					
				}
				else {
					approleFunction.setIsreadonly(isreadonly);
				}
				approlefunctionService.save(approleFunction);
				
				//Kiểm tra menu đang chọn đã được check chưa?  chưa thì phải check
				AppFunction appfunction = appfunctionService.findOne(functionid_link);
				String menuid_link = appfunction.getMenuid_link();
				AppRoleMenu role_menu =  rolemenuService.getRoleMenu_by_MenuAndRole(menuid_link, roleid_link);
				if(role_menu == null) {
					role_menu = new AppRoleMenu();
					role_menu.setId(null);
					role_menu.setMenuid_link(menuid_link);
					role_menu.setRoleid_link(roleid_link);
					
					rolemenuService.save(role_menu);
				}
			}
			else {
				AppRoleFunction appFunction = approlefunctionService.getby_role_and_function(roleid_link, functionid_link);
				if(appFunction != null) {
					approlefunctionService.delete(appFunction);
				}
				else {
					AppRoleFunction approleFunction = new AppRoleFunction();
					approleFunction.setFunctionid_link(functionid_link);
					approleFunction.setId(null);
					approleFunction.setIshidden(false);
					approleFunction.setIsreadonly(isreadonly);
					approleFunction.setRoleid_link(roleid_link);
					
					approlefunctionService.save(approleFunction);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
