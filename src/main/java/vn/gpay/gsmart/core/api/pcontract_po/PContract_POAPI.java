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
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_DService;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Service;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price_D;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontract_po")
 public class PContract_POAPI {
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPContract_Price_Service pcontractpriceService;
	@Autowired IPContract_Price_DService pcontractpriceDService;
	@Autowired IPOrder_Service porderService;
	@Autowired private IPOrder_Req_Service porder_req_Service;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PContract_pocreate_response> PContractCreate(@RequestBody PContract_pocreate_request entity,HttpServletRequest request ) {
		PContract_pocreate_response response = new PContract_pocreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			long pcontractid_link = entity.pcontractid_link;
			
			PContract_PO pcontract_po = entity.data;
			if( pcontract_po.getId() == null) {
				pcontract_po.setOrgrootid_link(orgrootid_link);
				pcontract_po.setUsercreatedid_link(usercreatedid_link);
				pcontract_po.setDatecreated(new Date());
				pcontract_po.setPcontractid_link(pcontractid_link);
				pcontract_po.setStatus(POStatus.PO_STATUS_UNCONFIRM);
			}
//			else {
//				PContract_PO pcontract_po_old = pcontract_POService.findOne(pcontract_po.getId());
//				pcontract_po.setOrgrootid_link(pcontract_po_old.getOrgrootid_link());
//				pcontract_po.setUsercreatedid_link(pcontract_po_old.getUsercreatedid_link());
//				pcontract_po.setDatecreated(pcontract_po_old.getDatecreated());
//			}
			pcontract_po = pcontract_POService.save(pcontract_po);
			
			long pcontract_poid_link = pcontract_po.getId();
			//Cập nhật lại giá

			//Xóa list cũ
			List<PContract_Price> list_price = pcontractpriceService.getPrice_ByPO(pcontract_poid_link);
			for(PContract_Price price : list_price) {
				pcontractpriceService.delete(price);
			}
			List<PContract_Price_D> list_price_d = pcontractpriceDService.getPrice_D_ByPO(pcontract_poid_link);
			for(PContract_Price_D price_d : list_price_d) {
				pcontractpriceDService.delete(price_d);
			}

			//them list moi
			List<PContract_Price> list_price_new  = entity.data.getPcontract_price();
			for(PContract_Price price : list_price_new) {
				price.setId(null);
				price.setPcontract_poid_link(pcontract_poid_link);
				price.setPcontractid_link(pcontractid_link);
				price.setOrgrootid_link(orgrootid_link);
				pcontractpriceService.save(price);
			}
			
			//Update POrders
//			List<POrder> lst_porders = entity.po_orders;
//			String po_code = pcontract_po.getPo_vendor().length() > 0?pcontract_po.getPo_vendor():pcontract_po.getPo_buyer();
//			for(POrder porder : lst_porders) {
//				if (null == porder.getId() || 0 == porder.getId()){
//					//Them moi POrder
//					porder.setPcontractid_link(pcontractid_link);
//					porder.setPcontract_poid_link(pcontract_poid_link);
//					
//					porder.setGolivedate(pcontract_po.getShipdate());
//					porder.setProductiondate(pcontract_po.getProductiondate());
//					
//					porder.setOrgrootid_link(orgrootid_link);
//					porder.setProductid_link(pcontract_po.getProductid_link());
//					porder.setOrderdate(new Date());
//					porder.setUsercreatedid_link(user.getId());
//					porder.setStatus(pcontract_po.getStatus() == POStatus.PO_STATUS_UNCONFIRM?POrderStatus.PORDER_STATUS_UNCONFIRM:POrderStatus.PORDER_STATUS_FREE);
//					porder.setTimecreated(new Date());
//				} 
//				//Save to DB
//				porderService.savePOrder(porder, po_code);
//			}
			
			//Update POrder_Req
			List<POrder> lst_porders = entity.po_orders;
//			String po_code = pcontract_po.getPo_vendor().length() > 0?pcontract_po.getPo_vendor():pcontract_po.getPo_buyer();
			for(POrder porder : lst_porders) {
				if (null == porder.getId() || 0 == porder.getId()){
					//Them moi POrder
					POrder_Req porder_req = new POrder_Req();
					
					porder_req.setPcontractid_link(pcontractid_link);
					porder_req.setPcontract_poid_link(pcontract_poid_link);
					
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setGranttoorgid_link(porder.getGranttoorgid_link());
					
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setProductid_link(pcontract_po.getProductid_link());
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(user.getId());
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				} else {
					POrder_Req porder_req = porder_req_Service.findOne(porder.getId());
					porder_req.setTotalorder(porder.getTotalorder());
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				}
			}
			
			//Response to Client
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
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<PContract_pocreate_response> PContractUpdate(@RequestBody PContract_pocreate_request entity,HttpServletRequest request ) {
		PContract_pocreate_response response = new PContract_pocreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract_PO pcontract_po = entity.data;
			pcontract_po = pcontract_POService.save(pcontract_po);
			
			//Update POrder_Req
			List<POrder> lst_porders = entity.po_orders;
//			String po_code = pcontract_po.getPo_vendor().length() > 0?pcontract_po.getPo_vendor():pcontract_po.getPo_buyer();
			for(POrder porder : lst_porders) {
				if (null == porder.getId() || 0 == porder.getId()){
					//Them moi POrder
					POrder_Req porder_req = new POrder_Req();
					
					porder_req.setPcontractid_link(pcontract_po.getPcontractid_link());
					porder_req.setPcontract_poid_link(pcontract_po.getId());
					
					porder_req.setTotalorder(porder.getTotalorder());
					porder_req.setGranttoorgid_link(porder.getGranttoorgid_link());
					
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setProductid_link(pcontract_po.getProductid_link());
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(usercreatedid_link);
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				} else {
					POrder_Req porder_req = porder_req_Service.findOne(porder.getId());
					porder_req.setTotalorder(porder.getTotalorder());
					//Save to DB
					porder_req_Service.savePOrder_Req(porder_req);
				}
			}
			//Response to Client
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
			
			List<PContract_PO> pcontract = pcontract_POService.getPOByContractProduct(orgrootid_link, entity.pcontractid_link, entity.productid_link, user.getId());
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getleafonly",method = RequestMethod.POST)
	public ResponseEntity<PContract_getbycontractproduct_response> getPOLeafOnly(@RequestBody PContract_getbycontractproduct_request entity,HttpServletRequest request ) {
		PContract_getbycontractproduct_response response = new PContract_getbycontractproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			List<PContract_PO> pcontract = pcontract_POService.getPO_LeafOnly(orgrootid_link, entity.pcontractid_link, entity.productid_link, user.getId());
			response.data = pcontract;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContract_getbycontractproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PContract_getbycontractproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/accept",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Accept(@RequestBody PContractPO_accept_request entity,
			HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			PContract_PO po = pcontract_POService.findOne(entity.pcontract_poid_link);
			po.setOrgmerchandiseid_link(entity.orgid_link);
			po.setMerchandiserid_link(entity.userid_link);
			po.setStatus(POStatus.PO_STATUS_CONFIRMED);
			
			pcontract_POService.save(po);			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<ResponseBase> PContract_PODelete(@RequestBody PContract_podelete_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			PContract_PO thePO = pcontract_POService.findOne(entity.id);
			if (null != thePO){
				for(PContract_Price thePrice: thePO.getPcontract_price()){
					for (PContract_Price_D thePrice_D: thePrice.getPcontract_price_d()){
						pcontractpriceDService.delete(thePrice_D);
					}
					pcontractpriceService.delete(thePrice);
				}
				pcontract_POService.delete(thePO);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	    
	}
}
