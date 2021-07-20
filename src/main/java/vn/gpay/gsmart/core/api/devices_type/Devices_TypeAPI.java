package vn.gpay.gsmart.core.api.devices_type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.devices_type.Devices_Type;
import vn.gpay.gsmart.core.devices_type.IDevices_TypeService;

import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/devices_type")
public class Devices_TypeAPI {
	@Autowired
	IDevices_TypeService devices_typeService;

//load danh sach
	@RequestMapping(value = "/load_devices_type", method = RequestMethod.POST)
	public ResponseEntity<Devices_Type_load_response> Devices_Type_Load(@RequestBody Devices_Type_load_request entity,
			HttpServletRequest request) {
		Devices_Type_load_response response = new Devices_Type_load_response();
		try {
			// list devices_type
			response.data = devices_typeService.loadDivicesType();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Devices_Type_load_response>(response, HttpStatus.OK);
	}

//them devices_type
	@RequestMapping(value = "/add_devices_type", method = RequestMethod.POST)
	public ResponseEntity<Devices_Type_create_response> Devices_Type_Add(
			@RequestBody Devices_Type_create_request entity, HttpServletRequest request) {
		Devices_Type_create_response response = new Devices_Type_create_response();
		try {
			// list devices_type
			Devices_Type devices = entity.data;
			devices_typeService.save(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Devices_Type_create_response>(response, HttpStatus.OK);
	}

	// xoa devices_type
	@RequestMapping(value = "/delete_devices_type", method = RequestMethod.POST)
	public ResponseEntity<Devices_Type_delete_response> Devices_Type_Delete(
			@RequestBody Devices_Type_delete_request entity, HttpServletRequest request) {
		Devices_Type_delete_response response = new Devices_Type_delete_response();
		try {
			// list devices_type
			Devices_Type devices = entity.data;
			devices_typeService.delete(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Devices_Type_delete_response>(response, HttpStatus.OK);
	}

}
