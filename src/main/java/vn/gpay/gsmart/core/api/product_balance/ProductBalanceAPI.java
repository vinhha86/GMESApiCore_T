package vn.gpay.gsmart.core.api.product_balance;

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
import vn.gpay.gsmart.core.product_balance.IProductBalanceService;
import vn.gpay.gsmart.core.product_balance.ProductBalance;
import vn.gpay.gsmart.core.product_balance_process.IProductBalanceProcessService;
import vn.gpay.gsmart.core.product_balance_process.ProductBalanceProcess;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/product_balance")
public class ProductBalanceAPI {
	@Autowired IProductBalanceService productBalanceService;
	@Autowired IProductBalanceProcessService productBalanceProcessService;
	
//	@RequestMapping(value = "/create", method = RequestMethod.POST)
//	public ResponseEntity<ResponseBase> create(@RequestBody ProductBalance_create_request entity,
//			HttpServletRequest request) {
//		ResponseBase response = new ResponseBase();
//		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
//			
//			Long productid_link = entity.productid_link;
//			Integer amount = entity.amount;
//			
//			List<ProductBalance> listProductBalance = productBalanceService.getByProduct(productid_link);
//			Integer listSize = listProductBalance.size();
//			
//			for(int i=1;i<=amount;i++) {
//				ProductBalance newProductBalance = new ProductBalance();
//				newProductBalance.setId(null);
//				newProductBalance.setOrgrootid_link(orgrootid_link);
//				newProductBalance.setProductid_link(productid_link);
//				
////				String balanceName = "Cụm công đoạn " + (listSize + i);
//				String balanceName = "" + (listSize + i);
//				newProductBalance.setBalance_name(balanceName);
//				
//				Integer sortValue = listSize + i;
//				newProductBalance.setSortvalue(sortValue);
//				
//				productBalanceService.save(newProductBalance);
//			}
//			
//			
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody ProductBalance_create_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
			Long productid_link = entity.productid_link;
			String name = entity.name;
			
			List<ProductBalance> productBalance_list = productBalanceService.getByBalanceName_Product(name, productid_link);
			if(productBalance_list.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Cụm công đoạn đã tồn tại");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}
			
			List<ProductBalance> listProductBalance = productBalanceService.getByProduct(productid_link);
			Integer listSize = listProductBalance.size();
			
			ProductBalance newProductBalance = new ProductBalance();
			newProductBalance.setId(null);
			newProductBalance.setOrgrootid_link(orgrootid_link);
			newProductBalance.setProductid_link(productid_link);
			newProductBalance.setBalance_name(name);
			Integer sortValue = listSize;
			newProductBalance.setSortvalue(sortValue);
			productBalanceService.save(newProductBalance);
			
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
	public ResponseEntity<ResponseBase> delete(@RequestBody ProductBalance_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			
			ProductBalance productBalance = productBalanceService.findOne(id);
			Long productid_link = productBalance.getProductid_link();
			
			List<ProductBalance> productBalance_list = productBalanceService.getByProduct(productid_link);
			
			// set name, sortvalue
			Boolean isAfterDeleteRec = false;
			for(ProductBalance item : productBalance_list) {
				if(item.getId().equals(id)) {
					isAfterDeleteRec = true;
					continue;
				}
				if(isAfterDeleteRec) {
					Integer sortValue = item.getSortvalue() - 1;
					item.setSortvalue(sortValue);
					item.setBalance_name("Cụm công đoạn " + sortValue);
					productBalanceService.save(item);
				}
			}
			
			// delete
			List<ProductBalanceProcess> productBalanceProcess_list = productBalance.getProductBalanceProcesses();
			if(productBalanceProcess_list != null) {
				if(productBalanceProcess_list.size() > 0) {
					for(ProductBalanceProcess item : productBalanceProcess_list) {
						productBalanceProcessService.deleteById(item.getId());
					}
				}
			}
			productBalanceService.deleteById(id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete_multi", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete_multi(@RequestBody ProductBalance_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> idList = entity.idList;
			
			for(Long id : idList) {
				ProductBalance productBalance = productBalanceService.findOne(id);
				Long productid_link = productBalance.getProductid_link();
				
				List<ProductBalance> productBalance_list = productBalanceService.getByProduct(productid_link);
				
				// set name, sortvalue
				Boolean isAfterDeleteRec = false;
				for(ProductBalance item : productBalance_list) {
					if(item.getId().equals(id)) {
						isAfterDeleteRec = true;
						continue;
					}
					if(isAfterDeleteRec) {
						Integer sortValue = item.getSortvalue() - 1;
						item.setSortvalue(sortValue);
						item.setBalance_name("Cụm công đoạn " + sortValue);
						productBalanceService.save(item);
					}
				}
				
				// delete
				List<ProductBalanceProcess> productBalanceProcess_list = productBalance.getProductBalanceProcesses();
				if(productBalanceProcess_list != null) {
					if(productBalanceProcess_list.size() > 0) {
						for(ProductBalanceProcess item : productBalanceProcess_list) {
							productBalanceProcessService.deleteById(item.getId());
						}
					}
				}
				productBalanceService.deleteById(id);
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
	
	@RequestMapping(value = "/getByProduct", method = RequestMethod.POST)
	public ResponseEntity<ProductBalance_response> getByProduct(@RequestBody ProductBalance_getByProduct_request entity,
			HttpServletRequest request) {
		ProductBalance_response response = new ProductBalance_response();
		try {
			response.data = productBalanceService.getByProduct(entity.productid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ProductBalance_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ProductBalance_response>(response, HttpStatus.OK);
		}
	}
}
