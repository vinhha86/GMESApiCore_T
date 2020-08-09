package vn.gpay.gsmart.core.api.users;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.gpay.gsmart.core.approle.AppFunction;
import vn.gpay.gsmart.core.approle.AppRole_User;
import vn.gpay.gsmart.core.approle.AppRole_User_Service;
import vn.gpay.gsmart.core.approle.AppUserFunction;
import vn.gpay.gsmart.core.approle.AppUserFunction_Service;
import vn.gpay.gsmart.core.approle.AppUserMenu;
import vn.gpay.gsmart.core.approle.AppUserMenu_Service;
import vn.gpay.gsmart.core.approle.IAppFunctionService;
import vn.gpay.gsmart.core.approle.IAppRole_User_Service;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.menu.IUserMenuService;
import vn.gpay.gsmart.core.menu.Menu;
import vn.gpay.gsmart.core.menu.MenuServiceImpl;
import vn.gpay.gsmart.core.menu.UserMenu;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.security.GpayRole;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.IGpayUserService;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/users")
public class UserAPI {

	private PasswordEncoder passwordEncoder;
	@Autowired IGpayUserService  userDetailsService ;
	@Autowired IOrgService orgService;
	@Autowired IUserMenuService  userMenuService ;
	@Autowired IAppRole_User_Service appuserService;
	@Autowired MenuServiceImpl menuService;
	@Autowired AppUserMenu_Service usermenuService;
	@Autowired AppUserFunction_Service appuserfService;
	@Autowired AppRole_User_Service roleuserService;
	@Autowired IAppFunctionService appfuncService;
	@Autowired IPContractService pcontractService;
	
	@RequestMapping(value = "/user_create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> GetSkuByCode( @RequestBody UserCreateRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser appuser =entity.data;
			if(appuser.getId()!=null && appuser.getId()>0) {
				GpayUser appuserold = userDetailsService.findById(appuser.getId());
				Org org = orgService.findOne(entity.data.getOrgid_link());
				appuserold.setOrg_type(org.getOrgtypeid_link());
				appuserold.setRootorgid_link(org.getOrgrootid_link());
				appuserold.setLastname(appuser.getLastname());
				appuserold.setMiddlename(appuser.getMiddlename());
				appuserold.setFirstname(appuser.getFirstname());
				appuserold.setStatus(appuser.getStatus());
				
				userDetailsService.save(appuserold);
				List<UserMenu> listmenu = userMenuService.findByUserid(appuser.getId());
				if(listmenu!=null) {
					for (UserMenu userMenu : listmenu) {
						userMenuService.delete(userMenu);
					}
				}
				
				for(MenuId entry : entity.usermenu) {
					UserMenu userMenu = new UserMenu();
					userMenu.setMenuid(entry.id);
					userMenu.setUserid(appuser.getId());
					userMenuService.save(userMenu);
				}
				
			}
			else {
				Org org = orgService.findOne(entity.data.getOrgid_link());
				appuser.setPassword(passwordEncoder.encode(appuser.getPassword()));
				appuser.setOrg_type(org.getOrgtypeid_link());
				appuser.setRootorgid_link(org.getOrgrootid_link());
				List<GpayRole> roles = new ArrayList<GpayRole>();
				appuser.setRoles(roles);
				GpayUser appusernew = userDetailsService.save(appuser);
				
				for(MenuId entry : entity.usermenu) {
					UserMenu userMenu = new UserMenu();
					userMenu.setMenuid(entry.id);
					userMenu.setUserid(appusernew.getId());
					userMenuService.save(userMenu);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_list",method = RequestMethod.POST)
	public ResponseEntity<UserResponse> GetUserList( @RequestBody UserRequest entity,HttpServletRequest request ) {
		UserResponse response = new UserResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			//GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
			//		.getPrincipal();
			long orgid_link = user.getOrgId();
			response.data=userDetailsService.getUserList(orgid_link,entity.textsearch, entity.status);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updatepass",method = RequestMethod.POST)
	public ResponseEntity<update_pass_response> UpdatePass( @RequestBody update_pass_request entity,HttpServletRequest request ) {
		update_pass_response response = new update_pass_response();
		try {
			String result = "";
			String line;
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String urlPush = "http://gpay.vn:8181/o2admin/changepass";
			String token = request.getHeader("Authorization");
						
			URL url = new URL(urlPush);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            appParNode.put("username", user.getUsername());
            appParNode.put("oldpwd", entity.old_pass);
            appParNode.put("newpwd", entity.new_pass);
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
            OutputStream os = conn.getOutputStream();
            os.write(jsonReq.getBytes());
            os.flush();
                     
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
            
            conn.disconnect();
            
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(result);
            
            String status = map.get("status").toString();
            if(!status.equals("0")) {
            	response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
    			response.setMessage(map.get("msg").toString());
            }
            else {
            	response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
    			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
            }
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    
		}
		return new ResponseEntity<update_pass_response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getbyorg",method = RequestMethod.POST)
	public ResponseEntity<UserResponse> GetByOrg( @RequestBody user_getbyorg_request entity,HttpServletRequest request ) {
		UserResponse response = new UserResponse();
		try {
			long orgid_link = entity.orgid_link;
			
			response.data=userDetailsService.getUserList(orgid_link,"", 1);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getby_org_buyer",method = RequestMethod.POST)
	public ResponseEntity<getby_org_pcontract_response> GetByOrg( @RequestBody getby_org_pcontract_request entity,HttpServletRequest request ) {
		getby_org_pcontract_response response = new getby_org_pcontract_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long orgid_link = entity.orgid_link;
			long orgbuyerid_link = entity.orgbuyerid_link;
			
			response.data=userDetailsService.getUserList(orgid_link,"", 1);
			
			for(GpayUser _user : response.data) {
				long merchandiserid_link = _user.getId();
				_user.index = pcontractService.getby_buyer_merchandiser(orgrootid_link, orgbuyerid_link, merchandiserid_link);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_org_pcontract_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_org_pcontract_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete( @RequestBody UserByIdRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			userDetailsService.deleteById(entity.userid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_getbyid",method = RequestMethod.POST)
	public ResponseEntity<UserByIdResponse> GetByID( @RequestBody UserByIdRequest entity,HttpServletRequest request ) {
		UserByIdResponse response = new UserByIdResponse();
		try {
			response.data=userDetailsService.findById(entity.userid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserByIdResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<UserByIdResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_list_bypage",method = RequestMethod.POST)
	public ResponseEntity<UserResponse> GetUserList_Page( @RequestBody User_getList_byPage_request entity,HttpServletRequest request ) {
		UserResponse response = new UserResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			//GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
			//		.getPrincipal();
			String firstname = entity.firstname;
			String middlename = entity.middlename;
			String lastname = entity.lastname;
			String username = entity.username;
			Long groupuserid_link = entity.groupuserid_link;
			
			
			response.data=userDetailsService.getUserList_page( firstname, middlename, lastname, username, groupuserid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_getinfo",method = RequestMethod.POST)
	public ResponseEntity<UserByIdResponse> GetByID(HttpServletRequest request, @RequestBody User_getinfo_request entity ) {
		UserByIdResponse response = new UserByIdResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			if(entity.id == null || entity.id == 0) {
				response.data=userDetailsService.findById(user.getUserId());
			}
			else
				response.data=userDetailsService.findById(entity.id);
			
			// TODO: kiem tra quyen truoc khi tra len
			
			response.data.setPassword("");
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserByIdResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<UserByIdResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_update",method = RequestMethod.POST)
	public ResponseEntity<UserResponse> UserUpdatet( @RequestBody User_update_request entity,HttpServletRequest request ) {
		UserResponse response = new UserResponse();
		try {
			GpayUser appuser = userDetailsService.findOne(entity.user.getId());
			appuser.setEmail(entity.user.getEmail());
			appuser.setFirstname(entity.user.getFirstname());
			appuser.setMiddlename(entity.user.getMiddlename());
			appuser.setLastname(entity.user.getLastname());
			appuser.setOrgid_link(entity.user.getOrgid_link());
			appuser.setTel_mobile(entity.user.getTel_mobile());
			appuser.setTel_office(entity.user.getTel_office());
			appuser.setStatus(entity.user.getStatus());
			
			userDetailsService.save(appuser);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<UserResponse>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_updaterole",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateRole(HttpServletRequest request, @RequestBody User_updateRole_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			long roleid_link = entity.roleid_link;
			long userid = entity.userid;
			//checked = true : thêm mới , false: xóa
			if(entity.checked) {
				//Lưu nhóm quyền
				AppRole_User appuser = new AppRole_User();
				appuser.setId(null);
				appuser.setRole_id(roleid_link);
				appuser.setUser_id(userid);
				appuserService.save(appuser);
				
				//Lưu menu
				List<Menu> listmenu = menuService.getMenu_byRole(roleid_link);
				for(Menu menu : listmenu) {
					String menuid_link = menu.getId();
					AppUserMenu usermenu = new AppUserMenu();
					usermenu.setId(null);
					usermenu.setMenuid(menuid_link);
					usermenu.setUserid(userid);
					usermenuService.save(usermenu);
					
					//Lưu function
					List<AppFunction> list_app_role_func = appfuncService.getAppFunction_inmenu(menuid_link, roleid_link);
					for(AppFunction appf : list_app_role_func) {
						long functionid_link = appf.getId();
						AppUserFunction appuserf = new AppUserFunction();
						appuserf.setFunctionid_link(functionid_link);
						appuserf.setId(null);
						appuserf.setIshidden(false);
						appuserf.setIsreadonly(false);
						appuserf.setUserid_link(userid);
						
						appuserfService.save(appuserf);
					}
				}
				
				
			}
			else {
				//Xóa nhóm quyền
				AppRole_User appuser = appuserService.getby_user_and_role(userid, roleid_link).get(0);
				appuserService.delete(appuser);
				
				//Xóa menu
				List<Menu> listmenu = menuService.getMenu_byRole(roleid_link);
				for(Menu menu: listmenu) {
					String menuid_link = menu.getId();
					AppUserMenu usermenu = usermenuService.getuser_menu_by_menuid_and_userid(menuid_link, userid).get(0);
					usermenuService.delete(usermenu);
					
					List<AppFunction> list_app_role_func = appfuncService.getAppFunction_inmenu(menuid_link, roleid_link);
					for(AppFunction appf : list_app_role_func) {
						long functionid_link = appf.getId();
						AppUserFunction appuserf = appuserfService.getby_function_and_user(functionid_link, userid).get(0);
						appuserfService.delete(appuserf);
					}
				}
				
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/user_create_fromauthen",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create_FromAuthen( @RequestBody user_create_user_fromauthen_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = new GpayUser();			
			
			if(!entity.enable) {
				user.setOrgid_link((long)0);
				user.setId((long)entity.id);
				user.setFirstname(entity.firstname);
				user.setMiddlename(entity.middlename);
				user.setLastname(entity.lastname);
				user.setFullname(entity.fullname);
				user.setEmail(entity.email);
				user.setUsername(entity.username);
				user.setRootorgid_link((long)entity.orgrootid);
				user.setStatus(1);		
				user.setEnabled(true);
				user.setUserrole("ROLE_USER");
				user.setOrg_type(1);
				user.setRootorgid_link(user.getRootorgid_link());

				user = userDetailsService.save(user);
			}
			
			
			if(entity.isrootadmin) {
				AppRole_User role = new AppRole_User();
				role.setId(null);
				role.setRole_id(AtributeFixValues.role_id_admin);
				role.setUser_id((long)entity.id);
				roleuserService.save(role);
			}
			else {
				//Xoa quyen cua user
				List<AppRole_User> listrole = roleuserService.getby_user_and_role((long)entity.id, (long)0);
				for(AppRole_User role : listrole) {
					roleuserService.delete(role);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
}
