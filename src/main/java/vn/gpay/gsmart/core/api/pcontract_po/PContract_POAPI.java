package vn.gpay.gsmart.core.api.pcontract_po;

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
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontract_po")
public class PContract_POAPI {
	@Autowired IPContract_POService pcontract_POService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PContract_pocreate_response> PContractCreate(@RequestBody PContract_pocreate_request entity,HttpServletRequest request ) {
		PContract_pocreate_response response = new PContract_pocreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract_PO pcontract_po = entity.data;
			if(pcontract_po.getId() == 0 || pcontract_po.getId() == null) {
				pcontract_po.setOrgrootid_link(orgrootid_link);
				pcontract_po.setUsercreatedid_link(usercreatedid_link);
				pcontract_po.setDatecreated(new Date());
			}
			else {
				PContract_PO pcontract_po_old = pcontract_POService.findOne(pcontract_po.getId());
				pcontract_po.setOrgrootid_link(pcontract_po_old.getOrgrootid_link());
				pcontract_po.setUsercreatedid_link(pcontract_po_old.getUsercreatedid_link());
				pcontract_po.setDatecreated(pcontract_po_old.getDatecreated());
			}
			pcontract_po = pcontract_POService.save(pcontract_po);
			
			response.id = pcontract_po.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));	
			
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_pocreate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbycontractproduct",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOByContractProduct(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPOByContractProduct(orgrootid_link, entity.pcontractid_link, entity.productid_link);
			response.data = pcontract;
			response.totalCount = pcontract.size();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbycontract",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOByContract(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPOByContract(orgrootid_link, entity.pcontractid_link);
			response.data = pcontract;
			response.totalCount = pcontract.size();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<PContract_pogetone_response> PContractGetOne(@RequestBody PContract_pogetone_request entity,HttpServletRequest request ) {
		PContract_pogetone_response response = new PContract_pogetone_response();
		try {
			
			response.data = pcontract_POService.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_pogetone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_pogetone_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> PContractDelete(@RequestBody PContract_podelete_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			pcontract_POService.deleteById(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
	    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
