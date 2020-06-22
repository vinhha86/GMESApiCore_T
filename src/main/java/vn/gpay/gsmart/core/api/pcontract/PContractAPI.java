package vn.gpay.gsmart.core.api.pcontract;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontract")
public class PContractAPI {
	@Autowired IPContractService pcontractService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PContract_create_response> PContractCreate(@RequestBody PContract_create_request entity,HttpServletRequest request ) {
		PContract_create_response response = new PContract_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract pcontract = entity.data;
			if(pcontract.getId() == 0 || pcontract.getId() == null) {
				pcontract.setOrgrootid_link(orgrootid_link);
				pcontract.setUsercreatedid_link(usercreatedid_link);
				pcontract.setDatecreated(new Date());
			}
			else {
				PContract pc_old = pcontractService.findOne(pcontract.getId());
				pcontract.setOrgrootid_link(pc_old.getOrgrootid_link());
				pcontract.setUsercreatedid_link(pc_old.getUsercreatedid_link());
				pcontract.setDatecreated(pc_old.getDatecreated());
			}
			
			
			
			String contractcode = pcontract.getContractcode();
			long pcontractid_link = pcontract.getId();
			
			List<PContract> lstcheck = pcontractService.getby_code(orgrootid_link, contractcode, pcontractid_link);
			if(lstcheck.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Mã đã tồn tại trong hệ thống!");
			}
			else {
				pcontract = pcontractService.save(pcontract);				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));			
			}
			response.id = pcontract.getId();
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
	    return new ResponseEntity<PContract_create_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getbypaging",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> PContractGetpage(@RequestBody PContract_getbypaging_request entity,HttpServletRequest request ) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			Page<PContract> pcontract = pcontractService.getall_by_orgrootid_paging(orgrootid_link, entity);
			response.data = pcontract.getContent();
			response.totalCount = pcontract.getTotalElements();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbypaging_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbypaging_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<PContract_getone_response> PContractGetOne(@RequestBody PContract_getone_request entity,HttpServletRequest request ) {
		PContract_getone_response response = new PContract_getone_response();
		try {
			
			response.data = pcontractService.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getone_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> PContractDelete(@RequestBody PContract_delete_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			pcontractService.deleteById(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
	    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
