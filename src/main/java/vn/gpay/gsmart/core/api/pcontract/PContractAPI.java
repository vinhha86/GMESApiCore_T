package vn.gpay.gsmart.core.api.pcontract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.IPContract_AutoID_Service;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontract")
public class PContractAPI {
	@Autowired IPContractService pcontractService;
	@Autowired IPContract_AutoID_Service pcontract_AutoID_Service;
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PContract_create_response> PContractCreate(@RequestBody PContract_create_request entity,HttpServletRequest request ) {
		PContract_create_response response = new PContract_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract pcontract = entity.data;
			if(pcontract.getId() == 0 || pcontract.getId() == null) {
				if (null == pcontract.getContractcode() || pcontract.getContractcode().length() == 0){
					Org theBuyer = orgService.findOne(pcontract.getOrgbuyerid_link());
					if (null != theBuyer)
						pcontract.setContractcode(pcontract_AutoID_Service.getLastID(theBuyer.getCode()));
					else
						pcontract.setContractcode(pcontract_AutoID_Service.getLastID("UNKNOWN"));
				} else {
					String contractcode = pcontract.getContractcode();
					long pcontractid_link = pcontract.getId();
					
					List<PContract> lstcheck = pcontractService.getby_code(orgrootid_link, contractcode, pcontractid_link);
					if(lstcheck.size() > 0) {
						response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
						response.setMessage("Mã đã tồn tại trong hệ thống!");
						return new ResponseEntity<PContract_create_response>(response, HttpStatus.BAD_REQUEST);
					}					
				}
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
			
			pcontract = pcontractService.save(pcontract);
			response.id = pcontract.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_create_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContract_create_response>(response, HttpStatus.BAD_REQUEST);
		}
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
	
	@RequestMapping(value = "/getlistbypaging",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbypaging_response> PContractGetpageList(@RequestBody PContract_getbypaging_request entity,HttpServletRequest request ) {
		PContract_getbypaging_response response = new PContract_getbypaging_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract> pcontract = pcontractService.getalllist_by_orgrootid_paging(orgrootid_link, entity);
			
			response.data = new ArrayList<PContract>();
			
			for(PContract pc : pcontract) {
				String cc = pc.getContractcode().toLowerCase();
				String pl = pc.getProductlist().toLowerCase();
				String pol = pc.getPolist().toLowerCase();
				if(!cc.contains(entity.contractcode.toLowerCase())) continue;
				if(!pl.contains(entity.style.toLowerCase())) continue;
				if(!pol.contains(entity.po.toLowerCase())) continue;
				response.data.add(pc);
			}
			response.totalCount = response.data.size();
			
			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) page.getOffset();
			int end = (start + page.getPageSize()) > response.data.size() ? response.data.size() : (start + page.getPageSize());
			Page<PContract> pageToReturn = new PageImpl<PContract>(response.data.subList(start, end), page, response.data.size()); 
			
			response.data = pageToReturn.getContent();
//			response.totalCount = pcontract.getTotalElements();
			
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
