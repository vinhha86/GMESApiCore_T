package vn.gpay.gsmart.core.api.contractbuyer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.contractbuyer.ContractBuyer;
import vn.gpay.gsmart.core.contractbuyer.IContractBuyerService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/contractbuyer")
public class ContractBuyerAPI {
	@Autowired IContractBuyerService contractBuyerService;
	
	@RequestMapping(value = "/getbypaging",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_getbypaging_response> ContractBuyerGetpage(@RequestBody ContractBuyer_getbypaging_request entity,HttpServletRequest request ) {
		ContractBuyer_getbypaging_response response = new ContractBuyer_getbypaging_response();
		try {
			List<ContractBuyer> cblist = contractBuyerService.getContractBuyerBySearch(entity);
			
			List<ContractBuyer> temp = new ArrayList<ContractBuyer>();
			
			for(ContractBuyer cb : cblist) {
				String contract_code = cb.getContract_code().toLowerCase();
				if(!contract_code.contains(entity.contract_code.toLowerCase())) continue;
				temp.add(cb);
			}
			
			response.totalCount = temp.size();
			
			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) page.getOffset();
			int end = (start + page.getPageSize()) > temp.size() ? temp.size() : (start + page.getPageSize());
			Page<ContractBuyer> pageToReturn = new PageImpl<ContractBuyer>(temp.subList(start, end), page, temp.size()); 
			
			response.data = pageToReturn.getContent();
//			response.data = temp;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ContractBuyer_getbypaging_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ContractBuyer_getbypaging_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_getbypaging_response> ContractBuyerGetAll(HttpServletRequest request ) {
		ContractBuyer_getbypaging_response response = new ContractBuyer_getbypaging_response();
		try {
			List<ContractBuyer> contractBuyers = contractBuyerService.findAll();
			
			response.data = contractBuyers;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ContractBuyer_getbypaging_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ContractBuyer_getbypaging_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_getone_response> ContractBuyerGetOne(@RequestBody ContractBuyer_getone_request entity,HttpServletRequest request ) {
		ContractBuyer_getone_response response = new ContractBuyer_getone_response();
		try {
			
			response.data = contractBuyerService.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ContractBuyer_getone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ContractBuyer_getone_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_create_response> ContractBuyerCreate(@RequestBody ContractBuyer_create_request entity,HttpServletRequest request ) {
		ContractBuyer_create_response response = new ContractBuyer_create_response();
		try {
			ContractBuyer cb = entity.data;
			// check nếu mã đã tồn tại 
			if(cb.getId() == 0) {
				// create
				List<ContractBuyer> cblist = contractBuyerService.getByContractCode(cb.getContract_code());
				
				if(cblist.size() == 0) {
					// chưa tồn tại
					int year = Calendar.getInstance().get(Calendar.YEAR);
					cb.setContract_year(year);
					ContractBuyer temp = contractBuyerService.save(cb);
					response.id = temp.getId();
					response.setMessage("Lưu thành công");
				}else {
					// đã tồn tại
					response.setMessage("Mã hợp đồng đã tồn tại");
				}
			}else {
				// update
				List<ContractBuyer> cblist = contractBuyerService.getOtherContractBuyerByContractCode(cb.getContract_code(), cb.getId());
				
				if(cblist.size() == 0) {
					// chưa tồn tại
					ContractBuyer temp = contractBuyerService.save(cb);
					response.id = temp.getId();
					response.setMessage("Lưu thành công");
				}else {
					// đã tồn tại
					response.setMessage("Mã hợp đồng đã tồn tại");
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<ContractBuyer_create_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ContractBuyer_create_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> ContractBuyerDelete(@RequestBody ContractBuyer_delete_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			contractBuyerService.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getyears",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_getbypaging_response> ContractBuyerGetYears(HttpServletRequest request ) {
		ContractBuyer_getbypaging_response response = new ContractBuyer_getbypaging_response();
		try {
			List<Integer> years = contractBuyerService.getAllYears();
//			years.add(0, null);
			
			List<ContractBuyer> data = new ArrayList<>();
			for(Integer year : years) {
				ContractBuyer temp = new ContractBuyer();
				temp.setContract_year(year);
				data.add(temp);
			}
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ContractBuyer_getbypaging_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ContractBuyer_getbypaging_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByBuyer",method = RequestMethod.POST)
	public ResponseEntity<ContractBuyer_getbypaging_response> ContractBuyerGetByBuyer(@RequestBody ContractBuyer_getone_request entity, HttpServletRequest request ) {
		ContractBuyer_getbypaging_response response = new ContractBuyer_getbypaging_response();
		try {
			response.data = contractBuyerService.getByBuyer(entity.id); // buyerid_link
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ContractBuyer_getbypaging_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ContractBuyer_getbypaging_response>(response, HttpStatus.OK);
		}
	}
}