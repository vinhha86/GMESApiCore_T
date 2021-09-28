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
import vn.gpay.gsmart.core.porder_balance_process.IPOrderBalanceProcessService;
import vn.gpay.gsmart.core.porder_balance_process.POrderBalanceProcess;
import vn.gpay.gsmart.core.porder_grant_balance.IPOrderGrantBalanceService;
import vn.gpay.gsmart.core.porder_grant_balance.POrderGrantBalance;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_balance")
public class POrderBalanceAPI {
	@Autowired IPOrderBalanceService porderBalanceService;
	@Autowired IPOrderGrantBalanceService porderGrantBalanceService;
	@Autowired IPOrderBalanceProcessService porderBalanceProcessService;
	
//	@RequestMapping(value = "/create", method = RequestMethod.POST)
//	public ResponseEntity<ResponseBase> create(@RequestBody POrderBalance_create_request entity,
//			HttpServletRequest request) {
//		ResponseBase response = new ResponseBase();
//		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
//			
//			Long porderid_link = entity.porderid_link;
//			Integer amount = entity.amount;
//			
//			List<POrderBalance> listPOrderBalance = porderBalanceService.getByPorder(porderid_link);
//			Integer listSize = listPOrderBalance.size();
//			
//			for(int i=1;i<=amount;i++) {
//				POrderBalance newPOrderBalance = new POrderBalance();
//				newPOrderBalance.setId(null);
//				newPOrderBalance.setOrgrootid_link(orgrootid_link);
//				newPOrderBalance.setPorderid_link(porderid_link);
//				
////				String balanceName = "Cụm công đoạn " + (listSize + i);
//				String balanceName = "" + (listSize + i);
//				newPOrderBalance.setBalance_name(balanceName);
//				
//				Integer sortValue = listSize + i;
//				newPOrderBalance.setSortvalue(sortValue);
//				
//				porderBalanceService.save(newPOrderBalance);
//			}
//			
//			
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody POrderBalance_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long porderid_link = entity.porderid_link;
			String name = entity.name;
			
			List<POrderBalance> porderBalance_list = porderBalanceService.getByBalanceName_POrder(name, porderid_link);
			if(porderBalance_list.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Cụm công đoạn đã tồn tại");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}
			
			List<POrderBalance> listPOrderBalance = porderBalanceService.getByPorder(porderid_link);
			Integer listSize = listPOrderBalance.size();
			
			POrderBalance newPOrderBalance = new POrderBalance();
			newPOrderBalance.setId(null);
			newPOrderBalance.setOrgrootid_link(orgrootid_link);
			newPOrderBalance.setPorderid_link(porderid_link);
			newPOrderBalance.setBalance_name(name);
			Integer sortValue = listSize;
			newPOrderBalance.setSortvalue(sortValue);
			porderBalanceService.save(newPOrderBalance);
			
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
	public ResponseEntity<ResponseBase> delete(@RequestBody POrderBalance_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			
			POrderBalance porderBalance = porderBalanceService.findOne(id);
			Long porderid_link = porderBalance.getPorderid_link();
			
			List<POrderBalance> porderBalance_list = porderBalanceService.getByPorder(porderid_link);
			
			// set name, sortvalue
			Boolean isAfterDeleteRec = false;
			for(POrderBalance item : porderBalance_list) {
				if(item.getId().equals(id)) {
					isAfterDeleteRec = true;
					continue;
				}
				if(isAfterDeleteRec) {
					Integer sortValue = item.getSortvalue() - 1;
					item.setSortvalue(sortValue);
//					item.setBalance_name("Cụm công đoạn " + sortValue);
					porderBalanceService.save(item);
				}
			}
			
			// delete
			List<POrderBalanceProcess> porderBalanceProcess_list = porderBalance.getPorderBalanceProcesses();
			if(porderBalanceProcess_list != null) {
				if(porderBalanceProcess_list.size() > 0) {
					for(POrderBalanceProcess item : porderBalanceProcess_list) {
						porderBalanceProcessService.deleteById(item.getId());
					}
				}
			}
			porderBalanceService.deleteById(id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete_multi", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete_multi(@RequestBody POrderBalance_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> idList = entity.idList;
			
			for(Long id : idList) {
				POrderBalance porderBalance = porderBalanceService.findOne(id);
				Long porderid_link = porderBalance.getPorderid_link();
				
				List<POrderBalance> porderBalance_list = porderBalanceService.getByPorder(porderid_link);
				
				// set name, sortvalue
				Boolean isAfterDeleteRec = false;
				for(POrderBalance item : porderBalance_list) {
					if(item.getId().equals(id)) {
						isAfterDeleteRec = true;
						continue;
					}
					if(isAfterDeleteRec) {
						Integer sortValue = item.getSortvalue() - 1;
						item.setSortvalue(sortValue);
//						item.setBalance_name("Cụm công đoạn " + sortValue);
						porderBalanceService.save(item);
					}
				}
				
				// delete
				List<POrderBalanceProcess> porderBalanceProcess_list = porderBalance.getPorderBalanceProcesses();
				if(porderBalanceProcess_list != null) {
					if(porderBalanceProcess_list.size() > 0) {
						for(POrderBalanceProcess item : porderBalanceProcess_list) {
							porderBalanceProcessService.deleteById(item.getId());
						}
					}
				}
				porderBalanceService.deleteById(id);
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
	
	@RequestMapping(value = "/porder_balance_reorder",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> porder_balance_reorder(@RequestBody POrderBalance_reorder_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
		
			for (POrderBalance porderBalance:entity.data){
				POrderBalance pb = porderBalanceService.findOne(porderBalance.getId());
				if (null != pb){
					pb.setSortvalue(porderBalance.getSortvalue());
					porderBalanceService.save(pb);
				}
				
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
