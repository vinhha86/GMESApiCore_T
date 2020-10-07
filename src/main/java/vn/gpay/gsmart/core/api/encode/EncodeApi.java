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
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.tagencode.IWareHouse_Encode_D_Service;
import vn.gpay.gsmart.core.tagencode.IWareHouse_Encode_EPC_History_Service;
import vn.gpay.gsmart.core.tagencode.IWareHouse_Encode_EPC_Service;
import vn.gpay.gsmart.core.tagencode.IWareHouse_Encode_Service;
import vn.gpay.gsmart.core.tagencode.WareHouse_Encode;
import vn.gpay.gsmart.core.tagencode.WareHouse_Encode_D;
import vn.gpay.gsmart.core.tagencode.WareHouse_Encode_EPC;
import vn.gpay.gsmart.core.tagencode.WareHouse_Encode_EPC_History;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/encode")
public class EncodeApi {

	@Autowired
	IWareHouse_Encode_EPC_Service encodeepcService;
	@Autowired
	IWareHouse_Encode_Service encodeService;
	@Autowired
	IWareHouse_Encode_D_Service encodeskuService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IWareHouse_Encode_EPC_History_Service epc_his_Service;
	@Autowired
	IWarehouseService warehouseService;
	@Autowired
	Common common;

	@RequestMapping(value = "/encode_push", method = RequestMethod.POST)
	public ResponseEntity<Encode_push_response> Encode_Push(@RequestBody Encode_push_request entity, HttpServletRequest request) {
		Encode_push_response response = new Encode_push_response();
		GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
		try {
			SKU sku = skuService.getSKU_byCode(entity.data.getSku(), user.getRootorgid_link());
			Long orgencodeid_link = user.getOrgId();
			Long orgrootid_link = user.getRootorgid_link();
			
			//Nếu SKU hợp lệ mới cho thực hiện
			if (null != sku){
				//Nếu warehouse_encodeid_link = NULL --> Tạo mới phiên
				if (null == entity.warehouse_encodeid_link){
					WareHouse_Encode encode_ws = new WareHouse_Encode();
					encode_ws.setOrgrootid_link(orgrootid_link);
					encode_ws.setOrgencodeid_link(orgencodeid_link);
					encode_ws.setUsercreateid_link(user.getUserId());
					encode_ws.setTimecreate(new Date());
					encode_ws.setDeviceid_link(entity.data.getDeviceid_link());
					encode_ws.setStatus(0);
					entity.warehouse_encodeid_link = encodeService.save(encode_ws).getId();
				}
				
				//Nếu chưa có dòng encode_d --> Insert
				WareHouse_Encode_D encode_d = encodeskuService.getwarehouse_encode_d_By_skuid(sku.getId(),
						entity.warehouse_encodeid_link);
				if (null == encode_d){
					WareHouse_Encode_D encode_d_new = new WareHouse_Encode_D();
					encode_d_new.setOrgrootid_link(orgencodeid_link);
					encode_d_new.setWarehouse_encodeid_link(entity.warehouse_encodeid_link);
					encode_d_new.setSkucode(sku.getCode());
					encode_d_new.setSkuid_link(sku.getId());
					encode_d_new.setUsercreateid_link(user.getUserId());
					encode_d_new.setTimecreate(new Date());
					entity.warehouse_encodedid_link = encodeskuService.save(encode_d_new).getId();
				} else {
					entity.warehouse_encodedid_link = encode_d.getId();
				}
				
				
				List<WareHouse_Encode_EPC> tagEncodes = encodeepcService.encode_getbyepc(orgrootid_link, entity.data.getOldepc());
				if (tagEncodes.size() > 0){
					//Nếu chip đã được encode trước đó --> update warehouse_encode_epc và Insert warehouse_encode_epc_history

					//Nếu chíp đã nhập kho Warehouse mà bị lấy ra mã lại --> Update warehouse
					String warehouse_epc = null;
					List<Warehouse> listwh = warehouseService.findMaterialByEPC(entity.data.getOldepc(), orgencodeid_link);
					if (listwh.size() > 0) {
						Warehouse warehouse = listwh.get(0);
						warehouse_epc = warehouse.getEpc();
						
						//Cập nhật lại trong warehouse luôn
						warehouse.setOrgrootid_link(orgencodeid_link);
						warehouse.setEpc(entity.data.getEpc());
						warehouse.setEncryptdatetime(new Date());
						warehouse.setSkuid_link(sku.getId());
						warehouse.setSkucode(sku.getCode());
						warehouse.setLasttimeupdate(new Date());
						warehouse.setLastuserupdateid_link(user.getUserId());
						warehouseService.save(warehouse);

					}
					
					WareHouse_Encode_EPC epc = tagEncodes.get(0);
					//Insert warehouse_encode_epc_history
					WareHouse_Encode_EPC_History encode_his = new WareHouse_Encode_EPC_History();
					encode_his.setOrgrootid_link(epc.getOrgrootid_link());
					encode_his.setOrgencodeid_link(epc.getOrgencodeid_link());
					encode_his.setEpc(epc.getEpc());
					encode_his.setOldepc(epc.getOldepc());
					encode_his.setTid(epc.getTid());
					encode_his.setSkuid_link(epc.getSkuid_link());
					encode_his.setDeviceid_link(epc.getDeviceid_link());
					encode_his.setWarehouse_encodeid_link(epc.getWarehouse_encodeid_link());
					encode_his.setWarehouse_encodedid_link(epc.getWarehouse_encodedid_link());
					encode_his.setUsercreateid_link(epc.getUsercreateid_link());
					encode_his.setTimecreate(epc.getTimecreate());
					
					encode_his.setWarehouse_epc(warehouse_epc);
					
					epc_his_Service.save(encode_his);
					
					//Update warehouse_encode_epc
					epc.setOrgencodeid_link(orgencodeid_link);
					epc.setEpc(entity.data.getEpc());
					epc.setOldepc(entity.data.getOldepc());
					epc.setTid(entity.data.getTid());
					epc.setSkuid_link(sku.getId());
					epc.setDeviceid_link(entity.data.getDeviceid_link());
					epc.setWarehouse_encodeid_link(entity.warehouse_encodeid_link);
					epc.setWarehouse_encodedid_link(entity.warehouse_encodedid_link);
					epc.setStatus(0);
					epc.setUsercreateid_link(user.getUserId());
					epc.setTimecreate(new Date());
					encodeepcService.save(epc);
				} else {
					//Nếu chip encode lần đầu
					WareHouse_Encode_EPC epc = new WareHouse_Encode_EPC();
					epc.setOrgrootid_link(orgrootid_link);
					epc.setOrgencodeid_link(orgencodeid_link);
					epc.setEpc(entity.data.getEpc());
					epc.setOldepc(entity.data.getOldepc());
					epc.setTid(entity.data.getTid());
					epc.setSkuid_link(sku.getId());
					epc.setDeviceid_link(entity.data.getDeviceid_link());
					epc.setWarehouse_encodeid_link(entity.warehouse_encodeid_link);
					epc.setWarehouse_encodedid_link(entity.warehouse_encodedid_link);
					epc.setStatus(0);
					epc.setUsercreateid_link(user.getUserId());
					epc.setTimecreate(new Date());					
					encodeepcService.save(epc);
				}
				
				response.warehouse_encodeid_link = entity.warehouse_encodeid_link;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<Encode_push_response>(response, HttpStatus.OK);
			} else {
				//Loi SKU
				response.setRespcode(ResponseMessage.KEY_RC_SKU_INVALID);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SKU_INVALID));
				return new ResponseEntity<Encode_push_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Encode_push_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Hàm cũ để để xem lại
	@RequestMapping(value = "/encode_checkepc", method = RequestMethod.POST)
	public ResponseEntity<Encode_checkepc_response> Encode_CheckEPC(@RequestBody Encode_checkepc_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		Encode_checkepc_response response = new Encode_checkepc_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			String OldEPC = entity.data.getOldepc();
			String EPC = entity.data.getEpc();
			String skucode = entity.data.getSku();
			String tid = entity.data.getTid();
			SKU sku = skuService.getSKU_byCode(skucode, orgrootid_link);

			WareHouse_Encode encode = entity.encode;
			WareHouse_Encode_D encode_sku = new WareHouse_Encode_D();
			WareHouse_Encode_EPC encode_epc = new WareHouse_Encode_EPC();
			WareHouse_Encode_EPC_History epc_his = new WareHouse_Encode_EPC_History();

			long warehouse_encodeid_link = encode.getId() == null ? 0 : encode.getId();

			List<WareHouse_Encode_EPC> tagEncodes = encodeepcService.encode_getbyepc(orgrootid_link, OldEPC);

			// OldEPC không có trong warehouse_encode_epc --> Chíp encode lần đầu --> Insert warehouse_encode_epc
			// OldEPC = EPC trong warehouse_encode_epc --> Encode lại --> update warehouse_encode_epc và Insert warehouse_encode_epc_history
			if (tagEncodes.size() == 0) {
				encode_epc.setTimecreate(new Date());
				encode_epc.setOrgrootid_link(orgrootid_link);
				encode_epc.setId(null);
				encode_epc.setDeviceid_link(encode.getDeviceid_link());
				encode_epc.setOrgencodeid_link(encode.getOrgencodeid_link());
				encode_epc.setSkuid_link(sku.getId());
				encode_epc.setUsercreateid_link(user.getUserId());
				encode_epc.setWarehouse_encodeid_link(null);
				encode_epc.setWarehouse_encodedid_link(null);
				encode_epc.setEpc(EPC);
				encode_epc.setOldepc(OldEPC);
				encode_epc.setTid(tid);
				encode_epc.setStatus(0);
				encode_epc.setSku(sku);

				if (encode.getId() == null || encode.getId() == 0) {

					encode_sku.setOrgrootid_link(orgrootid_link);
					encode_sku.setTimecreate(new Date());
					encode_sku.setUsercreateid_link(user.getUserId());
					encode_sku.setWarehouse_encodeid_link(null);
					encode_sku.setId(null);
					encode_sku.setSkuid_link(sku.getId());
					encode_sku.setSkucode(sku.getCode());
					encode_sku.getWarehouse_encode_epc().add(encode_epc);

					encode.getWarehouse_encode_d().add(encode_sku);
					encode.getWarehouse_encode_epc().add(encode_epc);
					encode.setOrgrootid_link(orgrootid_link);
					encode.setStatus(0);
				} else {
					encode_epc.setWarehouse_encodeid_link(encode.getId());
					WareHouse_Encode_D data = encodeskuService.getwarehouse_encode_d_By_skucode(skucode,
							warehouse_encodeid_link);
					if (data != null) {
						encode_sku = data;
					} else {
						encode_sku.setOrgrootid_link(orgrootid_link);
						encode_sku.setTimecreate(new Date());
						encode_sku.setUsercreateid_link(user.getUserId());
						encode_sku.setId(null);
						encode_sku.setSkucode(sku.getCode());
						encode_sku.setSkuid_link(sku.getId());
						encode_sku.setSku(sku);
						encode_sku.setWarehouse_encodeid_link(encode.getId());
						encode_sku = encodeskuService.save(encode_sku);
					}

					encode_epc.setWarehouse_encodedid_link(encode_sku.getId());
					encode_epc.setWarehouse_encodeid_link(encode.getId());
					encodeepcService.save(encode_epc);

					encode = encodeService.findOne(encode.getId());

					encode.setLasttimeupdate(new Date());
					encode.setLastuserupdate(user.getUserId());
				}
			}
			// Oldepc trùng với epc trong warehouse_encode_epc
			else {
				encode_epc = tagEncodes.get(0);
				encode_epc.setEpc(EPC);
				encode_epc.setOldepc(OldEPC);
				encode_epc.setLasttimeupdate(new Date());
				encode_epc.setLastuserupdateid_link(user.getUserId());

				// Kiểm tra phiên là tạo mới hay update
				if (encode.getId() == null || encode.getId() == 0) {
					encode_sku.setOrgrootid_link(orgrootid_link);
					encode_sku.setTimecreate(new Date());
					encode_sku.setUsercreateid_link(user.getUserId());
					encode_sku.setWarehouse_encodeid_link(null);
					encode_sku.setId(null);
					encode_sku.setSkuid_link(sku.getId());
					encode_sku.setSkucode(sku.getCode());
					encode_sku.getWarehouse_encode_epc().add(encode_epc);

					encode.getWarehouse_encode_d().add(encode_sku);
					encode.getWarehouse_encode_epc().add(encode_epc);
					encode.setOrgrootid_link(orgrootid_link);
					encode.setStatus(0);
				} else {
					WareHouse_Encode_D data = encodeskuService.getwarehouse_encode_d_By_skucode(skucode,
							encode.getId());
					if (data != null) {
						encode_sku = data;
					} else {
						encode_sku.setOrgrootid_link(orgrootid_link);
						encode_sku.setTimecreate(new Date());
						encode_sku.setUsercreateid_link(user.getUserId());
						encode_sku.setId(null);
						encode_sku.setSkucode(sku.getCode());
						encode_sku.setSkuid_link(sku.getId());
						encode_sku.setSku(sku);
						encode_sku.setWarehouse_encodeid_link(encode.getId());
						encode_sku = encodeskuService.save(encode_sku);
					}
					encode_epc.setWarehouse_encodedid_link(encode_sku.getId());
					encode_epc.setWarehouse_encodeid_link(encode.getId());
					encodeepcService.save(encode_epc);

					encode = encodeService.findOne(encode.getId());

					encode.setLasttimeupdate(new Date());
					encode.setLastuserupdate(user.getUserId());
				}

				encodeepcService.save(encode_epc);
			}

			encode = encodeService.save(encode);
			encode_epc.setWarehouse_encodeid_link(encode.getId());
			encode_epc.setWarehouse_encodedid_link(encode_sku.getId());

			// Kiểm tra xem chip vừa encode đã có trong warehouse hay chưa ?
			// Có rồi thì phải lưu lại epc trong bảng warehouse của chip để sau còn
			// cập nhật lại trong bảng warehouse
			List<Warehouse> listwh = warehouseService.findMaterialByEPC(OldEPC, encode.getOrgencodeid_link());
			if (listwh.size() > 0) {
				epc_his.setWarehouse_epc(listwh.get(0).getEpc());
				
				//Cập nhật lại trong warehouse luôn
				Warehouse warehouse = listwh.get(0);
				warehouse.setOrgrootid_link(user.getRootorgid_link());
				warehouse.setEpc(encode_epc.getEpc());
				warehouse.setEncryptdatetime(new Date());
				warehouse.setSkuid_link(encode_epc.getSkuid_link());
				warehouse.setUsercreateid_link(user.getUserId());
				warehouse.setLasttimeupdate(new Date());
				warehouse.setLastuserupdateid_link(user.getUserId());
				warehouse.setSkucode(encode_epc.getSkucode());
				warehouse.setStockid_link(encode.getOrgencodeid_link());
				warehouse.setTimecreate(new Date());
				warehouseService.save(warehouse);
			}

			// Insert vao bang History
			epc_his.setId(null);
			epc_his.setDeviceid_link(encode.getDeviceid_link());
			epc_his.setEpc(encode_epc.getEpc());
			epc_his.setLasttimeupdate(new Date());
			epc_his.setLastuserupdateid_link(user.getUserId());
			epc_his.setOldepc(encode_epc.getOldepc());
			epc_his.setOrgencodeid_link(encode.getOrgencodeid_link());
			epc_his.setOrgrootid_link(orgrootid_link);
			epc_his.setSkuid_link(encode_epc.getSkuid_link());
			epc_his.setStatus(encode_epc.getStatus());
			epc_his.setTid(encode_epc.getTid());
			epc_his.setTimecreate(new Date());
			epc_his.setUsercreateid_link(user.getUserId());
			epc_his.setWarehouse_encodedid_link(encode_epc.getWarehouse_encodedid_link());
			epc_his.setWarehouse_encodeid_link(encode_epc.getWarehouse_encodeid_link());
			epc_his_Service.save(epc_his);

			response.data = encodeService.findOne(encode.getId());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Encode_checkepc_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<Encode_checkepc_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/encode_getlist", method = RequestMethod.POST)
	public ResponseEntity<encode_getlist_response> Encode_GetList(@RequestBody encode_getlist_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		encode_getlist_response response = new encode_getlist_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			long orgencodeid_link = entity.orgencodeid_link;
			long usercreateid_link = entity.usercreateid_link;
			Date timecreatefrom = entity.timecreatefrom;
			Date timecreateto = entity.timecreateto;
			int limit = entity.limit;
			int page = entity.page;

			Page<WareHouse_Encode> pageencode = encodeService.getlist_bypage(orgrootid_link, orgencodeid_link,
					usercreateid_link, timecreatefrom, timecreateto, limit, page);

			response.data = pageencode.getContent();
			response.totalCount = pageencode.getTotalElements();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<encode_getlist_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<encode_getlist_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/encode_delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Encode_Delete(@RequestBody Encode_delete_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		create_encode_response response = new create_encode_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			WareHouse_Encode encode = encodeService.findOne(entity.id);
			encode.setStatus(-1);
			encode.setLastuserupdate(user.getUserId());
			encode.setLasttimeupdate(new Date());

			encodeService.save(encode);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/encode_getinfo", method = RequestMethod.POST)
	public ResponseEntity<encode_getinfo_response> Encode_getinfo(@RequestBody encode_getinfo_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		encode_getinfo_response response = new encode_getinfo_response();
		try {
			response.data = encodeService.findOne(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<encode_getinfo_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<encode_getinfo_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/encode_getepc", method = RequestMethod.POST)
	public ResponseEntity<encode_getepc_response> Encode_getEpc(@RequestBody encode_getinfo_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		encode_getepc_response response = new encode_getepc_response();
		try {
			response.data = encodeepcService.encode_getbyencodeid(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<encode_getepc_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<encode_getepc_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/encode_getsku", method = RequestMethod.POST)
	public ResponseEntity<encode_getsku_response> Encode_getSku(@RequestBody encode_getinfo_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		encode_getsku_response response = new encode_getsku_response();
		try {
			response.data = encodeskuService.encode_getbyencodeid(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<encode_getsku_response>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<encode_getsku_response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/encode_approve", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Encode_Approve(@RequestBody Encode_approve_request entity,
			HttpServletRequest request) {// @RequestParam("type")
		create_encode_response response = new create_encode_response();
		try {
			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			// Kiểm tra trong warehouse có epc nào trùng với oldepc
			WareHouse_Encode encode = encodeService.findOne(entity.id);

			encode.setStatus(entity.status);
			encode.setLastuserupdate(user.getUserId());
			encode.setLasttimeupdate(new Date());
			encode.setUserapproveid_link(user.getUserId());
			encode.setTimeapprove(new Date());

			for (WareHouse_Encode_EPC encode_epc : encode.getWarehouse_encode_epc()) {
				List<Warehouse> listwh = warehouseService.findMaterialByEPC(encode_epc.getEpc(), encode.getOrgencodeid_link());
				if (listwh.size() == 0){
					Warehouse warehouse = new Warehouse();
					warehouse.setOrgrootid_link(user.getRootorgid_link());
					warehouse.setEpc(encode_epc.getEpc());
					warehouse.setEncryptdatetime(encode_epc.getTimecreate());
					warehouse.setSkuid_link(encode_epc.getSkuid_link());
					warehouse.setUsercreateid_link(user.getUserId());
					warehouse.setLasttimeupdate(new Date());
					warehouse.setLastuserupdateid_link(user.getUserId());
					warehouse.setSkucode(encode_epc.getSkucode());
					warehouse.setStockid_link(encode.getOrgencodeid_link());
					warehouse.setTimecreate(new Date());
					warehouseService.save(warehouse);
				}
			}

			encodeService.save(encode);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/encode_getbydevice", method = RequestMethod.POST)
	public ResponseEntity<?> EncodeGetByDevice(@RequestBody EncodeGetByDeviceIdRequest entity,
			HttpServletRequest request) {// @RequestParam("type")
		EncodeResponse response = new EncodeResponse();
		try {

			GpayAuthentication user = (GpayAuthentication) SecurityContextHolder.getContext().getAuthentication();
			response.data = encodeepcService.encode_getbydevice(user.getRootorgid_link(), entity.deviceid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<EncodeResponse>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
			return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
