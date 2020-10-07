package vn.gpay.gsmart.core.api.encode;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.encode.Encode;
import vn.gpay.gsmart.core.encode.Encode_Service;
import vn.gpay.gsmart.core.encodeepc.Encode_EPC;
import vn.gpay.gsmart.core.encodeepc.Encode_EPC_Service;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU_Encode;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.gsmart.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/encodeporder")
public class EncodePorderAPI {
	@Autowired
	Common common;
	@Autowired
	Encode_Service porderencodeService;
	@Autowired
	Encode_EPC_Service porderencodeepcService;
	@Autowired
	IPOrder_Product_SKU_Service porderskuService;
	@Autowired
	IStocking_UniqueCode_Service stockingService;
	
	@RequestMapping(value = "/encode_porder_create", method = RequestMethod.POST)
	public ResponseEntity<encode_porder_create_response> Encode_Porder_Create(
			@RequestBody encode_porder_create_request entity, HttpServletRequest request) {// @RequestParam("type")
		encode_porder_create_response response = new encode_porder_create_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long deviceid_link = entity.data.getDeviceid_link();
			long id = 0;

			// Kiểm tra xem thiết bị có đang được sử dụng trong 1 phiên khác hay không
			List<Encode> listencode = porderencodeService.get_encode_using_device(orgrootid_link, deviceid_link);
			if (listencode.size() == 0) {
				String sessioncode = common.GetSessionCode();
				Encode encode = entity.data;
				encode.setId(null);
				encode.setStatus(0);
				encode.setOrgrootid_link(orgrootid_link);
				encode.setSession_code(sessioncode);
				encode.setTimecreate(new Date());
				encode.setUsercreateid_link(user.getUserId());
				encode = porderencodeService.save(encode);
				
				id = encode.getId();
				
				Stocking_UniqueCode unique = stockingService.getby_type(3);
				unique.setStocking_max(unique.getStocking_max()+ 1);
				stockingService.save(unique);
			}
			else
			{
				long encodeid_link = listencode.get(0).getId();
				Encode encode = porderencodeService.findOne(encodeid_link);
				encode.setStatus(0);
				encode.setTimecreate(new Date());
				encode.setUsercreateid_link(user.getUserId());
				encode.setEncode_amount(0);
				porderencodeService.save(encode);
				
				List<Encode_EPC> list_epc = porderencodeepcService.get_epc_by_encodeid_link(encodeid_link);
				//Nếu danh sách epc = 0 và là của user đang đăng nhập tạo thì cập nhật lại vào phiên đó 
				if(list_epc.size() == 0 && listencode.get(0).getUsercreateid_link() == user.getUserId()) {
					id = encodeid_link;
				}
			}

			response.id = id;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<encode_porder_create_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<encode_porder_create_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/encode_porder_update_epc", method = RequestMethod.POST)
	public ResponseEntity<Encode_update_epc_response> Encode_Porder_Update_EPC(
			@RequestBody Encode_update_epc_request entity, HttpServletRequest request) {// @RequestParam("type")
		Encode_update_epc_response response = new Encode_update_epc_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long encodeid_link = entity.encodeid_link;
			
			Encode encode = porderencodeService.findOne(encodeid_link);
			List<Encode_EPC> list_epc = porderencodeepcService.get_epc_by_encodeid_link(encodeid_link);
			
			for(Encode_EPC epc : list_epc) {
				epc.setDeviceid_link(encode.getDeviceid_link());
				epc.setOrgrootid_link(orgrootid_link);
				epc.setSkuid_link(encode.getSkuid_link());
				epc.setStatus(1);
				porderencodeepcService.save(epc);
			}

			encode.setStatus(1);
			encode.setEncode_amount(list_epc.size());
			porderencodeService.save(encode);

			encode = porderencodeService.findOne(encodeid_link);
			response.data = encode;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Encode_update_epc_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<Encode_update_epc_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/encode_porder_getlist_bypage", method = RequestMethod.POST)
	public ResponseEntity<Encode_Porder_getlist_response> Encode_Porder_Destroy_Session(
			@RequestBody Encode_Porder_getlist_request entity, HttpServletRequest request) {// @RequestParam("type")
		Encode_Porder_getlist_response response = new Encode_Porder_getlist_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			String pordercode = entity.pordercode;
			Long usercreateid_link = entity.usercreateid_link;
			Date encodedatefrom = entity.encodedatefrom;
			Date encodedateto = entity.encodedateto;
			int limit = entity.limit;
			int page = entity.page;
			
			Page<Encode> pageencode = porderencodeService.getlist_bypage(orgrootid_link, pordercode, usercreateid_link, 
					encodedatefrom, encodedateto, limit, page);
			
			response.data = pageencode.getContent();
			response.totalCount = pageencode.getTotalElements();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Encode_Porder_getlist_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<Encode_Porder_getlist_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/encode_porder_getinfo", method = RequestMethod.POST)
	public ResponseEntity<Encode_porder_getinfo_response> Getinfo(
			@RequestBody Encode_porder_getinfo_request entity, HttpServletRequest request) {// @RequestParam("type")
		Encode_porder_getinfo_response response = new Encode_porder_getinfo_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			
			response.data = porderencodeService.findOne(entity.id);
			long porderid_link = response.data.getPorderid_link();
			long skuid_link = response.data.getSkuid_link();
			
			int pquantity_encode = 0;
			List<Encode> list_encode = porderencodeService.get_encode_by_porder_and_sku(orgrootid_link, porderid_link, skuid_link);
			for(Encode encode : list_encode) {
				pquantity_encode += encode.getAmount_encoded();
			}
			
			POrder_Product_SKU porder_sku = porderskuService.get_sku_in_encode(porderid_link, skuid_link);
			POrder_Product_SKU_Encode sku_encode = new POrder_Product_SKU_Encode();
			sku_encode.setId(porder_sku.getId());
			sku_encode.setOrgrootid_link(porder_sku.getOrgrootid_link());
			sku_encode.setPorderid_link(porder_sku.getPorderid_link());
			sku_encode.setPquantity_encode(pquantity_encode);
			sku_encode.setPquantity_total(porder_sku.getPquantity_total());
			sku_encode.setProductid_link(porder_sku.getProductid_link());
			sku_encode.setSkuid_link(porder_sku.getSkuid_link());
			sku_encode.setColor_name(porder_sku.getColor_name());
			sku_encode.setSize_name(porder_sku.getSize_name());
			sku_encode.setSkucode(porder_sku.getSkucode());
			sku_encode.setSkuname(porder_sku.getSkuname());
			sku_encode.setProduct_code(porder_sku.getSku_product_code());
			sku_encode.setPorderyear(porder_sku.getPorder_year());
			sku_encode.setPordercode(porder_sku.getOrdercode());
			response.skuencode = sku_encode;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Encode_porder_getinfo_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<Encode_porder_getinfo_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
