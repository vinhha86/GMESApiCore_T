package vn.gpay.gsmart.core.api.devices;

import java.util.Date;
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
import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.devices.IDevicesService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/device")
public class DevicesAPI {
   
	@Autowired IDevicesService devicesService;
	
	@RequestMapping(value = "/device_listtree",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceListtree(@RequestBody DeviceTreeRequest entity,HttpServletRequest request ) {
		DevicesResponse response = new DevicesResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = devicesService.device_list(user.getRootorgid_link(),entity.org_governid_link,entity.search, entity.disable);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deviceGetByOrg",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetByOrg(@RequestBody Devices_getByOrg_request entity, HttpServletRequest request ) {
		DevicesResponse response = new DevicesResponse();
		try {
			
			List<Devices> listdata = devicesService.findByOrg(entity.org_governid_link);
			
			response.data = listdata;//listdevice;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deviceGetByOrgEPC",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetByOrgEPC(@RequestBody Devices_getByOrgEPC_Request entity, HttpServletRequest request ) {
		Devices_GetByOrgEPC_Response response = new Devices_GetByOrgEPC_Response();
		try {
			
			Devices devicedata = devicesService.finByOrgEPC(entity.org_governid_link, entity.epc);
			
			response.data = devicedata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Devices_GetByOrgEPC_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/device_getactivate",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetaActivate(@RequestBody DevicesTypeRequest entity, HttpServletRequest request ) {
		DevicesResponse response = new DevicesResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			List<Devices> listdata = devicesService.device_govern(user.getOrgId(),entity.type);
			/*
			String url = "http://gpay.vn:9091/service/devicestate";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(200==responseCode) {
				

			}
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			ObjectMapper mapper = new ObjectMapper();
			List<Devices> listdevice = new ArrayList<Devices>();
			List<DeviceLocal> listlocal = mapper.readValue(resp.toString(), new TypeReference<List<DeviceLocal>>(){});
			listlocal.forEach(devicelocal -> {
				Devices devices = listdata.stream()
						  .filter(device -> devicelocal.getDeviceid().equals(device.getCode())  && device.getType()==entity.type)
						  .findAny()
						  .orElse(null);
				if(devices!=null) {
					listdevice.add(devices);
				}
			});*/
			response.data = listdata;//listdevice;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/device_create",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceCreate(@RequestBody DevicesRequest entity,HttpServletRequest request ) {
		Device_create_response response = new Device_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Devices devices = entity.data.get(0);
			if(devices.getId()==null || devices.getId()==0) {
				devices.setOrgrootid_link(user.getRootorgid_link());
				devices.setType(devices.getDevicegroupid_link());
				devices.setUsercreateid_link(user.getUserId());
				devices.setTimecreate(new Date());
			}else {
				Devices devices_old =  devicesService.findOne(devices.getId());
				devices.setType(entity.data.get(0).getDevicegroupid_link());
				devices.setOrgrootid_link(user.getRootorgid_link());
				devices.setUsercreateid_link(devices_old.getUsercreateid_link());
				devices.setTimecreate(devices_old.getTimecreate());
				devices.setLastuserupdateid_link(user.getOrgId());
				devices.setStatus(devices_old.getStatus());
				devices.setLasttimeupdate(new Date());
			}
			devices = devicesService.save(devices);
			response.id = devices.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Device_create_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/device_lock",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceLook(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Devices devices =  devicesService.findOne(entity.id);
			devices.setStatus(3);//trang thai khoa
			devices.setDisable(true);
			devices.setLastuserupdateid_link(user.getOrgId());
			devices.setLasttimeupdate(new Date());
			devicesService.save(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/device_delete",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceDelete(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Devices devices =  devicesService.findOne(entity.id);
			devices.setStatus(-1);//trang thai khoa
			devices.setDisable(true);
			devices.setLastuserupdateid_link(user.getOrgId());
			devices.setLasttimeupdate(new Date());
			devicesService.save(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/device_unlock",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceUnLook(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Devices devices =  devicesService.findOne(entity.id);
			devices.setDisable(false);//trang thai mo khoa
			devices.setStatus(0); // Mở khóa trạng thái mặc định là O : Offline
			devices.setLastuserupdateid_link(user.getOrgId());
			devices.setLasttimeupdate(new Date());
			devicesService.save(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deviceGetById",method = RequestMethod.POST)
	public ResponseEntity<?> DeviceGetById(@RequestBody DevicesByIDRequest entity,HttpServletRequest request ) {
		DevicesByIDResponse response = new DevicesByIDResponse();
		try {
			
			response.data = devicesService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DevicesByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
