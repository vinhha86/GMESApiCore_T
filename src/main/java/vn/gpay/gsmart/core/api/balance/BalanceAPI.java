package vn.gpay.gsmart.core.api.balance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBom2API;
import vn.gpay.gsmart.core.api.pcontractproductbom.get_bom_by_product_request;
import vn.gpay.gsmart.core.api.pcontractproductbom.get_bom_by_product_response;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.IPContract_bom2_npl_poline_sku_Service;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_grant_sku_plan.IPOrderGrant_SKU_Plan_Service;
import vn.gpay.gsmart.core.porder_grant_sku_plan.POrderGrant_SKU_Plan;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceAPI {
	@Autowired
	IPContractService pcontractService;
	@Autowired
	IPContract_POService pcontract_POService;
	@Autowired
	IPContractProductSKUService po_SKU_Service;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	PContractProductBom2API bom2Service;
	@Autowired
	IPOrder_Product_SKU_Service pOrder_SKU_Service;
	@Autowired
	IPOrder_Service porder_Service;
	@Autowired
	IOrgService orgService;
	@Autowired
	IPOrderGrant_Service pordergrantService;
	@Autowired
	IPOrderGrant_SKUService pordergrantSkuService;
	@Autowired
	IPOrderGrant_SKU_Plan_Service pordergrantSkuPlanService;

	@Autowired
	IPContract_bom2_npl_poline_Service bomPOLine_Service;
	@Autowired
	IPContract_bom2_npl_poline_sku_Service bomPOLine_SKU_Service;
	@Autowired
	IPContractBOM2SKUService bom2SKUService;

	@RequestMapping(value = "/cal_balance_bypo", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_bypo(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
			// Check if PO exist
			PContract_PO thePO = pcontract_POService.findOne(entity.pcontract_poid_link);
			List<Long> ls_productid = new ArrayList<Long>();
			// Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0) {
				String[] s_productid = entity.list_productid.split(";");
				for (String sID : s_productid) {
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}
			if (null != thePO) {
				// 1. Lay danh sach Product, Color va SL yeu cau SX theo PO
				// (Neu la PO cha thi lay tong yeu cau cua cac PO con)
				List<Balance_Product_Data> ls_Product = get_BalanceProduct_List(entity.pcontract_poid_link,
						ls_productid);
				if (null != ls_Product) {
					// 2. Lay tong hop BOM theo PContractid_link, Productid_link, Colorid_link
					List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
					for (Balance_Product_Data theProduct : ls_Product) {
						cal_demand(request, ls_SKUBalance, thePO.getPcontractid_link(), theProduct.productid_link,
								theProduct.colorid_link, theProduct.amount, entity.materialid_link);
					}

					// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
					CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());
					for (SKUBalance_Data mat_sku : ls_SKUBalance) {
						Balance_SKU theBalance = new Balance_SKU(ls_SKUBalance, thePO.getPcontractid_link(), null,
								thePO.getId(), null, mat_sku, request.getHeader("Authorization"), latch);
						theBalance.start();
					}
					latch.await();
					response.data = ls_SKUBalance;
					response.product_data = ls_Product;
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Chưa khai báo chi tiết màu cỡ");
					return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("PO Không tồn tại");
				return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/cal_balance_bycontract", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_bycontract_new(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
			// Lay danh sách sku của pcontract (có SKU nghĩa là đã chốt và có PO chi tiết)
//			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getsumsku_bypcontract(entity.pcontractid_link);
			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getlistsku_bypcontract(user.getRootorgid_link(),
					entity.pcontractid_link);
//			long productid_link = Long.parseLong(entity.list_productid);
//			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getlistsku_byproduct_and_pcontract(user.getRootorgid_link(), productid_link, entity.pcontractid_link);
			List<Balance_Product_Data> ls_Product_Total = new ArrayList<Balance_Product_Data>();
//			List<SKUBalance_Data> ls_SKUBalance_Total = new ArrayList<SKUBalance_Data>();

			List<Long> ls_productid = new ArrayList<Long>();
			// Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0) {
				String[] s_productid = entity.list_productid.split(";");
				for (String sID : s_productid) {
					if (sID.length()>0) {
						Long lID = Long.valueOf(sID);
						ls_productid.add(lID);
					}
				}
			}

//			System.out.println(ls_productid);
			// Duyệt qua từng màu, cỡ của sản phẩm (SKU) để tính nhu cầu NPL cho màu, cỡ đó
			List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
			for (PContractProductSKU thePContractSKU : ls_Product_SKU) {
//				SKU theProduct_SKU = skuService.findOne(thePContractSKU.getSkuid_link());
//				System.out.println(thePContractSKU.getProductid_link() + "-" + thePContractSKU.getProductcode());
				// Chỉ tính các sku của SP trong danh sách chọn
				if (ls_productid.contains(thePContractSKU.getProductid_link())) {
//					System.out.println(thePContractSKU.getProductcode() + "-" + thePContractSKU.getMauSanPham() + "-" + thePContractSKU.getCoSanPham()
//					+ "-" + thePContractSKU.getPcontract_poid_link() + "-" + thePContractSKU.getPquantity_total());
					cal_demand_bysku(ls_SKUBalance, entity.pcontractid_link, thePContractSKU.getPcontract_poid_link(),
							thePContractSKU.getProductid_link(), thePContractSKU.getSkuid_link(),
							thePContractSKU.getSkuCode(), thePContractSKU.getMauSanPham(),
							thePContractSKU.getCoSanPham(), thePContractSKU.getPquantity_total(),
							thePContractSKU.getPo_buyer(), thePContractSKU.getPquantity_porder(), entity.balance_limit, entity.materialid_link);

//					ls_SKUBalance_Total.addAll(ls_SKUBalance);
//		            ls_Product_Total.addAll(ls_Product);
				}
			}

			// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
			CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());

			for (SKUBalance_Data mat_sku : ls_SKUBalance) {
				Balance_SKU theBalance = new Balance_SKU(ls_SKUBalance, entity.pcontractid_link, null, null, null,
						mat_sku, request.getHeader("Authorization"), latch);
				theBalance.start();
			}
			latch.await();

			response.data = ls_SKUBalance;
			response.product_data = ls_Product_Total;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/get_material_bycontract", method = RequestMethod.POST)
	public ResponseEntity<Balance_MaterialContract_Response> get_material_bycontract(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		Balance_MaterialContract_Response response = new Balance_MaterialContract_Response();
		try {
			response.data = bom2SKUService.getMateriallist_ByContract(entity.pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_MaterialContract_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_MaterialContract_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/get_productlist_bymaterial", method = RequestMethod.POST)
	public ResponseEntity<Balance_MaterialContract_Response> get_productlist_bymaterial(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		Balance_MaterialContract_Response response = new Balance_MaterialContract_Response();
		try {
//			System.out.println(entity.pcontractid_link + "-" + entity.materialid_link);
			response.data = bom2SKUService.getProductlist_ByMaterial(entity.pcontractid_link, entity.materialid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_MaterialContract_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_MaterialContract_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
//	public ResponseEntity<Balance_Response> cal_balance_bycontract(HttpServletRequest request,
//			@RequestBody Balance_Request entity) {
////		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Balance_Response response = new Balance_Response();
//		try {
//			//Lay danh sách các PO Cha đã chốt của pcontract
//			List<PContract_PO> ls_PO = pcontract_POService.getPO_Offer_Accept_ByPContract(entity.pcontractid_link, Long.parseLong("0"));
//			List<Balance_Product_Data> ls_Product_Total = new ArrayList<Balance_Product_Data>();
//			List<SKUBalance_Data> ls_SKUBalance_Total = new ArrayList<SKUBalance_Data>();
//			
//			List<Long> ls_productid = new ArrayList<Long>();
//			//Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
//			if (null != entity.list_productid && entity.list_productid.length() > 0){
//				String[] s_productid = entity.list_productid.split(";"); 
//				for(String sID:s_productid){
//					Long lID = Long.valueOf(sID);
//					ls_productid.add(lID);
//				}
//			}
//			
//			for(PContract_PO thePO: ls_PO)
//			{
//				//1. Lay danh sach Product, Color va SL yeu cau SX theo PO 
//				//(Neu la PO cha thi lay tong yeu cau cua cac PO con)
//				List<Balance_Product_Data> ls_Product = get_BalanceProduct_List(thePO.getId(), ls_productid);
//				
//				if (null!=ls_Product){
//					//2. Lay tong hop BOM theo PContractid_link, Productid_link, Colorid_link
//					List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
//					for(Balance_Product_Data theProduct:ls_Product){
//						cal_demand(request, ls_SKUBalance, thePO.getPcontractid_link(), theProduct.productid_link,theProduct.colorid_link, theProduct.amount);
//					}
//		
//					//3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
//					CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());
//
//					for(SKUBalance_Data mat_sku:ls_SKUBalance){
//						Balance_SKU theBalance =  new Balance_SKU(
//								ls_SKUBalance,
//								thePO.getPcontractid_link(),
//								thePO.getId(),
//								mat_sku,
//								request.getHeader("Authorization"),
//								latch);
//						theBalance.start();
//					}
//					latch.await();
//					ls_SKUBalance_Total.addAll(ls_SKUBalance);
//		            ls_Product_Total.addAll(ls_Product);
//				} 
//			}
//			
//            response.data = ls_SKUBalance_Total;
//            response.product_data = ls_Product_Total;
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
//		}
//	}	
	
	@RequestMapping(value = "/cal_balance_byporder", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_byporder(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
			POrder thePorder = porder_Service.findOne(entity.porderid_link);
			if (null != thePorder) {
				// Lay danh sách sku của porder
//				List<POrder_Product_SKU> ls_Product_SKU = pOrder_SKU_Service.getsumsku_byporder(entity.porderid_link);
				List<POrder_Product_SKU> ls_Product_SKU = pOrder_SKU_Service
						.getlist_sku_in_porder(user.getRootorgid_link(), entity.porderid_link);
				
				List<Balance_Product_Data> ls_Product_Total = new ArrayList<Balance_Product_Data>();

				List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
				for (POrder_Product_SKU thePContractSKU : ls_Product_SKU) {
//					SKU theProduct_SKU = skuService.findOne(thePContractSKU.getSkuid_link());
					
					cal_demand_bysku(ls_SKUBalance, thePorder.getPcontractid_link(), thePorder.getPcontract_poid_link(),
							thePorder.getProductid_link(), thePContractSKU.getSkuid_link(),
							thePContractSKU.getSkucode(), thePContractSKU.getMauSanPham(),
							thePContractSKU.getCoSanPham(), thePContractSKU.getPquantity_total(), "", // tinh cho lenh
																										// thi khong can
																										// group theo
																										// po_buyer
							thePContractSKU.getPquantity_porder(), entity.balance_limit, entity.materialid_link);
				}

				// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
				CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());

				// Lấy danh sách các kho nguyên liệu của phân xưởng
				List<Org> ls_Stock = orgService.findChildByType(user.getRootorgid_link(),
						thePorder.getGranttoorgid_link(), OrgType.ORG_TYPE_STOCK_MAT);

				for (Org theStock : ls_Stock)
					for (SKUBalance_Data mat_sku : ls_SKUBalance) {
						Balance_SKU theBalance = new Balance_SKU(ls_SKUBalance, thePorder.getPcontractid_link(),
								theStock.getId(), null, thePorder.getId(), mat_sku, request.getHeader("Authorization"),
								latch);
						theBalance.start();
					}
				latch.await();

				response.data = ls_SKUBalance;
				response.product_data = ls_Product_Total;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lệnh sản xuất không tồn tại");
				return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cal_balance_bypordergrant", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_bypordergrant(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
//			POrder thePorder = porder_Service.findOne(entity.porderid_link);
			POrderGrant thePorderGrant = pordergrantService.findOne(entity.pordergrantid_link);
			
			if (null != thePorderGrant) {
				Long pcontractid_link = thePorderGrant.getPcontractid_link();
				Long porderid_link = thePorderGrant.getPorderid_link();
				POrder porder = porder_Service.findOne(porderid_link);
				// Lay danh sách sku của porderGrant
				List<POrderGrant_SKU> ls_POrderGrant_SKU = pordergrantSkuService.getPOrderGrant_SKU(thePorderGrant.getId());
				
				List<Balance_Product_Data> ls_Product_Total = new ArrayList<Balance_Product_Data>();

				List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
				for (POrderGrant_SKU thePOrderGrant_SKU : ls_POrderGrant_SKU) {
					
					cal_demand_bysku(ls_SKUBalance, pcontractid_link, thePOrderGrant_SKU.getPcontract_poid_link(),
							porder.getProductid_link(), thePOrderGrant_SKU.getSkuid_link(),
							thePOrderGrant_SKU.getSkucode(), thePOrderGrant_SKU.getMauSanPham(),
							thePOrderGrant_SKU.getCoSanPham(), thePOrderGrant_SKU.getGrantamount(), "", // tinh cho lenh
																										// thi khong can
																										// group theo
																										// po_buyer
							porder.getTotalorder(), entity.balance_limit, entity.materialid_link);
				}
				
				// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
				CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());

				// Lấy danh sách các kho nguyên liệu của phân xưởng
				List<Org> ls_Stock = orgService.findChildByType(user.getRootorgid_link(),
						porder.getGranttoorgid_link(), OrgType.ORG_TYPE_STOCK_MAT);

				for (Org theStock : ls_Stock)
					for (SKUBalance_Data mat_sku : ls_SKUBalance) {
						Balance_SKU theBalance = new Balance_SKU(ls_SKUBalance, porder.getPcontractid_link(),
								theStock.getId(), null, porder.getId(), mat_sku, request.getHeader("Authorization"),
								latch);
						theBalance.start();
					}
				latch.await();

				response.data = ls_SKUBalance;
				response.product_data = ls_Product_Total;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lệnh sản xuất không tồn tại");
				return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cal_balance_bypordergrant_date", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_bypordergrant_date(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
//			POrder thePorder = porder_Service.findOne(entity.porderid_link);
			List<Date> date_list = entity.date_list;
			POrderGrant thePorderGrant = pordergrantService.findOne(entity.pordergrantid_link);
			
			if (null != thePorderGrant) {
				Long pcontractid_link = thePorderGrant.getPcontractid_link();
				Long porderid_link = thePorderGrant.getPorderid_link();
				POrder porder = porder_Service.findOne(porderid_link);
				// Lay danh sách sku của porderGrant
				List<POrderGrant_SKU> ls_POrderGrant_SKU = pordergrantSkuService.getPOrderGrant_SKU(thePorderGrant.getId());
				
				List<Balance_Product_Data> ls_Product_Total = new ArrayList<Balance_Product_Data>();

				List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
				for (POrderGrant_SKU thePOrderGrant_SKU : ls_POrderGrant_SKU) {
//				for (Long skuid : skuid_list) {
					// Tính sl sp theo ngày (không lấy toàn bộ theo grant_sku)
					Integer amount = 0;
					for(Date date : date_list) {
						List<POrderGrant_SKU_Plan> porderGrant_SKU_Plan_list = pordergrantSkuPlanService.getByPOrderGrant_SKU_inDate(thePorderGrant.getId(), thePOrderGrant_SKU.getSkuid_link(), date, thePOrderGrant_SKU.getPcontract_poid_link());
						for(POrderGrant_SKU_Plan porderGrant_SKU_Plan : porderGrant_SKU_Plan_list) {
							Integer planAmount = porderGrant_SKU_Plan.getAmount() == null ? 0 : porderGrant_SKU_Plan.getAmount();
							amount += planAmount;
						}
					}
					
					cal_demand_bysku(ls_SKUBalance, pcontractid_link, thePOrderGrant_SKU.getPcontract_poid_link(),
							porder.getProductid_link(), thePOrderGrant_SKU.getSkuid_link(),
							thePOrderGrant_SKU.getSkucode(), thePOrderGrant_SKU.getMauSanPham(),
							thePOrderGrant_SKU.getCoSanPham(), amount, "",  // tinh cho lenh
																										// thi khong can
																										// group theo
																										// po_buyer
							porder.getTotalorder(), entity.balance_limit, entity.materialid_link);
				}
				
				// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
				CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());

				// Lấy danh sách các kho nguyên liệu của phân xưởng
				List<Org> ls_Stock = orgService.findChildByType(user.getRootorgid_link(),
						porder.getGranttoorgid_link(), OrgType.ORG_TYPE_STOCK_MAT);

				for (Org theStock : ls_Stock)
					for (SKUBalance_Data mat_sku : ls_SKUBalance) {
						Balance_SKU theBalance = new Balance_SKU(ls_SKUBalance, porder.getPcontractid_link(),
								theStock.getId(), null, porder.getId(), mat_sku, request.getHeader("Authorization"),
								latch);
						theBalance.start();
					}
				latch.await();

				response.data = ls_SKUBalance;
				response.product_data = ls_Product_Total;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lệnh sản xuất không tồn tại");
				return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/get_material_bypcontract", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> get_material_bypcontract(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		Balance_Response response = new Balance_Response();
		try {
			// Lay danh sach PO cua PContract
			List<PContract_PO> ls_PO = pcontract_POService.getPOByContract(orgrootid_link, entity.pcontractid_link);
			List<Long> ls_productid = new ArrayList<Long>();
			// Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0) {
				String[] s_productid = entity.list_productid.split(";");
				for (String sID : s_productid) {
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}

			List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
			List<Balance_Product_Data> ls_Product = new ArrayList<Balance_Product_Data>();
			for (PContract_PO thePO : ls_PO) {
				// 1. Lay danh sach Product, Color va SL yeu cau SX theo PO
				// (Neu la PO cha thi lay tong yeu cau cua cac PO con)
				List<Balance_Product_Data> ls_Product_PO = get_BalanceProduct_List(thePO.getId(), ls_productid);
				if (null != ls_Product) {
					// 2. Lay tong hop BOM theo PContractid_link, Productid_link, Colorid_link
					for (Balance_Product_Data theProduct : ls_Product_PO) {
						cal_demand(request, ls_SKUBalance, thePO.getPcontractid_link(), theProduct.productid_link,
								theProduct.colorid_link, theProduct.amount, entity.materialid_link);
					}
					ls_Product.addAll(ls_Product_PO);
				}
			}
			response.data = ls_SKUBalance;
			response.product_data = ls_Product;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// Lay danh sach SKU cua Product trong PO
	private List<Balance_Product_Data> get_BalanceProduct_List(Long pcontract_poid_link, List<Long> ls_productid) {
		try {
			PContract_PO thePO = pcontract_POService.findOne(pcontract_poid_link);
			if (null != thePO) {
				List<PContractProductSKU> ls_SKU_Balance = new ArrayList<PContractProductSKU>();
				// Kiem tra xem co phai la PO chao gia khong
				if (null == thePO.getParentpoid_link()) {
					// Neu la PO chao gia --> Load sum theo SKU của các PO line con
					ls_SKU_Balance = po_SKU_Service.getsumsku_bypo_parent(pcontract_poid_link);

				} else {
					// Neu la PO Line --> Load danh sach chi tiet SKU
					ls_SKU_Balance = po_SKU_Service.getlistsku_bypo(pcontract_poid_link);
				}

				// Tổng hợp theo Productid, Color_id
				List<Balance_Product_Data> ls_Product_Balance = new ArrayList<Balance_Product_Data>();
				for (PContractProductSKU poSKU : ls_SKU_Balance) {
					if (ls_productid.size() > 0) {
						if (ls_productid.contains(poSKU.getProductid_link())) {
							SKU theSKU = skuService.findOne(poSKU.getSkuid_link());
							if (null != theSKU) {
								Balance_Product_Data theBalance = ls_Product_Balance.stream()
										.filter(prod -> prod.productid_link.equals(poSKU.getProductid_link())
												&& prod.colorid_link.equals(theSKU.getColorid_link()))
										.findAny().orElse(null);
								if (null != theBalance) {
									theBalance.amount += poSKU.getPquantity_total();
								} else {
									Balance_Product_Data newBalance = new Balance_Product_Data();
									newBalance.productid_link = poSKU.getProductid_link();
									newBalance.product_code = theSKU.getCode();
									newBalance.product_name = theSKU.getName();
									newBalance.colorid_link = theSKU.getColorid_link();
									newBalance.color_name = theSKU.getColor_name();
									newBalance.amount = poSKU.getPquantity_total();
									ls_Product_Balance.add(newBalance);
								}
							}
						}
					} else {
						SKU theSKU = skuService.findOne(poSKU.getSkuid_link());
						if (null != theSKU) {
							Balance_Product_Data theBalance = ls_Product_Balance.stream()
									.filter(prod -> prod.productid_link.equals(poSKU.getProductid_link())
											&& prod.colorid_link.equals(theSKU.getColorid_link()))
									.findAny().orElse(null);
							if (null != theBalance) {
								theBalance.amount += poSKU.getPquantity_total();
							} else {
								Balance_Product_Data newBalance = new Balance_Product_Data();
								newBalance.productid_link = poSKU.getProductid_link();
								newBalance.product_code = theSKU.getCode();
								newBalance.product_name = theSKU.getName();
								newBalance.colorid_link = theSKU.getColorid_link();
								newBalance.color_name = theSKU.getColor_name();
								newBalance.amount = poSKU.getPquantity_total();
								ls_Product_Balance.add(newBalance);
							}
						}
					}
				}
				return ls_Product_Balance;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Tinh nhu cau NPL theo định mức
	private void cal_demand(HttpServletRequest request, List<SKUBalance_Data> ls_SKUBalance, Long pcontractid_link,
			Long productid_link, Long colorid_link, Integer p_amount, Long materialid_limit) {
		get_bom_by_product_request entity = new get_bom_by_product_request();
		entity.pcontractid_link = pcontractid_link;
//		entity.colorid_link = colorid_link;
		entity.productid_link = productid_link;
		entity.materialid_link = materialid_limit;

		ResponseEntity<get_bom_by_product_response> bom_response = bom2Service.GetBomByProduct(request, entity);

		List<Map<String, String>> ls_bomdata = bom_response.getBody().data;
		if (null != ls_bomdata) {
			for (Map<String, String> bomdata : ls_bomdata) {
//				System.out.println(bomdata);
				Long materialid_link = Long.valueOf(bomdata.get("materialid_link"));
				Float amount_color = (float) 0;
				if (null != bomdata.get("amount") && Float.valueOf(bomdata.get("amount")) > (float) 0) {
					amount_color = Float.valueOf(bomdata.get("amount"));
				} else {
					amount_color = null != bomdata.get("amount_color") ? Float.valueOf(bomdata.get("amount_color")) : 0;
				}

				Float lostratio = null != bomdata.get("lost_ratio") ? Float.valueOf(bomdata.get("lost_ratio")) : 0;
				SKUBalance_Data theSKUBalance = ls_SKUBalance.stream()
						.filter(sku -> sku.getMat_skuid_link().equals(materialid_link)).findAny().orElse(null);
				if (null != theSKUBalance) {
					// Tinh tong dinh muc
//					theSKUBalance.setMat_sku_bom_amount(theSKUBalance.getMat_sku_bom_amount() + amount_color);
					Float f_skudemand = amount_color * p_amount;
					Float f_lost = (f_skudemand * lostratio) / 100;

//					theSKUBalance.setMat_sku_bom_amount((theSKUBalance.getMat_sku_bom_amount() + amount_color)/2);
					theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + f_skudemand + f_lost);
				} else {
					SKUBalance_Data newSKUBalance = new SKUBalance_Data();
					newSKUBalance.setMat_skuid_link(materialid_link);

					newSKUBalance.setMat_sku_code(bomdata.get("materialCode"));
					newSKUBalance.setMat_sku_name(bomdata.get("materialName"));
					newSKUBalance.setMat_sku_desc(bomdata.get("thanhPhanVai"));
					newSKUBalance.setMat_sku_unit_name(bomdata.get("unitName"));
					newSKUBalance.setMat_sku_size_name(bomdata.get("coKho"));
					newSKUBalance.setMat_sku_color_name(bomdata.get("color_name"));// Mau san pham
					newSKUBalance.setMat_sku_product_typename(bomdata.get("product_typename"));

					newSKUBalance.setMat_sku_bom_lostratio(lostratio);
					newSKUBalance.setMat_sku_bom_amount(amount_color);

					Float f_skudemand = amount_color * p_amount;
					Float f_lost = (f_skudemand * lostratio) / 100;
					newSKUBalance.setMat_sku_demand(f_skudemand + f_lost);

					ls_SKUBalance.add(newSKUBalance);
				}
			}
		}
	}

	private void cal_demand_bysku(List<SKUBalance_Data> ls_SKUBalance, Long pcontractid_link, Long pcontract_poid_link,
			Long productid_link, Long product_skuid_link, String product_sku_code, String product_sku_color,
			String product_sku_size, Integer p_amount, String po_buyer, Integer p_amount_dh, Integer balance_limit, Long materialid_link) {
		try {
			List<PContractBOM2SKU> bom_response = bom2Service.getBOM_By_PContractSKU(pcontractid_link,
					product_skuid_link);
			
			ExecutorService executor = Executors.newFixedThreadPool(bom_response.size() + 1);
			for (PContractBOM2SKU skubom : bom_response) {
				//Check xem co tinh can doi cho 1 Material cu the ko
				if (null != materialid_link && !skubom.getMaterial_skuid_link().equals(materialid_link)) {
//					System.out.println("out: " + skubom.getMaterial_skuid_link());
					continue;
				}
				
//				System.out.println("in:" + materialid_link);
				if (balance_limit == 1) {// Chi tinh nguyen lieu
					if (skubom.getProduct_type() >= 30 || skubom.getProduct_type() < 20)
						continue;
				} else if (balance_limit == 2) {// Chi tinh phu lieu
					if (skubom.getProduct_type() < 30 || skubom.getProduct_type() > 50)
						continue;
				}
				Runnable demand = new calDemand(skubom, ls_SKUBalance, pcontractid_link, pcontract_poid_link,
						productid_link, product_skuid_link, product_sku_code, product_sku_color, product_sku_size,
						p_amount, bomPOLine_Service, po_buyer, p_amount_dh);
				executor.execute(demand);
			}
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static class calDemand implements Runnable {
		private final PContractBOM2SKU skubom;
		private final List<SKUBalance_Data> ls_SKUBalance;
		private final Long pcontractid_link;
		private final Long pcontract_poid_link;
		private final Long productid_link;
		private final Long product_skuid_link;
		private final String product_sku_code;
		private final String product_sku_color;
		private final String product_sku_size;
		private final Integer p_amount;
		private final String po_buyer;
		private final Integer p_amount_dh;

		private final IPContract_bom2_npl_poline_Service bomPOLine_Service;

		calDemand(PContractBOM2SKU skubom, List<SKUBalance_Data> ls_SKUBalance, Long pcontractid_link,
				Long pcontract_poid_link, Long productid_link, Long product_skuid_link, String product_sku_code,
				String product_sku_color, String product_sku_size, Integer p_amount,
				IPContract_bom2_npl_poline_Service bomPOLine_Service, String po_buyer, Integer p_amount_dh) {
			this.skubom = skubom;
			this.ls_SKUBalance = ls_SKUBalance;
			this.pcontractid_link = pcontractid_link;
			this.pcontract_poid_link = pcontract_poid_link;
			this.productid_link = productid_link;
			this.product_skuid_link = product_skuid_link;
			this.product_sku_code = product_sku_code;
			this.product_sku_color = product_sku_color;
			this.product_sku_size = product_sku_size;
			this.p_amount = p_amount;
			this.bomPOLine_Service = bomPOLine_Service;
			this.po_buyer = po_buyer;
			this.p_amount_dh = p_amount_dh;
		}

		@Override
		public void run() {
			try {
				// if (skubom.getMaterialCode().contains("CXI55020")){
				// System.out.println(skubom.getMaterial_skuid_link() + "/" +
				// skubom.getMaterialCode() + "-" + p_amount);
				// }
				// Kiểm tra xem NPL có trong danh sách giới hạn PO không
				// (pcontract_bom2_npl_poline)?
				// Nếu có, kiểm tra tiếp xem có giới hạn áp dụng cụ thể cho từng product_sku ko?
				// SL áp dụng là bao nhiêu
				List<PContract_bom2_npl_poline> ls_poline = bomPOLine_Service.getby_product_and_npl(productid_link,
						pcontractid_link, skubom.getMaterial_skuid_link());
				if (ls_poline.size() > 0) {
					boolean isfound = false;
					// Check nếu poline gửi vào ko có trong danh sách --> Bỏ qua NPL này
					for (PContract_bom2_npl_poline thebom_poline : ls_poline) {
						if (pcontract_poid_link.equals(thebom_poline.getPcontract_poid_link())) {
							isfound = true;
							// Kiem tra tiep xem có giới hạn sâu hơn trong pcontract_bom2_npl_poline_sku ko?

						}
					}
					// Bo qua NPL
					if (!isfound)
						return;
				}

				SKUBalance_Data theSKUBalance = ls_SKUBalance.stream()
						.filter(sku -> sku.getMat_skuid_link().equals(skubom.getMaterial_skuid_link())).findAny()
						.orElse(null);
				if (null != theSKUBalance) {
					// Tinh tong dinh muc
					Float f_skudemand = skubom.getAmount() * p_amount * skubom.getLost_ratio();
					Float f_skudemand_dh = skubom.getAmount() * p_amount_dh * skubom.getLost_ratio();
//					Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;

					// Tinh trung binh dinh muc
					Float f_skubomamount = (theSKUBalance.getMat_sku_bom_amount() + skubom.getAmount()) / 2;
					theSKUBalance.setMat_sku_bom_amount(f_skubomamount);

					theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + f_skudemand);
					theSKUBalance.setMat_sku_product_total(theSKUBalance.getMat_sku_product_total() + p_amount);
					theSKUBalance.setMat_sku_product(theSKUBalance.getMat_sku_product() + p_amount_dh);
					theSKUBalance.setMat_sku_demand_dh(theSKUBalance.getMat_sku_demand_dh() + f_skudemand_dh);

					// Thong tin chi tiet mau co
					SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
					product_d.setP_skuid_link(product_skuid_link);
					product_d.setP_sku_code(product_sku_code);
					product_d.setP_sku_color(product_sku_color);
					product_d.setP_sku_size(product_sku_size);
					product_d.setP_amount(p_amount);
					product_d.setP_bom_amount(skubom.getAmount());
					product_d.setP_bom_lostratio(skubom.getLost_ratio());
					product_d.setP_bom_demand(f_skudemand);
					product_d.setP_bom_demand_dh(f_skudemand_dh);
					product_d.setPo_buyer(po_buyer);
					product_d.setP_amount_dh(p_amount_dh);
					theSKUBalance.getProduct_d().add(product_d);

				} else {
					SKUBalance_Data newSKUBalance = new SKUBalance_Data();
					newSKUBalance.setMat_skuid_link(skubom.getMaterial_skuid_link());
					newSKUBalance.setMat_sku_product_total(p_amount);
					newSKUBalance.setMat_sku_product(p_amount_dh);

					newSKUBalance.setMat_sku_code(skubom.getMaterialCode());
					newSKUBalance.setMat_sku_name(skubom.getMaterialCode());
					newSKUBalance.setMat_sku_desc(skubom.getDescription_product());
					newSKUBalance.setMat_sku_unit_name(skubom.getUnitName());
					newSKUBalance.setMat_sku_size_name(skubom.getCoKho());
					newSKUBalance.setMat_sku_color_name(skubom.getTenMauNPL());
					newSKUBalance.setMat_sku_product_typename(skubom.getProduct_typeName());
					newSKUBalance.setMat_sku_product_typeid_link(skubom.getProduct_type());

					newSKUBalance.setMat_sku_bom_lostratio(skubom.getLost_ratio());
					newSKUBalance.setMat_sku_bom_amount(skubom.getAmount());

					Float f_skudemand = skubom.getAmount() * p_amount * skubom.getLost_ratio();
					Float f_skudemand_dh = skubom.getAmount() * p_amount_dh * skubom.getLost_ratio();
//					Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;
					newSKUBalance.setMat_sku_demand(f_skudemand);
					newSKUBalance.setMat_sku_demand_dh(f_skudemand_dh);

					// Thong tin chi tiet mau co
					SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
					product_d.setP_skuid_link(product_skuid_link);
					product_d.setP_sku_code(product_sku_code);
					product_d.setP_sku_color(product_sku_color);
					product_d.setP_sku_size(product_sku_size);
					product_d.setP_amount(p_amount);
					product_d.setP_bom_amount(skubom.getAmount());
					product_d.setP_bom_lostratio(skubom.getLost_ratio());
					product_d.setP_bom_demand(f_skudemand);
					product_d.setP_bom_demand_dh(f_skudemand_dh);
					product_d.setPo_buyer(po_buyer);
					product_d.setP_amount_dh(p_amount_dh);
					newSKUBalance.getProduct_d().add(product_d);

					ls_SKUBalance.add(newSKUBalance);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
