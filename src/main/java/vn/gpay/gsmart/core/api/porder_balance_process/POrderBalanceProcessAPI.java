package vn.gpay.gsmart.core.api.porder_balance_process;

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
import vn.gpay.gsmart.core.porder_balance_process.IPOrderBalanceProcessService;
import vn.gpay.gsmart.core.porder_balance_process.POrderBalanceProcess;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_balance_process")
public class POrderBalanceProcessAPI {
	@Autowired IPOrderBalanceProcessService porderBalanceProcessService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody POrderBalanceProcess_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long porderbalanceid_link = entity.porderbalanceid_link;
			Long pordersewingcostid_link = entity.pordersewingcostid_link;
			
			POrderBalanceProcess newPOrderBalanceProcess = new POrderBalanceProcess();
			newPOrderBalanceProcess.setId(0L);
			newPOrderBalanceProcess.setOrgrootid_link(orgrootid_link);
			newPOrderBalanceProcess.setPorderbalanceid_link(porderbalanceid_link);
			newPOrderBalanceProcess.setPordersewingcostid_link(pordersewingcostid_link);
			
			porderBalanceProcessService.save(newPOrderBalanceProcess);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete(@RequestBody POrderBalanceProcess_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
			
			Long porders_balance_processid_link = entity.porders_balance_processid_link;
			porderBalanceProcessService.deleteById(porders_balance_processid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updatePorderBalanceId", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updatePorderBalanceId(@RequestBody POrderBalanceProcess_updatePorderBalanceId_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
			
			Long porders_balance_processid_link = entity.porders_balance_processid_link;
			Long porderbalanceid_link_next = entity.porderbalanceid_link_next;
			
			POrderBalanceProcess porderBalanceProcess = porderBalanceProcessService.findOne(porders_balance_processid_link);
			porderBalanceProcess.setPorderbalanceid_link(porderbalanceid_link_next);
			porderBalanceProcessService.save(porderBalanceProcess);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
}
