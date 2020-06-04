package vn.gpay.gsmart.core.api.list;

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
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.category.IColorService;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.stock.IStockrowService;
import vn.gpay.gsmart.core.stock.IStockspaceService;
import vn.gpay.gsmart.core.stock.Stockrow;
import vn.gpay.gsmart.core.stock.Stockspace;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/list")
public class ListAPI {

	@Autowired ISKU_Service skuService;
	@Autowired IColorService colorService;
	@Autowired IStockrowService stockrowService;
	@Autowired IStockspaceService stockspaceService;
	
	@RequestMapping(value = "/getAllProducts",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllProducts( @RequestBody ListRequest entity,HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			response.data = skuService.getSKU_ByType("",1);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getAllMainMaterials",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllMainMaterials( @RequestBody ListRequest entity,HttpServletRequest request ) {
		SkuResponse response = new SkuResponse();
		try {
			response.data = skuService.getSKU_ByType("",2);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SkuResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getAllColors",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllColors( @RequestBody ListRequest entity,HttpServletRequest request ) {
		ColorResponse response =new ColorResponse();
		try {
			response.data = colorService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ColorResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getAllStockSpaces",method = RequestMethod.POST)
	public ResponseEntity<?> GetAllStockSpaces( @RequestBody ListRequest entity,HttpServletRequest request ) {
		StockRowResponse response =new StockRowResponse();
		try {
			response.data = stockrowService.findStockrowByOrgID(3);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockRowResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/addStockRow",method = RequestMethod.POST)
	public ResponseEntity<?> AddStockRow( @RequestBody Stockrow entity,HttpServletRequest request ) {
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		ResponseBase response =new ResponseBase();
		if (user != null){
			try {
				entity.setOrgid_link(user.getOrgId());
				stockrowService.create(entity);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}catch (RuntimeException e) {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage(e.getMessage());
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else
			return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);		
	}
	@RequestMapping(value = "/addStockSpaceToRow",method = RequestMethod.POST)
	public ResponseEntity<?> AddStockSpaceToRow( @RequestBody Stockspace entity,HttpServletRequest request ) {
		ResponseBase response =new ResponseBase();
		try {
			stockspaceService.create(entity);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getOneStockSpace",method = RequestMethod.POST)
	public ResponseEntity<?> GetOneStockSpace( @RequestBody StockSpaceRequest entity,HttpServletRequest request ) {
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		StockSpaceResponse response =new StockSpaceResponse();
		if (user != null){
			try {
				response.data = stockspaceService.findStockspaceByEpc(user.getOrgId(), entity.spaceepc);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<StockSpaceResponse>(response,HttpStatus.OK);
			}catch (RuntimeException e) {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage(e.getMessage());
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else
			return new ResponseEntity<StockSpaceResponse>(response,HttpStatus.BAD_REQUEST);
	}
}
