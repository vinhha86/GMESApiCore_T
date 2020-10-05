package vn.gpay.gsmart.core.api.porderprocessing;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

//import vn.gpay.gsmart.core.actionlog.IActionLogs_Service;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrderSetReady;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;

//import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.porderprocessing.TVSOrgStatusShow;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/pprocess")
public class POrderProcessingAPI {
    @Autowired private IPOrderProcessing_Service pprocessRepository;
    @Autowired private IPOrder_Service pordersRepository;
//    @Autowired private IOrgService orgsRepository;
    @Autowired private IPOrderGrant_Service pordergrantRepository;
//    @Autowired private IActionLogs_Service actionLogsRepository;
    ObjectMapper mapper = new ObjectMapper();
    
    @GetMapping("/getall")
    public List<POrderProcessing> getAll() {
        return pprocessRepository.findAll();
    }
    
    @GetMapping("/getalllatest")
    public ResponseEntity<POrderProcessingResponse> getAll_Latest() {
    	POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			//GPayUserDetail user = (GPayUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//List<Menu> menu = menuRepository.findByUserid(user.getAppuser().getId());
			List<POrderProcessing> pprocess = pprocessRepository.getLatest_All();
			response.data=pprocess;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));			
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}    	
    }
    
    @RequestMapping(value = "/getbyid",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getById(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
//			List<POrderProcessing> pprocessList = pprocessRepository.getByDateAll(entity.processingdate_to, entity.orgid);

			List<POrderProcessing> pprocessList = new ArrayList<POrderProcessing>();
			pprocessList.add(pprocessRepository.findOne(entity.orgid)); // entity.orgid here is pprocess id
			
			//If processingdate <> entity.processingdate_to --> Calcucate Amount's value of provided date
			for(POrderProcessing pprocess: pprocessList){
//				System.out.println(pprocess.getGranttoorgid_link());
				//Nếu số cắt TT ==0 --> gán bằng số cắt dự kiến để vẫn cho vào chuyền, hiện đỏ để lưu í
				pprocess.setIscuttt(0);
				if (null == pprocess.getAmountcutsum() || 0 == pprocess.getAmountcutsum()){
					pprocess.setIscuttt(1);
					pprocess.setAmountcutsum(pprocess.getGrantamount());
				}
				
				if (GPAYDateFormat.atStartOfDay(pprocess.getProcessingdate()).before(GPAYDateFormat.atStartOfDay(entity.processingdate_to))){
					pprocess.setProcessingdate(entity.processingdate_to);
					
					pprocess.setAmountcutsumprev(pprocess.getAmountcutsum());
					pprocess.setAmountcut(0);
					pprocess.setAmountcutsum(pprocess.getAmountcutsum());
					
					pprocess.setAmountinputsumprev(pprocess.getAmountinputsum());
					pprocess.setAmountinput(0);
					pprocess.setAmountinputsum(pprocess.getAmountinputsum());
					
					pprocess.setAmountoutputsumprev(pprocess.getAmountoutputsum());
					pprocess.setAmountoutput(0);
					pprocess.setAmountoutputsum(pprocess.getAmountoutputsum());
					
					pprocess.setAmounterrorsumprev(pprocess.getAmounterrorsum());
					pprocess.setAmounterror(0);
					pprocess.setAmounterrorsum(pprocess.getAmounterrorsum());
					
					pprocess.setAmountkcssumprev(pprocess.getAmountkcssum());
					pprocess.setAmountkcs(0);
					pprocess.setAmountkcssum(pprocess.getAmountkcssum());	
					
					pprocess.setAmountpackedsumprev(pprocess.getAmountpackedsum());
					pprocess.setAmountpacked(0);
					pprocess.setAmountpackedsum(pprocess.getAmountpackedsum());		
					
					pprocess.setAmountstockedsumprev(pprocess.getAmountstockedsum());
					pprocess.setAmountstocked(0);
					pprocess.setAmountstockedsum(pprocess.getAmountstockedsum());
					
					pprocess.setAmounttarget(0);
					pprocess.setAmounttargetprev(pprocess.getAmounttarget());
					
					pprocess.setAmountkcsreg(0);
					pprocess.setAmountkcsregprev(pprocess.getAmountkcsreg());
					
					pprocess.setComment("");
				} else {
	    			List<POrderProcessing> pprocessList_BeforeDate = pprocessRepository.getByBeforeDateAndOrderGrantID(pprocess.getPordergrantid_link(), entity.processingdate_to);
	    			if (null != pprocessList_BeforeDate && pprocessList_BeforeDate.size() > 0){
	    				POrderProcessing pprocess_beforedate = pprocessList_BeforeDate.get(0);
	    				pprocess.setAmountkcsregprev(pprocess_beforedate.getAmountkcsreg());
	    				pprocess.setAmounttargetprev(pprocess_beforedate.getAmounttarget());
	    			}
				}
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
     
	@RequestMapping(value = "/getbydate",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByDate(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pprocessList = pprocessRepository.getByDateAndFactory(entity.processingdate_to,entity.orgid);
			
			//If processingdate <> entity.processingdate_to --> Calcucate Amount's value of provided date
			for(POrderProcessing pprocess: pprocessList){
//				System.out.println(pprocess.getGranttoorgid_link());
				//Nếu số cắt TT ==0 --> gán bằng số cắt dự kiến để vẫn cho vào chuyền, hiện đỏ để lưu í
				pprocess.setIscuttt(0);
				if (null == pprocess.getAmountcutsum() || 0 == pprocess.getAmountcutsum()){
					pprocess.setIscuttt(1);
					pprocess.setAmountcutsum(pprocess.getGrantamount());
				}
				
				if (GPAYDateFormat.atStartOfDay(pprocess.getProcessingdate()).before(GPAYDateFormat.atStartOfDay(entity.processingdate_to))){
					pprocess.setProcessingdate(entity.processingdate_to);
					
					pprocess.setAmountcutsumprev(pprocess.getAmountcutsum());
					pprocess.setAmountcut(0);
					pprocess.setAmountcutsum(pprocess.getAmountcutsum());
					
					pprocess.setAmountinputsumprev(pprocess.getAmountinputsum());
					pprocess.setAmountinput(0);
					pprocess.setAmountinputsum(pprocess.getAmountinputsum());
					
					pprocess.setAmountoutputsumprev(pprocess.getAmountoutputsum());
					pprocess.setAmountoutput(0);
					pprocess.setAmountoutputsum(pprocess.getAmountoutputsum());
					
					pprocess.setAmounterrorsumprev(pprocess.getAmounterrorsum());
					pprocess.setAmounterror(0);
					pprocess.setAmounterrorsum(pprocess.getAmounterrorsum());
					
					pprocess.setAmountkcssumprev(pprocess.getAmountkcssum());
					pprocess.setAmountkcs(0);
					pprocess.setAmountkcssum(pprocess.getAmountkcssum());	
					
					pprocess.setAmountpackedsumprev(pprocess.getAmountpackedsum());
					pprocess.setAmountpacked(0);
					pprocess.setAmountpackedsum(pprocess.getAmountpackedsum());		
					
					pprocess.setAmountstockedsumprev(pprocess.getAmountstockedsum());
					pprocess.setAmountstocked(0);
					pprocess.setAmountstockedsum(pprocess.getAmountstockedsum());
					
					pprocess.setAmounttarget(0);
					pprocess.setAmounttargetprev(pprocess.getAmounttarget());
					
					pprocess.setAmountkcsreg(0);
					pprocess.setAmountkcsregprev(pprocess.getAmountkcsreg());
					
					pprocess.setComment("");
				} else {
	    			List<POrderProcessing> pprocessList_BeforeDate = pprocessRepository.getByBeforeDateAndOrderGrantID(pprocess.getPordergrantid_link(), entity.processingdate_to);
	    			if (null != pprocessList_BeforeDate && pprocessList_BeforeDate.size() > 0){
	    				POrderProcessing pprocess_beforedate = pprocessList_BeforeDate.get(0);
	    				pprocess.setAmountkcsregprev(pprocess_beforedate.getAmountkcsreg());
	    				pprocess.setAmounttargetprev(pprocess_beforedate.getAmounttarget());
	    			}
				}
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
	
	//Lay danh sach thong tin tat ca cac ngay phat sinh du lieu tien do cua 1 lenh sx
	@RequestMapping(value = "/getbyorderid",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByOrderId(@RequestBody PProcessByOrderIdRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pprocessList = pprocessRepository.getByOrderId(entity.porderid_link);
			
			response.data=pprocessList;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/getbysalarymonth",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getBySalarymonth(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pprocessList = pprocessRepository.getBySalaryMonth(entity.salaryyear, entity.salarymonth);
			
			//If processingdate <> entity.processingdate_to --> Calcucate Amount's value of provided date
			for(POrderProcessing pprocess: pprocessList){
				//Nếu số cắt TT ==0 --> gán bằng số cắt dự kiến để vẫn cho vào chuyền, hiện đỏ để lưu í
				pprocess.setIscuttt(0);
				if (null == pprocess.getAmountcutsum() || 0 == pprocess.getAmountcutsum()){
					pprocess.setIscuttt(1);
					pprocess.setAmountcutsum(pprocess.getTotalorder());
				}
				
				if (pprocess.getProcessingdate().before(entity.processingdate_to)){
					pprocess.setProcessingdate(entity.processingdate_to);
					
					pprocess.setAmountcutsumprev(pprocess.getAmountcutsum());
					pprocess.setAmountcut(0);
					pprocess.setAmountcutsum(pprocess.getAmountcutsum());
					
					pprocess.setAmountinputsumprev(pprocess.getAmountinputsum());
					pprocess.setAmountinput(0);
					pprocess.setAmountinputsum(pprocess.getAmountinputsum());
					
					pprocess.setAmountoutputsumprev(pprocess.getAmountoutputsum());
					pprocess.setAmountoutput(0);
					pprocess.setAmountoutputsum(pprocess.getAmountoutputsum());
					
					pprocess.setAmounterrorsumprev(pprocess.getAmounterrorsum());
					pprocess.setAmounterror(0);
					pprocess.setAmounterrorsum(pprocess.getAmounterrorsum());
					
					pprocess.setAmountkcssumprev(pprocess.getAmountkcssum());
					pprocess.setAmountkcs(0);
					pprocess.setAmountkcssum(pprocess.getAmountkcssum());	
					
					pprocess.setAmountpackedsumprev(pprocess.getAmountpackedsum());
					pprocess.setAmountpacked(0);
					pprocess.setAmountpackedsum(pprocess.getAmountpackedsum());		
					
					pprocess.setAmountstockedsumprev(pprocess.getAmountstockedsum());
					pprocess.setAmountstocked(0);
					pprocess.setAmountstockedsum(pprocess.getAmountstockedsum());
					
					pprocess.setAmounttarget(0);
					pprocess.setAmounttargetprev(pprocess.getAmounttarget());
					
					pprocess.setAmountkcsreg(0);
					pprocess.setAmountkcsregprev(pprocess.getAmountkcsreg());
					
					pprocess.setComment("");
				} else {
	    			List<POrderProcessing> pprocessList_BeforeDate = pprocessRepository.getByBeforeDateAndOrderID(pprocess.getPorderid_link(), entity.processingdate_to);
	    			if (pprocessList_BeforeDate.size() > 0){
	    				POrderProcessing pprocess_beforedate = pprocessList_BeforeDate.get(0);
	    				pprocess.setAmountkcsregprev(pprocess_beforedate.getAmountkcsreg());
	    				pprocess.setAmounttargetprev(pprocess_beforedate.getAmounttarget());
	    			}
				}
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
	
	@RequestMapping(value = "/getbydate_inlines",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByDateInLines(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			response.data=pprocessRepository.getByDateAndStatus(entity.processingdate_to, POrderStatus.PORDER_SHORTVALUE_RUNNING);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}  			
	}
	
	@RequestMapping(value = "/getbydate_ready",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByDate_Ready(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pList_READY = pprocessRepository.getByDateAndStatus(entity.processingdate_to, POrderStatus.PORDER_SHORTVALUE_READY);
			List<POrderProcessing> pList_CUTTT = new ArrayList<POrderProcessing>();
			for(POrderProcessing orderReady: pList_READY){
				if (orderReady.getIscuttt() == 0){
					//Neu da co so cat thuc te --> Loai khoi danh sach yeu cau
					pList_CUTTT.add(orderReady);
				}
//				else {
//					POrder pOrder_Lst = pordersRepository.findOne(orderReady.getPorderid_link());
//					if (null != pOrder_Lst){
//						//Date prodDate = pOrder_Lst.get(0).getProductiondate();
//						orderReady.setProductiondate(pOrder_Lst.getProductiondate());
//					}
//				}
			}
			pList_READY.removeAll(pList_CUTTT);
			response.data=pList_READY;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
		
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/getbydate_granted",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByDate_Granted(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			List<POrderProcessing> pList_READY = pprocessRepository.getByDateAndStatus(entity.processingdate_to, POrderStatus.PORDER_SHORTVALUE_READY);
			List<POrderProcessing> pList_SUBPROCESS = pprocessRepository.getByDateAndStatus(entity.processingdate_to, POrderStatus.PORDER_SHORTVALUE_SUBPROCESS);
			pList_READY.addAll(pList_SUBPROCESS);
			
			List<POrderProcessing> pList_CUTTT = new ArrayList<POrderProcessing>();
			for(POrderProcessing orderReady: pList_READY){
				if (orderReady.getIscuttt() == 0){
					//Neu da co so cat thuc te --> Loai khoi danh sach yeu cau
					pList_CUTTT.add(orderReady);
				}
			}
			pList_READY.removeAll(pList_CUTTT);			
			response.data=pList_READY;
			
			//Update STT from Local DB here
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
		
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/getbydate_orgstatus",method = RequestMethod.POST)
	public ResponseEntity<TVSOrgStatusResponse> getByDate_OrgStatus(@RequestBody PProcessByDateRequest entity, HttpServletRequest request) {
		TVSOrgStatusResponse response = new TVSOrgStatusResponse();
		try {
//			Date queryDate = DateUtils.addDays(DateFormat.toDateWithoutTime(entity.processingdate_to), -1);
			List<POrderProcessing> pList_RUNNING = pprocessRepository.getByDateAndStatus(entity.processingdate_to, POrderStatus.PORDER_STATUS_RUNNING);
			List<TVSOrgStatusShow> listOrg = new ArrayList<TVSOrgStatusShow>();
			
			TVSOrgStatusShow listOrgShow = new TVSOrgStatusShow();
			
			for(POrderProcessing orderRunning: pList_RUNNING){

				if (null != orderRunning.getOrdercode()&&null!=orderRunning.getAmountoutputsum()&&null!=orderRunning.getAmountinputsum())
				if (orderRunning.getAmountoutputsum() > 0 || orderRunning.getAmountinputsum() > 0)
				{
//					switch(orderRunning.getGranttoorgid_link().intValue()){
					switch(orderRunning.getGranttoorgname()){
						case "Tổ 1":
							if (listOrgShow.org1_orderlist.length() == 0)
								listOrgShow.org1_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org1_orderlist = listOrgShow.org1_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org1_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org1_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org1_amountoutputsum += orderRunning.getAmountoutputsum();
							//float z = (float) listOrgShow.org1_amountoutputsum / listOrgShow.org1_amountcutsum;
							listOrgShow.org1_percentcomplete = ((float) listOrgShow.org1_amountoutputsum / listOrgShow.org1_amountcutsum)*100;
//							System.out.println(listOrgShow.org1_amountoutputsum);
//							System.out.println(listOrgShow.org1_amountcutsum);
//							System.out.println(z);
//							System.out.println(listOrgShow.org1_percentcomplete);
							listOrgShow.org1_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 2":
							if (listOrgShow.org2_orderlist.length() == 0)
								listOrgShow.org2_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org2_orderlist = listOrgShow.org2_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org2_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org2_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org2_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org2_percentcomplete = ((float)listOrgShow.org2_amountoutputsum/listOrgShow.org2_amountcutsum)*100;
							listOrgShow.org2_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 3":
							if (listOrgShow.org3_orderlist.length() == 0)
								listOrgShow.org3_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org3_orderlist = listOrgShow.org3_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org3_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org3_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org3_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org3_percentcomplete = ((float)listOrgShow.org3_amountoutputsum/listOrgShow.org3_amountcutsum)*100;
							listOrgShow.org3_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 4":
							if (listOrgShow.org4_orderlist.length() == 0)
								listOrgShow.org4_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org4_orderlist = listOrgShow.org4_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org4_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org4_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org4_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org4_percentcomplete = ((float)listOrgShow.org4_amountoutputsum/listOrgShow.org4_amountcutsum)*100;
							listOrgShow.org4_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 5":
							if (listOrgShow.org5_orderlist.length() == 0)
								listOrgShow.org5_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org5_orderlist = listOrgShow.org5_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org5_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org5_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org5_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org5_percentcomplete = ((float)listOrgShow.org5_amountoutputsum/listOrgShow.org5_amountcutsum)*100;
							listOrgShow.org5_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 6":
							if (listOrgShow.org6_orderlist.length() == 0)
								listOrgShow.org6_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org6_orderlist = listOrgShow.org6_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org6_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org6_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org6_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org6_percentcomplete = ((float)listOrgShow.org6_amountoutputsum/listOrgShow.org6_amountcutsum)*100;
							listOrgShow.org6_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 7":
							if (listOrgShow.org7_orderlist.length() == 0)
								listOrgShow.org7_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org7_orderlist = listOrgShow.org7_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org7_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org7_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org7_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org7_percentcomplete = ((float)listOrgShow.org7_amountoutputsum/listOrgShow.org7_amountcutsum)*100;
							listOrgShow.org7_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 8":
							if (listOrgShow.org8_orderlist.length() == 0)
								listOrgShow.org8_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org8_orderlist = listOrgShow.org8_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org8_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org8_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org8_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org8_percentcomplete = ((float)listOrgShow.org8_amountoutputsum/listOrgShow.org8_amountcutsum)*100;
							listOrgShow.org8_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 9":
							if (listOrgShow.org9_orderlist.length() == 0)
								listOrgShow.org9_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org9_orderlist = listOrgShow.org9_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org9_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org9_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org9_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org9_percentcomplete = ((float)listOrgShow.org9_amountoutputsum/listOrgShow.org9_amountcutsum)*100;
							listOrgShow.org9_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 10":
							if (listOrgShow.org10_orderlist.length() == 0)
								listOrgShow.org10_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org10_orderlist = listOrgShow.org10_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org10_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org10_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org10_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org10_percentcomplete = ((float)listOrgShow.org10_amountoutputsum/listOrgShow.org10_amountcutsum)*100;
							listOrgShow.org10_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 11":
							if (listOrgShow.org11_orderlist.length() == 0)
								listOrgShow.org11_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org11_orderlist = listOrgShow.org11_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org11_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org11_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org11_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org11_percentcomplete = ((float)listOrgShow.org11_amountoutputsum/listOrgShow.org11_amountcutsum)*100;
							listOrgShow.org11_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 12":
							if (listOrgShow.org12_orderlist.length() == 0)
								listOrgShow.org12_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org12_orderlist = listOrgShow.org12_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org12_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org12_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org12_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org12_percentcomplete = ((float)listOrgShow.org12_amountoutputsum/listOrgShow.org12_amountcutsum)*100;
							listOrgShow.org12_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 13":
							if (listOrgShow.org13_orderlist.length() == 0)
								listOrgShow.org13_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org13_orderlist = listOrgShow.org13_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org13_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org13_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org13_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org13_percentcomplete = ((float)listOrgShow.org13_amountoutputsum/listOrgShow.org13_amountcutsum)*100;
							listOrgShow.org13_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 14":
							if (listOrgShow.org14_orderlist.length() == 0)
								listOrgShow.org14_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org14_orderlist = listOrgShow.org14_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org14_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org14_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org14_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org14_percentcomplete = ((float)listOrgShow.org14_amountoutputsum/listOrgShow.org14_amountcutsum)*100;
							listOrgShow.org14_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 15":
							if (listOrgShow.org15_orderlist.length() == 0)
								listOrgShow.org15_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org15_orderlist = listOrgShow.org15_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org15_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org15_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org15_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org15_percentcomplete = ((float)listOrgShow.org15_amountoutputsum/listOrgShow.org15_amountcutsum)*100;
							listOrgShow.org15_golivedate = orderRunning.getGolivedesc();
							break;
						case "Tổ 16":
							if (listOrgShow.org16_orderlist.length() == 0)
								listOrgShow.org16_orderlist = orderRunning.getOrdercode();
							else
								listOrgShow.org16_orderlist = listOrgShow.org16_orderlist + "; " + orderRunning.getOrdercode();
							listOrgShow.org16_amountcutsum += orderRunning.getAmountcutsum();
							listOrgShow.org16_amountinputsum += orderRunning.getAmountinputsum();
							listOrgShow.org16_amountoutputsum += orderRunning.getAmountoutputsum();
							listOrgShow.org16_percentcomplete = ((float)listOrgShow.org16_amountoutputsum/listOrgShow.org16_amountcutsum)*100;
							listOrgShow.org16_golivedate = orderRunning.getGolivedesc();
							break;							
					}
					
					
//					TVSOrgStatus objDuplicate = listOrg.stream().filter(org -> orderRunning.getGranttoorgid_link().equals(org.getGranttoorgid_link())).findAny().orElse(null);
//					if (null != objDuplicate) {
//						//if (orderRunning.getAmountinput() !=0 || orderRunning.getAmountoutput() != 0){
//							objDuplicate.addOrder(orderRunning.getOrdercode());
//							objDuplicate.setAmountcutsum(objDuplicate.getAmountcutsum() + orderRunning.getAmountcutsum());
//							objDuplicate.setAmountinputsum(objDuplicate.getAmountinputsum() + orderRunning.getAmountinputsum());
//							objDuplicate.setAmountoutputsum(objDuplicate.getAmountoutputsum() + orderRunning.getAmountoutputsum());							
//						//}
//					} else {
//						//if (orderRunning.getAmountinput() !=0 || orderRunning.getAmountoutput() != 0){
//							TVSOrgStatus objOrg = new TVSOrgStatus();
//							objOrg.setGranttoorgid_link(orderRunning.getGranttoorgid_link());
//							objOrg.setAmountcutsum(orderRunning.getAmountcutsum());
//							objOrg.setAmountinputsum(orderRunning.getAmountinputsum());
//							objOrg.setAmountoutputsum(orderRunning.getAmountoutputsum());
//							objOrg.setOrgname("Tổ " + orderRunning.getGranttoorgid_link().toString());
//							objOrg.addOrder(orderRunning.getOrdercode());
//							objOrg.setStatus(1);
//							listOrg.add(objOrg);
//						//}
//					}
				}
			}
			listOrg.add(listOrgShow);
			response.data=listOrg;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<TVSOrgStatusResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<TVSOrgStatusResponse>(HttpStatus.OK);
		}    			
	}
	
	//update processing values
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<PProcessUpdateResponse> updatePProcess(@RequestBody PProcessUpdateRequest entity, HttpServletRequest request) {
//		System.out.println(entity.data.getProcessingdate());
	
		PProcessUpdateResponse response = new PProcessUpdateResponse();
		
		try {
			POrder porder = pordersRepository.findOne(entity.data.getPorderid_link());
			if (null != porder){
				porder.setGolivedesc(entity.data.getGolivedesc());
				porder.setComment(entity.data.getComment());
				
				//Neu la so lieu vao chuyen dau tien --> Set Productiondate
				if ((null==entity.data.getAmountinput()?0:entity.data.getAmountinput()) > 0 
						&& (null==entity.data.getAmountinputsum()?0:entity.data.getAmountinputsum()) == 0)
					porder.setProductiondate(entity.data.getProcessingdate());
				
		        //If having processing data on date --> Update; else --> Create New processing line
		        List<POrderProcessing> pprocessList = pprocessRepository.findByIdAndPDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.data.getProcessingdate());
		        
		        if (pprocessList.size() > 0) {
		        	POrderProcessing pprocess = pprocessList.get(0);
		        	
		        	if (entity.data.getAmountcut() != entity.data.getAmountcutold()){
				        pprocess.setAmountcut(entity.data.getAmountcut());
				        pprocess.setAmountcutsum((pprocess.getAmountcutsumprev()!=null?pprocess.getAmountcutsumprev():0) + (entity.data.getAmountcut()!=null?entity.data.getAmountcut():0));
		        	}
		        	
		        	if (entity.data.getAmountinput() != entity.data.getAmountinputold()){
				        pprocess.setAmountinput(entity.data.getAmountinput());
				        pprocess.setAmountinputsum((pprocess.getAmountinputsumprev()!=null?pprocess.getAmountinputsumprev():0) + (entity.data.getAmountinput()!=null?entity.data.getAmountinput():0));
		        	}
		        	
		        	if (entity.data.getAmountoutput() != entity.data.getAmountoutputold()){
				        pprocess.setAmountoutput(entity.data.getAmountoutput());
				        pprocess.setAmountoutputsum((pprocess.getAmountoutputsumprev()!=null?pprocess.getAmountoutputsumprev():0) + (entity.data.getAmountoutput()!=null?entity.data.getAmountoutput():0));
		        	}
		        	
		        	if (entity.data.getAmounterror() != entity.data.getAmounterrorold()){
				        pprocess.setAmounterror(entity.data.getAmounterror());
				        pprocess.setAmounterrorsum((pprocess.getAmounterrorsumprev()!=null?pprocess.getAmounterrorsumprev():0) + (entity.data.getAmounterror()!=null?entity.data.getAmounterror():0));
		        	}

		        	if (entity.data.getAmounttarget() != entity.data.getAmounttargetold()){
		        		pprocess.setAmounttarget(entity.data.getAmounttarget());
		        	}
		        	
		        	if (entity.data.getAmountkcsreg() != entity.data.getAmountkcsregold()){
		        		pprocess.setAmountkcsreg(entity.data.getAmountkcsreg());
		        	}
		        	
		        	if (entity.data.getAmountkcs() != entity.data.getAmountkcsold()){
			        	pprocess.setAmountkcs(entity.data.getAmountkcs());
				        pprocess.setAmountkcssum((pprocess.getAmountkcssumprev()!=null?pprocess.getAmountkcssumprev():0) + (entity.data.getAmountkcs()!=null?entity.data.getAmountkcs():0));
		        	}
		        	
		        	if (entity.data.getAmountpacked() != entity.data.getAmountpackedold()){
				        pprocess.setAmountpacked(entity.data.getAmountpacked());
				        pprocess.setAmountpackedsum((pprocess.getAmountpackedsumprev()!=null?pprocess.getAmountpackedsumprev():0) + (entity.data.getAmountpacked()!=null?entity.data.getAmountpacked():0));
		        	}
		        	
		        	if (entity.data.getAmountstocked() != entity.data.getAmountstockedold()){
				        pprocess.setAmountstocked(entity.data.getAmountstocked());
				        pprocess.setAmountstockedsum((pprocess.getAmountstockedsumprev()!=null?pprocess.getAmountstockedsumprev():0) + (entity.data.getAmountstocked()!=null?entity.data.getAmountstocked():0));
		        	}
		        	
			        pprocess.setComment(entity.data.getComment());
			        
			        if ((null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum()) > 0){
			        	if (((null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum()) 
			        			+ (null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum()))  
			        			< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
			        		pprocess.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
			        	}
			        	else {
			        		if ((null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum()) 
			        				< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
			        			pprocess.setStatus(POrderStatus.PORDER_STATUS_DONE);
			        		}
			        		else {
			        			pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
			        		}
			        	}
			        }
			        
			        pprocessRepository.save(pprocess);
			        
			        //Cộng dồn trong trường hợp sửa số của ngày trước ngày hiện tại
			        //Update Amount SUM of following days. In case update amount of prev day
			        
			        List<POrderProcessing> pprocessListAfter = pprocessRepository.getAfterDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.data.getProcessingdate());
			        
			        int iAmountCutSum = null==pprocess.getAmountcutsum()?0:pprocess.getAmountcutsum();
			        int iAmountInputSum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
			        int iAmountOuputSum = null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum();
			        int iAmountErrorSum = null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum();
			        int iAmountKcsSum = null==pprocess.getAmountkcssum()?0:pprocess.getAmountkcssum();
			        int iAmountPackedSum = null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum();
			        int iAmountStockedSum = null==pprocess.getAmountstockedsum()?0:pprocess.getAmountstockedsum();
			        int iLastStatus = pprocess.getStatus();
			        
			        for(POrderProcessing pprocessAfter: pprocessListAfter){
			        	pprocessAfter.setAmountcutsumprev(iAmountCutSum);
			        	pprocessAfter.setAmountcutsum(iAmountCutSum + (null==pprocessAfter.getAmountcut()?0:pprocessAfter.getAmountcut()));
			        	
			        	pprocessAfter.setAmountinputsumprev(iAmountInputSum);
			        	pprocessAfter.setAmountinputsum(iAmountInputSum + (null==pprocessAfter.getAmountinput()?0:pprocessAfter.getAmountinput()));
			        	
			        	pprocessAfter.setAmountoutputsumprev(iAmountOuputSum);
			        	pprocessAfter.setAmountoutputsum(iAmountOuputSum + (null==pprocessAfter.getAmountoutput()?0:pprocessAfter.getAmountoutput()));
			        	
			        	pprocessAfter.setAmounterrorsumprev(iAmountErrorSum);
			        	pprocessAfter.setAmounterrorsum(iAmountErrorSum + (null==pprocessAfter.getAmounterror()?0:pprocessAfter.getAmounterror()));
			        	
			        	pprocessAfter.setAmountkcssumprev(iAmountKcsSum);
			        	pprocessAfter.setAmountkcssum(iAmountKcsSum + (null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum()));
			        	
			        	pprocessAfter.setAmountpackedsumprev(iAmountPackedSum);
			        	pprocessAfter.setAmountpackedsum(iAmountPackedSum + (null==pprocessAfter.getAmountpacked()?0:pprocessAfter.getAmountpacked()));
			        	
			        	pprocessAfter.setAmountstockedsumprev(iAmountStockedSum);
			        	pprocessAfter.setAmountstockedsum(iAmountStockedSum + (null==pprocessAfter.getAmountstocked()?0:pprocessAfter.getAmountstocked()));
			        	
				        if ((null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum()) > 0){
				        	if (((null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum()) 
				        			+ (null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum()))  
				        			< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
				        	}
				        	else {
				        		if ((null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum()) 
				        				< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_DONE);
				        		}
				        		else {
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
				        		}
				        	}
				        }
				        
			        	pprocessRepository.save(pprocessAfter);
			        	
				        iAmountCutSum = null==pprocessAfter.getAmountcutsum()?0:pprocess.getAmountcutsum();
				        iAmountInputSum = null==pprocessAfter.getAmountinputsum()?0:pprocess.getAmountinputsum();
				        iAmountOuputSum = null==pprocessAfter.getAmountoutputsum()?0:pprocess.getAmountoutputsum();
				        iAmountErrorSum = null==pprocessAfter.getAmounterrorsum()?0:pprocess.getAmounterrorsum();
				        iAmountKcsSum = null==pprocessAfter.getAmountkcssum()?0:pprocess.getAmountkcssum();
				        iAmountPackedSum = null==pprocessAfter.getAmountpackedsum()?0:pprocess.getAmountpackedsum();
				        iAmountStockedSum = null==pprocessAfter.getAmountstockedsum()?0:pprocess.getAmountstockedsum();
				        iLastStatus = pprocessAfter.getStatus();
			        }
			        
			        //Update status of Porder to last status of Processing
			        porder.setStatus(iLastStatus);
			        pordersRepository.save(porder);
			        
			        //Return sum to interface
			        response.amountcutsum = pprocess.getAmountcutsum();
			        response.amountinputsum = pprocess.getAmountinputsum();
			        response.amountoutputsum = pprocess.getAmountoutputsum();
			        response.amounterrorsum = pprocess.getAmounterrorsum();
			        response.amountkcssum = pprocess.getAmountkcssum();
			        response.amountpackedsum = pprocess.getAmountpackedsum();
			        response.amountstockedsum = pprocess.getAmountstockedsum();
			        response.status = pprocess.getStatus();
		        }
		        else {
//		        	System.out.println("add new processing line");
		        	//New line for a processing date
		        	POrderProcessing pprocess = new POrderProcessing();
		        	
		        	pprocess.setOrgrootid_link(entity.data.getOrgrootid_link()); 
		        	pprocess.setProcessingdate(entity.processingdate);
		        	pprocess.setPorderid_link(entity.data.getPorderid_link());
		        	pprocess.setPordergrantid_link(entity.data.getPordergrantid_link());
//		        	pprocess.setOrdercode(entity.data.getOrdercode());
		        	pprocess.setGranttoorgid_link(entity.data.getGranttoorgid_link());
//		        	pprocess.setGranttoorgname(entity.data.getGranttoorgname());
		        	pprocess.setTotalorder(entity.data.getTotalorder());
		        	
			        pprocess.setAmountcut(entity.data.getAmountcut());
			        pprocess.setAmountcutsumprev(entity.data.getAmountcutsum());
			        pprocess.setAmountcutsum((pprocess.getAmountcutsumprev()!=null?pprocess.getAmountcutsumprev():0) + (entity.data.getAmountcut()!=null?entity.data.getAmountcut():0));
		
			        pprocess.setAmountinput(entity.data.getAmountinput());
			        pprocess.setAmountinputsumprev(entity.data.getAmountinputsum());
			        pprocess.setAmountinputsum((pprocess.getAmountinputsumprev()!=null?pprocess.getAmountinputsumprev():0) + (entity.data.getAmountinput()!=null?entity.data.getAmountinput():0));
			        
			        pprocess.setAmountoutput(entity.data.getAmountoutput());
			        pprocess.setAmountoutputsumprev(entity.data.getAmountoutputsum());
			        pprocess.setAmountoutputsum((pprocess.getAmountoutputsumprev()!=null?pprocess.getAmountoutputsumprev():0) + (entity.data.getAmountoutput()!=null?entity.data.getAmountoutput():0));
			        
			        pprocess.setAmounterror(entity.data.getAmounterror());
			        pprocess.setAmounterrorsumprev(entity.data.getAmounterrorsum());
			        pprocess.setAmounterrorsum((pprocess.getAmounterrorsumprev()!=null?pprocess.getAmounterrorsumprev():0) + (entity.data.getAmounterror()!=null?entity.data.getAmounterror():0));
			        
		        	pprocess.setAmounttarget(entity.data.getAmounttarget());
	        		pprocess.setAmountkcsreg(entity.data.getAmountkcsreg());
		        	
			        pprocess.setAmountkcs(entity.data.getAmountkcs());
			        pprocess.setAmountkcssumprev(entity.data.getAmountkcssum());
			        pprocess.setAmountkcssum((pprocess.getAmountkcssumprev()!=null?pprocess.getAmountkcssumprev():0) + (entity.data.getAmountkcs()!=null?entity.data.getAmountkcs():0));
			        
			        pprocess.setAmountpacked(entity.data.getAmountpacked());
			        pprocess.setAmountpackedsumprev(entity.data.getAmountpackedsum());
			        pprocess.setAmountpackedsum((pprocess.getAmountpackedsumprev()!=null?pprocess.getAmountpackedsumprev():0) + (entity.data.getAmountpacked()!=null?entity.data.getAmountpacked():0));
			        
			        pprocess.setAmountstocked(entity.data.getAmountstocked());
			        pprocess.setAmountstockedsumprev(entity.data.getAmountstockedsum());
			        pprocess.setAmountstockedsum((pprocess.getAmountstockedsumprev()!=null?pprocess.getAmountstockedsumprev():0) + (entity.data.getAmountstocked()!=null?entity.data.getAmountstocked():0));		        
			        pprocess.setComment(entity.data.getComment());
			        
			        //Update status of porder
			        //PORDER_STATUS_RUNNING    = 3; //Đang sản xuất [VC > 0 AND (RC + Lỗi trên chuyền) < Cắt TT]
			        //PORDER_STATUS_DONE       = 4; //Đã sản xuất xong, chưa nhập kho TP hết [VC > 0 AND (RC + Lỗi trên chuyền) >= Cắt TT]
			        //PORDER_STATUS_FINISHED   = 5; //Đã hoàn thành mã hàng [TP >= Cắt TT]
			        if ((null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum()) > 0){
			        	if (((null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum()) 
			        			+ (null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum()))  
			        			< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
			        		pprocess.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
			        	}
			        	else {
			        		if ((null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum()) 
			        				< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
			        			pprocess.setStatus(POrderStatus.PORDER_STATUS_DONE);
			        		}
			        		else {
			        			pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
			        		}
			        	}
			        }
			        
			        pprocessRepository.save(pprocess);
			        
			        //Cộng dồn trong trường hợp sửa số của ngày trước ngày hiện tại
			        //Update Amount SUM of following days. In case update amount of prev day
			        
			        List<POrderProcessing> pprocessListAfter = pprocessRepository.getAfterDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.data.getProcessingdate());
			        
			        int iAmountCutSum = pprocess.getAmountcutsum();
			        int iAmountInputSum = pprocess.getAmountinputsum();
			        int iAmountOuputSum = pprocess.getAmountoutputsum();
			        int iAmountErrorSum = pprocess.getAmounterrorsum();
			        int iAmountKcsSum = pprocess.getAmountkcssum();
			        int iAmountPackedSum = pprocess.getAmountpackedsum();
			        int iAmountStockedSum = pprocess.getAmountstockedsum();
			        int iLastStatus = pprocess.getStatus();
			        
			        for(POrderProcessing pprocessAfter: pprocessListAfter){
			        	pprocessAfter.setAmountcutsumprev(iAmountCutSum);
			        	pprocessAfter.setAmountcutsum(pprocessAfter.getAmountcutsumprev() + pprocessAfter.getAmountcutsum());
			        	pprocessAfter.setAmountinputsumprev(iAmountInputSum);
			        	pprocessAfter.setAmountinputsum(pprocessAfter.getAmountinputsumprev() + pprocessAfter.getAmountinputsum());
			        	pprocessAfter.setAmountoutputsumprev(iAmountOuputSum);
			        	pprocessAfter.setAmountoutputsum(pprocessAfter.getAmountoutputsumprev() + pprocessAfter.getAmountoutputsum());
			        	pprocessAfter.setAmounterrorsumprev(iAmountErrorSum);
			        	pprocessAfter.setAmounterrorsum(pprocessAfter.getAmounterrorsumprev() + pprocessAfter.getAmounterrorsum());
			        	pprocessAfter.setAmountkcssumprev(iAmountKcsSum);
			        	pprocessAfter.setAmountkcssum(pprocessAfter.getAmountkcssumprev() + pprocessAfter.getAmountkcssum());
			        	pprocessAfter.setAmountpackedsumprev(iAmountPackedSum);
			        	pprocessAfter.setAmountpackedsum(pprocessAfter.getAmountpackedsumprev() + pprocessAfter.getAmountpackedsum());
			        	pprocessAfter.setAmountstockedsumprev(iAmountStockedSum);
			        	pprocessAfter.setAmountstockedsum(pprocessAfter.getAmountstockedsumprev() + pprocessAfter.getAmountstockedsum());
			        	
				        if ((null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum()) > 0){
				        	if (((null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum()) 
				        			+ (null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum()))  
				        			< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
				        	}
				        	else {
				        		if ((null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum()) 
				        				< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_DONE);
				        		}
				        		else {
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
				        		}
				        	}
				        }
				        
			        	pprocessRepository.save(pprocessAfter);
			        	
				        iAmountCutSum = pprocessAfter.getAmountcutsum();
				        iAmountInputSum = pprocessAfter.getAmountinputsum();
				        iAmountOuputSum = pprocessAfter.getAmountoutputsum();
				        iAmountErrorSum = pprocessAfter.getAmounterrorsum();
				        iAmountKcsSum = pprocessAfter.getAmountkcssum();
				        iAmountPackedSum = pprocessAfter.getAmountpackedsum();
				        iAmountStockedSum = pprocessAfter.getAmountstockedsum();
				        iLastStatus = pprocessAfter.getStatus();
			        }
			        
			        //Update status of Porder to last status of Processing
			        porder.setStatus(iLastStatus);
			        pordersRepository.save(porder);
			        
			        //Return sum to interface
			        response.amountcutsum = pprocess.getAmountcutsum();
			        response.amountinputsum = pprocess.getAmountinputsum();
			        response.amountoutputsum = pprocess.getAmountoutputsum();
			        response.amounterrorsum = pprocess.getAmounterrorsum();
			        response.amountkcssum = pprocess.getAmountkcssum();
			        response.amountpackedsum = pprocess.getAmountpackedsum();
			        response.amountstockedsum = pprocess.getAmountstockedsum();
			        response.status = pprocess.getStatus();	        	
		        }
		        
	      
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
				return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));			
			    return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.OK);
		}    			
	}
	
	//update processing values
	@RequestMapping(value = "/update_single",method = RequestMethod.POST)
	public ResponseEntity<PProcessUpdateResponse> updatePProcess_Single(@RequestBody PProcessUpdate_SingleRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long rootorgid_link = user.getRootorgid_link();
		PProcessUpdateResponse response = new PProcessUpdateResponse();
		try {
	        POrderGrant porder_grant = pordergrantRepository.findOne(entity.data.getPordergrantid_link());
	        if (null != porder_grant)
	        {
	        	POrderProcessing pprocess;
		        //If having processing data on date --> Update; else --> Create New processing line
		        List<POrderProcessing> pprocessList = pprocessRepository.findByIdAndPDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.processingdate);
	        	if (pprocessList.size() > 0) {
	        		pprocess = pprocessList.get(0);
	        	} else {
		        	pprocess = new POrderProcessing();
		        	
		        	pprocess.setOrgrootid_link(rootorgid_link); 
		        	pprocess.setProcessingdate(entity.processingdate);
		        	pprocess.setPorderid_link(entity.data.getPorderid_link());
		        	pprocess.setPordergrantid_link(entity.data.getPordergrantid_link());
		        	pprocess.setGranttoorgid_link(porder_grant.getGranttoorgid_link());
		        	
			        pprocess.setAmountcut(entity.data.getAmountcut());
			        pprocess.setAmountcutsumprev(entity.data.getAmountcutsum());
			        pprocess.setAmountcutsum(entity.data.getAmountcutsum());
		
			        pprocess.setAmountinput(entity.data.getAmountinput());
			        pprocess.setAmountinputsumprev(entity.data.getAmountinputsum());
			        pprocess.setAmountinputsum(entity.data.getAmountinputsum());
			        
			        pprocess.setAmountoutput(entity.data.getAmountoutput());
			        pprocess.setAmountoutputsumprev(entity.data.getAmountoutputsum());
			        pprocess.setAmountoutputsum(entity.data.getAmountoutputsum());
			        
			        pprocess.setAmounterror(entity.data.getAmounterror());
			        pprocess.setAmounterrorsumprev(entity.data.getAmounterrorsum());
			        pprocess.setAmounterrorsum(entity.data.getAmounterrorsum());
			        
		        	pprocess.setAmounttargetprev(entity.data.getAmounttargetprev());
	        		pprocess.setAmountkcsregprev(entity.data.getAmountkcsregprev());
		        	
			        pprocess.setAmountkcs(entity.data.getAmountkcs());
			        pprocess.setAmountkcssumprev(entity.data.getAmountkcssum());
			        pprocess.setAmountkcssum(entity.data.getAmountkcssum());
			        
			        pprocess.setAmountpacked(entity.data.getAmountpacked());
			        pprocess.setAmountpackedsumprev(entity.data.getAmountpackedsum());
			        pprocess.setAmountpackedsum(entity.data.getAmountpackedsum());
			        
			        pprocess.setAmountstocked(entity.data.getAmountstocked());
			        pprocess.setAmountstockedsumprev(entity.data.getAmountstockedsum());
			        pprocess.setAmountstockedsum(entity.data.getAmountstockedsum());
			        
//		        	pprocess.setOrdercode(porder_grant.getOrdercode());
		        	pprocess.setTotalorder(porder_grant.getGrantamount());	  
		        	
		        	pprocess.setUsercreatedid_link(user.getId());
		        	pprocess.setTimecreated(new Date());
	        	}
	        	
	        	//Update value
	        	switch(entity.dataIndex) {
	    			case "amounttarget":
	    				pprocess.setAmounttarget(entity.data.getAmounttarget());
	    				break;
	        		case "amountcut":
	        			pprocess.setAmountcut(entity.data.getAmountcut());
	        			pprocess.setAmountcutsum((null==entity.data.getAmountcutsumprev()?0:entity.data.getAmountcutsumprev()) 
	        					+ (null==entity.data.getAmountcut()?0:entity.data.getAmountcut()));
	        			break;
	        		case "amountinput":
	        			pprocess.setAmountinput(entity.data.getAmountinput());
	        			pprocess.setAmountinputsum((null==entity.data.getAmountinputsumprev()?0:entity.data.getAmountinputsumprev()) 
	        					+ (null==entity.data.getAmountinput()?0:entity.data.getAmountinput()));
	        			break;
	        		case "amountoutput":
	        			pprocess.setAmountoutput(entity.data.getAmountoutput());
	        			pprocess.setAmountoutputsum((null==entity.data.getAmountoutputsumprev()?0:entity.data.getAmountoutputsumprev()) 
	        					+ (null==entity.data.getAmountoutput()?0:entity.data.getAmountoutput()));
	        			break; 
	        		case "amounterror":
	        			pprocess.setAmounterror(entity.data.getAmounterror());
	        			pprocess.setAmounterrorsum((null==entity.data.getAmounterrorsumprev()?0:entity.data.getAmounterrorsumprev()) 
	        					+ (null==entity.data.getAmounterror()?0:entity.data.getAmounterror()));
	        			break;   	      	        			
	        		case "amountkcsreg":
	        			pprocess.setAmountkcsreg(entity.data.getAmountkcsreg());
	    				break;	 
	        		case "amountkcs":
	        			pprocess.setAmountkcs(entity.data.getAmountkcs());
	        			pprocess.setAmountkcssum((null==entity.data.getAmountkcssumprev()?0:entity.data.getAmountkcssumprev()) 
	        					+ (null==entity.data.getAmountkcs()?0:entity.data.getAmountkcs()));
	        			break;
	        		case "amountpacked":
	        			pprocess.setAmountpacked(entity.data.getAmountpacked());
	        			pprocess.setAmountpackedsum((null==entity.data.getAmountpackedsumprev()?0:entity.data.getAmountpackedsumprev()) 
	        					+ (null==entity.data.getAmountpacked()?0:entity.data.getAmountpacked()));
	        			break;
	        		case "amountstocked":
	        			pprocess.setAmountstocked(entity.data.getAmountstocked());
	        			pprocess.setAmountstockedsum((null==entity.data.getAmountstockedsumprev()?0:entity.data.getAmountstockedsumprev()) 
	        					+ (null==entity.data.getAmountstocked()?0:entity.data.getAmountstocked()));
	        			break;
	        		case "comment":
	        			pprocess.setComment(entity.data.getComment());
	    				break;            	        				
	        	}

	        	//Update trang thai lenh tuong ung
		        if ((null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum()) > 0){
		        	if (((null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum()) 
		        			+ (null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum()))  
		        			< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
		        		pprocess.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
		        	}
		        	else {
		        		if ((null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum()) 
		        				< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
		        			pprocess.setStatus(POrderStatus.PORDER_STATUS_DONE);
		        		}
		        		else {
		        			pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
		        		}
		        	}
		        }
		        
		        pprocessRepository.save(pprocess);
		        
		        //Cộng dồn trong trường hợp sửa số của ngày trước ngày hiện tại
		        //Update Amount SUM of following days. In case update amount of prev day
		        if (GPAYDateFormat.atStartOfDay(entity.processingdate).before(GPAYDateFormat.atStartOfDay(new Date()))){
			        List<POrderProcessing> pprocessListAfter = pprocessRepository.getAfterDate(entity.data.getPorderid_link(), entity.data.getPordergrantid_link(), entity.processingdate);
			        
			        int iAmountCutSum = null==pprocess.getAmountcutsum()?0:pprocess.getAmountcutsum();
			        int iAmountInputSum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
			        int iAmountOuputSum = null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum();
			        int iAmountErrorSum = null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum();
			        int iAmountKcsSum = null==pprocess.getAmountkcssum()?0:pprocess.getAmountkcssum();
			        int iAmountPackedSum = null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum();
			        int iAmountStockedSum = null==pprocess.getAmountstockedsum()?0:pprocess.getAmountstockedsum();
			        int iLastStatus = pprocess.getStatus();
			        
			        for(POrderProcessing pprocessAfter: pprocessListAfter){
			        	pprocessAfter.setAmountcutsumprev(iAmountCutSum);
			        	pprocessAfter.setAmountcutsum(iAmountCutSum + (null==pprocessAfter.getAmountcut()?0:pprocessAfter.getAmountcut()));
			        	
			        	pprocessAfter.setAmountinputsumprev(iAmountInputSum);
			        	pprocessAfter.setAmountinputsum(iAmountInputSum + (null==pprocessAfter.getAmountinput()?0:pprocessAfter.getAmountinput()));
			        	
			        	pprocessAfter.setAmountoutputsumprev(iAmountOuputSum);
			        	pprocessAfter.setAmountoutputsum(iAmountOuputSum + (null==pprocessAfter.getAmountoutput()?0:pprocessAfter.getAmountoutput()));
			        	
			        	pprocessAfter.setAmounterrorsumprev(iAmountErrorSum);
			        	pprocessAfter.setAmounterrorsum(iAmountErrorSum + (null==pprocessAfter.getAmounterror()?0:pprocessAfter.getAmounterror()));
			        	
			        	pprocessAfter.setAmountkcssumprev(iAmountKcsSum);
			        	pprocessAfter.setAmountkcssum(iAmountKcsSum + (null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum()));
			        	
			        	pprocessAfter.setAmountpackedsumprev(iAmountPackedSum);
			        	pprocessAfter.setAmountpackedsum(iAmountPackedSum + (null==pprocessAfter.getAmountpacked()?0:pprocessAfter.getAmountpacked()));
			        	
			        	pprocessAfter.setAmountstockedsumprev(iAmountStockedSum);
			        	pprocessAfter.setAmountstockedsum(iAmountStockedSum + (null==pprocessAfter.getAmountstocked()?0:pprocessAfter.getAmountstocked()));
			        	
				        if ((null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum()) > 0){
				        	if (((null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum()) 
				        			+ (null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum()))  
				        			< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
				        	}
				        	else {
				        		if ((null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum()) 
				        				< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_DONE);
				        		}
				        		else {
				        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
				        		}
				        	}
				        }
				        
			        	pprocessRepository.save(pprocessAfter);
				        iLastStatus = pprocessAfter.getStatus();
			        }
			        
			        //Update status of Porder_Grant to last status of Processing
		        	porder_grant.setStatus(iLastStatus);
		        	pordergrantRepository.save(porder_grant);		        	
		        } else {
		        	porder_grant.setStatus(pprocess.getStatus());
		        	pordergrantRepository.save(porder_grant);	
		        }

		        
		        //Return sum to interface
		        response.amountcutsum = pprocess.getAmountcutsum();
		        response.amountinputsum = pprocess.getAmountinputsum();
		        response.amountoutputsum = pprocess.getAmountoutputsum();
		        response.amounterrorsum = pprocess.getAmounterrorsum();
		        response.amountkcssum = pprocess.getAmountkcssum();
		        response.amountpackedsum = pprocess.getAmountpackedsum();
		        response.amountstockedsum = pprocess.getAmountstockedsum();		        
		        response.status = pprocess.getStatus();
		        
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
				return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.OK);
				
	        } else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Không tồn tại lệnh sản xuất được phân cho tổ chuyền");			
			    return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.BAD_REQUEST);
	        }
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<PProcessUpdateResponse>(response,HttpStatus.BAD_REQUEST);
		}    			
	}	
	//Grant POrder to Org for processing
	@RequestMapping(value = "/grant",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> grantPProcess(@RequestBody POrderGrantRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ResponseBase response = new ResponseBase();
		try {
		
	        //For each selected order --> grant to Org
			for(POrderGrant pprocessgrant: entity.data){
		        POrder porder = pordersRepository.findOne(pprocessgrant.getPorderid_link());
				//Get Org Information
//		        Org org = orgsRepository.findOne(pprocessgrant.getGranttoorgid_link());
		        
		        if (null != porder){
					//Remove from POrder_Grant
					pordergrantRepository.deleteByOrderId(porder.getId());
					
					//Add to porder_grant table	
			        POrderGrant pordergrant = new POrderGrant();
			        pordergrant.setOrgrootid_link(pprocessgrant.getOrgrootid_link());
			        pordergrant.setPorderid_link(pprocessgrant.getPorderid_link());
			        pordergrant.setOrdercode(pprocessgrant.getOrdercode());
			        pordergrant.setGranttoorgid_link(pprocessgrant.getGranttoorgid_link());
			        pordergrant.setGrantdate(new Date());
			        
			        pordergrant.setGrantamount(pprocessgrant.getGrantamount());
			        
			        pordergrant.setStatus(1);
			        pordergrant.setUsercreatedid_link(user.getId());
			        pordergrant.setTimecreated(new Date());
			        pordergrantRepository.save(pordergrant);
			        
			        //pordergrantRepository.
			        //Add to porder_processing table
			        if (null == pprocessgrant.getId()){
				        POrderProcessing pprocess = new POrderProcessing();
				        
				        pprocess.setOrgrootid_link(porder.getOrgrootid_link());
				        pprocess.setPorderid_link(porder.getId());
//				        pprocess.setOrdercode(porder.getOrdercode());
				        pprocess.setGranttoorgid_link(pprocessgrant.getGranttoorgid_link());
//				        pprocess.setGranttoorgname(org.getName());
				        pprocess.setTotalorder(porder.getTotalorder());
				        
				        pprocess.setProcessingdate(new Date());
				        
				        pprocess.setAmountcut(pprocessgrant.getGrantamount());
				        pprocess.setAmountcutsum(pprocessgrant.getGrantamount());
				        pprocess.setAmountcutsumprev(0);
		
				        pprocess.setAmountinput(0);
				        pprocess.setAmountinputsum(0);
				        pprocess.setAmountinputsumprev(0);
				        
				        pprocess.setAmountoutput(0);
				        pprocess.setAmountoutputsum(0);
				        pprocess.setAmountoutputsumprev(0);
				        
				        pprocess.setAmounterror(0);
				        pprocess.setAmounterrorsum(0);
				        pprocess.setAmounterrorsumprev(0);
				        
				        pprocess.setAmountkcs(0);
				        pprocess.setAmountkcssum(0);
				        pprocess.setAmountkcssumprev(0);
				        
				        pprocess.setAmountpacked(0);
				        pprocess.setAmountpackedsum(0);
				        pprocess.setAmountpackedsumprev(0);
				        
				        pprocess.setAmountstocked(0);
				        pprocess.setAmountstockedsum(0);
				        pprocess.setAmountstockedsumprev(0);
				        
				        pprocess.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				        pprocess.setUsercreatedid_link(porder.getUsercreatedid_link());
				        pprocess.setTimecreated(new Date());
				        
				        pprocessRepository.save(pprocess);
			        } else {
			        	POrderProcessing pprocess = pprocessRepository.findOne(pprocessgrant.getId());
			        	if (null != pprocess){
					        pprocess.setAmountcut(pprocessgrant.getGrantamount());
					        pprocess.setAmountcutsum(pprocessgrant.getGrantamount());
					        pprocess.setAmountcutsumprev(0);
					        
					        pprocessRepository.save(pprocess);
			        	}
			        }
			        //Update status of porder --> granted
			        porder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
			        pordersRepository.save(porder);
		        }
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/setready",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> setReady(@RequestBody POrderSetReadyRequest entity, HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
	        //For each selected order --> set Ready and update Local Database for ProductionDate
			for(POrderSetReady pprocessgrant: entity.data){
				//Update Productiondate to LocalDB
				POrder pOrder = pordersRepository.findOne(pprocessgrant.getPorderid_link());
				boolean isStatusReady = false;
				if (null != pOrder){
					if (pOrder.getStatus() < 2 && pOrder.getProductiondate() == null && pprocessgrant.getProductiondate() !=  null){
						isStatusReady = true;
						pOrder.setStatus(POrderStatus.PORDER_STATUS_READY);
					}
					pOrder.setProductiondate(pprocessgrant.getProductiondate());
					pOrder.setMaterial_date(pprocessgrant.getMaterial_date());
					pOrder.setSample_date(pprocessgrant.getSample_date());
					pOrder.setCut_date(pprocessgrant.getCut_date());
					pOrder.setPacking_date(pprocessgrant.getPacking_date());
					pOrder.setQc_date(pprocessgrant.getQc_date());
					pOrder.setStockout_date(pprocessgrant.getStockout_date());
					
					//Thiet lap thu tu uu tien sx mac dinh
					pOrder.setPriority((null==pordersRepository.getMaxPriority()?0:pordersRepository.getMaxPriority()) + 1);
					pordersRepository.save(pOrder);
					
					POrderProcessing pprocess = pprocessRepository.findOne(pprocessgrant.getPprocesingid());
					if (null != pprocess && isStatusReady){
						pprocess.setStatus(POrderStatus.PORDER_STATUS_READY);
						pprocessRepository.save(pprocess);
					}
						
				}
				else {
					response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));				
					return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
				}
			}
		
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/ungrant",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> setUngrant(@RequestBody POrderSetReadyRequest entity, HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			POrderSetReady orderEntity = entity.data.get(0);
			POrder order = pordersRepository.findOne(orderEntity.getPorderid_link());
			if (null != order){
				//Set status POrder
				order.setStatus(POrderStatus.PORDER_STATUS_FREE);
				pordersRepository.save(order);
				
				//Remove line on POrderprocessing
				pprocessRepository.deleteByOrderID(order.getId());
				
				//Remove from POrder_Grant
				pordergrantRepository.deleteByOrderId(orderEntity.getPorderid_link());
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}    			
	}	
	
	@RequestMapping(value = "/unready",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> setUnready(@RequestBody POrderSetReadyRequest entity, HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			//Set status POrder
			POrderSetReady orderEntity = entity.data.get(0);
			POrder order = pordersRepository.findOne(orderEntity.getPorderid_link());
			if (null != order){
				order.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				pordersRepository.save(order);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		} 		
	}	
	
    @GetMapping("/pprocess/{id}")
    public ResponseEntity<POrderProcessing> getPProcessById(@PathVariable(value = "id") Long pprocessId){
        POrderProcessing pprocess = pprocessRepository.findOne(pprocessId);
        return ResponseEntity.ok().body(pprocess);
    }
    
    @PostMapping("/pprocess")
    public POrderProcessing createPOrderProcessing(@Valid @RequestBody POrderProcessing pprocess) {
        return pprocessRepository.save(pprocess);
    }

    @PutMapping("/pprocess/{id}")
    public ResponseEntity<POrderProcessing> updatePOrderProcessing(@PathVariable(value = "id") Long pprocessId,
         @Valid @RequestBody POrderProcessing pprocessDetails){
        POrderProcessing pprocess = pprocessRepository.findOne(pprocessId);

        if (null != pprocess) {
	        pprocess.setAmountcut(pprocessDetails.getAmountcut());
	        pprocess.setAmountcutsum(pprocessDetails.getAmountcutsum());
	        pprocess.setAmountcutsumprev(pprocessDetails.getAmountcutsumprev());
	
	        pprocess.setAmountinput(pprocessDetails.getAmountinput());
	        pprocess.setAmountinputsum(pprocessDetails.getAmountinputsum());
	        pprocess.setAmountinputsumprev(pprocessDetails.getAmountinputsumprev());
	        
	        pprocess.setAmountoutput(pprocessDetails.getAmountoutput());
	        pprocess.setAmountoutputsum(pprocessDetails.getAmountoutputsum());
	        pprocess.setAmountoutputsumprev(pprocessDetails.getAmountoutputsumprev());
	        
	        pprocess.setAmounterror(pprocessDetails.getAmounterror());
	        pprocess.setAmounterrorsum(pprocessDetails.getAmounterrorsum());
	        pprocess.setAmounterrorsumprev(pprocessDetails.getAmounterrorsumprev());
	        
	        pprocess.setAmountkcs(pprocessDetails.getAmountkcs());
	        pprocess.setAmountkcssum(pprocessDetails.getAmountkcssum());
	        pprocess.setAmountkcssumprev(pprocessDetails.getAmountkcssumprev());        
	        
	        pprocess.setAmountpacked(pprocessDetails.getAmountpacked());
	        pprocess.setAmountpackedsum(pprocessDetails.getAmountpackedsum());
	        pprocess.setAmountpackedsumprev(pprocessDetails.getAmountpackedsumprev());
	        
	        pprocess.setAmountstocked(pprocessDetails.getAmountstocked());
	        pprocess.setAmountstockedsum(pprocessDetails.getAmountstockedsum());
	        pprocess.setAmountstockedsumprev(pprocessDetails.getAmountstockedsumprev());
	        
	        pprocess.setComment(pprocessDetails.getComment());
        }
        
        final POrderProcessing updatedPOrderProcessing = pprocessRepository.save(pprocess);
        return ResponseEntity.ok(updatedPOrderProcessing);
    }

	@RequestMapping(value = "/finish",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> setFinish(@RequestBody POrderSetReadyRequest entity, HttpServletRequest request) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ResponseBase response = new ResponseBase();
		try {
			//Check if the order has been granted to other Org????
			
	        //For each selected order --> set Ready and update Local Database for ProductionDate
			for(POrderSetReady pprocessgrant: entity.data){
				//Update PProcessing
				POrderProcessing pprocess = pprocessRepository.findOne(pprocessgrant.getPprocesingid());
				if (null != pprocess){
					pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
					pprocess.setComment(pprocessgrant.getComment());
					pprocessRepository.save(pprocess);
				}
				
				//Update POrder
				POrder porder = pordersRepository.findOne(pprocessgrant.getPorderid_link());
				if (null != porder){
					porder.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
					porder.setComment(pprocessgrant.getComment());
					pordersRepository.save(porder);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}    			
	}	    
    @DeleteMapping("/pprocess/{id}")
    public Map<String, Boolean> deletePOrderProcessing(@PathVariable(value = "id") Long pprocessId){
        POrderProcessing pprocess = pprocessRepository.findOne(pprocessId);

        pprocessRepository.delete(pprocess);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
    @RequestMapping(value = "/getByPOrderAndPOrderGrant",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> getByPOrderAndPOrderGrant(@RequestBody POrderProcess_getByPOrderAndPOrderGrant_request entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			response.data = pprocessRepository.getByPOrderAndPOrderGrant(entity.porderid_link, entity.pordergrantid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}    			
	}
    
    @RequestMapping(value = "/createPProcess",method = RequestMethod.POST)
	public ResponseEntity<POrderProcessingResponse> createPProcess(@RequestBody POrderProcessing_createPProcess_request entity, HttpServletRequest request) {
		POrderProcessingResponse response = new POrderProcessingResponse();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long rootorgid_link = user.getRootorgid_link();
			////
			Long porderid_link = entity.porderid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			Date processingdate = entity.processingdate;
			////
			POrder porder = pordersRepository.findOne(porderid_link);
			POrderGrant porderGrant = pordergrantRepository.findOne(pordergrantid_link);
			//// date tồn tại ? return
			List<POrderProcessing> list1 = pprocessRepository.findByIdAndPDate(porderid_link, pordergrantid_link, processingdate);
			if(list1.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Ngày đã tồn tại");
				return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
			}
			//// date không tồn tại ? lưu
			POrderProcessing porderProcessing = new POrderProcessing();
			porderProcessing.setId(0L);
			porderProcessing.setProcessingdate(processingdate);
			porderProcessing.setPorderid_link(porderid_link);
			porderProcessing.setPordergrantid_link(pordergrantid_link);
			//
			porderProcessing.setOrgrootid_link(rootorgid_link);
			porderProcessing.setOrderdate(porder.getOrderdate());
			porderProcessing.setGranttoorgid_link(porderGrant.getGranttoorgid_link());
			porderProcessing.setTotalorder(porderGrant.getGrantamount());
			porderProcessing.setStatus(1);
			porderProcessing.setUsercreatedid_link(user.getId());
			porderProcessing.setTimecreated(new Date());
			//// set các trường amount
			// previous processing date gần nhất
			List<POrderProcessing> list2 = pprocessRepository.getBeforeDate(porderid_link, pordergrantid_link, processingdate);
			if(list2.size() > 0) {
				POrderProcessing temp2 = list2.get(0);
				porderProcessing.setAmountcut(0); // lấy theo value processingdate gần nhất
				porderProcessing.setAmountcutsum(temp2.getAmountcutsum());
				porderProcessing.setAmountcutsumprev(temp2.getAmountcutsum());
				porderProcessing.setAmountinput(0);
				porderProcessing.setAmountinputsum(temp2.getAmountinputsum());
				porderProcessing.setAmountinputsumprev(temp2.getAmountinputsum());
				porderProcessing.setAmountoutput(0);
				porderProcessing.setAmountoutputsum(temp2.getAmountoutputsum());
				porderProcessing.setAmountoutputsumprev(temp2.getAmountoutputsum());
				porderProcessing.setAmounterror(0);
				porderProcessing.setAmounterrorsum(temp2.getAmounterrorsum());
				porderProcessing.setAmounterrorsumprev(temp2.getAmounterrorsum());
				porderProcessing.setAmounttarget(0);
				porderProcessing.setAmounttargetprev(temp2.getAmounttarget());
				porderProcessing.setAmountkcsreg(0);
				porderProcessing.setAmountkcsregprev(temp2.getAmountkcsreg());
				porderProcessing.setAmountkcs(0);
				porderProcessing.setAmountkcssum(temp2.getAmountkcssum());
				porderProcessing.setAmountkcssumprev(temp2.getAmountkcssum());
				porderProcessing.setAmountpackstocked(0);
				porderProcessing.setAmountpackstockedsum(temp2.getAmountpackstockedsum());
				porderProcessing.setAmountpackstockedsumprev(temp2.getAmountpackstockedsum());
				porderProcessing.setAmountpacked(0);
				porderProcessing.setAmountpackedsum(temp2.getAmountpackedsum());
				porderProcessing.setAmountpackedsumprev(temp2.getAmountpackedsum());
				porderProcessing.setAmountstocked(0);
				porderProcessing.setAmountstockedsum(temp2.getAmountstockedsum());
				porderProcessing.setAmountstockedsumprev(temp2.getAmountstockedsum());
				porderProcessing.setTotalstocked(temp2.getTotalstocked());
			}
			// next processingdate gần nhất
			List<POrderProcessing> list3 = pprocessRepository.getAfterDate(porderid_link, pordergrantid_link, processingdate);
			if(list3.size() > 0) {
				POrderProcessing temp3 = list3.get(0);
				temp3.setAmounttargetprev(0); // new processingdate có value = 0
				temp3.setAmountkcsregprev(0);
				pprocessRepository.save(temp3);
			}
			//
			porderProcessing = pprocessRepository.save(porderProcessing);
			response.data = new ArrayList<POrderProcessing>();
			response.data.add(porderProcessing);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderProcessingResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderProcessingResponse>(HttpStatus.OK);
		}    			
	}
}
