package vn.gpay.gsmart.core.api.porder_grant;


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

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_grant")
public class POrder_GrantAPI {
	@Autowired private IPOrderGrant_Service porderGrantService;
	@Autowired private IPOrderGrant_SKUService porderGrant_SKUService;
	@Autowired private IPOrder_Service porderService;
	@Autowired private IPOrderProcessing_Service pprocessRepository;
    ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<POrder_Grant_GetOne_Response> POrderGetOne(@RequestBody POrder_Grant_GetOne_Request entity,HttpServletRequest request ) {
		POrder_Grant_GetOne_Response response = new POrder_Grant_GetOne_Response();
		try {
			
			response.data = porderGrantService.getByOrderIDAndOrg(entity.granttoorgid_link, entity.porderid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Grant_GetOne_Response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrder_Grant_GetOne_Response>(response, HttpStatus.BAD_REQUEST);
		}
	} 
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<POrder_GrantCreate_response> Create(HttpServletRequest request,
			@RequestBody POrder_GrantCreate_request entity) {
		POrder_GrantCreate_response response = new POrder_GrantCreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrderGrant porder_grant = entity.data;
			
			//Lay thong tin PO
			POrder thePOrder = porderService.findOne(porder_grant.getPorderid_link());
			
			if (porder_grant.getId() == null || porder_grant.getId() == 0) {
				porder_grant.setOrgrootid_link(orgrootid_link);
				porder_grant.setOrdercode(thePOrder.getOrdercode());
				
				porder_grant.setGrantdate(new Date());
				porder_grant.setUsercreatedid_link(user.getId());
				porder_grant.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				porder_grant.setTimecreated(new Date());
			} 
			
			porderGrantService.save(porder_grant);
			response.id = porder_grant.getId();
			

			//Xoa List SKU cu
			List<POrderGrant_SKU> list_sku = porderGrant_SKUService.getPOrderGrant_SKU(porder_grant.getId());
			for(POrderGrant_SKU sku : list_sku) {
				porderGrant_SKUService.delete(sku);
			}

			//them list moi
			for(POrderGrant_SKU theGrantSKU: porder_grant.getPorder_grant_sku()){
				POrderGrant_SKU newGrantSKU = new POrderGrant_SKU();
				newGrantSKU.setId(null);
				newGrantSKU.setOrgrootid_link(orgrootid_link);
				newGrantSKU.setPordergrantid_link(porder_grant.getId());
				newGrantSKU.setSkuid_link(theGrantSKU.getSkuid_link());
				newGrantSKU.setGrantamount(theGrantSKU.getGrantamount());
				porderGrant_SKUService.save(newGrantSKU);
			}
			//Create line on Porder Procesing
			if (porder_grant.getId() != null || porder_grant.getId() != 0){
		        POrderProcessing pprocess = new POrderProcessing();
		        
		        pprocess.setOrgrootid_link(porder_grant.getOrgrootid_link());
		        pprocess.setPorderid_link(porder_grant.getPorderid_link());
		        pprocess.setOrdercode(thePOrder.getOrdercode());
		        pprocess.setGranttoorgid_link(porder_grant.getGranttoorgid_link());
		        pprocess.setGranttoorgname(porder_grant.getGranttoorgname());
		        pprocess.setTotalorder(porder_grant.getGrantamount());
		        
		        pprocess.setProcessingdate(new Date());
		        
		        pprocess.setAmountcut(porder_grant.getGrantamount());
		        pprocess.setAmountcutsum(porder_grant.getGrantamount());
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
		        pprocess.setUsercreatedid_link(user.getId());
		        pprocess.setTimecreated(new Date());
		        
		        pprocessRepository.save(pprocess);
			}
			
			thePOrder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
			porderService.save(thePOrder);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_GrantCreate_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_GrantCreate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
