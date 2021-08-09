package vn.gpay.gsmart.core.api.dictonary_type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.gpay.gsmart.core.base.ResponseError;

import vn.gpay.gsmart.core.dictionary_type.DictionaryType;
import vn.gpay.gsmart.core.dictionary_type.IDictionaryType_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/dictionary_type")
public class Dictionary_TypeAPI {
	@Autowired
	IDictionaryType_Service dictionary_typeService;

	/**
	 * Lấy danh sách từ điển
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/load_dictionary_type", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_Type_load_response> Devices_Type_Load(HttpServletRequest request) {
		Dictionary_Type_load_response response = new Dictionary_Type_load_response();
		try {
			// list dictionary_type
			response.data = dictionary_typeService.loadDictionaryType();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));

		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_Type_load_response>(response, HttpStatus.OK);
	}

	/**
	 * Thêm từ điển
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_Type_create_response> Devices_Type_Add(
			@RequestBody Dictionary_Type_create_request entity, HttpServletRequest request) {
		Dictionary_Type_create_response response = new Dictionary_Type_create_response();
		try {
			DictionaryType devices = entity.data;
			dictionary_typeService.save(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_Type_create_response>(response, HttpStatus.OK);
	}

	/**
	 * xóa loại từ điển
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Dictionary_Type_delete_response> Devices_Type_Delete(
			@RequestBody Dictionary_Type_delete_request entity, HttpServletRequest request) {
		Dictionary_Type_delete_response response = new Dictionary_Type_delete_response();
		try {
			// list devices_type
			DictionaryType devices = entity.data;
			dictionary_typeService.delete(devices);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (RuntimeException e) {
			response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Dictionary_Type_delete_response>(response, HttpStatus.OK);
	}
}
