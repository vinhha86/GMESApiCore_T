package vn.gpay.gsmart.core.api.salary;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.salary.ISalary_Sum_POrdersService;
import vn.gpay.gsmart.core.salary.Salary_Sum_POrders;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/salarysum_porders")
public class Salary_Sum_POrdersAPI {
	@Autowired private ISalary_Sum_POrdersService salarysum_pordersService;
	@Autowired private IPOrderGrant_Service porderGrantService;
	
	@RequestMapping(value = "/getall_byorg", method = RequestMethod.POST)
	public ResponseEntity<salary_sum_porders_response> getall_byorg(HttpServletRequest request,
			@RequestBody salary_sum_byorg_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		salary_sum_porders_response response = new salary_sum_porders_response();
		try {
			response.data = salarysum_pordersService.getall_byorg(entity.orgid_link, entity.year, entity.month);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<salary_sum_porders_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<salary_sum_porders_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(HttpServletRequest request,
			@RequestBody salary_sum_porders_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			for (Long pordergrantid_link:entity.listId){
				POrderGrant thePorderGrant = porderGrantService.findOne(pordergrantid_link);
				if (null!=thePorderGrant && thePorderGrant.getGranttoorgid_link() == entity.orgid_link){
					Salary_Sum_POrders newEntity = new Salary_Sum_POrders();
					newEntity.setOrgid_link(entity.orgid_link);
					newEntity.setYear(entity.year);
					newEntity.setMonth(entity.month);
					newEntity.setPordergrantid_link(pordergrantid_link);
					newEntity.setPorderid_link(thePorderGrant.getPorderid_link());
					newEntity.setAmountstocked(thePorderGrant.getGrantamount());
					salarysum_pordersService.saveWithCheck(newEntity);
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> delete(HttpServletRequest request,
			@RequestBody salary_sum_porders_delete_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Long orgrootid_link = user.getRootorgid_link();
		ResponseBase response = new ResponseBase();
		try {
			Salary_Sum_POrders theEntity = salarysum_pordersService.findOne(entity.id);
			if (null != theEntity){
				salarysum_pordersService.delete(theEntity);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã chức vụ không tồn tại");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
}
