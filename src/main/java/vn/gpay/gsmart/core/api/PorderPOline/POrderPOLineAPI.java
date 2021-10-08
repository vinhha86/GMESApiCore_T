package vn.gpay.gsmart.core.api.PorderPOline;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_po_shipping.PContract_PO_Shipping;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.porders_poline.POrder_POLine;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderpoline")
public class POrderPOLineAPI {
	@Autowired
	IPOrder_POLine_Service porder_line_Service;
	@Autowired
	IPOrder_Service porderService;
	@Autowired
	IPContract_POService poService;
	@Autowired
	IPOrder_Product_SKU_Service porderskuService;
	@Autowired
	IPContractProductSKUService pcontractskuService;
	@Autowired
	IPOrderGrant_Service grantService;
	@Autowired
	IPOrderGrant_SKUService grantskuService;
	@Autowired
	IOrgService orgService;
	@Autowired
	IGpayUserOrgService userOrgService;
	@Autowired
	Common commonService;

	@RequestMapping(value = "/add_porder", method = RequestMethod.POST)
	public ResponseEntity<add_porder_response> AddPorder(@RequestBody add_porder_request entity,
			HttpServletRequest request) {
		add_porder_response response = new add_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontract_poid_link = entity.pcontract_poid_link;
			boolean onlyGrant = false;
			boolean onlyPorder = false;

			if (entity.data.size() == 1)
				onlyPorder = true;

			for (POrder porder : entity.data) {
				POrder_POLine porder_line = new POrder_POLine();
				porder_line.setId(null);
				porder_line.setPcontract_poid_link(pcontract_poid_link);
				porder_line.setPorderid_link(porder.getId());

				porder_line_Service.save(porder_line);

				// Cap nhat trang thai po sang da map voi lenh
				PContract_PO po = poService.findOne(pcontract_poid_link);
				po.setIsmap(true);
				poService.save(po);

				int total = 0;
				if (onlyPorder) {
					// Cap nhat lai so luong mau co trong porder_sku
					List<POrder_Product_SKU> list_porder_sku = porderskuService.getby_porder(porder.getId());
					for (POrder_Product_SKU pordersku : list_porder_sku) {
						porderskuService.delete(pordersku);
					}

					// kiem tra neu có 1 to thi lay luon mau co vao to do
					List<POrderGrant> list_grant = grantService.getByOrderId(porder.getId());
					if (list_grant.size() == 1) {
						onlyGrant = true;
					}

					List<PContractProductSKU> list_pcontract_sku = pcontractskuService
							.getlistsku_bypo(pcontract_poid_link);
					for (PContractProductSKU pcontractsku : list_pcontract_sku) {
						POrder_Product_SKU pordersku = new POrder_Product_SKU();
						pordersku.setId(null);
						pordersku.setOrgrootid_link(orgrootid_link);
						pordersku.setPcontract_poid_link(pcontract_poid_link);
						pordersku.setPorderid_link(porder.getId());
						pordersku.setPquantity_granted(0);
						pordersku.setPquantity_porder(pcontractsku.getPquantity_porder());
						pordersku.setPquantity_production(pcontractsku.getPquantity_production());
						pordersku.setPquantity_sample(pcontractsku.getPquantity_sample());
						pordersku.setPquantity_total(pcontractsku.getPquantity_total());
						pordersku.setProductid_link(pcontractsku.getProductid_link());
						pordersku.setSkuid_link(pcontractsku.getSkuid_link());
						porderskuService.save(pordersku);

						total += pcontractsku.getPquantity_total() == null ? 0 : pcontractsku.getPquantity_total();

						if (onlyGrant) {
							POrderGrant_SKU grant_sku = new POrderGrant_SKU();
							grant_sku.setId(null);
							grant_sku.setOrgrootid_link(orgrootid_link);
							grant_sku.setPcontract_poid_link(pcontract_poid_link);
							grant_sku.setPordergrantid_link(list_grant.get(0).getId());
							grant_sku.setGrantamount(pcontractsku.getPquantity_total());
							grant_sku.setSkuid_link(pcontractsku.getSkuid_link());
							grantskuService.save(grant_sku);
						}
					}
				}

				// Cap nhat trang thai porder sang da map voi poline
				porder.setIsMap(true);
				porder.setTotalorder(total);
				porder.setGolivedate(po.getShipdate());
				porderService.save(porder);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<add_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/add_pordergrant", method = RequestMethod.POST)
	public ResponseEntity<add_grant_response> AddPorderGrant(@RequestBody add_grant_request entity,
			HttpServletRequest request) {
		add_grant_response response = new add_grant_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontract_poid_link = entity.pcontract_poid_link;
			Long pordergrantid_link = entity.pordergrantid_link;
			Long productid_link = entity.productid_link;
//			Long orgid_link = user.getOrgid_link();

			POrderGrant grant = grantService.findOne(pordergrantid_link);
			Long porderid_link = grant.getPorderid_link();

			POrder_POLine porder_line = new POrder_POLine();
			porder_line.setId(null);
			porder_line.setPcontract_poid_link(pcontract_poid_link);
			porder_line.setPordergrantid_link(pordergrantid_link);
			porder_line.setPorderid_link(porderid_link);

			porder_line_Service.save(porder_line);

			// Cap nhat trang thai po sang da map
			List<PContractProductSKU> list_sku = pcontractskuService.getbypo_and_product(pcontract_poid_link,
					productid_link);
			for (PContractProductSKU ppsku : list_sku) {
				ppsku.setIsmap(true);
				pcontractskuService.save(ppsku);
			}
			List<PContractProductSKU> listsku_notmap = pcontractskuService.getsku_notmap(pcontract_poid_link);
			if (listsku_notmap.size() == 0) {
				PContract_PO po = poService.findOne(pcontract_poid_link);

				po.setIsmap(true);
				poService.save(po);
			}

			int total = 0;

			// lay chi tiet mau co vao grant
			List<PContractProductSKU> list_pcontract_sku = pcontractskuService.getbypo_and_product(pcontract_poid_link,
					productid_link);
			for (PContractProductSKU pcontractsku : list_pcontract_sku) {
				total += pcontractsku.getPquantity_total() == null ? 0 : pcontractsku.getPquantity_total();
				Long skuid_link = pcontractsku.getSkuid_link();

				POrderGrant_SKU grant_sku = new POrderGrant_SKU();
				grant_sku.setId(null);
				grant_sku.setOrgrootid_link(orgrootid_link);
				grant_sku.setPcontract_poid_link(pcontract_poid_link);
				grant_sku.setPordergrantid_link(pordergrantid_link);
				grant_sku.setGrantamount(pcontractsku.getPquantity_total());
				grant_sku.setSkuid_link(skuid_link);
				grantskuService.save(grant_sku);

				// cap nhat vao porder_sku
				List<POrder_Product_SKU> porderskus = porderskuService.getby_porderandsku_and_po(porderid_link,
						skuid_link, pcontract_poid_link);
				if (porderskus.size() == 0) {
					POrder_Product_SKU pordersku = new POrder_Product_SKU();
					pordersku.setId(null);
					pordersku.setOrgrootid_link(orgrootid_link);
					pordersku.setPcontract_poid_link(pcontract_poid_link);
					pordersku.setPorderid_link(porderid_link);
					pordersku.setPquantity_granted(0);
					pordersku.setPquantity_porder(pcontractsku.getPquantity_porder());
					pordersku.setPquantity_production(pcontractsku.getPquantity_production());
					pordersku.setPquantity_sample(pcontractsku.getPquantity_sample());
					pordersku.setPquantity_total(pcontractsku.getPquantity_total());
					pordersku.setProductid_link(pcontractsku.getProductid_link());
					pordersku.setSkuid_link(pcontractsku.getSkuid_link());
					porderskuService.save(pordersku);
				} else {
					POrder_Product_SKU pordersku = porderskus.get(0);
					pordersku.setPquantity_porder(pcontractsku.getPquantity_porder() + pordersku.getPquantity_porder());
					pordersku.setPquantity_production(
							pcontractsku.getPquantity_production() + pordersku.getPquantity_production());
					pordersku.setPquantity_sample(pcontractsku.getPquantity_sample() + pordersku.getPquantity_sample());
					pordersku.setPquantity_total(pcontractsku.getPquantity_total() + pordersku.getPquantity_total());
					porderskuService.save(pordersku);
				}

				// Cap nhat lai mau co da phan chuyen
				pcontractsku.setPquantity_granted(pcontractsku.getPquantity_total());
				pcontractsku.setPquantity_lenhsx(pcontractsku.getPquantity_total());
				pcontractskuService.save(pcontractsku);
			}

			// Cap nhat so luong cua grant
			grant.setGrantamount(total);
			grant.setIsmap(true);
			grantService.save(grant);

			// Cap nhat lai so luogn tong cua lenh san xuat
			POrder porder = porderService.findOne(porderid_link);
			porder.setTotalorder(porder.getTotalorder() + total);
			porderService.save(porder);

			String name = "";

			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);

			if (porder != null) {
				String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
				name += "PO: " + PO + "-" + decimalFormat.format(total);
			}

			// Common ReCalculate
//			Date startDate = grant.getStart_date_plan();
//			Calendar calDate = Calendar.getInstance();
//			calDate.setTime(startDate);
			Date endDate = commonService.ReCalculate(grant.getId(), orgrootid_link);

			response.duration = commonService.getDuration_byProductivity(total, grant.getProductivity());
//			 = grant.getFinish_date_plan();
			response.endDate = endDate;
			response.porderinfo = name;
			response.amount = total;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<add_grant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<add_grant_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete_porder", method = RequestMethod.POST)
	public ResponseEntity<delete_porder_response> DeletePorder(@RequestBody delete_porder_request entity,
			HttpServletRequest request) {
		delete_porder_response response = new delete_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontract_poid_link = entity.pcontract_poid_link;
			Long productid_link = entity.productid_link;

			List<PContractProductSKU> list_sku = pcontractskuService.getbypo_and_product(pcontract_poid_link,
					productid_link);
			for (PContractProductSKU sku : list_sku) {
				sku.setIsmap(false);
				pcontractskuService.save(sku);
			}

			List<PContractProductSKU> list_notmap = pcontractskuService.getsku_notmap(pcontract_poid_link);

			PContract_PO linett = poService.findOne(pcontract_poid_link);
			if (list_notmap.size() == 0) {
				linett.setIsmap(true);
			} else {
				linett.setIsmap(false);
			}
			poService.save(linett);

			List<POrder_POLine> list_porder = porder_line_Service.get_porderline_by_po_and_product(pcontract_poid_link,
					productid_link);

			if (list_porder.size() > 0) {

				// Cap nhat lai thong tin lenh san xuat
				POrder porder = porderService.findOne(list_porder.get(0).getPorderid_link());
				POrderGrant grant = grantService.findOne(list_porder.get(0).getPordergrantid_link());
				// neu lenh tu sinh thi xoa di con ko thi cap nhat lai trang thai
				PContract_PO linekh = poService.findOne(porder.getPcontract_poid_link());
				if (linekh.getPo_typeid_link() == POType.PO_LINE_PLAN) {
					porder.setIsMap(false);
					porder.setTotalorder(linekh.getPo_quantity());
					porder.setGolivedate(linekh.getShipdate());
					porderService.save(porder);

					grant.setGrantamount(grant.getTotalamount_tt());
					grant.setIsmap(false);
					grantService.save(grant);

					String name = "";

					DecimalFormat decimalFormat = new DecimalFormat("#,###");
					decimalFormat.setGroupingSize(3);

					if (porder != null) {
						String PO = porder.getPo_buyer() == null ? "" : porder.getPo_vendor();
						name += "PO: " + PO + "-" + decimalFormat.format(grant.getTotalamount_tt());
					}

					// Common ReCalculate
					Date startDate = grant.getStart_date_plan();
					Calendar calDate = Calendar.getInstance();
					calDate.setTime(startDate);
					commonService.ReCalculate(grant.getId(), orgrootid_link);

					response.duration = commonService.getDuration_byProductivity(grant.getTotalamount_tt(),
							grant.getProductivity());
					Date endDate = grant.getFinish_date_plan();
					response.endDate = endDate;
					response.porderinfo = name;
					response.amount = grant.getTotalamount_tt();
				} else {
					// kiem tra xem co line tren bieu do chua thi xoa line tren bieu do
					grantService.delete(grant);
					porderService.delete(porder);
				}

				// Xoa het porder-sku
				List<POrder_Product_SKU> list_porder_sku = porderskuService.getby_porder(porder.getId());
				for (POrder_Product_SKU porder_sku : list_porder_sku) {
					porderskuService.delete(porder_sku);
				}

				// xoa het trong porder_grant_sku

				List<POrderGrant_SKU> list_grant_sku = grantskuService.getPOrderGrant_SKU(grant.getId());
				for (POrderGrant_SKU grantsku : list_grant_sku) {
					grantskuService.delete(grantsku);
				}

				// xoa trong bang porder-poline
				porder_line_Service.delete(list_porder.get(0));

				response.pordergrantid_link = grant.getId();
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<delete_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete_many_porder", method = RequestMethod.POST)
	public ResponseEntity<delete_many_porder_response> DeleteManyPorder(@RequestBody delete_many_porder_request entity,
			HttpServletRequest request) {
		delete_many_porder_response response = new delete_many_porder_response();
		try {
			List<Long> list_id = new ArrayList<Long>();

			for (PContract_PO_Shipping po : entity.data) {
				Long pcontract_poid_link = po.getPcontract_poid_link();
				Long productid_link = po.getProductid_link();

//				List<PContractProductSKU> list_sku = pcontractskuService.getbypo_and_product(pcontract_poid_link,
//						productid_link);
//				for (PContractProductSKU sku : list_sku) {
//					sku.setIsmap(false);
//					pcontractskuService.save(sku);
//				}

				List<POrder_POLine> list_porder = porder_line_Service
						.get_porderline_by_po_and_product(pcontract_poid_link, productid_link);

				if (list_porder.size() > 0) {

					// Cap nhat lai thong tin lenh san xuat
					POrder porder = porderService.findOne(list_porder.get(0).getPorderid_link());
					POrderGrant grant = grantService.findOne(list_porder.get(0).getPordergrantid_link());
					// neu lenh tu sinh thi xoa di con ko thi cap nhat lai trang thai
					PContract_PO linekh = poService.findOne(porder.getPcontract_poid_link());
					if (linekh.getPo_typeid_link() == POType.PO_LINE_PLAN) {
						porder.setIsMap(false);
						porder.setTotalorder(linekh.getPo_quantity());
						porder.setGolivedate(linekh.getShipdate());
						porderService.save(porder);

						grant.setGrantamount(grant.getTotalamount_tt());
						grant.setIsmap(false);
						grantService.save(grant);
					} else {
						// kiem tra xem co line tren bieu do chua thi xoa line tren bieu do
						grantService.delete(grant);
						porderService.delete(porder);
					}

					// Xoa het porder-sku
					List<POrder_Product_SKU> list_porder_sku = porderskuService.getby_porder(porder.getId());
					for (POrder_Product_SKU porder_sku : list_porder_sku) {
						porderskuService.delete(porder_sku);

						// cap nhat lai trong pcontract_sku chua map
						Long skuid_link = porder_sku.getSkuid_link();
						List<PContractProductSKU> list_po_sku = pcontractskuService
								.getlistsku_bysku_and_product_PO(skuid_link, pcontract_poid_link, productid_link);
						if (list_po_sku.size() > 0) {
							PContractProductSKU posku = list_po_sku.get(0);
							posku.setIsmap(false);
							pcontractskuService.save(posku);
						}
					}

					List<PContractProductSKU> list_notmap = pcontractskuService.getsku_notmap(pcontract_poid_link);

					PContract_PO linett = poService.findOne(pcontract_poid_link);
					if (list_notmap.size() == 0) {
						linett.setIsmap(true);
					} else {
						linett.setIsmap(false);
					}
					poService.save(linett);

					// xoa het trong porder_grant_sku

					List<POrderGrant_SKU> list_grant_sku = grantskuService.getPOrderGrant_SKU(grant.getId());
					for (POrderGrant_SKU grantsku : list_grant_sku) {
						grantskuService.delete(grantsku);
					}

					// xoa trong bang porder-poline
					porder_line_Service.delete(list_porder.get(0));

					list_id.add(grant.getId());
				}
			}
			response.list_grantid_link = list_id;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<delete_many_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_many_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getporder_by_po", method = RequestMethod.POST)
	public ResponseEntity<getporder_by_po_response> GetPOrderByPO(@RequestBody getporder_by_po_request entity,
			HttpServletRequest request) {
		getporder_by_po_response response = new getporder_by_po_response();
		try {
			Long pcontract_poid_link = entity.pcontract_poid_link;
			response.data = porder_line_Service.getbyPO(pcontract_poid_link);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			return new ResponseEntity<getporder_by_po_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getporder_by_po_response>(response, HttpStatus.OK);
		}
	}
}
