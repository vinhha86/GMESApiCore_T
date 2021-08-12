package vn.gpay.gsmart.core.api.personnel_position;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.personnel_position.IPersonnel_Position_Service;
import vn.gpay.gsmart.core.personnel_position.Personnel_Position;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel_position")
public class Personnel_Position_API {
	@Autowired IPersonnel_Position_Service personnel_position_service;
	
	/*
	 * Load danh sach
	 */
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public ResponseEntity<Personnel_Position_load_response> Personnel_Position_Load(
			HttpServletRequest request) {
		Personnel_Position_load_response response = new Personnel_Position_load_response();
		try {
			//list personnel_position
			response.data = personnel_position_service.getPersonnel_Position();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Personnel_Position_load_response>(response, HttpStatus.OK);
	}
	/**
	 * thêm
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Personnel_Position_create_reponse> Personnel_Position_create(
			@RequestBody Personnel_Position_create_request entity, HttpServletRequest request) {
		Personnel_Position_create_reponse response = new Personnel_Position_create_reponse();
		try {
			
			Personnel_Position personnel_position = entity.data;
			personnel_position_service.save(personnel_position);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Personnel_Position_create_reponse>(response, HttpStatus.OK);
	}
	/**
	 * Xoa
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Personnel_Position_delete_response> Personnel_Position_Delete(
			@RequestBody Personnel_Position_delete_request entity, HttpServletRequest request) {
		Personnel_Position_delete_response response = new Personnel_Position_delete_response();
		try {
			
			Personnel_Position personnel_position = entity.data;
			personnel_position_service.delete(personnel_position);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Personnel_Position_delete_response>(response, HttpStatus.OK);
	}
}
