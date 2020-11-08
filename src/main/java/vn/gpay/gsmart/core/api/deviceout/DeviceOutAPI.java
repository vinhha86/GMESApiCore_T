package vn.gpay.gsmart.core.api.deviceout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import vn.gpay.gsmart.core.deviceout.DeviceOut;
import vn.gpay.gsmart.core.deviceout.IDeviceOutService;
import vn.gpay.gsmart.core.deviceout_d.DeviceOut_d;
import vn.gpay.gsmart.core.deviceout_d.IDeviceOut_dService;
import vn.gpay.gsmart.core.deviceout_type.IDeviceOutTypeService;
import vn.gpay.gsmart.core.devices.Devices;
import vn.gpay.gsmart.core.devices.IDevicesService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.DeviceoutStatus;
import vn.gpay.gsmart.core.utils.DeviceoutType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/deviceout")
public class DeviceOutAPI {

	@Autowired IDeviceOutService deviceoutService;
	@Autowired IDevicesService deviceService;
	@Autowired IDeviceOut_dService deviceout_dService;
	@Autowired IDeviceOutTypeService deviceoutTypeService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<DeviceOutCreate_Response> Create(@RequestBody DeviceOutCreate_Request entity, HttpServletRequest request) {
		DeviceOutCreate_Response response = new DeviceOutCreate_Response();
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		Long orgrootid_link = user.getRootorgid_link();
		try {
			DeviceOut newDeviceOut = entity.data;
			if (null != newDeviceOut && newDeviceOut.getDeviceout_d().size() > 0){
				if (null == newDeviceOut.getId() || 0 == newDeviceOut.getId()){
					//Them moi
					newDeviceOut.setStatus(DeviceoutStatus.DEVICEOUT_STATUS_OK);
					newDeviceOut.setOrgrootid_link(orgrootid_link);
					newDeviceOut.setUsercreateid_link(user.getUserId());
					newDeviceOut.setTimecreate(new Date());
//					newDeviceOut = deviceoutService.save(newDeviceOut);
				} 
				
				//Update bang Device
				for(DeviceOut_d theDeviceout_d: newDeviceOut.getDeviceout_d()){
					theDeviceout_d.setOrgrootid_link(orgrootid_link);
					//Neu la giao dich xuat ban hoac xuat huy --> Xoa Device
					if (newDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_SELL || 
							newDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_DELETE){
						Devices theDevice = deviceService.finByEPC(theDeviceout_d.getEpc());
						if (null != theDevice){
							//Neu da ton tai ==>Cap nhat thong tin
							theDevice.setOrg_governid_link(null);
							theDevice.setStatus(-1);
							theDevice.setDisable(true);
							theDevice.setLastuserupdateid_link(user.getUserId());
							theDevice.setLasttimeupdate(new Date());
							theDevice = deviceService.save(theDevice);
							
							theDeviceout_d.setDeviceid_link(theDevice.getId());
						} else {
							throw new RuntimeException("Thiết bị EPC:" + theDeviceout_d.getDeviceEPCCode() + " không tồn tại!!!");
						}
					} else {
						//Neu la Phieu xuat dieu chuyen --> Doi dơn vi quan ly tbi ve null						
						if (newDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_MOVE){
							Devices theDevice = deviceService.finByEPC(theDeviceout_d.getEpc());
							if (null != theDevice){
								theDevice.setOrg_governid_link(null);
								theDevice.setLastuserupdateid_link(user.getUserId());
								theDevice.setLasttimeupdate(new Date());
								deviceService.save(theDevice);
								
								theDeviceout_d.setDeviceid_link(theDevice.getId());
							} else {
								throw new RuntimeException("Thiết bị EPC:" + theDeviceout_d.getEpc() + " không tồn tại!!!");
							}							
						}						
					}
				}
				
				deviceoutService.save(newDeviceOut);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			    return new ResponseEntity<DeviceOutCreate_Response>(response, HttpStatus.OK);
			} else {
				throw new RuntimeException("Danh sách thiết bị không được để trắng");
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
//				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
//			    return new ResponseEntity<DeviceOutCreate_Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<DeviceOutCreate_Response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getbyid", method = RequestMethod.POST)
	public ResponseEntity<DeviceOutGetById_Response> GetById(@RequestBody DeviceOutGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
//		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		DeviceOutGetById_Response response = new DeviceOutGetById_Response();
		try {
			DeviceOut theDeviceOut = deviceoutService.findOne(entity.id);
			if (null != theDeviceOut){
				response.data =  theDeviceOut;
	
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<DeviceOutGetById_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage("Không tìm thấy Phiếu nhập");
				return new ResponseEntity<DeviceOutGetById_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceOutGetById_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/filterall", method = RequestMethod.POST)
	public ResponseEntity<DeviceOutFilter_Response> FilterAll(@RequestBody DeviceOutFilter_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		DeviceOutFilter_Response response = new DeviceOutFilter_Response();
		try {
			List<Integer> status = entity.status;
			List<DeviceOut> result = new ArrayList<DeviceOut>();
			response.data = new ArrayList<DeviceOut>();
			if(status.size() == 0) {
				result = deviceoutService.filterAll(
						user.getRootorgid_link(), 
						entity.deviceout_code, 
						entity.deviceout_date_from, 
						entity.deviceout_date_to, 
						entity.deviceouttypeid_link, 
						entity.invoice_code, 
						entity.orgid_from_link, 
						entity.orgid_to_link, 
						entity.usercreateid_link,
						null);
			}else {
				for(Integer num : status) {
					List<DeviceOut> temp = deviceoutService.filterAll(
							user.getRootorgid_link(), 
							entity.deviceout_code, 
							entity.deviceout_date_from, 
							entity.deviceout_date_to, 
							entity.deviceouttypeid_link, 
							entity.invoice_code, 
							entity.orgid_from_link, 
							entity.orgid_to_link, 
							entity.usercreateid_link,
							num);
					result.addAll(temp);
				}
			}
			
			response.data.addAll(result);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<DeviceOutFilter_Response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<DeviceOutFilter_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody DeviceOutGetById_Request entity,
			HttpServletRequest request) {// @RequestParam("type")
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			DeviceOut theDeviceOut = deviceoutService.findOne(entity.id);
			if (null != theDeviceOut){
				theDeviceOut.setStatus(DeviceoutStatus.DEVICEOUT_STATUS_DELETED);
				theDeviceOut.setUserupdateid_link(user.getUserId());
				theDeviceOut.setTimeupdate(new Date());
	
				deviceoutService.save(theDeviceOut);
				
				for(DeviceOut_d theDeviceout_d:theDeviceOut.getDeviceout_d()){
					//Neu la Phieu xuat ban hoac tieu huy --> Khoi phuc lai trang thai cua Device
					if (theDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_SELL || 
							theDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_DELETE){
						Devices theDevice = deviceService.findOne(theDeviceout_d.getDeviceid_link());
						if (null != theDevice){
							theDevice.setOrg_governid_link(theDeviceOut.getOrgid_from_link());
							theDevice.setStatus(0);
							theDevice.setDisable(false);
							theDevice.setLastuserupdateid_link(user.getUserId());
							theDevice.setLasttimeupdate(new Date());
							deviceService.save(theDevice);
						}
					} else {
						//Neu la Phieu xuat dieu chuyen --> Doi dơn vi quan ly tbi ve org_fromid_link						
						if (theDeviceOut.getDeviceouttypeid_link() == DeviceoutType.DEVICEOUT_TYPE_MOVE){
							Devices theDevice = deviceService.findOne(theDeviceout_d.getDeviceid_link());
							if (null != theDevice){
								theDevice.setOrg_governid_link(theDeviceOut.getOrgid_from_link());
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
	public ResponseEntity<Deviceout_gettype_response> Gettype(HttpServletRequest request ) {
		Deviceout_gettype_response response = new Deviceout_gettype_response();
		try {
			response.data = deviceoutTypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Deviceout_gettype_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Deviceout_gettype_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}