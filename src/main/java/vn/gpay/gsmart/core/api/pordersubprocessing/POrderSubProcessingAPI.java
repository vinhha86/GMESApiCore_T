package vn.gpay.gsmart.core.api.pordersubprocessing;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
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

import vn.gpay.gsmart.core.api.workingprocess.WorkingProcessResponse;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.pordersubprocessing.IPOrderSubProcessing_Service;
import vn.gpay.gsmart.core.pordersubprocessing.POrderSubProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.workingprocess.IWorkingProcess_Service;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

@RestController
@RequestMapping("/api/v1/psubprocess")
public class POrderSubProcessingAPI {
    @Autowired
    private IPOrderSubProcessing_Service subprocessingRepository;
    @Autowired
    private IPOrderProcessing_Service pprocessRepository;
    @Autowired
    private IWorkingProcess_Service workingprocessRepository;

    ObjectMapper mapper = new ObjectMapper();
    
    @GetMapping("/psubprocess")
    public List<POrderSubProcessing> getAllSubProcess() {
        return subprocessingRepository.findAll();
    }
    @RequestMapping(value = "/getbyorderid",method = RequestMethod.POST)
    public ResponseEntity<WorkingProcessResponse> getByOrderID(@RequestBody PSubProcessByCodeRequest entity) throws IOException {
    	//List<POrderSubProcessing> ord_subprocess_a = subprocessingRepository.findByOrderCode(ordercode);
    	List<WorkingProcess> process_list = workingprocessRepository.findAll_SubProcess();
    	WorkingProcessResponse response = new WorkingProcessResponse();
    	
    	try {
//	    	for (WorkingProcess processItem: process_list){
//	    		List<POrderSubProcessing> ord_subprocess = subprocessingRepository.findByProcessID(entity.porderid_link, processItem.getId());
////	    		if (ord_subprocess.size() > 0){
////	    			processItem.setIsselected(true);
////	    		}else {
////	    			processItem.setIsselected(false);
////	    		}
//	    	}
			response.data=process_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<WorkingProcessResponse>(response,HttpStatus.OK);    	
	    }catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<WorkingProcessResponse>(HttpStatus.OK);
		}    
    }
    @RequestMapping(value = "/getbyordercode",method = RequestMethod.POST)
    public ResponseEntity<WorkingProcessResponse> getByOrderCode(@RequestBody PSubProcessByCodeRequest entity) throws IOException {
    	//List<POrderSubProcessing> ord_subprocess_a = subprocessingRepository.findByOrderCode(ordercode);
    	List<WorkingProcess> process_list = workingprocessRepository.findAll_SubProcess();
    	WorkingProcessResponse response = new WorkingProcessResponse();
    	
    	try {
	    	for (WorkingProcess processItem: process_list){
	    		Long processid_link = processItem.getId();
	    		List<POrderSubProcessing> ord_subprocess = subprocessingRepository.findByProcessID(entity.ordercode, processid_link);
	    		if (ord_subprocess.size() > 0){
	    			processItem.setIsselected(true);
	    		}else {
	    			processItem.setIsselected(false);
	    		}
	    	}
			response.data=process_list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<WorkingProcessResponse>(response,HttpStatus.OK);    	
	    }catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<WorkingProcessResponse>(HttpStatus.OK);
		}    
    }
    @GetMapping("/psubprocess/{id}")
    public ResponseEntity<POrderSubProcessing> getSubProcessById(@PathVariable(value = "id") Long subprocessId){
        POrderSubProcessing subprocesss = subprocessingRepository.findOne(subprocessId);
        return ResponseEntity.ok().body(subprocesss);
    }
    
    @PostMapping("/subprocesss")
    public POrderSubProcessing createPOrderSubProcessing(@Valid @RequestBody POrderSubProcessing subprocesss) {
        return subprocessingRepository.save(subprocesss);
    }

    @PutMapping("/subprocesss/{id}")
    public ResponseEntity<POrderSubProcessing> updatePOrderSubProcessing(@PathVariable(value = "id") Long subprocessId,
         @Valid @RequestBody POrderSubProcessing subprocesssDetails){
        POrderSubProcessing subprocesss = subprocessingRepository.findOne(subprocessId);

        subprocesss.setStatus(subprocesssDetails.getStatus());
        final POrderSubProcessing updatedPOrderSubProcessing = subprocessingRepository.save(subprocesss);
        return ResponseEntity.ok(updatedPOrderSubProcessing);
    }

    @DeleteMapping("/subprocesss/{id}")
    public Map<String, Boolean> deletePOrderSubProcessing(@PathVariable(value = "id") Long subprocessId){
        POrderSubProcessing subprocesss = subprocessingRepository.findOne(subprocessId);

        subprocessingRepository.delete(subprocesss);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }	
    
	@RequestMapping(value = "/update_subprocess",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updateSubProcess(@RequestBody PSubProcessUpdateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		ResponseBase response = new ResponseBase();
		try{
			
			String sComment = "";
			for(WorkingProcess workingProcess: entity.data){
				List<POrderSubProcessing> ord_subprocess = subprocessingRepository.findByProcessID(entity.porderid_link,workingProcess.getId());

		        if (workingProcess.getIsselected() == true){
		        	if (ord_subprocess.size() == 0){
			        	POrderSubProcessing new_subprocesss = new POrderSubProcessing();
			        	new_subprocesss.setOrgrootid_link(workingProcess.getOrgrootid_link());
			        	new_subprocesss.setPorderid_link(entity.porderid_link);
			        	new_subprocesss.setOrdercode(entity.ordercode);
			        	new_subprocesss.setWorkingprocessid_link(workingProcess.getId());
			        	new_subprocesss.setWorkingprocessname(workingProcess.getName());
			        	new_subprocesss.setStatus(0);
			        	new_subprocesss.setUsercreatedid_link(user.getId());
			        	new_subprocesss.setTimecreated(new Date());
			        	subprocessingRepository.save(new_subprocesss);
		        	}
		        	if (sComment != "")
		        		sComment += " + " + workingProcess.getName();
		        	else
		        		sComment = workingProcess.getName();
		        }else {
		        	if (ord_subprocess.size() > 0){
		        		subprocessingRepository.delete(ord_subprocess.get(0));
		        	}
		        }
			}

			
			//Update Comment value to local MES DB
			POrderProcessing pOrderprocessing = pprocessRepository.findOne(entity.pprocesingid);
			if (null != pOrderprocessing){
				pOrderprocessing.setStatus(POrderStatus.PORDER_STATUS_SUBPROCESS);
				pOrderprocessing.setComment(sComment);
				pprocessRepository.save(pOrderprocessing);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch(Exception ex){
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ex.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
		}
	}
    
}
