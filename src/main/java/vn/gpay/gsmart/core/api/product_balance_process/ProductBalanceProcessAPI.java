package vn.gpay.gsmart.core.api.product_balance_process;

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
import vn.gpay.gsmart.core.product_balance_process.IProductBalanceProcessService;
import vn.gpay.gsmart.core.product_balance_process.ProductBalanceProcess;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/product_balance_process")
public class ProductBalanceProcessAPI {
	@Autowired IProductBalanceProcessService productBalanceProcessService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody ProductBalanceProcess_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long productbalanceid_link = entity.productbalanceid_link;
			Long productsewingcostid_link = entity.productsewingcostid_link;
			
			ProductBalanceProcess newProductBalanceProcess = new ProductBalanceProcess();
			newProductBalanceProcess.setId(0L);
			newProductBalanceProcess.setOrgrootid_link(orgrootid_link);
			newProductBalanceProcess.setProductbalanceid_link(productbalanceid_link);
			newProductBalanceProcess.setProductsewingcostid_link(productsewingcostid_link);
			
			productBalanceProcessService.save(newProductBalanceProcess);
			
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
	public ResponseEntity<ResponseBase> delete(@RequestBody ProductBalanceProcess_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
			
			Long products_balance_processid_link = entity.product_balance_processid_link;
			productBalanceProcessService.deleteById(products_balance_processid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updateProductBalanceId", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updateProductBalanceId(@RequestBody ProductBalanceProcess_updateProductBalanceId_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
			
			Long products_balance_processid_link = entity.product_balance_processid_link;
			Long productbalanceid_link_next = entity.productbalanceid_link_next;
			
			ProductBalanceProcess productBalanceProcess = productBalanceProcessService.findOne(products_balance_processid_link);
			productBalanceProcess.setProductbalanceid_link(productbalanceid_link_next);
			productBalanceProcessService.save(productBalanceProcess);
			
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
