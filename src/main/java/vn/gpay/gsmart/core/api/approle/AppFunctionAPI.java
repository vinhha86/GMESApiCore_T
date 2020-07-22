package vn.gpay.gsmart.core.api.approle;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.approle.AppFunctionBinding;
import vn.gpay.gsmart.core.approle.AppRoleFunction;
import vn.gpay.gsmart.core.approle.IAppFunctionService;
import vn.gpay.gsmart.core.approle.IAppRoleFunctionService;
import vn.gpay.gsmart.core.base.ResponseError;

@RestController
@RequestMapping("/api/v1/appfunction")
public class AppFunctionAPI {
	@Autowired
	IAppFunctionService appfuncService;
	@Autowired
	IAppRoleFunctionService approlefuncService;
	
	@RequestMapping(value = "/getby_roleid",method = RequestMethod.POST)
	public ResponseEntity<?> getby_roleid(HttpServletRequest request, @RequestBody AppFunction_getby_role_request entity ) {
		try {
			AppFunction_getby_role_response response = new AppFunction_getby_role_response();
			String menuid_link = entity.menuid_link;
			long roleid_link = entity.roleid_link;
			
			List<AppFunction> list_app_func = appfuncService.getAppFunction_byMenu(menuid_link);
			List<Long> list_app_role_func = approlefuncService.getfunction_byrole(roleid_link);
			List<AppFunctionBinding> listbinding = new ArrayList<AppFunctionBinding>();
			
			for(AppFunction appfunc : list_app_func) {
				AppFunctionBinding binding = new AppFunctionBinding();
				binding.setIcon(appfunc.getIcon());
				binding.setId(appfunc.getId());
				binding.setName(appfunc.getName());
				if(list_app_role_func.contains(appfunc.getId())) {
					binding.setChecked(true);
					AppRoleFunction arf = approlefuncService.getby_role_and_function(roleid_link, appfunc.getId());
					binding.setReadonly(arf.getIsreadonly());
				}					
				else {
					binding.setChecked(false);
					binding.setReadonly(false);
				}
					
				
				listbinding.add(binding);
			}
			response.data = listbinding;
			return new ResponseEntity<AppFunction_getby_role_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getin_roleid",method = RequestMethod.POST)
	public ResponseEntity<?> getin_roleid(HttpServletRequest request, @RequestBody AppFunction_getby_role_request entity ) {
		try {
			AppFunction_getby_role_response response = new AppFunction_getby_role_response();
			String menuid_link = entity.menuid_link;
			long roleid_link = entity.roleid_link;
			
			List<AppFunction> list_app_role_func = appfuncService.getAppFunction_inmenu(menuid_link, roleid_link);
			List<AppFunctionBinding> listbinding = new ArrayList<AppFunctionBinding>();
			
			for(AppFunction appfunc : list_app_role_func) {
				AppFunctionBinding binding = new AppFunctionBinding();
				binding.setIcon(appfunc.getIcon());
				binding.setId(appfunc.getId());
				binding.setName(appfunc.getName());
				
				listbinding.add(binding);
			}
			response.data = listbinding;
			return new ResponseEntity<AppFunction_getby_role_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
