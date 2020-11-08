package vn.gpay.gsmart.core.api.deviceout;

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
import vn.gpay.gsmart.core.deviceout_d.DeviceOut_d;
import vn.gpay.gsmart.core.deviceout_d.IDeviceOut_dService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/deviceout_d")
public class DeviceOut_dAPI {

	@Autowired IDeviceOut_dService deviceout_dService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<DeviceOut_dCreate_Response> Create(@RequestBody DeviceOut_dCreate_Request entity, HttpServletRequest request) {
		DeviceOut_dCreate_Response response = new DeviceOut_dCreate_Response();
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		Long orgrootid_link = user.getRootorgid_link();
		try {
			DeviceOut_d newDeviceOut_d = entity.data;
			if (null != newDeviceOut_d){
				if (null == newDeviceOut_d.getId() || 0 == newDeviceOut_d.getId()){
					//Them moi
					newDeviceOut_d.setOrgrootid_link(orgrootid_link);
					deviceout_dService.save(newDeviceOut_d);
				} else {
					//Edit
					deviceout_dService.save(newDeviceOut_d);
				}
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			    return new ResponseEntity<DeviceOut_dCreate_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
			    return new ResponseEntity<DeviceOut_dCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<DeviceOut_dCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody DeviceOutGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
//			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			DeviceOut_d theDeviceOut_d = deviceout_dService.findOne(entity.id);
			if (null != theDeviceOut_d){
	
				deviceout_dService.delete(theDeviceOut_d);
	
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
}
