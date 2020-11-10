package vn.gpay.gsmart.core.api.devicesinvcheck;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.devicesinvcheck.DevicesInvCheckEPC;
import vn.gpay.gsmart.core.devicesinvcheck.IDevicesInvCheckEPCService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devicesinvcheckepc")
public class DevicesInvCheckEPCAPI {
	@Autowired IDevicesInvCheckEPCService devicesInvCheckEPCService;
	
	@RequestMapping(value = "/getByDevicesInvCheck",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetByOrg(@RequestBody DevicesInvCheckEPC_getByDevicesInvCheck_request entity, HttpServletRequest request ) {
		DevicesInvCheckEPC_getByDevicesInvCheck_response response = new DevicesInvCheckEPC_getByDevicesInvCheck_response();
		try {
			
			List<DevicesInvCheckEPC> listdata = devicesInvCheckEPCService.findByDevicesInvCheckId(entity.devices_invcheckid_link);
			
			response.data = listdata;//listdevice;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesInvCheckEPC_getByDevicesInvCheck_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
