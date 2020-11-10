package vn.gpay.gsmart.core.api.devicesinvcheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.devicesinvcheck.DevicesInvCheck;
import vn.gpay.gsmart.core.devicesinvcheck.DevicesInvCheckEPC;
import vn.gpay.gsmart.core.devicesinvcheck.IDevicesInvCheckService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devicesinvcheck")
public class DevicesInvCheckAPI {
	@Autowired IDevicesInvCheckService devicesInvCheckService;
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/devicesinvcheckGetByOrg",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetByOrg(@RequestBody DevicesInvCheck_getByOrg_request entity, HttpServletRequest request ) {
		DevicesInvCheck_getByOrg_response response = new DevicesInvCheck_getByOrg_response();
		try {
			
			List<DevicesInvCheck> listdata = devicesInvCheckService.findByOrgCheckId(entity.orgcheckid_link);
			
			response.data = listdata;//listdevice;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesInvCheck_getByOrg_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/devicesinvcheckCreate",method = RequestMethod.POST)
	public ResponseEntity<?> DevicesInvCheckCreate(@RequestBody DevicesInvCheck_create_request entity, HttpServletRequest request ) {
		DevicesInvCheck_getByOrg_response response = new DevicesInvCheck_getByOrg_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			UUID uuid = UUID.randomUUID();
		    String invcheckcode = uuid.toString();
		    DevicesInvCheck invcheck = entity.data;
		    
		    invcheck.setOrgrootid_link(user.getRootorgid_link());
			invcheck.setUsercreateid_link(user.getUserId());
			invcheck.setLastuserupdateid_link(user.getUserId());
			invcheck.setInvcheckdatetime(new Date());
			invcheck.setInvcheckcode(invcheckcode);
			int isChecksuccess = 1;
			for (DevicesInvCheckEPC epc: invcheck.getEpcs()){
				if (epc.getCheckstatus() == 0){
					isChecksuccess = 0;
					break;
				}
			}
			invcheck.setStatus(isChecksuccess);
			invcheck = devicesInvCheckService.save(invcheck);
			
			//
			
			//
			
			response.data = new ArrayList<DevicesInvCheck>();
			response.data.add(invcheck);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesInvCheck_getByOrg_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
