package vn.gpay.gsmart.core.api.cutting;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.actionlog.ActionLogs;
import vn.gpay.gsmart.core.actionlog.IActionLogs_Service;
import vn.gpay.gsmart.core.api.porderprocessing.POrderProcessingResponse;
import vn.gpay.gsmart.core.api.porderprocessing.POrderSetReadyRequest;
import vn.gpay.gsmart.core.api.porderprocessing.PProcessByDateRequest;
import vn.gpay.gsmart.core.api.porderprocessing.PProcessUpdateRequest;
import vn.gpay.gsmart.core.api.porderprocessing.PProcessUpdateResponse;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.stockout.IStockOutService;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.StockoutTypes;
import vn.gpay.gsmart.core.porder.POrderSetReady;
@RestController
@RequestMapping("/api/v1/cutting")
public class CutAPI {
    @Autowired private IPOrderProcessing_Service pprocessService;
    @Autowired private IPOrder_Service pordersService;
    @Autowired private IActionLogs_Service actionLogsService;    
    @Autowired private IStockOutService stockoutService;

	@RequestMapping(value = "/startcutting",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> startCutting(@RequestBody POrderSetReadyRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = getClientIp(request);
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("pprocess_startcutting");
		
		ResponseBase response = new ResponseBase();
		try {
			//Check if the order has been granted to other Org????
			
	        //For each selected order --> set Ready and update Local Database for ProductionDate
			for(POrderSetReady pprocessgrant: entity.data){
				POrder pOrder = pordersService.findOne(pprocessgrant.getPorderid_link());
				if (null != pOrder){
					pOrder.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
					pordersService.save(pOrder);
					
					List<POrderProcessing> pOrderProcessing_Lst = pprocessService.getPProcess_Cutting(new Date(), pOrder.getId());
					if (pOrderProcessing_Lst.size() == 0){
						POrderProcessing pOrderProcessing = new POrderProcessing();
						pOrderProcessing.setProcessingdate(new Date());
						
						pOrderProcessing.setTotalorder(pOrder.getTotalorder());
						pOrderProcessing.setAmountcutsum(pOrder.getTotalcut());
						
						pOrderProcessing.setOrgrootid_link(entity.granttoorgid_link);
						pOrderProcessing.setOrdercode(pOrder.getOrdercode());
						pOrderProcessing.setPorderid_link(pOrder.getId());
						
						pOrderProcessing.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
						pOrderProcessing.setUsercreatedid_link(user.getId());
						pOrderProcessing.setTimecreated(new Date());
						pprocessService.save(pOrderProcessing);
					}
				}
				actionLogs.setOrdercode(pprocessgrant.getOrdercode());
			}
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsService.save(actionLogs);	
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsService.save(actionLogs);	
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}    			
	}    
	
	@RequestMapping(value = "/getbydate",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByDate(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pprocessList = pprocessService.getByDate_Cutting(entity.processingdate_to);
			
			//If processingdate <> entity.processingdate_to --> Calcucate Amount's value of provided date
			for(POrderProcessing pprocess: pprocessList){
				//Kiem tra xem da co phieu xuat vai sang to cat chua
				if (stockoutService.getByTypeAndOrderID(StockoutTypes.STOCKOUT_TYPE_FORCUT, pprocess.getPorderid_link()).size() > 0)
					pprocess.setIsstockouttocut(true);
				else
					pprocess.setIsstockouttocut(false);
			}
			
			response.data=pprocessList;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
		
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}    			
	}
	
	//update cutting values
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<PProcessUpdateResponse> updatePProcess(@RequestBody PProcessUpdateRequest entity, HttpServletRequest request) {
		System.out.println(entity.data.getProcessingdate());
	
		PProcessUpdateResponse response = new PProcessUpdateResponse();
		try {
	        POrder porder = pordersService.findOne(entity.data.getPorderid_link());

	        //If having processing data on date --> Update; else --> Create New processing line
	        List<POrderProcessing> pprocessList = pprocessService.findByIdAndPDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.data.getProcessingdate());
	        
	        if (pprocessList.size() > 0) {
	        	System.out.println("update processing cutting");
	        	POrderProcessing pprocess = pprocessList.get(0);
	        	
	        	if (entity.data.getAmountcutting() != entity.data.getAmountcuttingold()){
			        pprocess.setAmountcutting(entity.data.getAmountcutting());
			        pprocess.setAmountcuttingsum((pprocess.getAmountcuttingsumprev()!=null?pprocess.getAmountcuttingsumprev():0) + (entity.data.getAmountcutting()!=null?entity.data.getAmountcutting():0));
	        	}
	        	if (entity.data.getAmountnumbering() != entity.data.getAmountnumberingold()){
			        pprocess.setAmountnumbering(entity.data.getAmountnumbering());
			        pprocess.setAmountnumberingsum((pprocess.getAmountnumberingsumprev()!=null?pprocess.getAmountnumberingsumprev():0) + (entity.data.getAmountnumbering()!=null?entity.data.getAmountnumbering():0));
	        	}
	        	if (entity.data.getAmountmex() != entity.data.getAmountmexold()){
			        pprocess.setAmountmex(entity.data.getAmountmex());
			        pprocess.setAmountmexsum((pprocess.getAmountmexsumprev()!=null?pprocess.getAmountmexsumprev():0) + (entity.data.getAmountmex()!=null?entity.data.getAmountmex():0));
	        	}
	        	if (entity.data.getAmounttoline() != entity.data.getAmounttolineold()){
			        pprocess.setAmounttoline(entity.data.getAmounttoline());
			        pprocess.setAmounttolinesum((pprocess.getAmounttolinesumprev()!=null?pprocess.getAmounttolinesumprev():0) + (entity.data.getAmounttoline()!=null?entity.data.getAmounttoline():0));
	        	}
		        
		        pprocess.setComment(entity.data.getComment());
		        
		        if (null != pprocess.getAmountcuttingsum() && pprocess.getAmountcuttingsum() > 0){
		        	if (null != pprocess.getAmountnumbering() && pprocess.getAmountnumbering() > 0){
		        		if (null != pprocess.getAmountmexsum() && pprocess.getAmountmexsum() > 0){
		        			if (null != pprocess.getAmounttolinesum() && pprocess.getAmounttolinesum() > 0){
		        				porder.setStatus(POrderStatus.PORDER_STATUS_TOLINE);
		        				pprocess.setStatus(POrderStatus.PORDER_STATUS_TOLINE);
		        			} else {
		        				porder.setStatus(POrderStatus.PORDER_STATUS_MEX);
		        				pprocess.setStatus(POrderStatus.PORDER_STATUS_MEX);
		        			}
		        		} else {
		        			porder.setStatus(POrderStatus.PORDER_STATUS_NUMBERING);
		        			pprocess.setStatus(POrderStatus.PORDER_STATUS_NUMBERING);
		        		}
		        	} else {
		        		porder.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
		        		pprocess.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
		        	}
		        }
		        pprocessService.save(pprocess);
		        
		        //Cộng dồn trong trường hợp sửa số của ngày trước ngày hiện tại
		        //Update Amount SUM of following days. In case update amount of prev day
		        
		        List<POrderProcessing> pprocessListAfter = pprocessService.getAfterDate(entity.data.getPorderid_link(),entity.data.getPordergrantid_link(), entity.data.getProcessingdate());
		        
		        int iAmountCuttingSum = null==pprocess.getAmountcuttingsum()?0:pprocess.getAmountcuttingsum();
		        int iAmountNumberingSum = null==pprocess.getAmountnumberingsum()?0:pprocess.getAmountnumberingsum();
		        int iAmountMexSum = null==pprocess.getAmountmexsum()?0:pprocess.getAmountmexsum();
		        int iAmountTolineSum = null==pprocess.getAmounttolinesum()?0:pprocess.getAmounttolinesum();
		        
		        for(POrderProcessing pprocessAfter: pprocessListAfter){
		        	pprocessAfter.setAmountcuttingsumprev(iAmountCuttingSum);
		        	pprocessAfter.setAmountcuttingsum(iAmountCuttingSum + (null==pprocessAfter.getAmountcutting()?0:pprocessAfter.getAmountcutting()));
		        	
		        	pprocessAfter.setAmountnumberingsumprev(iAmountNumberingSum);
		        	pprocessAfter.setAmountnumberingsum(iAmountNumberingSum + (null==pprocessAfter.getAmountnumbering()?0:pprocessAfter.getAmountnumbering()));

		        	pprocessAfter.setAmountmexsumprev(iAmountMexSum);
		        	pprocessAfter.setAmountmexsum(iAmountMexSum + (null==pprocessAfter.getAmountmex()?0:pprocessAfter.getAmountmex()));

		        	pprocessAfter.setAmounttolinesumprev(iAmountTolineSum);
		        	pprocessAfter.setAmounttolinesum(iAmountTolineSum + (null==pprocessAfter.getAmounttoline()?0:pprocessAfter.getAmounttoline()));

			        if (null != pprocessAfter.getAmountcuttingsum() && pprocessAfter.getAmountcuttingsum() > 0){
			        	if (null != pprocessAfter.getAmountnumbering() && pprocessAfter.getAmountnumbering() > 0){
			        		if (null != pprocessAfter.getAmountmexsum() && pprocessAfter.getAmountmexsum() > 0){
			        			if (null != pprocessAfter.getAmountmexsum() && pprocessAfter.getAmounttolinesum() > 0){
			        				porder.setStatus(POrderStatus.PORDER_STATUS_TOLINE);
			        				pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_TOLINE);
			        			} else {
			        				porder.setStatus(POrderStatus.PORDER_STATUS_MEX);
			        				pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_MEX);
			        			}
			        		} else {
			        			porder.setStatus(POrderStatus.PORDER_STATUS_NUMBERING);
			        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_NUMBERING);
			        		}
			        	} else {
			        		porder.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
			        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_CUTTING);
			        	}
			        }
		        	pprocessService.save(pprocessAfter);
		        	
			        iAmountCuttingSum = null==pprocessAfter.getAmountcuttingsum()?0:pprocess.getAmountcuttingsum();
			        iAmountNumberingSum = null==pprocessAfter.getAmountnumberingsum()?0:pprocess.getAmountnumberingsum();
			        iAmountMexSum = null==pprocessAfter.getAmountmexsum()?0:pprocess.getAmountmexsum();
			        iAmountTolineSum = null==pprocessAfter.getAmounttolinesum()?0:pprocess.getAmounttolinesum();
		        }
		        
		        pordersService.save(porder);
		        
		        response.amountcuttingsum = pprocess.getAmountcuttingsum();
		        response.amountnumberingsum = pprocess.getAmountnumberingsum();
		        response.amountmexsum = pprocess.getAmountmexsum();
		        response.amounttolinesum = pprocess.getAmounttolinesum();
		        response.status = porder.getStatus();
	        }
	        
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.OK);
		}    			
	}	
    private String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
