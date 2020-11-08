package vn.gpay.gsmart.core.api.devicein;

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
import vn.gpay.gsmart.core.devicein_d.DeviceIn_d;
import vn.gpay.gsmart.core.devicein_d.IDeviceIn_dService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devicein_d")
public class DeviceIn_dAPI {

	@Autowired IDeviceIn_dService devicein_dService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<DeviceIn_dCreate_Response> Create(@RequestBody DeviceIn_dCreate_Request entity, HttpServletRequest request) {
		DeviceIn_dCreate_Response response = new DeviceIn_dCreate_Response();
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		Long orgrootid_link = user.getRootorgid_link();
		try {
			DeviceIn_d newDeviceIn_d = entity.data;
			if (null != newDeviceIn_d){
				if (null == newDeviceIn_d.getId() || 0 == newDeviceIn_d.getId()){
					//Them moi
					newDeviceIn_d.setOrgrootid_link(orgrootid_link);
					devicein_dService.save(newDeviceIn_d);
				} else {
					//Edit
					devicein_dService.save(newDeviceIn_d);
				}
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			    return new ResponseEntity<DeviceIn_dCreate_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
			    return new ResponseEntity<DeviceIn_dCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<DeviceIn_dCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody DeviceInGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
//			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			DeviceIn_d theDeviceIn_d = devicein_dService.findOne(entity.id);
			if (null != theDeviceIn_d){
	
				devicein_dService.delete(theDeviceIn_d);
	
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Không tìm thấy thiết bị");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getByDeviceIn", method = RequestMethod.POST)
	public ResponseEntity<DeviceIn_dGetByDeviceIn_Response> getByDeviceIn(@RequestBody DeviceInGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		DeviceIn_dGetByDeviceIn_Response response = new DeviceIn_dGetByDeviceIn_Response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			Long orgrootid_link = user.getRootorgid_link();
			response.data = devicein_dService.getByDeviceIn(orgrootid_link, entity.id);
//			response.data = devicein_dService.getByDeviceIn(1L, entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DeviceIn_dGetByDeviceIn_Response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceIn_dGetByDeviceIn_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
