package vn.gpay.gsmart.core.api.devices;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.devices.DeviceGroup;
import vn.gpay.gsmart.core.devices.DeviceGroupTree;
import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.devices.IDeviceGroupService;
import vn.gpay.gsmart.core.devices.IDevicesService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devicegroup")
public class DeviceGroupAPI {
	@Autowired
	IDeviceGroupService devicegroupService;
	@Autowired
	IDevicesService deviceService;
	
	@RequestMapping(value = "/getlist",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGroup_GetList(@RequestBody DeviceTreeRequest entity,HttpServletRequest request ) {
		DeviceGroup_getlist_response response = new DeviceGroup_getlist_response();
		try {
			response.data = devicegroupService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<DeviceGroup_getlist_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/devicegroup_tree",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGroupTree(HttpServletRequest request ) {
		try {
			DeviceGroup_getTree_response response = new DeviceGroup_getTree_response();
			List<DeviceGroup> menu = devicegroupService.findAll();
			List<DeviceGroupTree> children = devicegroupService.createTree(menu);
//			System.out.println(menu.size());
			response.children=children;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DeviceGroup_getTree_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/createDeviceGroup",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateDeviceGroup(@RequestBody DeviceGroup_create_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		DeviceGroup_create_response response = new DeviceGroup_create_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			DeviceGroup devicegroup = entity.data;
			devicegroup = devicegroupService.save(devicegroup);
			response.id = devicegroup.getId();
			response.devicegroup = devicegroup;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteDeviceGroup",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteDeviceGroup(@RequestBody DeviceGroup_delete_request entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			DeviceGroup dg = devicegroupService.findOne(entity.id);
			List<DeviceGroup> listDeviceGroup = devicegroupService.findByParentId(entity.id);
			if(listDeviceGroup.size() > 0 && dg.getParentid_link() == -1) {
				response.setMessage("Xoá thất bại, đã tồn tại nhóm thiết bị con");
			}else {
				List<Devices> listDevice = deviceService.findByDeviceGroup(dg.getId());
				if(listDevice.size() > 0) {
					response.setMessage("Xoá thất bại, đã tồn tại thiết bị thuộc nhóm này");
				}else {
					devicegroupService.delete(dg);
					response.setMessage("Xoá thành công");
				}
			}
//			devicegroupService.delete(dg);
//			response.setMessage("Xoá thành công");
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getById",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGroup_GetById(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		DeviceGroup_getone_response response = new DeviceGroup_getone_response();
		try {
			response.data = devicegroupService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<DeviceGroup_getone_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getByParentId",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGroup_GetByParentId(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		DeviceGroup_getlist_response response = new DeviceGroup_getlist_response();
		try {
			response.data = devicegroupService.findByParentId(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<DeviceGroup_getlist_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getAllParent",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGroup_GetAllParent(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		DeviceGroup_getlist_response response = new DeviceGroup_getlist_response();
		try {
			response.data = devicegroupService.findAllParent();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<DeviceGroup_getlist_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
