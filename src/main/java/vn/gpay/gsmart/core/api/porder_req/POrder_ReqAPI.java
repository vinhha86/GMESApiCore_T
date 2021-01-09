package vn.gpay.gsmart.core.api.porder_req;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContractPO_Product;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_flow.Task_Flow;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.TaskObjectType_Name;

@RestController
@RequestMapping("/api/v1/porder_req")
public class POrder_ReqAPI {
	@Autowired private IPOrder_Req_Service porder_req_Service;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPOrderGrant_Service porderGrantService;
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Req_Service reqService;
	@Autowired IPContractProductSKUService pcontract_ProductSKUService;
	@Autowired IPOrder_Product_SKU_Service porder_ProductSKUService;
	@Autowired ITask_Object_Service taskobjectService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired ITask_Service taskService;
	@Autowired ITask_Flow_Service commentService;
	@Autowired Common commonService;
	@Autowired IProductPairingService pairService;
	@Autowired IProductService productService;
	@Autowired IGpayUserOrgService userOrgService; 
	
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
	
	@RequestMapping(value = "/getby_offer_product",method = RequestMethod.POST)
	public ResponseEntity<getby_pcontractpo_offer_and_product_response> GetByOfferAndProduct(@RequestBody getby_pcontractpo_offer_and_product_request entity,HttpServletRequest request ) {
		getby_pcontractpo_offer_and_product_response response = new getby_pcontractpo_offer_and_product_response();
		try {
			long pcontractpo_id_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
			
			response.data = porder_req_Service.getbyOffer_and_Product(pcontractpo_id_link, productid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getby_pcontractpo_offer_and_product_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<getby_pcontractpo_offer_and_product_response>(response, HttpStatus.BAD_REQUEST);
		}
	}  
	
	@RequestMapping(value = "/update_iscalculate",method = RequestMethod.POST)
	public ResponseEntity<update_iscalculate_response > UpdateCalculate(@RequestBody update_iscalculate_request entity,HttpServletRequest request ) {
		update_iscalculate_response response = new update_iscalculate_response();
		try {
			POrder_Req req = porder_req_Service.findOne(entity.porder_reqid_link); 
			req.setIs_calculate(entity.check);
			porder_req_Service.save(req);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_iscalculate_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<update_iscalculate_response>(response, HttpStatus.BAD_REQUEST);
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
	
	@RequestMapping(value = "/create_from_po", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateFromPO(HttpServletRequest request,
			@RequestBody create_from_pcontractpo_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontractid_link = entity.data.getPcontractid_link();
			Long productid_link = entity.data.getProductid_link();
			
			Product product = productService.findOne(productid_link);
			
			if(product.getProducttypeid_link() == 5) {
				List<ProductPairing> list_pair = pairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, productid_link);
				for (ProductPairing productPairing : list_pair) {
					POrder_Req porder_req = new POrder_Req();
					porder_req.setGranttoorgid_link(entity.data.getGranttoorgid_link());
					porder_req.setPcontract_poid_link(entity.data.getPcontract_poid_link());
					porder_req.setPcontractid_link(entity.data.getPcontractid_link());
					porder_req.setIs_calculate(false);
					
					porder_req.setId(null);
					porder_req.setTotalorder(entity.data.getTotalorder()*productPairing.getAmount());
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(user.getId());
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
					porder_req.setProductid_link(productPairing.getProductid_link());
					porder_req.setAmount_inset(productPairing.getAmount());
					porder_req_Service.save(porder_req);
				}
			}
			else {
				POrder_Req porder_req = entity.data;
				
				if (porder_req.getId() == null || porder_req.getId() == 0) {
					porder_req.setOrgrootid_link(orgrootid_link);
					porder_req.setOrderdate(new Date());
					porder_req.setUsercreatedid_link(user.getId());
					porder_req.setStatus(POrderReqStatus.STATUS_FREE);
					porder_req.setTimecreated(new Date());
				}
				porder_req.setAmount_inset(1);
				porder_req_Service.save(porder_req);
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/get_bypo", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_GetByPO_Response> GetByPO(HttpServletRequest request,
			@RequestBody POrder_Req_GetByPO_Request entity) {
		POrder_Req_GetByPO_Response response = new POrder_Req_GetByPO_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
			
			List<Long> orgs = new ArrayList<Long>();
			if(orgid_link != 0 && orgid_link != 1) {
				for(GpayUserOrg userorg:userOrgService.getall_byuser(user.getId())){
					orgs.add(userorg.getOrgid_link());
				}
				//Them chinh don vi cua user
				orgs.add(orgid_link);
				
				response.data = new ArrayList<POrder_Req>();
				for(Long orgid : orgs) {
					response.data.addAll(porder_req_Service.getByPO_and_org(entity.pcontract_poid_link, orgid));
				}
			}
			else
				response.data = porder_req_Service.getByPO_and_org(entity.pcontract_poid_link, orgid_link);
			
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
//			long orgid_link = 2;
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			
			if(orgid_link == 1) {
				for(Org theOrg:lsOrgChild){
					List<POrder_Req> a = reqService.get_by_org(theOrg.getId());
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						if(pr.getPorderlist().size() == 0)
							result.add(pr);
					}
					
	//				if(a.size()>0)
	//					response.data.addAll(a);
					if(result.size()>0)
						response.data.addAll(result);
				}
			}else {
				//Lay danh sách org user dang quan ly
				List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
				List<Long> list_org = new ArrayList<Long>();
				
				for (GpayUserOrg userorg : list_user_org) {
					list_org.add(userorg.getOrgid_link());
				}
				if(!list_org.contains(orgid_link))
					list_org.add(orgid_link);
				
				for (Long orgid : list_org) {
					List<POrder_Req> a = reqService.get_by_org(orgid);
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						if(pr.getPorderlist().size() == 0)
							result.add(pr);
					}
					
					if(result.size()>0)
						response.data.addAll(result);
				}
				
				
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
	
	@RequestMapping(value = "/getpoline_product_by_org", method = RequestMethod.POST)
	public ResponseEntity<get_poline_product_byorg_response> GetPOline_Product_ByOrg(HttpServletRequest request){
		get_poline_product_byorg_response response = new get_poline_product_byorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
			
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			List<POrder_Req> list_req = new ArrayList<POrder_Req>();
			
			if(orgid_link == 1) {
				for(Org theOrg:lsOrgChild){
					List<POrder_Req> a = reqService.get_by_org(theOrg.getId());
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						if(pr.getPorderlist().size() == 0)
							result.add(pr);
					}
					
	//				if(a.size()>0)
	//					response.data.addAll(a);
					if(result.size()>0)
						list_req.addAll(result);
				}
			}else {
				//Lay danh sách org user dang quan ly
				List<GpayUserOrg> list_user_org = userOrgService.getall_byuser(user.getId());
				List<Long> list_org = new ArrayList<Long>();
				
				for (GpayUserOrg userorg : list_user_org) {
					list_org.add(userorg.getOrgid_link());
				}
				if(!list_org.contains(orgid_link))
					list_org.add(orgid_link);
				
				for (Long orgid : list_org) {
					List<POrder_Req> a = reqService.get_by_org(orgid);
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						if(pr.getPorderlist().size() == 0)
							result.add(pr);
					}
					
					if(result.size()>0)
						list_req.addAll(result);
				}
			}
			Long pcontractpoid_link = null;
			Map<Long, Date> map = new HashedMap<Long, Date>();
			
			//lay ra nhung line giao hang som nhat 
			for(POrder_Req req : list_req) {
				if(map.size() == 0) {
					if(req.getPO_Offer()!=null) {
						pcontractpoid_link = req.getPO_Offer();
						map.put(pcontractpoid_link, req.getShipdate());
					}
				}
				else {
					if(req.getPO_Offer() != null) {
						if(req.getPO_Offer().equals(pcontractpoid_link)) {
							if(req.getShipdate().before(map.get(pcontractpoid_link))) {
								map.remove(pcontractpoid_link);
								map.put(pcontractpoid_link, req.getShipdate());
							}
						}
						else {
							map.put(req.getPO_Offer(), req.getShipdate());
						}
					}
				}
			}
			
			//remove nhung line giao hang khong hop le
			list_req.removeIf(c-> !map.containsKey(c.getPO_Offer()) || !map.get(c.getPO_Offer()).equals(c.getShipdate()));
			
			List<PContractPO_Product> listret = new ArrayList<PContractPO_Product>();
			for(POrder_Req req : list_req) {
				PContractPO_Product po_product = new PContractPO_Product();
				po_product.setBuyername(req.getBuyername());
				po_product.setGranttoorgid_link(req.getGranttoorgid_link());
				po_product.setPcontract_poid_link(req.getPO_Offer());
				po_product.setProductid_link(req.getProductid_link());
				po_product.setShipdate(req.getShipdate());
				po_product.setVendorname(req.getVendorname());
				po_product.setProduct_buyername(req.getProduct_code());
				po_product.setPo_buyer(req.getPo_buyer());
				po_product.setQuantity(req.getPo_parent_quantity());
				po_product.setOrgname(req.getGranttoorgcode());
				
				listret.add(po_product);
			}
			
			response.data = listret;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_poline_product_byorg_response>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_poline_product_byorg_response>(response, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/get_req_granted", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_getbyorg_response> GetReqGranted(HttpServletRequest request){
		POrder_Req_getbyorg_response response = new POrder_Req_getbyorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
//			long orgid_link = 2;
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			
			if(orgid_link == 1) {
				for(Org theOrg:lsOrgChild){
					List<POrder_Req> a = reqService.get_req_granted(theOrg.getId());
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						if(pr.getPorderlist().size() > 0)
							result.add(pr);
					}
					
					if(result.size()>0)
						response.data.addAll(result);
				}
			}else {
				List<POrder_Req> a = reqService.get_req_granted(orgid_link);
				
				List<POrder_Req> result = new ArrayList<POrder_Req>();
				for(POrder_Req pr : a) {
					if(pr.getPorderlist().size() > 0)
						result.add(pr);
				}
				
				if(result.size()>0)
					response.data.addAll(result);
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
	
	@RequestMapping(value = "/deleteReqGranted", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deleteReqGranted(HttpServletRequest request,
			@RequestBody POrder_Req_Granted_delete_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			for(POrder_Req porderReq : entity.data) {
				POrder_Req temp = porder_req_Service.findOne(porderReq.getId());
				temp.setStatus(0);
				
				List<POrder> porderList = porderService.getByPOrder_Req(temp.getPcontract_poid_link(), temp.getId());
				for(POrder porder : porderList) {
					List<POrderGrant> porderGrantList = porderGrantService.getByOrderId(porder.getId());
					for(POrderGrant porderGrant : porderGrantList) {
						porderGrantService.deleteById(porderGrant.getId());
					}
					porderService.deleteById(porder.getId());
				}
				
				if(entity.isDeleteReq) {
					porder_req_Service.deleteById(temp.getId());
				}else {
					porder_req_Service.save(temp);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
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
				List<PContractProductSKU> po_SKUList = pcontract_ProductSKUService.getbypo_and_product_free(entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), entity.productid_link);				
				POrder thePOrder = porderService.get_oneby_po_org_product(orgrootid_link, porder_req.getGranttoorgid_link(), thePO.getId(), entity.productid_link);
				List<POrder_Product_SKU> porder_SKU = new ArrayList<POrder_Product_SKU>();
				Integer totalorder = 0;
				
				List<String> size_list = null;
				List<String> color_list = null;
				if (entity.size_list.length() > 0){
					String[] size_arr = entity.size_list.split(";");
					size_list = Arrays.asList(size_arr);
				}
				if (entity.color_list.length() > 0){
					String[] color_arr = entity.color_list.split(";");
					color_list = Arrays.asList(color_arr);
				}
				//Tao danh sach SKU, loc theo size_list va color_list
				for(PContractProductSKU theSKU:po_SKUList){
					if (null != size_list){
						if (!size_list.contains(theSKU.getSizeid_link().toString())){
							continue;
						}
					}
					if (null != color_list){
						if (!color_list.contains(theSKU.getColor_id().toString())){
							continue;
						}
					}
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
				
				long porderid_link = 0;
				if (porder_SKU.size() > 0){
					if (null != thePOrder){
						porderid_link = thePOrder.getId();
						//Xoa product SKU
						for(POrder_Product_SKU thePOrderSKU: thePOrder.getPorder_product_sku()){
							porder_ProductSKUService.delete(thePOrderSKU);
						}
						if(thePOrder.getStatus() == POrderStatus.PORDER_STATUS_UNCONFIRM) {
							thePOrder.setStatus(POrderStatus.PORDER_STATUS_FREE);
							
							//tao viec trong taskboard
							long pcontractid_link = thePO.getPcontractid_link();
							long productid_link = entity.productid_link;
							long porder_req_id_link = entity.porderreqid_link;
							
							List<Long> list_task = taskobjectService.getby_pcontract_and_product(pcontractid_link, productid_link, porder_req_id_link);
							for(Long taskid_link : list_task) {
								//Lay checklist cua task
								long tasktype_checklits_id_link = 10;
								List<Task_CheckList> list_sub = checklistService.getby_taskid_link_and_typechecklist(taskid_link, tasktype_checklits_id_link);
								
								if(list_sub.size() > 0 ) {
									Task task = taskService.findOne(taskid_link);
									task.setDatefinished(new Date());
									task.setStatusid_link(2);
									taskService.save(task);
									
									Task_Flow flow = new Task_Flow();
									flow.setDatecreated(new Date());
									flow.setDescription("Đã tạo lệnh SX");
									flow.setFlowstatusid_link(3);
									flow.setFromuserid_link(user.getId());
									flow.setId(null);
									flow.setOrgrootid_link(orgrootid_link);
									flow.setTaskid_link(taskid_link);
									flow.setTaskstatusid_link(2);
									flow.setTouserid_link(task.getUsercreatedid_link());
									commentService.save(flow);
								}
								
								for(Task_CheckList checklist : list_sub) {
									checklist.setDone(true);
									checklistService.save(checklist);
								}
							}
							
							//Tao viec moi 
							long orgid_link = porder_req.getGranttoorgid_link();
							long userid_link = user.getId();
							Long userinchargeid_link = null;
							List<Task_Object> list_object = new ArrayList<Task_Object>();
							
							Task_Object obj_pcontract = new Task_Object();
							obj_pcontract.setId(null);
							obj_pcontract.setObjectid_link(thePO.getPcontractid_link());
							obj_pcontract.setOrgrootid_link(orgrootid_link);
							obj_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
							list_object.add(obj_pcontract);
							
							Task_Object obj_product = new Task_Object();
							obj_product.setId(null);
							obj_product.setObjectid_link(entity.productid_link);
							obj_product.setOrgrootid_link(orgrootid_link);
							obj_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
							list_object.add(obj_product);
							
							Task_Object obj_req = new Task_Object();
							obj_req.setId(null);
							obj_req.setObjectid_link(entity.porderreqid_link);
							obj_req.setOrgrootid_link(orgrootid_link);
							obj_req.setTaskobjecttypeid_link((long)TaskObjectType_Name.YeuCauSanXuat);
							list_object.add(obj_req);
							
							Task_Object obj_porder = new Task_Object();
							obj_porder.setId(null);
							obj_porder.setObjectid_link(porderid_link);
							obj_porder.setOrgrootid_link(orgrootid_link);
							obj_porder.setTaskobjecttypeid_link((long)TaskObjectType_Name.LenhSanXuat);
							list_object.add(obj_porder);
							
							Task_Object obj_po = new Task_Object();
							obj_po.setId(null);
							obj_po.setObjectid_link(thePO.getId());
							obj_po.setOrgrootid_link(orgrootid_link);
							obj_po.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
							list_object.add(obj_po);

							long tasktypeid_link_phanchuyen = 5; //type phan chuyen
							commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_phanchuyen, list_object, userinchargeid_link);
							
							long tasktypeid_link_dmsx = 6; //type phan chuyen
							commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_dmsx, list_object, userinchargeid_link);
							
						}
						
						thePOrder.setTotalorder(totalorder);
						thePOrder.getPorder_product_sku().clear();
						for(POrder_Product_SKU theSKU: porder_SKU){
							thePOrder.getPorder_product_sku().add(theSKU);
						}

						porderService.savePOrder(thePOrder, po_code);
						
						porder_req.setTotalorder(totalorder);
						porder_req_Service.save(porder_req);
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
						
						porderid_link = porderService.savePOrder(porder, po_code).getId();
						
						porder_req.setTotalorder(totalorder);
						porder_req_Service.save(porder_req);
						
						//Tao viec trong taskboard
						long pcontractid_link = thePO.getPcontractid_link();
						long productid_link = entity.productid_link;
						long porder_req_id_link = entity.porderreqid_link;
						
						List<Long> list_task = taskobjectService.getby_pcontract_and_product(pcontractid_link, productid_link, porder_req_id_link);
						for(Long taskid_link : list_task) {
							//Lay checklist cua task
							long tasktype_checklits_id_link = 10;
							List<Task_CheckList> list_sub = checklistService.getby_taskid_link_and_typechecklist(taskid_link, tasktype_checklits_id_link);
							
							if(list_sub.size() > 0 ) {
								Task task = taskService.findOne(taskid_link);
								task.setDatefinished(new Date());
								task.setStatusid_link(2);
								taskService.save(task);
								
								Task_Flow flow = new Task_Flow();
								flow.setDatecreated(new Date());
								flow.setDescription("Đã tạo lệnh SX");
								flow.setFlowstatusid_link(3);
								flow.setFromuserid_link(user.getId());
								flow.setId(null);
								flow.setOrgrootid_link(orgrootid_link);
								flow.setTaskid_link(taskid_link);
								flow.setTaskstatusid_link(2);
								flow.setTouserid_link(task.getUsercreatedid_link());
								commentService.save(flow);
							}
							
							for(Task_CheckList checklist : list_sub) {
								checklist.setDone(true);
								checklistService.save(checklist);
							}
						}
						
						//Tao viec moi 
						long orgid_link = porder_req.getGranttoorgid_link();
						long userid_link = user.getId();
						Long userinchargeid_link = null;
						List<Task_Object> list_object = new ArrayList<Task_Object>();
						
						Task_Object obj_pcontract = new Task_Object();
						obj_pcontract.setId(null);
						obj_pcontract.setObjectid_link(thePO.getPcontractid_link());
						obj_pcontract.setOrgrootid_link(orgrootid_link);
						obj_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
						list_object.add(obj_pcontract);
						
						Task_Object obj_product = new Task_Object();
						obj_product.setId(null);
						obj_product.setObjectid_link(entity.productid_link);
						obj_product.setOrgrootid_link(orgrootid_link);
						obj_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
						list_object.add(obj_product);
						
						Task_Object obj_req = new Task_Object();
						obj_req.setId(null);
						obj_req.setObjectid_link(entity.porderreqid_link);
						obj_req.setOrgrootid_link(orgrootid_link);
						obj_req.setTaskobjecttypeid_link((long)TaskObjectType_Name.YeuCauSanXuat);
						list_object.add(obj_req);
						
						Task_Object obj_porder = new Task_Object();
						obj_porder.setId(null);
						obj_porder.setObjectid_link(porderid_link);
						obj_porder.setOrgrootid_link(orgrootid_link);
						obj_porder.setTaskobjecttypeid_link((long)TaskObjectType_Name.LenhSanXuat);
						list_object.add(obj_porder);
						
						Task_Object obj_po = new Task_Object();
						obj_po.setId(null);
						obj_po.setObjectid_link(thePO.getId());
						obj_po.setOrgrootid_link(orgrootid_link);
						obj_po.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
						list_object.add(obj_po);

						long tasktypeid_link_phanchuyen = 5; //type phan chuyen
						commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_phanchuyen, list_object, userinchargeid_link);
						
						long tasktypeid_link_dmsx = 6; //type phan chuyen
						commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_dmsx, list_object, userinchargeid_link);
						
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
	
	@RequestMapping(value = "/get_total_select", method = RequestMethod.POST)
	public ResponseEntity<get_totalselect_response> GetTotalSelect(HttpServletRequest request,
			@RequestBody get_totalselect_request entity) {
		get_totalselect_response response = new get_totalselect_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			POrder_Req porder_req = porder_req_Service.findOne(entity.porderreqid_link);

			//Kiem tra san pham co phai la bo hay khong
			long pcontractid_link = porder_req.getPcontractid_link();
			List<ProductPairing> list_pair = pairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, entity.productid_link);
			
			List<PContractProductSKU> po_SKUList = new ArrayList<PContractProductSKU>();
			Integer totalorder = 0;
			List<String> size_list = null;
			List<String> color_list = null;
			
			if(list_pair.size() == 0) {
				po_SKUList = (pcontract_ProductSKUService.getbypo_and_product_free(
						entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), entity.productid_link));
				
				if (entity.size_list.length() > 0){
					String[] size_arr = entity.size_list.split(";");
					size_list = Arrays.asList(size_arr);
				}
				if (entity.color_list.length() > 0){
					String[] color_arr = entity.color_list.split(";");
					color_list = Arrays.asList(color_arr);
				}
				//Tao danh sach SKU, loc theo size_list va color_list
				for(PContractProductSKU theSKU:po_SKUList){
					if (null != size_list){
						if (!size_list.contains(theSKU.getSizeid_link().toString())){
							continue;
						}
					}
					if (null != color_list){
						if (!color_list.contains(theSKU.getColor_id().toString())){
							continue;
						}
					}
					
					totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
				}
			}
			else {
				for(ProductPairing pair : list_pair) {
					po_SKUList = (pcontract_ProductSKUService.getbypo_and_product_free(
							entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), pair.getProductid_link()));
					
					if (entity.size_list.length() > 0){
						String[] size_arr = entity.size_list.split(";");
						size_list = Arrays.asList(size_arr);
					}
					if (entity.color_list.length() > 0){
						String[] color_arr = entity.color_list.split(";");
						color_list = Arrays.asList(color_arr);
					}
					//Tao danh sach SKU, loc theo size_list va color_list
					for(PContractProductSKU theSKU:po_SKUList){
						if (null != size_list){
							if (!size_list.contains(theSKU.getSizeid_link().toString())){
								continue;
							}
						}
						if (null != color_list){
							if (!color_list.contains(theSKU.getColor_id().toString())){
								continue;
							}
						}
						
						totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
					}
				}
			}
						
			response.total = commonService.FormatNumber(totalorder)+" / "+commonService.FormatNumber(porder_req.getTotalorder());
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_totalselect_response>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_totalselect_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<ResponseBase> delete(HttpServletRequest request,
			@RequestBody POrder_Req_GetOne_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			 POrder_Req thePOrder_Req = porder_req_Service.findOne(entity.id);
			 if (null != thePOrder_Req){
				//Kiem tra xem da co lenh sx dc tao chua, neu co roi --> Bao loi
				if(thePOrder_Req.getPorderlist_running().size() > 0){
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_POREQ_DELETE_PORDEREXISTED));
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);					
				} else {
					//Xoa cac lenh sx tuong ung
					for (POrder thePOrder: thePOrder_Req.getPorderlist()){
						//Xoa cac dong sku tuong ung voi lenh
						for (POrder_Product_SKU theProductSKU:thePOrder.getPorder_product_sku()){
							porder_ProductSKUService.delete(theProductSKU);
						}
						
						//Xoa cac Porder_grant va sku tuong ung voi lenh
						for (POrderGrant thePorder_grant: thePOrder.getList_pordergrant()){
							porderGrantService.deleteAll(thePorder_grant);
						}
						
						//Xoa lenh
						porderService.delete(thePOrder);
					}
					
					//Xoa Porder_req
					porder_req_Service.delete(thePOrder_Req);
					
					//Update lai SL cho các POrder_Req con lai
					long pcontractpo_id_link = thePOrder_Req.getPcontract_poid_link();
					PContract_PO po = pcontract_POService.findOne(pcontractpo_id_link);
					List<POrder_Req> list_req = porder_req_Service.getByPO_is_calculate(pcontractpo_id_link);
					//Loai porder_req dang xoa (chua xoa do dang trong transaction
					list_req.removeIf(c->c.getId().equals(entity.id));
					if(list_req.size() > 0) {
						int amount = po.getPo_quantity()/list_req.size();
						for(POrder_Req req : list_req) {
							req.setTotalorder(amount);
							porder_req_Service.save(req);
						}
						POrder_Req req = list_req.get(list_req.size()-1);
						req.setTotalorder(po.getPo_quantity() - amount* (list_req.size()-1));
						porder_req_Service.save(req);
					}
					
	
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
}
