package vn.gpay.gsmart.core.api.invcheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import vn.gpay.gsmart.core.invcheck.IInvcheckEpcService;
import vn.gpay.gsmart.core.invcheck.IInvcheckService;
import vn.gpay.gsmart.core.invcheck.IInvcheckSkuService;
import vn.gpay.gsmart.core.invcheck.Invcheck;
import vn.gpay.gsmart.core.invcheck.InvcheckEpc;
import vn.gpay.gsmart.core.invcheck.InvcheckSku;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;

@RestController
@RequestMapping("/api/v1/invcheck")
public class InvCheckAPI {

	@Autowired IInvcheckService invcheckService;
	@Autowired IInvcheckEpcService invcheckEpcService;
	@Autowired IInvcheckSkuService invcheckSkuService;
	@Autowired IWarehouseService warehouseService;
	@Autowired ISKU_Service skuService;
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/invcheck_create",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckCreate( @RequestBody InvcheckCreateRequest entity,HttpServletRequest request ) {
		InvcheckResponse responseBase = new InvcheckResponse();
		try {
			List<Invcheck> datacheck =invcheckService.invcheck_getactive(entity.orgfrom_code);
			if(datacheck!=null && datacheck.size()> 0) {
				responseBase.setRespcode(ResponseMessage.KEY_RC_CREATE_INVCHECK_FAIL);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_CREATE_INVCHECK_FAIL));
				return new ResponseEntity<InvcheckResponse>(responseBase,HttpStatus.OK);
			} 
			Org org = orgService.findOne(entity.orgfrom_code);
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			UUID uuid = UUID.randomUUID();
		    String invcheckcode = uuid.toString();
			Invcheck invcheck = new Invcheck();
			invcheck.setOrgid_link(user.getRootorgid_link());
			invcheck.setUsercreateid_link(user.getUserId());
			invcheck.setLastuserupdateid_link(user.getUserId());
			invcheck.setBossid_link(entity.bossid);
			invcheck.setP_skuid_link(entity.productcode);
			invcheck.setExtrainfo(entity.extrainfo);
			invcheck.setStatus(0);
			invcheck.setInvcheckdatetime(new Date());
			invcheck.setOrgcheckid_link(entity.orgfrom_code);
			invcheck.setInvcheckcode(invcheckcode);
			Invcheck invcheck1= invcheckService.create(invcheck);
			//insert invcheck_sku
//			List<InvcheckSku> list_sku = warehouseService.invcheck_sku(invcheck1.getId(), user.getAppuser().getRootorgid_link(), entity.bossid, entity.orgfrom_code, entity.productcode);
//			if(list_sku!=null) {
//				for(InvcheckSku sku : list_sku) {
//					invcheckSkuService.create(sku);
//				}
//			}
			List<InvcheckSku> list_sku = new ArrayList<InvcheckSku>();
			
			//insert invcheck_epc
			List<InvcheckEpc> list_epc =warehouseService.invcheck_epc(invcheck1.getId(), user.getRootorgid_link(), entity.bossid, entity.orgfrom_code, entity.productcode);
			if(list_epc!=null) {
				for(InvcheckEpc epc : list_epc) {
					
					//check if inventory check for shop or complete product stocks --> ydsorigin=1
					if(org.getOrgtypeid_link()==4 ||org.getOrgtypeid_link() ==8) {
						epc.setYdsorigin(1f);
						epc.setYdscheck(0f);
					}					
					invcheckEpcService.create(epc);
					if(epc.getUnitprice()==null) {
						SKU sku = skuService.findOne(epc.getSkuid_link());
						if(sku!=null) {
							epc.setUnitprice(sku.getSaleprice());
							if(epc.getUnitid_link()==null) {
								epc.setUnitid_link(sku.getUnitid_link());
							}
						}
					}
					
					//Update SKU
					InvcheckSku skuFound = list_sku.stream().filter(sku -> sku.getSkuid_link().equals(epc.getSkuid_link())).findAny().orElse(null);
					if (skuFound != null){
						skuFound.setTotalpackage(skuFound.getTotalpackage() + 1);
//						if(org.getOrgtypeid_link()==4 ||org.getOrgtypeid_link() ==8) {
//							skuFound.setYdsorigin(skuFound.getYdsorigin() + 1);
//						}else {
							skuFound.setYdsorigin(skuFound.getYdsorigin() + epc.getYdsorigin());
//						}
						skuFound.setUnitprice((skuFound.getUnitprice() + epc.getUnitprice())/2);

					}
					else {
						skuFound = new InvcheckSku();
						skuFound.setOrgid_link(invcheck.getOrgid_link());
						skuFound.setInvcheckid_link(invcheck1.getId());
						skuFound.setSkuid_link(epc.getSkuid_link());
						skuFound.setYdsorigin(epc.getYdsorigin());
						skuFound.setYdscheck(0f);
						skuFound.setTotalpackage(1);
						skuFound.setTotalpackagecheck(0);
						skuFound.setUnitprice(epc.getUnitprice());
						skuFound.setUnitid_link(epc.getUnitid_link());
						list_sku.add(skuFound);
					}
				}
			}
			
			if(list_sku!=null) {
				for(InvcheckSku sku : list_sku) {
					if(sku.getUnitprice()!=null) {
						sku.setTotalamount(sku.getUnitprice()*sku.getYdsorigin());
					}
					invcheckSkuService.create(sku);
				}
			}
			
			List<Invcheck> data =new ArrayList<Invcheck>();
			data.add(invcheck);
			responseBase.data=data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvcheckResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invcheck_post",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckPost( @RequestBody InvcheckPostRequest entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		List<Invcheck> data =invcheckService.invcheck_getbycode(entity.invcheckcode);
		if (data.size() > 0){
			try {
				//find and update epc on the invcheck_epc
				for (InvcheckEpc epc: entity.data){
					invcheckEpcService.update(epc);
					invcheckSkuService.updateTotalCheck(epc.getInvcheckid_link(), epc.getSkuid_link());
				}
				responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
			}catch (RuntimeException e) {
				ResponseError errorBase = new ResponseError();
				errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				errorBase.setMessage(e.getMessage());
			    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			responseBase.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
		    return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);			
		}
	}
	@RequestMapping(value = "/invcheck_list",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckList( @RequestBody InvcheckListRequest entity,HttpServletRequest request ) {
		InvcheckResponse responseBase = new InvcheckResponse();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GpayAuthentication authen = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			String orgfrom_code;
			if(authen.isOrgRoot()) {
				orgfrom_code =entity.orgfrom_code;
			}else {
				orgfrom_code =user.getOrgid_link().toString();
			}
			responseBase.data=invcheckService.invcheck_list(user.getRootorgid_link(),entity.stockcode, orgfrom_code, entity.invdateto_from, entity.invdateto_to,entity.status);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvcheckResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/invcheck_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckGetById( @RequestBody InvcheckByIdRequest entity,HttpServletRequest request ) {
		InvcheckResponse responseBase = new InvcheckResponse();
		try {
			Invcheck invcheck = invcheckService.findOne(entity.id);
			List<Invcheck> data =new ArrayList<Invcheck>();
			data.add(invcheck);
			responseBase.data=data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvcheckResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invcheck_getepcbysku",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckGetEpcBySku( @RequestBody EpcBySkuRequest entity,HttpServletRequest request ) {
		EpcBySkuResponse responseBase = new EpcBySkuResponse();
		try {
			responseBase.data=invcheckEpcService.findEpcBySkuId(entity.invcheckid_link, entity.skuid_link);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<EpcBySkuResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invcheck_getactive",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckGetActivate( @RequestBody InvcheckGetActiveRequest entity,HttpServletRequest request ) {
		GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		InvcheckResponse responseBase = new InvcheckResponse();
		try {
			List<Invcheck> data =invcheckService.invcheck_getactive(user.getOrgId());
			if(data==null) data = new ArrayList<>();
			responseBase.data=data;
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<InvcheckResponse>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/invcheck_deletebyid",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckDeleteById( @RequestBody InvcheckByIdRequest entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
			//kiem tra có can xoa sku +epc trươc ko
			invcheckService.deleteById(entity.id);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/invcheck_deactive",method = RequestMethod.POST)
	public ResponseEntity<?> InvcheckDeactive( @RequestBody InvcheckByIdRequest entity,HttpServletRequest request ) {
		ResponseBase responseBase = new ResponseBase();
		try {
			invcheckService.invcheck_deactive(entity.id);
			responseBase.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			responseBase.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(responseBase,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
