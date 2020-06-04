package vn.gpay.gsmart.core.api.warehouse;

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
import vn.gpay.gsmart.core.packinglist.IPackingListService;
import vn.gpay.gsmart.core.packinglist.PackingList;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.warehouse.IEpcWarehouseCheckService;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WareHouseAPI {

	@Autowired IPackingListService packingListService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IEpcWarehouseCheckService epcWarehouseCheckService;
	@RequestMapping(value = "/updateWarehouseEPC",method = RequestMethod.POST)
	public ResponseEntity<?> UpdateWarehouseEPC( @RequestBody PackingList entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
			PackingList data = packingListService.findOne(entity.getId());
			if(data!=null) {
				packingListService.update(entity);

				Warehouse whdata = null;
				try {
					whdata=	warehouseService.findOne(data.getEpc());
				}catch(Exception ex) {}
				if (whdata==null)
				{
					Warehouse newWarehouse = new Warehouse();
				//	newWarehouse.setOrgid_link(data.getOrgid_link());
					//newWarehouse.setStockindid_link(data.getStockindid_link());
					//newWarehouse.setInvoiceid_link(data.getInvoiceid_link());
					newWarehouse.setLotnumber(data.getLotnumber());
					newWarehouse.setPackageid(data.getPackageid());
					newWarehouse.setColorid_link(data.getColorid_link());
					//newWarehouse.setAmount(data.getAmountorigin());
					newWarehouse.setSkuid_link(data.getSkuid_link());
					//newWarehouse.setWidth(data.getWidth());
					//newWarehouse.setWeight(data.getNetweight());
				//	newWarehouse.setEpc(data.getEpc());
					newWarehouse.setEncryptdatetime(data.getEncryptdatetime());
				    warehouseService.create(newWarehouse);
				}
				else {
				//	whdata.setEpc(data.getEpc());
					whdata.setEncryptdatetime(data.getEncryptdatetime());
					warehouseService.update(whdata);
				}
				
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SERVER_ERROR);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SERVER_ERROR));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
			
			
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListBySpaceEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialListBySpaceEPC( @RequestBody GetMaterialListBySpaceEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			responseBase.data = warehouseService.findBySpaceepc(entity.spaceepc);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getMaterialListByLotNumber",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialListByLotNumber( @RequestBody GetMaterialListByLotNumber entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		System.out.println("lotnumber:" + entity.lotnumber);
		try {
			responseBase.data = warehouseService.findByLotNumber(entity.lotnumber);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@RequestMapping(value = "/addRollToSpace",method = RequestMethod.POST)
	public ResponseEntity<?> AddRollToSpace( @RequestBody AddRollToSpace entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
			for(Warehouse data : entity.data) {
				warehouseService.update(data);
			}
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getMaterialByEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetMaterialByEPC( @RequestBody GetMaterialByEPC entity,HttpServletRequest request ) {
		WareHouseResponse responseBase = new WareHouseResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			List<Warehouse> data = warehouseService.findMaterialByEPC(entity.epc, user.getOrgId());
			responseBase.data = data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/checkEPCExisted",method = RequestMethod.POST)
	public ResponseEntity<?> CheckEPCExisted( @RequestBody GetMaterialByEPC entity,HttpServletRequest request ) {
		ResponseBase  responseBase= new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			List<Warehouse> data = warehouseService.findMaterialByEPC(entity.epc, user.getOrgId());
			if(data!=null && data.size()>0) {
//				Warehouse input= data.get(0);
//				EpcWarehouseCheck epc = new EpcWarehouseCheck();
//				epc.setEpc(input.getEpc());
//				epc.setToken(UUID.fromString(entity.token));
//				epcWarehouseCheckService.create(epc);
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			}
			else {
				responseBase.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
			}

			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getCheckedEPC",method = RequestMethod.POST)
	public ResponseEntity<?> GetCheckedEPC( @RequestBody GetCheckedEPC entity,HttpServletRequest request ) {
		WareHouseResponse  responseBase= new WareHouseResponse();
		try {
			
			List<Warehouse> data = warehouseService.findCheckedEPC(entity.token);
			responseBase.data = data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<WareHouseResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
