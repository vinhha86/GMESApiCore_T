package vn.gpay.gsmart.core.api.cutplan_processing;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.cutplan.CutPlan_Row;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Row_Service;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Size_Service;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessing;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessingD;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingDService;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.WareHouseStatus;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/cutplan_processing")
public class CutplanProcessingAPI {
	@Autowired ICutplanProcessingService cutplanProcessingService;
	@Autowired ICutplanProcessingDService cutplanProcessingDService;
	@Autowired ICutPlan_Row_Service cutplanRowService;
	@Autowired ICutPlan_Size_Service cutplan_SizeService;
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Service porderService;
	@Autowired ICutPlan_Row_Service cutplanrowService;
	@Autowired IWarehouseService warehouseService;
	
	@RequestMapping(value = "/cutplan_processing_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> CutplanProcessingCreate(@RequestBody CutplanProcessingCreateRequest entity, HttpServletRequest request ) {
		CutplanProcessingResponse response = new CutplanProcessingResponse();
		
		try {
			
			if(entity.data.size()>0) {
				GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long orgrootid_link = user.getRootorgid_link();
				
				if (user != null) {
					CutplanProcessing cutplanProcessing =entity.data.get(0);
				    if(cutplanProcessing.getId()==null || cutplanProcessing.getId()==0) {
				    	cutplanProcessing.setOrgrootid_link(user.getRootorgid_link());
				    	cutplanProcessing.setUsercreatedid_link(user.getId());
				    	cutplanProcessing.setTimecreated(new Date());
				    	cutplanProcessing.setStatus(0);
				    }
				    
				    List<CutplanProcessingD> cutplanProcessingDs = cutplanProcessing.getCutplanProcessingD();
				    
				    int tong_so_la = 0;
				    for (CutplanProcessingD cutplanProcessingD : cutplanProcessingDs) {
				    	if(cutplanProcessingD.getId()==null || cutplanProcessingD.getId()==0) {
				    		cutplanProcessingD.setOrgrootid_link(user.getRootorgid_link());
				    		cutplanProcessingD.setTimecreated(new Date());
				    		
				    		// update status warehouse thanh da cat
				    		String epc = cutplanProcessingD.getEpc();
				    		Long warehouseid_link = cutplanProcessingD.getWarehouseid_link();
				    		List<Warehouse> warehouseList = warehouseService.findMaterialByEPC(epc);
				    		if(warehouseList.size() > 0) {
				    			Warehouse warehouse = warehouseList.get(0);
				    			warehouse.setMet(cutplanProcessingD.getCon_lai()); // update vải còn trong kho = số đầu tấm
				    			warehouse.setYds((float) (cutplanProcessingD.getCon_lai() / 0.9144)); // update vải còn trong kho = số đầu tấm
				    			if(warehouse.getStatus().equals(WareHouseStatus.WAREHOUSE_STATUS_UNCHECKED)
				    					|| warehouse.getStatus().equals(WareHouseStatus.WAREHOUSE_STATUS_CHECKED)
				    					) {
				    				// set status đã cắt
				    				// ko set thành đã cắt nữa vì lưu lại giá trị đầu tấm vào warehouse -> cây vải dùng tiếp
//					    			warehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_CUT);
				    			}
				    		}
				    	}
				    	tong_so_la += cutplanProcessingD.getLa_vai();
			    	};
			    	
			    	//Tính số lượng amount_cut dựa trên sơ đồ cắt và tổng số lá
			    	cutplanProcessing = reCalculateAmountCut(cutplanProcessing);
			    	
			    	Long cutplanrowid_link = cutplanProcessing.getCutplanrowid_link();
			    	Long material_skuid_link = cutplanProcessing.getMaterial_skuid_link();
			    	Long colorid_link = cutplanProcessing.getColorid_link();
			    	sync_porder_bom(cutplanrowid_link, material_skuid_link, colorid_link);
			    	
					response.data = cutplanProcessing;
					response.id = cutplanProcessing.getId();
					
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);					
				}
				else {
					response.setRespcode(ResponseMessage.KEY_RC_AUTHEN_ERROR);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_AUTHEN_ERROR));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);						
				}
			}
			else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
			    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_list",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanProcessingList(@RequestBody CutplanProcessingListRequest entity, HttpServletRequest request ) {
		CutplanProcessingListResponse response = new CutplanProcessingListResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgid_link = user.getOrgid_link();
//			Org userOrg = orgService.findOne(orgid_link);
//			Long userOrgId = userOrg.getId();
//			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			// 
			
			Page<CutplanProcessing> pageToReturn = cutplanProcessingService.cutplanProcessing_page(
					entity.processingdate_from, entity.processingdate_to, 
					entity.limit, entity.page, entity.porderid_link, entity.skuid_link);
			response.data = pageToReturn.getContent();
			response.totalCount = pageToReturn.getTotalElements();
			
//			response.data = cutplanProcessingService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingListResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_list_chart",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanProcessingList_Chart(@RequestBody CutplanProcessingListRequest entity, HttpServletRequest request ) {
		CutplanProcessingListResponse response = new CutplanProcessingListResponse();
		try {
			Long porderid_link = entity.porderid_link;
			Long skuid_link = entity.skuid_link;
			List<CutplanProcessing> data = cutplanProcessingService.getForChart_TienDoCat(porderid_link, skuid_link);
			response.data = data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingListResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByID(@RequestBody CutplanProcessingByIDRequest entity, HttpServletRequest request ) {
		CutplanProcessingByIDResponse response = new CutplanProcessingByIDResponse();
		try {
			response.data = cutplanProcessingService.findOne(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_row_by_porder",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanRowByPorder(@RequestBody CutPlanRow_GetByPorder_Request entity, HttpServletRequest request ) {
		CutPlanRow_List_Response response = new CutPlanRow_List_Response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long porderid_link = entity.porderid_link;
			
			// 
			response.data = cutplanRowService.findByPOrder(porderid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutPlanRow_List_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_delete",method = RequestMethod.POST)
	public ResponseEntity<?> cutplan_processing_delete(@RequestBody CutplanProcessingByIDRequest entity, HttpServletRequest request ) {
		CutplanProcessingByIDResponse response = new CutplanProcessingByIDResponse();
		try {
			CutplanProcessing cutplanProcessing = cutplanProcessingService.findOne(entity.id);
			
			// tìm cây vải warehouse và chuyển status từ đã cắt -> đã tở
			List<CutplanProcessingD> cutplanProcessingD_list = cutplanProcessing.getCutplanProcessingD();
			for(CutplanProcessingD item : cutplanProcessingD_list) {
				String epc = item.getEpc();
				if(epc != null) {
					List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
					if(warehouse_list.size() > 0) {
						Warehouse warehouse = warehouse_list.get(0);
						warehouse.setMet(item.getMet());
						warehouse.setYds((float) (item.getMet() / 0.9144));
//						warehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_CHECKED);
						warehouseService.save(warehouse);
					}
				}
			}
			
			// xoá
			Long cutplanrowid_link = cutplanProcessing.getCutplanrowid_link();
	    	Long material_skuid_link = cutplanProcessing.getMaterial_skuid_link();
	    	Long colorid_link = cutplanProcessing.getColorid_link();
	    	
			cutplanProcessingService.delete(cutplanProcessing);
	    	sync_porder_bom(cutplanrowid_link, material_skuid_link, colorid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_d_delete",method = RequestMethod.POST)
	public ResponseEntity<?> cutplan_processing_d_delete(@RequestBody CutplanProcessingByIDRequest entity, HttpServletRequest request ) {
		CutplanProcessingByIDResponse response = new CutplanProcessingByIDResponse();
		try {
			CutplanProcessingD cutplanProcessingD = cutplanProcessingDService.findOne(entity.id);
			Long cutplan_processingid_link = cutplanProcessingD.getCutplan_processingid_link();
			CutplanProcessing cutplanProcessing = cutplanProcessingService.findOne(cutplan_processingid_link);
			
			// tìm cây vải warehouse và chuyển status từ đã cắt -> đã tở
			String epc = cutplanProcessingD.getEpc();
			if(epc != null) {
				List<Warehouse> warehouse_list = warehouseService.findMaterialByEPC(epc);
				if(warehouse_list.size() > 0) {
					Warehouse warehouse = warehouse_list.get(0);
					warehouse.setMet(cutplanProcessingD.getMet());
					warehouse.setYds((float) (cutplanProcessingD.getMet() / 0.9144));
//					warehouse.setStatus(WareHouseStatus.WAREHOUSE_STATUS_CHECKED);
					warehouseService.save(warehouse);
				}
			}
			
			// xoá
			cutplanProcessingDService.delete(cutplanProcessingD);
			
			reCalculateAmountCut(cutplanProcessing);
			
			Long cutplanrowid_link = cutplanProcessing.getCutplanrowid_link();
	    	Long material_skuid_link = cutplanProcessing.getMaterial_skuid_link();
	    	Long colorid_link = cutplanProcessing.getColorid_link();
	    	sync_porder_bom(cutplanrowid_link, material_skuid_link, colorid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public CutplanProcessing reCalculateAmountCut(CutplanProcessing cutplanProcessing) {
		//Tính số lượng amount_cut dựa trên sơ đồ cắt và tổng số lá

    	Integer total_size_amount = cutplan_SizeService.getTotalAmount_By_CutPlanRow(cutplanProcessing.getCutplanrowid_link());
    	Integer tong_so_la = 0;
    	
    	List<CutplanProcessingD> cutplanProcessingDs = cutplanProcessing.getCutplanProcessingD();
    	for (CutplanProcessingD cutplanProcessingD : cutplanProcessingDs) {
	    	tong_so_la += cutplanProcessingD.getLa_vai();
    	};
    	cutplanProcessing.setAmountcut(total_size_amount*tong_so_la);
    	cutplanProcessing = cutplanProcessingService.save(cutplanProcessing);
		
		return cutplanProcessing;
	}
	
	public void sync_porder_bom(Long cutplanrowid_link, Long material_skuid_link, Long colorid_link) {
		GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();

//		Long cutplanrowid_link =  cutplanProcessing.getCutplanrowid_link();
    	CutPlan_Row cutPlan_Row = cutplanRowService.findOne(cutplanrowid_link);
    	Long porderid_link = cutPlan_Row.getPorderid_link();
    	POrder porder = porderService.findOne(porderid_link);
//    	Long material_skuid_link = cutplanProcessing.getMaterial_skuid_link();
//    	Long colorid_link = cutplanProcessing.getColorid_link();
		cutplanrowService.sync_porder_bom_from_cutprocesing(material_skuid_link, porder, colorid_link, user.getId(), orgrootid_link);
	}
	
	
}
