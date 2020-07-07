package vn.gpay.gsmart.core.api.porder_req;

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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ProductType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_req")
public class POrder_ReqAPI {
	@Autowired private IPOrder_Req_Service porder_req_Service;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPOrder_Service porderService;
    ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_GetOne_Response> GetOne(@RequestBody POrder_Req_GetOne_Request entity,HttpServletRequest request ) {
		POrder_Req_GetOne_Response response = new POrder_Req_GetOne_Response();
		try {
			
			response.data = porder_req_Service.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_GetOne_Response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrder_Req_GetOne_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}    
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_Create_Response> Create(HttpServletRequest request,
			@RequestBody POrder_Req_Create_Request entity) {
		POrder_Req_Create_Response response = new POrder_Req_Create_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder_Req porder_req = entity.data;
			
			//Lay thong tin PO
//			PContract_PO thePO = pcontract_POService.findOne(porder.getPcontract_poid_link());
//			String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
			
			if (porder_req.getId() == null || porder_req.getId() == 0) {
				porder_req.setOrgrootid_link(orgrootid_link);
				porder_req.setOrderdate(new Date());
				porder_req.setUsercreatedid_link(user.getId());
				porder_req.setStatus(POrderReqStatus.STATUS_FREE);
				porder_req.setTimecreated(new Date());
			} 
			
			response.id = porder_req_Service.savePOrder_Req(porder_req);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_Create_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_Create_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/get_bypo", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_GetByPO_Response> GetByPO(HttpServletRequest request,
			@RequestBody POrder_Req_GetByPO_Request entity) {
		POrder_Req_GetByPO_Response response = new POrder_Req_GetByPO_Response();
		try {
			response.data = porder_req_Service.getByPO(entity.pcontract_poid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_GetByPO_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_GetByPO_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gen_porder", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> GenPOrder(HttpServletRequest request,
			@RequestBody POrder_Req_Create_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder_Req porder_req = entity.data;
			
			//Lay thong tin PO
			PContract_PO thePO = pcontract_POService.findOne(porder_req.getPcontract_poid_link());
			if (thePO.getStatus() == POStatus.PO_STATUS_CONFIRMED){
				String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
				List<PContract_Price> thePriceList;
				if (null == thePO.getParentpoid_link()){
					thePriceList = thePO.getPcontract_price();
				} else {
					PContract_PO thePO_parent = pcontract_POService.findOne(thePO.getParentpoid_link());
					thePriceList = thePO_parent.getPcontract_price();
				}
				for (PContract_Price thePrice: thePriceList){
					if (thePrice.getProducttypeid_link() != ProductType.SKU_TYPE_PRODUCT_PAIR){
						
						POrder thePOrder = porderService.get_oneby_po_price(orgrootid_link, porder_req.getGranttoorgid_link(), thePO.getId(), thePrice.getProductid_link(), thePrice.getSizesetid_link());
						
						if (null != thePOrder){
							thePOrder.setTotalorder(thePrice.getQuantity());
							porderService.savePOrder(thePOrder, po_code);
						} else {
							POrder porder = new POrder();
							
							porder.setPcontractid_link(thePO.getPcontractid_link());
							porder.setPcontract_poid_link(thePO.getId());
							porder.setOrdercode(po_code);
							porder.setPorderreqid_link(porder_req.getId());
							
							porder.setGolivedate(thePO.getShipdate());
							porder.setFinishdate_plan(thePO.getShipdate());
							porder.setProductiondate(thePO.getProductiondate());
							porder.setProductiondate_plan(thePO.getProductiondate());
							
							porder.setGranttoorgid_link(porder_req.getGranttoorgid_link());
							porder.setProductid_link(thePrice.getProductid_link());
							porder.setSizesetid_link(thePrice.getSizesetid_link());
							porder.setTotalorder(thePrice.getQuantity());
							
							porder.setOrgrootid_link(orgrootid_link);
							porder.setOrderdate(new Date());
							porder.setUsercreatedid_link(user.getId());
							porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
							porder.setTimecreated(new Date());	
							
							porderService.savePOrder(porder, po_code);
						}
					}
				}
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Đơn hàng phải được xác nhận trước khi sinh lệnh");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
