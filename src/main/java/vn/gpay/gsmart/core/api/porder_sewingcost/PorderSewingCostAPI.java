package vn.gpay.gsmart.core.api.porder_sewingcost;

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

import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_sewingcost.IPorderSewingCost_Service;
import vn.gpay.gsmart.core.porder_sewingcost.POrderSewingCost;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.workingprocess.IWorkingProcess_Service;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;

@RestController
@RequestMapping("/api/v1/pordersewingcost")
public class PorderSewingCostAPI {
	@Autowired IPorderSewingCost_Service pordersewingService;
	@Autowired IWorkingProcess_Service workingprocessService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPContract_Price_Service pcontractpriceService;
	
	 @RequestMapping(value = "/create",method = RequestMethod.POST)
		public ResponseEntity<create_pordersewingcost_response> Create(HttpServletRequest request, @RequestBody create_pordersewingcost_request entity ) {
			create_pordersewingcost_response response = new create_pordersewingcost_response();
			try {
				GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				long porderid_link = entity.porderid_link;
				long orgrootid_link = user.getRootorgid_link();
				List<Long> list_id = entity.list_working;
				
				for(Long workingprocessid_link : list_id) {
					WorkingProcess wp = workingprocessService.findOne(workingprocessid_link);
					
					List<POrderSewingCost> list_sewing = pordersewingService.getby_porder_and_workingprocess(porderid_link, workingprocessid_link);
					if(list_sewing.size() == 0) {
						POrderSewingCost porderSewing = new POrderSewingCost();
						porderSewing.setAmount(0);
						porderSewing.setCost((float)0);
						porderSewing.setDatecreated(new Date());
						porderSewing.setId(null);
						porderSewing.setOrgrootid_link(orgrootid_link);
						porderSewing.setPorderid_link(porderid_link);
						porderSewing.setTotalcost((float)0);
						porderSewing.setUsercreatedid_link(user.getId());
						porderSewing.setWorkingprocessid_link(workingprocessid_link);
						porderSewing.setTechcomment(wp.getTechcomment());
						porderSewing.setLaborrequiredid_link(wp.getLaborrequiredid_link());
						porderSewing.setDevicerequiredid_link(wp.getDevicerequiredid_link());
						porderSewing.setTimespent_standard(wp.getTimespent_standard());
						
						pordersewingService.save(porderSewing);
					}
				}
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<create_pordersewingcost_response>(response,HttpStatus.OK);
			}catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(e.getMessage());
			    return new ResponseEntity<create_pordersewingcost_response>(response, HttpStatus.OK);
			}
		}
		
		@RequestMapping(value = "/getby_porder",method = RequestMethod.POST)
		public ResponseEntity<getby_porder_response> GetByPorder(HttpServletRequest request, @RequestBody getby_porder_request entity ) {
			getby_porder_response response = new getby_porder_response();
			try {
				long porderid_link = entity.porderid_link;
				
				long workingprocessid_link = 0; // 0 : lay theo porder
				response.data = pordersewingService.getby_porder_and_workingprocess(porderid_link, workingprocessid_link);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<getby_porder_response>(response,HttpStatus.OK);
			}catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(e.getMessage());
			    return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
			}
		}
		
		@RequestMapping(value = "/update",method = RequestMethod.POST)
		public ResponseEntity<update_pordersewingcost_response> Update(HttpServletRequest request, @RequestBody update_pordersewingcost_request entity ) {
			update_pordersewingcost_response response = new update_pordersewingcost_response();
			try {
				POrderSewingCost pordersewingcost = pordersewingService.findOne(entity.data.getId());
				float cost_old = pordersewingcost.getTotalcost();
				pordersewingService.save(entity.data);
				
				//Cap nhat gia moi nhat cho san pham vao bang workingprocess
				WorkingProcess wp = workingprocessService.findOne(entity.data.getWorkingprocessid_link());
				wp.setLastcost(entity.data.getCost());
				workingprocessService.save(wp);
				
				//Cap nhat gia len PContract_PO
				POrder porder = porderService.findOne(entity.data.getPorderid_link());
				long pcontract_poid_link = porder.getPcontract_poid_link();
				long productid_link = porder.getProductid_link();
				PContract_Price price = pcontractpriceService.getPrice_CMP(pcontract_poid_link, productid_link);
				float price_cost_old = price.getPrice_sewingcost();
				price.setPrice_sewingcost(price_cost_old - cost_old + entity.data.getTotalcost());
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<update_pordersewingcost_response>(response,HttpStatus.OK);
			}catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(e.getMessage());
			    return new ResponseEntity<update_pordersewingcost_response>(response, HttpStatus.OK);
			}
		}
		
		@RequestMapping(value = "/delete",method = RequestMethod.POST)
		public ResponseEntity<delete_porersewingcost_response> Delete(HttpServletRequest request, @RequestBody delete_pordersewingcost_request entity ) {
			delete_porersewingcost_response response = new delete_porersewingcost_response();
			try {
				pordersewingService.deleteById(entity.id);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<delete_porersewingcost_response>(response,HttpStatus.OK);
			}catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(e.getMessage());
			    return new ResponseEntity<delete_porersewingcost_response>(response, HttpStatus.OK);
			}
		}
		
}
