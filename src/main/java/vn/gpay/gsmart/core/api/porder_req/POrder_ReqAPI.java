package vn.gpay.gsmart.core.api.porder_req;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_req")
public class POrder_ReqAPI {
	@Autowired private IPOrder_Req_Service porder_req_Service;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPOrder_Service porderService;
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Req_Service reqService;
	@Autowired IPContractProductSKUService pcontract_ProductSKUService;
	
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
	
	@RequestMapping(value = "/getby_org", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_getbyorg_response> GetByOrg(HttpServletRequest request){
		POrder_Req_getbyorg_response response = new POrder_Req_getbyorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			for(Org theOrg:lsOrgChild){
				List<POrder_Req> a = reqService.get_by_org(theOrg.getId());
				if(a.size()>0)
					response.data.addAll(a);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_getbyorg_response>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_getbyorg_response>(response, HttpStatus.BAD_REQUEST);
		}
		
	}

	@RequestMapping(value = "/gen_porder", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> GenPOrder(HttpServletRequest request,
			@RequestBody POrder_Req_GenPOrder_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder_Req porder_req = porder_req_Service.findOne(entity.porderreqid_link);
			
			//Lay thong tin PO
			PContract_PO thePO = pcontract_POService.findOne(porder_req.getPcontract_poid_link());
			if (thePO.getStatus() == POStatus.PO_STATUS_CONFIRMED){
				String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
				List<PContractProductSKU> po_SKUList = pcontract_ProductSKUService.getbypo_and_product(porder_req.getPcontract_poid_link(), entity.productid_link);				
				//Ma san pham duoc truyen vao qua tham so
				//Neu Sizelist va colorlist deu trang --> Tao lenh sx cho san pham voi toan bo SKU cua san pham trong PO
				if (entity.size_list.length() == 0 && entity.color_list.length() == 0){
					POrder thePOrder = porderService.get_oneby_po_org_product(orgrootid_link, porder_req.getGranttoorgid_link(), thePO.getId(), entity.productid_link);
					List<POrder_Product_SKU> porder_SKU = new ArrayList<POrder_Product_SKU>();
					Integer totalorder = 0;
					for(PContractProductSKU theSKU:po_SKUList){
						POrder_Product_SKU theporder_SKU = new POrder_Product_SKU();
						
						theporder_SKU.setOrgrootid_link(orgrootid_link);
						theporder_SKU.setProductid_link(theSKU.getProductid_link());
						theporder_SKU.setSkuid_link(theSKU.getSkuid_link());
						theporder_SKU.setPquantity_porder(theSKU.getPquantity_porder());
						theporder_SKU.setPquantity_production(theSKU.getPquantity_production());
						theporder_SKU.setPquantity_sample(theSKU.getPquantity_sample());
						theporder_SKU.setPquantity_total(theSKU.getPquantity_total());
						
						totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
						
						porder_SKU.add(theporder_SKU);
					}
					if (null != thePOrder){
						thePOrder.setTotalorder(totalorder);
						thePOrder.setPorder_product_sku(porder_SKU);
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
						porder.setProductid_link(entity.productid_link);
						
						porder.setTotalorder(totalorder);
						
						porder.setOrgrootid_link(orgrootid_link);
						porder.setOrderdate(new Date());
						porder.setUsercreatedid_link(user.getId());
						porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
						porder.setTimecreated(new Date());	
						
						porder.setPorder_product_sku(porder_SKU);
						
						porderService.savePOrder(porder, po_code);
					}				
				} else {
					
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
