package vn.gpay.gsmart.core.api.porder_list;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrderOrigin;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderlist")
public class POrderListAPI {
	@Autowired
	private IPOrder_Service porderService;
	@Autowired
	private IPContractService pcontractService;
	@Autowired
	private IPOrderGrant_Service pordergrantService;
	@Autowired
	private IPOrderGrant_SKUService pordergrantskuService;
	@Autowired
	private IPOrder_Product_SKU_Service porderskuService;
	@Autowired
	private Common commonService;

	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> POrderGetAll(HttpServletRequest request) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {

			response.data = porderService.findAll();

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

//	@RequestMapping(value = "/getallbysearch",method = RequestMethod.POST)
//	public ResponseEntity<POrderList_getlist_response> POrderGetAllBySearch(@RequestBody POrderList_getlist_request entity, HttpServletRequest request ) {
//		POrderList_getlist_response response = new POrderList_getlist_response();
//		try {
//			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Long user_orgid_link = user.getOrgid_link();
//			Long granttoorgid_link = (long)0;
//			if(user_orgid_link == (long)1) granttoorgid_link = null;
//			else granttoorgid_link = user_orgid_link;
//			
//			List<Long> status = entity.status;
//			response.data = new ArrayList<>();
//			List<POrder> result = new ArrayList<>();
//			
//			if(status.size() == 0) {
//				result = porderService.getPOrderListBySearch(
//							entity.style, // style
//							entity.buyerid, // buyerid
//							entity.vendorid, // vendorid
//							entity.factoryid, // factoryid
////							entity.orderdatefrom, // orderdatefrom
////							entity.orderdateto, // orderdateto
//							null,
//							granttoorgid_link
//							);
//			}else {
//				for(Long num : status) {
//					List<POrder> temp = porderService.getPOrderListBySearch(
//							entity.style, // style
//							entity.buyerid, // buyerid
//							entity.vendorid, // vendorid
//							entity.factoryid, // factoryid
////							entity.orderdatefrom, // orderdatefrom
////							entity.orderdateto, // orderdateto
//							num,
//							granttoorgid_link
//							);
//					result.addAll(temp);
//				}
//			}
//			if(entity.pobuyer == null) entity.pobuyer="";
//			if(entity.povendor == null) entity.povendor="";
//			if(entity.style == null) entity.style="";
//			
//			for(POrder porder : result) {
//				String po_buyer = porder.getPo_buyer().toLowerCase();
//				String po_buyer_req = entity.pobuyer.toLowerCase();
//				String po_vendor = porder.getPo_vendor().toLowerCase();
//				String po_vendor_req = entity.povendor.toLowerCase();
//				String stylebuyer = porder.getStylebuyer().toLowerCase();
//				String style = entity.style.toLowerCase();
//				if(!po_buyer.contains(po_buyer_req)) {
//					continue;
//				}
//				if(!po_vendor.contains(po_vendor_req)) {
//					continue;
//				}
//				if(!stylebuyer.contains(style)) {
//					continue;
//				}
//				response.data.add(porder);
//			}
//			
//			Comparator<POrder> compareByGrantToOrgName = (POrder p1, POrder p2) -> p1.getGranttoorgname().compareTo( p2.getGranttoorgname());
//			Collections.sort(response.data, compareByGrantToOrgName);
//			
//			response.totalCount = response.data.size();
//			
//			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
//			int start = (int) page.getOffset();
//			int end = (start + page.getPageSize()) > response.data.size() ? response.data.size() : (start + page.getPageSize());
//			Page<POrder> pageToReturn = new PageImpl<POrder>(response.data.subList(start, end), page, response.data.size()); 
//			
//			response.data = pageToReturn.getContent();
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<POrderList_getlist_response>(response,HttpStatus.OK);
//		}catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//		    return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
//		}
//	}
	@RequestMapping(value = "/getallbysearch", method = RequestMethod.POST)
	public ResponseEntity<getbysearch_origin_response> POrderGetAllBySearch(
			@RequestBody POrderList_getlist_request entity, HttpServletRequest request) {
		getbysearch_origin_response response = new getbysearch_origin_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long user_orgid_link = user.getOrgid_link();
			Long granttoorgid_link = (long) 0;
			if (user_orgid_link == (long) 1)
				granttoorgid_link = null;
			else
				granttoorgid_link = user_orgid_link;

			response.data = new ArrayList<>();

			String contractcode = entity.contractcode;
			String pobuyer = entity.pobuyer;
			String stylebuyer = entity.style;
			Long buyerid = entity.buyerid;
			Long vendorid = entity.vendorid;
			Long factoryid = entity.factoryid;
			Date golivedatefrom = entity.golivedatefrom;
			Date golivedateto = entity.golivedateto;
			List<Integer> statuses = entity.status;

//			System.out.println(golivedatefrom);
//			System.out.println(golivedateto);
			List<POrder> list_porder = porderService.getPOrderBySearch(buyerid, vendorid, factoryid, pobuyer,
					stylebuyer, contractcode, statuses, granttoorgid_link, GPAYDateFormat.atStartOfDay(golivedatefrom),
					GPAYDateFormat.atEndOfDay(golivedateto));

			List<POrderOrigin> list = new ArrayList<POrderOrigin>();

			for (POrder p : list_porder) {
				POrderOrigin origin = new POrderOrigin();
				origin.setId(p.getId());
				origin.setBuyername(p.getBuyername());
				origin.setGolivedate(p.getGolivedate());
				origin.setOrdercode(p.getOrdercode());
				origin.setPo_buyer(p.getPo_buyer());
				origin.setStartDatePlan(p.getStartDatePlan());
				origin.setStatusName(p.getStatusName());
				origin.setStylebuyer(p.getStylebuyer());
				origin.setTotalorder(p.getTotalorder());
				origin.setVendorname(p.getVendorname());
				origin.setPcontractid_link(p.getPcontractid_link());
				origin.setProductid_link(p.getProductid_link());

				list.add(origin);
			}
			System.out.println(new Date());
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getbysearch_origin_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<getbysearch_origin_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getallbysearch_for_cutplanprocessing", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> POrderGetAllBySearch_for_cutplanprocessing(
			@RequestBody POrderList_getlist_request entity, HttpServletRequest request) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {

			Long granttoorgid_link = entity.granttoorgid_link;
			String ordercode = entity.ordercode;
			String buyercode = entity.buyercode;
			if (ordercode != null) {
				ordercode = ordercode.trim().toLowerCase();
			} else {
				ordercode = "";
			}
			if (buyercode != null) {
				buyercode = buyercode.trim().toLowerCase();
			} else {
				buyercode = "";
			}

			List<POrder> porder_list = porderService.getPOrderByOrderCodeAndProductBuyerCode(granttoorgid_link,
					ordercode, buyercode);

			response.data = porder_list;
//			System.out.println("porder_list: " + porder_list.size());

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getallbuyername", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllBuyer(HttpServletRequest request) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {

			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> buyers = new ArrayList<HashMap<String, Object>>();
			List<String> stringBuyers = new ArrayList<String>();

			for (POrder porder : allPOrder) {
				if (!stringBuyers.contains(porder.getBuyername())) {
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
			return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getallvendorname", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getListString_response> POrderGetAllVendor(HttpServletRequest request) {
		POrderList_getListString_response response = new POrderList_getListString_response();
		try {

			List<POrder> allPOrder = porderService.findAll();
			List<HashMap<String, Object>> vendors = new ArrayList<HashMap<String, Object>>();
			List<String> stringVendors = new ArrayList<String>();

			for (POrder porder : allPOrder) {
				if (!stringVendors.contains(porder.getVendorname())) {
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
			return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getListString_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getgrantbyporderid", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getGrantByPorderId_response> getGrantByPorderId(
			@RequestBody POrderList_getGrantByPorderId_request entity, HttpServletRequest request) {
		POrderList_getGrantByPorderId_response response = new POrderList_getGrantByPorderId_response();
		try {
			response.data = pordergrantService.getByOrderId(entity.porderid);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getGrantByPorderId_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getGrantByPorderId_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getgrantskubygrantid", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response> getGrantSKUByGrantId(
			@RequestBody POrderList_getPOrderGrantSKUbyGrantId_request entity, HttpServletRequest request) {
		POrderList_getPOrderGrantSKUbyGrantId_response response = new POrderList_getPOrderGrantSKUbyGrantId_response();
		try {
			response.data = pordergrantskuService.getPOrderGrant_SKU(entity.pordergrantid);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getPOrderGrantSKUbyGrantId_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getgrantskubygrantid_and_po", method = RequestMethod.POST)
	public ResponseEntity<getgrantsku_by_grant_po_response> getGrantSKUByGrantIdAndPO(
			@RequestBody getgrantsku_by_grant_po_request entity, HttpServletRequest request) {
		getgrantsku_by_grant_po_response response = new getgrantsku_by_grant_po_response();
		try {
			response.data = pordergrantskuService.getGrantSKUByGrantAndPO(entity.pordergrantid_link,
					entity.pcontract_poid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getgrantsku_by_grant_po_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getgrantsku_by_grant_po_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getproductskubyporder", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getProductSKUbyPorder_response> getProductSKUbyPorder(
			@RequestBody POrderList_getProductSKUbyPorder_request entity, HttpServletRequest request) {
		POrderList_getProductSKUbyPorder_response response = new POrderList_getProductSKUbyPorder_response();
		try {
			response.data = new ArrayList<POrder_Product_SKU>();
			List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(entity.porderid);
			List<POrderGrant_SKU> porderGrantSkus = pordergrantskuService.getPOrderGrant_SKU(entity.grantid);

			for (POrder_Product_SKU pps : porderProductSkus) {
				boolean flag = true;
				for (POrderGrant_SKU pgs : porderGrantSkus) {
					if (pps.getSkuid_link().equals(pgs.getSkuid_link())) {
						flag = false;
						if (flag == false)
							break;
					}
				}
				if (flag)
					response.data.add(pps);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getallproductskubyporder", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getProductSKUbyPorder_response> getAllProductSKUbyPorder(
			@RequestBody POrderList_getProductSKUbyPorder_request entity, HttpServletRequest request) {
		POrderList_getProductSKUbyPorder_response response = new POrderList_getProductSKUbyPorder_response();
		try {
			Long porderid_link = entity.porderid;
			List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(entity.porderid);

			for (POrder_Product_SKU sku : porderProductSkus) {
				Long skuid_link = sku.getSkuid_link();
				Long pcontract_poid_link = sku.getPcontract_poid_link();
				sku.setPquantity_granted(
						pordergrantskuService.porder_get_qty_grant(porderid_link, skuid_link, pcontract_poid_link));
			}

			response.data = porderProductSkus;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getProductSKUbyPorder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/deleteskuto_porder", method = RequestMethod.POST)
	public ResponseEntity<delete_sku_fromporder_response> DeleteSkuToPOrder(
			@RequestBody delete_sku_from_porder_request entity, HttpServletRequest request) {
		delete_sku_fromporder_response response = new delete_sku_fromporder_response();
		try {
			for (POrder_Product_SKU line_sku : entity.data) {

				porderskuService.delete(line_sku);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_sku_fromporder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_sku_fromporder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/update_pordersku", method = RequestMethod.POST)
	public ResponseEntity<update_quantity_sku_porder_response> UpdateSkuToPOrder(
			@RequestBody update_quantity_sku_porder_request entity, HttpServletRequest request) {
		update_quantity_sku_porder_response response = new update_quantity_sku_porder_response();
		try {
			porderskuService.save(entity.data);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_quantity_sku_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_quantity_sku_porder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/addskuto_porder", method = RequestMethod.POST)
	public ResponseEntity<add_slku_toporder_response> addSkuToPOrder(@RequestBody add_sku_toporder_request entity,
			HttpServletRequest request) {
		add_slku_toporder_response response = new add_slku_toporder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long porderid_link = entity.porderid_link;
			Long productid_link = entity.productid_link;
			Long pcontract_poid_link = entity.pcontract_poid_link;

			// save to porder_sku
			for (PContractProductSKU line_sku : entity.list_sku) {
				// kiem tra sku co chua thi them vao khong thi chi cong so luong
				List<POrder_Product_SKU> porder_skus = porderskuService.getby_porderandsku_and_po(porderid_link,
						line_sku.getSkuid_link(), pcontract_poid_link);
				if (porder_skus.size() > 0) {
					POrder_Product_SKU porder_sku = porder_skus.get(0);
					porder_sku.setPquantity_total(porder_sku.getPquantity_total() + line_sku.getPquantity_total()
							- line_sku.getPquantity_lenhsx());
					porderskuService.save(porder_sku);
				} else {
					POrder_Product_SKU sku_new = new POrder_Product_SKU();
					sku_new.setId(null);
					sku_new.setOrgrootid_link(orgrootid_link);
					sku_new.setPorderid_link(porderid_link);
					sku_new.setPquantity_total(line_sku.getPquantity_total() - line_sku.getPquantity_lenhsx());
					sku_new.setProductid_link(productid_link);
					sku_new.setSkuid_link(line_sku.getSkuid_link());
					sku_new.setPcontract_poid_link(pcontract_poid_link);

					porderskuService.save(sku_new);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<add_slku_toporder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<add_slku_toporder_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/addskutogrant", method = RequestMethod.POST)
	public ResponseEntity<addskutogrant_response> addSkuToGrant(@RequestBody POrderList_addSkuToGrant_request entity,
			HttpServletRequest request) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
//			Long pcontract_poid_link = entity.pcontract_poid_link;
			Long porderid_link = entity.idPOrder;
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(porderid_link);

			// save to porder_grant_sku
			for (POrder_Product_SKU pps : entity.porderSku) {
				Long pcontract_poid_link = pps.getPcontract_poid_link();
				POrderGrant_SKU pgs = pordergrantskuService.getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(
						pps.getSkuid_link(), entity.idGrant, pcontract_poid_link);

//				List<POrderGrant_SKU> listPorderGrantSku = pordergrantskuService.getByPContractPOAndSKU(
//						null, pps.getSkuid_link()
//						);
				Long skuid_link = pps.getSkuid_link();
				Integer granted = pordergrantskuService.porder_get_qty_grant(porderid_link, skuid_link,
						pcontract_poid_link);
//				for(POrderGrant_SKU porderGrantSku : listPorderGrantSku) {
//					//tru grant hien tai ra con dau cong vao
//					if(!porderGrantSku.getPordergrantid_link().equals(entity.idGrant))
//						granted += porderGrantSku.getGrantamount();
//				}

				if (pgs == null) {
					pgs = new POrderGrant_SKU();
					pgs.setId(null);
					pgs.setOrgrootid_link(user.getRootorgid_link());
					pgs.setPordergrantid_link(entity.idGrant);
					pgs.setSkuid_link(pps.getSkuid_link());
					pgs.setGrantamount(pps.getPquantity_total() - granted);
					pgs.setPcontract_poid_link(null);
					pgs.setPcontract_poid_link(pcontract_poid_link);
					pordergrantskuService.save(pgs);
				} else {
					pgs.setGrantamount(pgs.getGrantamount() + pps.getPquantity_total() - granted);
					pordergrantskuService.save(pgs);
				}
			}

			// re-calculate porder_grant grant_amount
			List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(grant.getId());
			int grantamountSum = 0;
			for (POrderGrant_SKU pgs : pgslist) {
				grantamountSum += pgs.getGrantamount() == null ? 0 : pgs.getGrantamount();
			}

			// kiem tra xem so luong bang 0 hay khong. Neu bang = thi lay so luong ban dau
			grantamountSum = grantamountSum == 0 ? grant.getTotalamount_tt() : grantamountSum;
			grant.setGrantamount(grantamountSum);
			pordergrantService.save(grant);

			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();

			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);

			if (porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#" + ST + "-PO: " + PO + "-" + decimalFormat.format(total) + "/"
						+ decimalFormat.format(totalPO);
			}

			// Common ReCalculate
			Date startDate = grant.getStart_date_plan();
			Calendar calDate = Calendar.getInstance();
			calDate.setTime(startDate);
			Date endDate = commonService.ReCalculate(grant.getId(), orgrootid_link);
			response.duration = commonService.getDuration_byProductivity(total, grant.getProductivity());
			response.endDate = endDate;
			response.porderinfo = name;
			response.amount = total;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/removeskufromgrant", method = RequestMethod.POST)
	public ResponseEntity<addskutogrant_response> removeSkuFromGrant(@RequestBody removesku_from_grant_request entity,
			HttpServletRequest request) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(entity.idPOrder);
			// save to porder_grant_sku
			List<POrderGrant_SKU> list_GrantSku = entity.porderSku;
			int amount_remove = 0;
			for (POrderGrant_SKU grant_sku : list_GrantSku) {
				amount_remove += grant_sku.getGrantamount();
				pordergrantskuService.delete(grant_sku);
			}

			grant.setGrantamount(grant.getGrantamount() - amount_remove);

			// re-calculate porder_grant grant_amount
//			List<POrderGrant> pglist = pordergrantService.getByOrderId(entity.idPOrder);
//			
//			for(POrderGrant pg : pglist) {
//				Integer grantamountSum = 0;
//				
//				List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(pg.getId());
//				for(POrderGrant_SKU pgs : pgslist) {
//					grantamountSum+=pgs.getGrantamount();
//				}
//				
//				//kiem tra xem so luong bang 0 hay khong. Neu bang = thi lay so luong ban dau
//				grantamountSum = grantamountSum == 0 ? pg.getTotalamount_tt() : grantamountSum;
//				
//				pg.setGrantamount(grantamountSum);
//				pordergrantService.save(pg);
//			}

			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();

			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);

			if (porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#" + ST + "-PO: " + PO + "-" + decimalFormat.format(total) + "/"
						+ decimalFormat.format(totalPO);
			}

			// Common ReCalculate
			Date startDate = grant.getStart_date_plan();
			Calendar calDate = Calendar.getInstance();
			calDate.setTime(startDate);
			commonService.ReCalculate(grant.getId(), orgrootid_link);

			Date endDate = grant.getFinish_date_plan();
			response.duration = commonService.getDuration_byProductivity(total, grant.getProductivity());
			response.endDate = endDate;
			response.porderinfo = name;
			response.amount = total;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/savegrantskuonchange", method = RequestMethod.POST)
	public ResponseEntity<addskutogrant_response> saveGrantSkuOnChange(
			@RequestBody POrderList_saveGrantSkuOnChange_request entity, HttpServletRequest request) {
		addskutogrant_response response = new addskutogrant_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			POrderGrant grant = pordergrantService.findOne(entity.idGrant);
			POrder porder = porderService.findOne(entity.idPOrder);
			// save to porder_grant_sku
			POrderGrant_SKU pordergrantsku = entity.data;

			Long skuid_link = pordergrantsku.getSkuid_link();

			List<POrder_Product_SKU> porder_skus = porderskuService.getby_porderandsku_and_po(entity.idPOrder,
					skuid_link, pordergrantsku.getPcontract_poid_link());
			POrder_Product_SKU porder_sku = porder_skus.get(0);

			List<POrderGrant_SKU> listPorderGrantSku = pordergrantskuService
					.getByPContractPOAndSKU(pordergrantsku.getPcontract_poid_link(), porder_sku.getSkuid_link());
			// lay suong luong da phan vao to cua sku
			Integer granted = 0;
			for (POrderGrant_SKU porderGrantSku : listPorderGrantSku) {
				if (!porderGrantSku.getPordergrantid_link().equals(grant.getId()))
					granted += porderGrantSku.getGrantamount();
			}

			Integer ungranted = porder_sku.getPquantity_total() - granted;

			if (pordergrantsku.getGrantamount() == 0) {
				pordergrantskuService.deleteById(pordergrantsku.getId());
				response.setMessage("Xóa thành công");
			} else {
				if (ungranted < pordergrantsku.getGrantamount()) {
					response.setMessage("Vượt quá số lượng chưa vào chuyền");
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					return new ResponseEntity<addskutogrant_response>(response, HttpStatus.OK);
				} else {
					pordergrantskuService.save(pordergrantsku);

//					porder_sku.setPquantity_granted(granted + pordergrantsku.getGrantamount());
					response.setMessage("Lưu thành công");
				}
			}

			// re-calculate porder_grant grant_amount
			Integer grantamountSum = 0;

			List<POrderGrant_SKU> pgslist = pordergrantskuService.getPOrderGrant_SKU(grant.getId());
			for (POrderGrant_SKU pgs : pgslist) {
				grantamountSum += pgs.getGrantamount();
			}

			// kiem tra xem so luong bang 0 hay khong. Neu bang = thi lay so luong ban dau
			grantamountSum = grantamountSum == 0 ? grant.getTotalamount_tt() : grantamountSum;

			grant.setGrantamount(grantamountSum);
			pordergrantService.save(grant);

			String name = "";
			int total = grant.getGrantamount() == null ? 0 : grant.getGrantamount();

			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);

			if (porder != null) {
				float totalPO = porder.getPo_quantity() == null ? 0 : porder.getPo_quantity();
				String ST = porder.getBuyername() == null ? "" : porder.getBuyername();
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "#" + ST + "-PO: " + PO + "-" + decimalFormat.format(total) + "/"
						+ decimalFormat.format(totalPO);
			}

			// Common ReCalculate
			Date startDate = grant.getStart_date_plan();
			Calendar calDate = Calendar.getInstance();
			calDate.setTime(startDate);
			commonService.ReCalculate(grant.getId(), orgrootid_link);
//			System.out.println(grant.getId());
//			System.out.println(orgrootid_link);
//			System.out.println(calDate.get(Calendar.YEAR));;
			response.duration = commonService.getDuration_byProductivity(total, grant.getProductivity());
			Date endDate = grant.getFinish_date_plan();
			response.endDate = endDate;
			response.porderinfo = name;
			response.amount = total;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<addskutogrant_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getbypordercode", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> getByPOrderCode(
			@RequestBody POrderList_getbypordercode_request entity, HttpServletRequest request) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {
			response.data = porderService.getPOrderByOrdercode(entity.pordercode, entity.granttoorgid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getbyexactpordercode", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> getByExactPOrderCode(
			@RequestBody POrderList_getbypordercode_request entity, HttpServletRequest request) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {
			List<POrder> list = porderService.getPOrderByExactOrdercode(entity.pordercode);
			if (list.size() == 0) {
				response.data = list;
//				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//				response.setMessage("Mã lệnh không tồn tại");
//			    return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Mã lệnh không tồn tại");
				return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
			}
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/getPorderByOrdercodeAndOrg", method = RequestMethod.POST)
	public ResponseEntity<POrderList_getlist_response> getPorderByOrdercodeAndOrg(
			@RequestBody POrderList_getbypordercode_request entity, HttpServletRequest request) {
		POrderList_getlist_response response = new POrderList_getlist_response();
		try {

			String ordercode = entity.ordercode;
			Long granttoorgid_link = entity.granttoorgid_link;

			List<POrder> list = porderService.getPorderByOrdercodeAndOrg(ordercode, granttoorgid_link);
			response.data = list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderList_getlist_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
