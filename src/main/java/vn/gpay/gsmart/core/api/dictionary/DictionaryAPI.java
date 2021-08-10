package vn.gpay.gsmart.core.api.dictionary;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.dictionary.Dictionary;
import vn.gpay.gsmart.core.dictionary.IDictionary_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/dictionary")
public class DictionaryAPI {
	@Autowired IDictionary_Service dictionary_service;
	
	/**
	 * Lay danh sasch dictionary
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_load_response> Devices_Type_Load(HttpServletRequest request) {
		Dictionary_load_response response = new Dictionary_load_response();
		try {
			// list dictionary_type
			response.data = dictionary_service.loadDictionary();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_load_response>(response, HttpStatus.OK);
	}
	/**
	 * Them moi dictionary
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_create_response> Devices_Type_Add(
			@RequestBody Dictionary_create_request entity, HttpServletRequest request) {
		Dictionary_create_response response = new Dictionary_create_response();
		try {
			Dictionary dictionary = entity.data;
			dictionary_service.save(dictionary);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_create_response>(response, HttpStatus.OK);
	}
	/**
	 * Xoa dictionary
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_delete_response> Devices_Type_Delete(
			@RequestBody Dictionary_delete_request entity, HttpServletRequest request) {
		Dictionary_delete_response response = new Dictionary_delete_response();
		try {
			// list dictionary
			Dictionary devices = entity.data;
			dictionary_service.delete(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_delete_response>(response, HttpStatus.OK);
	}
}
