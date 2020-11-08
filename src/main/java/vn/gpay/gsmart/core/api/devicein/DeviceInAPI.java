package vn.gpay.gsmart.core.api.devicein;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.devicein.DeviceIn;
import vn.gpay.gsmart.core.devicein.IDeviceInService;
import vn.gpay.gsmart.core.devicein_d.DeviceIn_d;
import vn.gpay.gsmart.core.devicein_d.IDeviceIn_dService;
import vn.gpay.gsmart.core.devicein_type.IDeviceInTypeService;
import vn.gpay.gsmart.core.deviceout.DeviceOut;
import vn.gpay.gsmart.core.deviceout.IDeviceOutService;
import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.devices.IDevicesService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.DeviceinStatus;
import vn.gpay.gsmart.core.utils.DeviceinType;
import vn.gpay.gsmart.core.utils.DeviceoutStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devicein")
public class DeviceInAPI {

	@Autowired IDeviceInService deviceinService;
	@Autowired IDevicesService deviceService;
	@Autowired IDeviceIn_dService devicein_dService;
	@Autowired IDeviceInTypeService deviceinTypeService;
	
	@Autowired IDeviceOutService deviceoutService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<DeviceInCreate_Response> Create(@RequestBody DeviceInCreate_Request entity, HttpServletRequest request) {
		DeviceInCreate_Response response = new DeviceInCreate_Response();
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		Long orgrootid_link = user.getRootorgid_link();
		try {
			DeviceIn newDeviceIn = entity.data;
			if (null != newDeviceIn && newDeviceIn.getDevicein_d().size() > 0){
				if (null == newDeviceIn.getId() || 0 == newDeviceIn.getId()){
					//Them moi
					newDeviceIn.setStatus(DeviceinStatus.DEVICEIN_STATUS_OK);
					newDeviceIn.setOrgrootid_link(orgrootid_link);
					newDeviceIn.setUsercreateid_link(user.getUserId());
					newDeviceIn.setTimecreate(new Date());
					newDeviceIn = deviceinService.save(newDeviceIn);
					
					//Neu la phieu dieu chuyen --> Update lai trang thai cua Phieu xuat tuong ung
					if (newDeviceIn.getDeviceintypeid_link() ==  DeviceinType.DEVICEIN_TYPE_MOVE){
						DeviceOut theDeviceout = deviceoutService.findOne(newDeviceIn.getDeviceoutid_link());
						if (null != theDeviceout){
							theDeviceout.setStatus(DeviceoutStatus.DEVICEOUT_STATUS_CONFIRMED);
							deviceoutService.save(theDeviceout);
						}
					}
					
					for(DeviceIn_d theDevicein_d: newDeviceIn.getDevicein_d()){
						//Kiem tra xem EPC cua Device da ton tai hay chua
						Devices theDevice = deviceService.finByEPC(theDevicein_d.getEpc());
						if (null != theDevice){
							//Neu da ton tai ==>Cap nhat thong tin
							theDevice.setCode(theDevicein_d.getCode());
							theDevice.setOrg_governid_link(newDeviceIn.getOrgid_to_link());
							theDevice.setStatus(0);
							theDevice.setDisable(false);
							theDevice.setLastuserupdateid_link(user.getUserId());
							theDevice.setLasttimeupdate(new Date());
							theDevice = deviceService.save(theDevice);
						} else {
							//Chua ton tai --> Tao moi tbi
							theDevice = new Devices();
							theDevice.setOrgrootid_link(orgrootid_link);
							theDevice.setDevicegroupid_link(theDevicein_d.getDevicegroupid_link());
							theDevice.setCode(theDevicein_d.getCode());
							theDevice.setEpc(theDevicein_d.getEpc());
							theDevice.setOrg_governid_link(newDeviceIn.getOrgid_to_link());
							theDevice.setStatus(0);
							theDevice.setDisable(false);
							theDevice.setUsercreateid_link(user.getUserId());
							theDevice.setTimecreate(new Date());
							theDevice = deviceService.save(theDevice);
						}
						
						//Them Devicein_d
						theDevicein_d.setOrgrootid_link(orgrootid_link);
						theDevicein_d.setDeviceinid_link(newDeviceIn.getId());
						theDevicein_d.setDeviceid_link(theDevice.getId());
						devicein_dService.save(theDevicein_d);
					}
					
					//Update lai trang thai cua Deviceout
				} else {
					//Edit
					DeviceIn temp = deviceinService.save(newDeviceIn);
					response.id = temp.getId();
				}
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			    return new ResponseEntity<DeviceInCreate_Response>(response, HttpStatus.OK);
			} else {
				throw new RuntimeException("Danh sách thiết bị không được để trắng");
			}

		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<DeviceInCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getbyid", method = RequestMethod.POST)
	public ResponseEntity<DeviceInGetById_Response> GetById(@RequestBody DeviceInGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
//		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		DeviceInGetById_Response response = new DeviceInGetById_Response();
		try {
			DeviceIn theDeviceIn = deviceinService.findOne(entity.id);
			if (null != theDeviceIn){
				response.data =  theDeviceIn;
	
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<DeviceInGetById_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Không tìm thấy Phiếu nhập");
				return new ResponseEntity<DeviceInGetById_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceInGetById_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/filterall", method = RequestMethod.POST)
	public ResponseEntity<DeviceInFilter_Response> FilterAll(@RequestBody DeviceInFilter_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		DeviceInFilter_Response response = new DeviceInFilter_Response();
		try {
			response.data = deviceinService.filterAll(
					user.getUserId(), 
					entity.devicein_code, 
					entity.devicein_date_from, 
					entity.devicein_date_to, 
					entity.deviceintypeid_link, 
					entity.invoice_code, 
					entity.deviceoutid_link, 
					entity.orgid_from_link, 
					entity.orgid_to_link, 
					entity.usercreateid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DeviceInFilter_Response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceInFilter_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/findall", method = RequestMethod.POST)
	public ResponseEntity<DeviceInFilter_Response> FindAll(@RequestBody DeviceInFilter_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
//		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		DeviceInFilter_Response response = new DeviceInFilter_Response();
		try {
			response.data = deviceinService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DeviceInFilter_Response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceInFilter_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody DeviceInGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			DeviceIn theDeviceIn = deviceinService.findOne(entity.id);
			if (null != theDeviceIn){
				theDeviceIn.setStatus(DeviceinStatus.DEVICEIN_STATUS_DELETED);
				theDeviceIn.setUserupdateid_link(user.getUserId());
				theDeviceIn.setTimeupdate(new Date());
	
				deviceinService.save(theDeviceIn);
				
				for(DeviceIn_d theDevicein_d:theDeviceIn.getDevicein_d()){
					//Neu la Phieu nhap mua moi --> Xoa Device
					if (theDeviceIn.getDeviceintypeid_link() == DeviceinType.DEVICEIN_TYPE_NEW){
						Devices theDevice = deviceService.findOne(theDevicein_d.getDeviceid_link());
						if (null != theDevice){
							theDevice.setOrg_governid_link(null);
							theDevice.setStatus(-1);
							theDevice.setDisable(true);
							theDevice.setLastuserupdateid_link(user.getUserId());
							theDevice.setLasttimeupdate(new Date());
							deviceService.save(theDevice);
						}
					} else {
						//Neu la Phieu nhap dieu chuyen --> Doi dơn vi quan ly tbi ve org_fromid_link						
						if (theDeviceIn.getDeviceintypeid_link() == DeviceinType.DEVICEIN_TYPE_MOVE){
							Devices theDevice = deviceService.findOne(theDevicein_d.getDeviceid_link());
							if (null != theDevice){
								theDevice.setOrg_governid_link(theDeviceIn.getOrgid_from_link());
								theDevice.setLastuserupdateid_link(user.getUserId());
								theDevice.setLasttimeupdate(new Date());
								deviceService.save(theDevice);
							}							
						}
					}
				}
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Không tìm thấy Phiếu nhập");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<Devicein_gettype_response> Gettype(HttpServletRequest request ) {
		Devicein_gettype_response response = new Devicein_gettype_response();
		try {
			response.data = deviceinTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Devicein_gettype_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Devicein_gettype_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}