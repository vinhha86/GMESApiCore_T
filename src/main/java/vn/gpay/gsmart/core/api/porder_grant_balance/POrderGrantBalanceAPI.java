package vn.gpay.gsmart.core.api.porder_grant_balance;

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
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant_balance.IPOrderGrantBalanceService;
import vn.gpay.gsmart.core.porder_grant_balance.POrderGrantBalance;
import vn.gpay.gsmart.core.porder_grant_timesheet.IPOrderGrantTimesheetService;
import vn.gpay.gsmart.core.porder_grant_timesheet.POrderGrantTimesheet;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_grant_balance")
public class POrderGrantBalanceAPI {
	@Autowired IPOrderGrantBalanceService porderGrantBalanceService;
	@Autowired IPOrderGrantTimesheetService porderGrantTimesheetService;
	@Autowired IPOrderGrant_Service porderGrantService;
	@Autowired IPOrder_Service porderService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody POrderGrantBalance_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Date date = new Date();
			
			// personnelid_link, porderbalanceid_link, pordergrantid_link
			Long personnelid_link = entity.personnelid_link;
			Long porderbalanceid_link = entity.porderbalanceid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			
			// vị trí này của grant này có người chưa
			List<POrderGrantBalance> listPOrderGrantBalance = 
					porderGrantBalanceService.getByPorderGrantAndPorderBalance(pordergrantid_link, porderbalanceid_link);
			
			// chưa có tạo mới
			if(listPOrderGrantBalance.size() == 0) {
				// create
				POrderGrantBalance newPOrderGrantBalance = new POrderGrantBalance();
				newPOrderGrantBalance.setId(0L);
				newPOrderGrantBalance.setOrgrootid_link(orgrootid_link);
				newPOrderGrantBalance.setPordergrantid_link(pordergrantid_link);
				newPOrderGrantBalance.setPorderbalanceid_link(porderbalanceid_link);
				newPOrderGrantBalance.setPersonnelid_link(personnelid_link);
				
				porderGrantBalanceService.save(newPOrderGrantBalance);
				
				POrderGrantTimesheet newPOrderGrantTimesheet = new POrderGrantTimesheet();
				newPOrderGrantTimesheet.setId(0L);
				newPOrderGrantTimesheet.setOrgrootid_link(orgrootid_link);
				newPOrderGrantTimesheet.setPordergrantid_link(pordergrantid_link);
				newPOrderGrantTimesheet.setPorderbalanceid_link(porderbalanceid_link);
				newPOrderGrantTimesheet.setPersonnelid_link(personnelid_link);
				newPOrderGrantTimesheet.setTime_in(date);
				porderGrantTimesheetService.save(newPOrderGrantTimesheet);
				
			}else {
				// update
				// porder status == 4 : đang sản xuất
				POrderGrantBalance porderGrantBalance = listPOrderGrantBalance.get(0);
				porderGrantBalance.setPersonnelid_link(personnelid_link);
				porderGrantBalanceService.save(porderGrantBalance);
				
				POrderGrant porderGrant = porderGrantService.findOne(pordergrantid_link);
				POrder porder = porderService.findOne(porderGrant.getPorderid_link());
				Integer porderStatus = porder.getStatus();
				if(porderStatus < 4) {
					// chưa sx, xoá timesheet và thêm timesheet
					List<POrderGrantTimesheet> listPOrderGrantTimesheet = 
							porderGrantTimesheetService.getByPorderGrantAndPorderBalance(
									pordergrantid_link, 
									porderbalanceid_link);
					if(listPOrderGrantTimesheet.size() > 0) {
						POrderGrantTimesheet porderGrantTimesheet = listPOrderGrantTimesheet.get(0);
						porderGrantTimesheetService.deleteById(porderGrantTimesheet.getId());
					}
					POrderGrantTimesheet newPOrderGrantTimesheet = new POrderGrantTimesheet();
					newPOrderGrantTimesheet.setId(0L);
					newPOrderGrantTimesheet.setOrgrootid_link(orgrootid_link);
					newPOrderGrantTimesheet.setPordergrantid_link(pordergrantid_link);
					newPOrderGrantTimesheet.setPorderbalanceid_link(porderbalanceid_link);
					newPOrderGrantTimesheet.setPersonnelid_link(personnelid_link);
					newPOrderGrantTimesheet.setTime_in(date);
					porderGrantTimesheetService.save(newPOrderGrantTimesheet);
					
					System.out.println("");
				}
				if(porderStatus == 4) {
					// đang sx, đổi người, thêm time out và time in
					List<POrderGrantTimesheet> listPOrderGrantTimesheet = 
							porderGrantTimesheetService.getByPorderGrantAndPorderBalance(
									pordergrantid_link, 
									porderbalanceid_link);
					if(listPOrderGrantTimesheet.size() > 0) {
						System.out.println("here yet");
						POrderGrantTimesheet porderGrantTimesheet = listPOrderGrantTimesheet.get(0);
						porderGrantTimesheet.setTime_out(date);
						porderGrantTimesheetService.save(porderGrantTimesheet);
					}
					POrderGrantTimesheet newPOrderGrantTimesheet = new POrderGrantTimesheet();
					newPOrderGrantTimesheet.setId(0L);
					newPOrderGrantTimesheet.setOrgrootid_link(orgrootid_link);
					newPOrderGrantTimesheet.setPordergrantid_link(pordergrantid_link);
					newPOrderGrantTimesheet.setPorderbalanceid_link(porderbalanceid_link);
					newPOrderGrantTimesheet.setPersonnelid_link(personnelid_link);
					newPOrderGrantTimesheet.setTime_in(date);
					porderGrantTimesheetService.save(newPOrderGrantTimesheet);
				}
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
}
