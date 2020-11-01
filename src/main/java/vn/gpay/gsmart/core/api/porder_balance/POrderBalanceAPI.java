package vn.gpay.gsmart.core.api.porder_balance;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.porder_balance.IPOrderBalanceService;
import vn.gpay.gsmart.core.porder_balance.POrderBalance;
import vn.gpay.gsmart.core.porder_balance.POrderBalanceBinding;
import vn.gpay.gsmart.core.porder_grant_balance.IPOrderGrantBalanceService;
import vn.gpay.gsmart.core.porder_grant_balance.POrderGrantBalance;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_balance")
public class POrderBalanceAPI {
	@Autowired IPOrderBalanceService porderBalanceService;
	@Autowired IPOrderGrantBalanceService porderGrantBalanceService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody POrderBalance_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long porderid_link = entity.porderid_link;
			Integer amount = entity.amount;
			
			List<POrderBalance> listPOrderBalance = porderBalanceService.getByPorder(porderid_link);
			Integer listSize = listPOrderBalance.size();
			
			for(int i=1;i<=amount;i++) {
				POrderBalance newPOrderBalance = new POrderBalance();
				newPOrderBalance.setId(0L);
				newPOrderBalance.setOrgrootid_link(orgrootid_link);
				newPOrderBalance.setPorderid_link(porderid_link);
				
				String balanceName = "Vị trí " + (listSize + i);
				newPOrderBalance.setBalance_name(balanceName);
				
				Integer sortValue = listSize + i;
				newPOrderBalance.setSortvalue(sortValue);
				
				porderBalanceService.save(newPOrderBalance);
			}
			
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByPorder", method = RequestMethod.POST)
	public ResponseEntity<POrderBalance_response> getByPorder(@RequestBody POrderBalance_getByPOrder_request entity,
			HttpServletRequest request) {
		POrderBalance_response response = new POrderBalance_response();
		try {
			response.data = porderBalanceService.getByPorder(entity.porderid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderBalance_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderBalance_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByPorderAndPorderGrant", method = RequestMethod.POST)
	public ResponseEntity<POrderBalanceBinding_response> getByPorderAndPorderGrant(@RequestBody POrderBalance_getByPOrder_request entity,
			HttpServletRequest request) {
		POrderBalanceBinding_response response = new POrderBalanceBinding_response();
		try {
//			
			List<POrderBalance> listPOrderBalance =porderBalanceService.getByPorder(entity.porderid_link);
			response.data = new ArrayList<POrderBalanceBinding>();
			
			for(POrderBalance porderBalance : listPOrderBalance) {
				POrderBalanceBinding temp = new POrderBalanceBinding();
				temp.setId(porderBalance.getId());
				temp.setOrgrootid_link(porderBalance.getOrgrootid_link());
				temp.setPorderid_link(porderBalance.getPorderid_link());
				temp.setBalance_name(porderBalance.getBalance_name());
				temp.setPrevbalanceid_link(porderBalance.getPrevbalanceid_link());
				temp.setParentbalanceid_link(porderBalance.getParentbalanceid_link());
				temp.setSortvalue(porderBalance.getSortvalue());
				//
				temp.setWorkingprocess_name(porderBalance.getWorkingprocess_name());
				temp.setTimespent_standard(porderBalance.getTimespent_standard());
				//
				temp.setPorderBalanceProcesses(porderBalance.getPorderBalanceProcesses());
				//
				List<POrderGrantBalance> listPorderGrantBalance  = 
						porderGrantBalanceService.getByPorderGrantAndPorderBalance(entity.pordergrantid_link, porderBalance.getId());
				if(listPorderGrantBalance.size() > 0) {
					POrderGrantBalance porderGrantBalance = listPorderGrantBalance.get(0);
					temp.setPersonnelId(porderGrantBalance.getPersonnelid_link());
					temp.setPersonnelFullName(porderGrantBalance.getPersonnelFullName());
				}
				//
				response.data.add(temp);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderBalanceBinding_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderBalanceBinding_response>(response, HttpStatus.OK);
		}
	}
}
