package vn.gpay.gsmart.core.api.porderprocessingns;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import vn.gpay.gsmart.core.porder_sewingcost.IPorderSewingCost_Service;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCost;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCostBinding;
import vn.gpay.gsmart.core.porderprocessingns.IPorderProcessingNsService;
import vn.gpay.gsmart.core.porderprocessingns.PorderProcessingNs;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderprocessingns")
public class PorderProcessingNsAPI {
	@Autowired IPorderProcessingNsService porderProcessingNsService;
	@Autowired IPOrderBalanceService porderBalanceService;
	@Autowired IPorderSewingCost_Service porderSewingCostService;
	
	@RequestMapping(value = "/getAll",method = RequestMethod.POST)
	public ResponseEntity<PorderProcessingNs_response> getAll(HttpServletRequest request ) {
		PorderProcessingNs_response response = new PorderProcessingNs_response();
		try {
			
			response.data = porderProcessingNsService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PorderProcessingNs_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PorderProcessingNs_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> save(@RequestBody PorderProcessingNs_save_request entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Date date = new Date();
			
			List<POrderSewingCostBinding> listPOrderSewingCostBinding = entity.data;
			Long personnelid_link = entity.personnelid_link;
			Integer shifttypeid_link = entity.shifttypeid_link;
			Date processingdate = entity.processingdate;
			Long porderid_link = entity.porderid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			
			LocalDate localDate = processingdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Integer month = localDate.getMonthValue();
			Integer year = localDate.getYear();
			Integer partitionkey = year%100*100 + month;
			
			for(POrderSewingCostBinding porderSewingCostBinding : listPOrderSewingCostBinding) {
				Long pordersewingcostid_link = porderSewingCostBinding.getId();
				Integer amount_complete = porderSewingCostBinding.getAmount_complete();
				Long porderbalanceid_link = 0L;
				
				POrderSewingCost porderSewingCost = porderSewingCostService.findOne(pordersewingcostid_link);
				
				List<POrderBalance> listPorderBalance = porderBalanceService.getByPOrderAndPOrderSewingCost(
						porderid_link, pordersewingcostid_link);
				if(listPorderBalance.size() > 0) {
					porderbalanceid_link = listPorderBalance.get(0).getId();
				}
				
				List<PorderProcessingNs> listPorderProcessingNs = 
						porderProcessingNsService.getByPersonnelDateAndShiftAndPOrderSewingCost(
								porderid_link, pordergrantid_link, personnelid_link, 
								processingdate, shifttypeid_link, pordersewingcostid_link);
				if(listPorderProcessingNs.size() > 0) { // existed
					PorderProcessingNs temp = listPorderProcessingNs.get(0);
					if(temp.getAmount_complete() != amount_complete) {
						temp.setAmount_complete(amount_complete);
						temp.setUserupdateid_link(user.getId());
						temp.setDate_updated(date);
						temp.setAmount_timespent(amount_complete * porderSewingCost.getTimespent_standard());
						porderProcessingNsService.save(temp);
					}
				}else { // not existed
					if(amount_complete > 0) {
						PorderProcessingNs temp = new PorderProcessingNs();
						temp.setOrgrootid_link(orgrootid_link);
						temp.setProcessingdate(processingdate);
						temp.setPartitionkey(partitionkey);
						temp.setShifttypeid_link(shifttypeid_link);
						temp.setPorderid_link(porderid_link);
						temp.setPordergrantid_link(pordergrantid_link);
						temp.setPersonnelid_link(personnelid_link);
						temp.setPorderbalanceid_link(porderbalanceid_link);
						temp.setPordersewingcostid_link(pordersewingcostid_link);
						temp.setAmount_complete(amount_complete);
						temp.setUserupdateid_link(user.getId());
						temp.setDate_updated(date);
						temp.setAmount_timespent(amount_complete * porderSewingCost.getTimespent_standard());
						porderProcessingNsService.save(temp);
					}
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
