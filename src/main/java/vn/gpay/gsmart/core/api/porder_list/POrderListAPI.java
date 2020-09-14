package vn.gpay.gsmart.core.api.porder_list;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import vn.gpay.gsmart.core.security.GpayUser;
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
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long user_orgid_link = user.getOrgid_link();
			Long granttoorgid_link = (long)0;
			if(user_orgid_link == (long)1) granttoorgid_link = null;
			else granttoorgid_link = user_orgid_link;
			
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
							null,
							granttoorgid_link
							);
			}else {
				for(Long num : status) {
					List<POrder> temp = porderService.getPOrderListBySearch(
							entity.style, // style
							entity.buyerid, // buyerid
							entity.vendorid, // vendorid
							entity.orderdatefrom, // orderdatefrom
							entity.orderdateto, // orderdateto
							num,
							granttoorgid_link
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
			
			Comparator<POrder> compareByGrantToOrgName = (POrder p1, POrder p2) -> p1.getGranttoorgname().compareTo( p2.getGranttoorgname());
			Collections.sort(response.data, compareByGrantToOrgName);
			
			response.totalCount = response.data.size();
			
			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) page.getOffset();
			int end = (start + page.getPageSize()) > response.data.size() ? response.data.size() : (start + page.getPageSize());
			Page<POrder> pageToReturn = new PageImpl<POrder>(response.data.subList(start, end), page, response.data.size()); 
			
			response.data = pageToReturn.getContent();
			
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
	
	@RequestMapping(value = "/getallproductskubyporder",method = RequestMethod.POST)
	public ResponseEntity<POrderList_getProductSKUbyPorder_response> getAllProductSKUbyPorder(@RequestBody POrderList_getProductSKUbyPorder_request entity, HttpServletRequest request ) {
		POrderList_getProductSKUbyPorder_response response = new POrderList_getProductSKUbyPorder_response();
		try {
			response.data = new ArrayList<POrder_Product_SKU>();
			List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(entity.porderid);
			
			response.data = porderProductSkus;
			
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
	public ResponseEntity<addskutogrant_response> addSkuToGrant(@RequestBody POrderList_addSkuToGrant_request entity, HttpServletRequest request ) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(entity.idPOrder);
//			List<POrderGrant> listGrant = pordergrantService.getByOrderId(entity.idPOrder);
			
			// save to porder_grant_sku
			for(Long productsku_id : entity.idSkus) {
				POrder_Product_SKU pps = porderskuService.findOne(productsku_id);
				POrderGrant_SKU pgs = null;
				
//				for(POrderGrant pg : listGrant) {
//					pgs = pordergrantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId( pps.getSkuid_link(),  pg.getId());
//					if(pgs == null) continue;
//					if(pgs != null) break;
//				}
				
				pgs = pordergrantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId( pps.getSkuid_link(),  entity.idGrant);
				
				if(pgs == null) {
					pgs = new POrderGrant_SKU();
					pgs.setId(0L);
					pgs.setOrgrootid_link(user.getRootorgid_link());
					pgs.setPordergrantid_link(entity.idGrant);
					pgs.setSkuid_link(pps.getSkuid_link());
					pgs.setGrantamount(pps.getRemainQuantity());
					pordergrantskuService.save(pgs);
				}else {
					System.out.println(pgs.getPordergrantid_link());
					System.out.println(entity.idGrant);
					if(pgs.getPordergrantid_link().equals(entity.idGrant)) {
						pgs.setOrgrootid_link(user.getRootorgid_link());
						pgs.setPordergrantid_link(entity.idGrant);
						pgs.setSkuid_link(pps.getSkuid_link());
						pgs.setGrantamount(pgs.getGrantamount() + pps.getRemainQuantity());
						pordergrantskuService.save(pgs);
					}else {
						pgs = new POrderGrant_SKU();
						pgs.setId(0L);
						pgs.setOrgrootid_link(user.getRootorgid_link());
						pgs.setPordergrantid_link(entity.idGrant);
						pgs.setSkuid_link(pps.getSkuid_link());
						pgs.setGrantamount(pps.getRemainQuantity());
						pordergrantskuService.save(pgs);
					}
				}
				
			}
			// re-calculate porder_grant grant_amount
			List<POrderGrant> pglist = pordergrantService.getByOrderId(entity.idPOrder);
			
			for(POrderGrant pg : pglist) {
				Integer grantamountSum = 0;
				
				List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(pg.getId());
				for(POrderGrant_SKU pgs : pgslist) {
					grantamountSum+=pgs.getGrantamount();
				}
				
				pg.setGrantamount(grantamountSum);
				pordergrantService.save(pg);
			}
			
			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();
			
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);
			
			if(porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#"+ST+"-PO: "+PO+"-"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
			}
			
			response.porderinfo = name;
			response.amount = total;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/removeskufromgrant",method = RequestMethod.POST)
	public ResponseEntity<addskutogrant_response> removeSkuFromGrant(@RequestBody POrderList_addSkuToGrant_request entity, HttpServletRequest request ) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(entity.idPOrder);
			// save to porder_grant_sku
			List<Long> idGrantSkus = entity.idSkus;
			for(Long idGrantSku : idGrantSkus) {
				pordergrantskuService.deleteById(idGrantSku);
			}
			
			// re-calculate porder_grant grant_amount
			List<POrderGrant> pglist = pordergrantService.getByOrderId(entity.idPOrder);
			
			for(POrderGrant pg : pglist) {
				Integer grantamountSum = 0;
				
				List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(pg.getId());
				for(POrderGrant_SKU pgs : pgslist) {
					grantamountSum+=pgs.getGrantamount();
				}
				
				pg.setGrantamount(grantamountSum);
				pordergrantService.save(pg);
			}
			
			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();
			
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);
			
			if(porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#"+ST+"-PO: "+PO+"-"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
			}
			
			response.porderinfo = name;
			response.amount = total;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/savegrantskuonchange",method = RequestMethod.POST)
	public ResponseEntity<addskutogrant_response> saveGrantSkuOnChange(@RequestBody POrderList_saveGrantSkuOnChange_request entity, HttpServletRequest request ) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(entity.idPOrder);
			// save to porder_grant_sku
			POrderGrant_SKU pordergrantsku = entity.data;
			POrderGrant_SKU original = pordergrantskuService.findOne(pordergrantsku.getId());
			List<POrder_Product_SKU> porderproductskus = porderskuService.getby_porderandsku(entity.idPOrder, pordergrantsku.getSkuid_link());
			POrder_Product_SKU porderproductsku = porderproductskus.get(0);
			int remain = porderproductsku.getRemainQuantity();
			
			if(pordergrantsku.getGrantamount() == 0) {
				// delete
				System.out.println(pordergrantsku.getId());
				pordergrantskuService.deleteById(pordergrantsku.getId());
				response.setMessage("Xóa thành công");
			}else {
				if(remain < pordergrantsku.getGrantamount() - original.getGrantamount()) {
					response.setMessage("Vượt quá số lượng chưa vào chuyền");
				}else {
					pordergrantskuService.save(pordergrantsku);
					response.setMessage("Lưu thành công");
				}
			}
			
			// re-calculate porder_grant grant_amount
			List<POrderGrant> pglist = pordergrantService.getByOrderId(entity.idPOrder);
			
			for(POrderGrant pg : pglist) {
				Integer grantamountSum = 0;
				
				List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(pg.getId());
				for(POrderGrant_SKU pgs : pgslist) {
					grantamountSum+=pgs.getGrantamount();
				}
				
				pg.setGrantamount(grantamountSum);
				pordergrantService.save(pg);
			}
			
			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();
			
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);
			
			if(porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#"+ST+"-PO: "+PO+"-"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
			}
			
			response.porderinfo = name;
			response.amount = total;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
