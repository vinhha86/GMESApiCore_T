package vn.gpay.gsmart.core.api.porder_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderlist")
public class POrderListAPI {
	@Autowired private IPOrder_Service porderService;
	@Autowired private IPContractService pcontractService;
	@Autowired private IPOrderGrant_Service pordergrantService;
	@Autowired private IPOrderGrant_SKUService pordergrantskuService;
	@Autowired private IPOrder_Product_SKU_Service porderskuService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> POrderGetAll(HttpServletRequest request ) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {
			
			response.data = porderService.findAll();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallbysearch",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> POrderGetAllBySearch(@RequestBody POrderList_getlist_request entity, HttpServletRequest request ) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {
			List<Long> status = entity.status;
			response.data = new ArrayList<>();
			List<POrder> result = new ArrayList<>();
			
			if(status.size() == 0) {
				result = porderService.getPOrderListBySearch(
							entity.style, // style
							entity.buyerid, // buyerid
							entity.vendorid, // vendorid
							entity.orderdatefrom, // orderdatefrom
							entity.orderdateto, // orderdateto
							null
							);
			}else {
				for(Long num : status) {
					List<POrder> temp = porderService.getPOrderListBySearch(
							entity.style, // style
							entity.buyerid, // buyerid
							entity.vendorid, // vendorid
							entity.orderdatefrom, // orderdatefrom
							entity.orderdateto, // orderdateto
							num
							);
					result.addAll(temp);
				}
			}
			if(entity.pobuyer == null) entity.pobuyer="";
			if(entity.povendor == null) entity.povendor="";
			if(entity.style == null) entity.style="";
			
			for(POrder porder : result) {
				String po_buyer = porder.getPo_buyer().toLowerCase();
				String po_buyer_req = entity.pobuyer.toLowerCase();
				String po_vendor = porder.getPo_vendor().toLowerCase();
				String po_vendor_req = entity.povendor.toLowerCase();
				String stylebuyer = porder.getStylebuyer().toLowerCase();
				String style = entity.style.toLowerCase();
				if(!po_buyer.contains(po_buyer_req)) {
					continue;
				}
				if(!po_vendor.contains(po_vendor_req)) {
					continue;
				}
				if(!stylebuyer.contains(style)) {
					continue;
				}
				response.data.add(porder);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallbuyername",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllBuyer(HttpServletRequest request ) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {
			
			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> buyers = new ArrayList<HashMap<String, Object>>();
			List<String> stringBuyers = new ArrayList<String>();
			
			for(POrder porder : allPOrder) {
				if(!stringBuyers.contains(porder.getBuyername())) {
					HashMap<String, Object> temp = new HashMap<>();
					stringBuyers.add(porder.getBuyername());
					temp.put("buyername", porder.getBuyername());
					
					PContract pcontract = pcontractService.findOne(porder.getPcontractid_link());
					Long orgbuyerid_link = pcontract.getOrgbuyerid_link();
					temp.put("orgbuyerid_link", orgbuyerid_link);
					buyers.add(temp);
				}
			}
			
			response.data = buyers;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getListString_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getallvendorname",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllVendor(HttpServletRequest request ) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {
			
			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> vendors = new ArrayList<HashMap<String, Object>>();
			List<String> stringVendors = new ArrayList<String>();
			
			for(POrder porder : allPOrder) {
				if(!stringVendors.contains(porder.getVendorname())) {
					HashMap<String, Object> temp = new HashMap<>();
					stringVendors.add(porder.getVendorname());
					temp.put("vendorname", porder.getVendorname());
					
					PContract pcontract = pcontractService.findOne(porder.getPcontractid_link());
					Long orgvendorid_link = pcontract.getOrgvendorid_link();
					temp.put("orgvendorid_link", orgvendorid_link);
					vendors.add(temp);
				}
			}
			
			response.data = vendors;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getListString_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getgrantbyporderid",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getGrantByPorderId_response> getGrantByPorderId(@RequestBody POrderList_getGrantByPorderId_request entity, HttpServletRequest request ) {
		POrderList_getGrantByPorderId_response response = new POrderList_getGrantByPorderId_response();
		try {
			response.data = pordergrantService.getByOrderId(entity.porderid);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getGrantByPorderId_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getGrantByPorderId_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getgrantskubygrantid",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response> getGrantSKUByGrantId(@RequestBody POrderList_getPOrderGrantSKUbyGrantId_request entity, HttpServletRequest request ) {
		POrderList_getPOrderGrantSKUbyGrantId_response response = new POrderList_getPOrderGrantSKUbyGrantId_response();
		try {
			response.data = pordergrantskuService.getPOrderGrant_SKU(entity.pordergrantid);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getproductskubyporder",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getProductSKUbyPorder_response> getProductSKUbyPorder(@RequestBody POrderList_getProductSKUbyPorder_request entity, HttpServletRequest request ) {
		POrderList_getProductSKUbyPorder_response response = new POrderList_getProductSKUbyPorder_response();
		try {
			response.data = new ArrayList<POrder_Product_SKU>();
			List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(entity.porderid);
			List<POrderGrant_SKU> porderGrantSkus = pordergrantskuService.getPOrderGrant_SKU(entity.grantid);
			
			for(POrder_Product_SKU pps : porderProductSkus) {
				boolean flag = true;
				for(POrderGrant_SKU pgs : porderGrantSkus) {
					if(pps.getSkuid_link().equals(pgs.getSkuid_link())) {
						flag = false;
						if(flag == false) break;
					}
				}
				if(flag) response.data.add(pps);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/addskutogrant",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> addSkuToGrant(@RequestBody POrderList_addSkuToGrant_request entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
//			System.out.println(entity.idSkus);
//			System.out.println(entity.idGrant);
			
			// save to porder_grant_sku
			for(Long productsku_id : entity.idSkus) {
				POrder_Product_SKU pps = porderskuService.findOne(productsku_id);
				POrderGrant_SKU pgs = pordergrantskuService.getPOrderGrant_SKUbySKUid_link(pps.getSkuid_link());
				pgs.setPordergrantid_link(entity.idGrant);
				pordergrantskuService.save(pgs);
			}
			
			// re-calculate porder_grant grant_amount
			List<POrderGrant> pglist = pordergrantService.getByOrderId(entity.idPOrder);
			
			for(POrderGrant pg : pglist) {
				Integer grandamounntSum = 0;
				
				List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(pg.getId());
				for(POrderGrant_SKU pgs : pgslist) {
					grandamounntSum+=pgs.getGrantamount();
				}
				
				pg.setGrantamount(grandamounntSum);
				pordergrantService.save(pg);
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
}
