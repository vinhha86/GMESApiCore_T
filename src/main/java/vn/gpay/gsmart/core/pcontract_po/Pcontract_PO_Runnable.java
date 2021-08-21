package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.gsmart.core.packingtype.IPackingTypeRepository;
import vn.gpay.gsmart.core.packingtype.PackingType;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.stockin.IStockInService;
import vn.gpay.gsmart.core.stockout.IStockOutService;

public class Pcontract_PO_Runnable implements Runnable {
	private Thread t;
	private PContract_PO po;
	private IProductPairingService pairService;
	private IPackingTypeRepository packing_repo;
	private IPOrder_POLine_Service porder_line_Service;
	private IPOrder_Product_SKU_Service porderskuService;
	private IPOrderBOMSKU_Service porderBOMSKU_Service;
	private ICutplanProcessingService cutplanProcessingService;
	private IPOrderGrant_Service porderGrantService;
	private IPOrderProcessing_Service pprocessRepository;
	private IStockInService stockInService;
	private IStockOutService stockOutService;
	private List<PContractPO_Shipping> list_shipping;
	CountDownLatch latch;
	List<PContract_PO> list_po;

	public Pcontract_PO_Runnable(PContract_PO po, IProductPairingService pairService,
			IPackingTypeRepository packing_repo, IPOrder_POLine_Service porder_line_Service,
			IPOrder_Product_SKU_Service porderskuService, IPOrderBOMSKU_Service porderBOMSKU_Service,
			ICutplanProcessingService cutplanProcessingService, IPOrderGrant_Service porderGrantService,
			IPOrderProcessing_Service pprocessRepository, IStockInService stockInService,
			IStockOutService stockOutService, List<PContractPO_Shipping> list_shipping, CountDownLatch latch,
			List<PContract_PO> list_po) {
		this.po = po;
		this.pairService = pairService;
		this.packing_repo = packing_repo;
		this.porder_line_Service = porder_line_Service;
		this.porderskuService = porderskuService;
		this.porderBOMSKU_Service = porderBOMSKU_Service;
		this.cutplanProcessingService = cutplanProcessingService;
		this.porderGrantService = porderGrantService;
		this.pprocessRepository = pprocessRepository;
		this.stockInService = stockInService;
		this.stockOutService = stockOutService;
		this.list_shipping = list_shipping;
		this.latch = latch;
		this.list_po = list_po;
	}

	public void start() {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread(this, String.valueOf(unboundedRandomValue));
			t.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			PContractPO_Shipping ship = new PContractPO_Shipping();
//			ship.setActual_quantity(po.getActual_quantity());
//			ship.setActual_shipdate(po.getActual_shipdate());
//			ship.setCode(po.getCode());
//			ship.setComment(po.getComment());
//			ship.setCurrencyid_link(po.getCurrencyid_link());
//			ship.setDate_importdata(po.getDate_importdata());
//			ship.setDatecreated(po.getDatecreated());
//			ship.setEtm_avr(po.getEtm_avr());
//			ship.setEtm_from(po.getEtm_from());
//			ship.setEtm_to(po.getEtm_to());
//			ship.setExchangerate(po.getExchangerate());
//			ship.setId(po.getId());
//			ship.setIs_tbd(po.getIs_tbd());
//			ship.setIsauto_calculate(po.getIsauto_calculate());
//			ship.setMatdate(po.getMatdate());
//			ship.setMerchandiserid_link(po.getMerchandiserid_link());
//			ship.setOrgmerchandiseid_link(po.getOrgmerchandiseid_link());
//			ship.setOrgrootid_link(po.getOrgrootid_link());
//			ship.setPackingnotice(po.getPackingnotice());
//			ship.setParentpoid_link(po.getParentpoid_link());
//			ship.setPcontractid_link(po.getPcontractid_link());
//			ship.setPlan_linerequired(po.getPlan_linerequired());
//			ship.setPlan_productivity(po.getPlan_productivity());
//			ship.setPo_buyer(po.getPo_buyer());
//			ship.setPo_quantity(po.getPo_quantity());
//			ship.setPo_typeid_link(po.getPo_typeid_link());
//			ship.setPo_vendor(po.getPo_vendor());
//			ship.setPortfromid_link(po.getPortfromid_link());
//			ship.setPorttoid_link(po.getPorttoid_link());
//			ship.setPrice_add(po.getPrice_add());
//			ship.setPrice_cmp(po.getPrice_cmp());
//			ship.setPrice_commission(po.getPrice_commission());
//			ship.setPrice_fob(po.getPrice_fob());
//			ship.setPrice_sweingfact(po.getPrice_sweingfact());
//			ship.setPrice_sweingtarget(po.getPrice_sweingtarget());
//			ship.setProductid_link(po.getProductid_link());
//			ship.setProductiondate(po.getProductiondate());
//			ship.setProductiondays(po.getProductiondays());
//			ship.setProductiondays_ns(po.getProductiondays_ns());
//			ship.setQcorgid_link(po.getQcorgid_link());
//			ship.setQcorgname(po.getQcorgname());
//			ship.setSalaryfund(po.getSalaryfund());
//			ship.setSewtarget_percent(po.getSewtarget_percent());
//			ship.setShipdate(po.getShipdate());
//			ship.setShipmodeid_link(po.getShipmodeid_link());
//			ship.setStatus(po.getStatus());
//			ship.setUnitid_link(po.getUnitid_link());
//			ship.setUsercreatedid_link(po.getUsercreatedid_link());
//			ship.setProductbuyercode(po.getProductbuyercode());
//			ship.setPortFrom(po.getPortFrom());
//			ship.setShipmode_name(po.getShipMode());
//			ship.setIsmap(po.getIsmap());
//			ship.setOrdercode(po.getOrdercode());

			List<ProductPairing> p = pairService.getproduct_pairing_detail_bycontract(po.getOrgrootid_link(),
					po.getPcontractid_link(), po.getProductid_link());
			int total = 1;
			if (p.size() > 0) {
				total = 0;
				for (ProductPairing pair : p) {
					total += pair.getAmount();
				}
			}
			ship.setTotalpair(total);

			if (!po.getPackingnotice().equals("") && !po.getPackingnotice().equals("null")
					&& !po.getPackingnotice().equals(null)) {
				String[] arr_id = po.getPackingnotice().split(";");
				List<Long> list_id = new ArrayList<Long>();
				for (String id : arr_id) {
					list_id.add(Long.parseLong(id));
				}
				List<PackingType> list_packing = packing_repo.getbylistid(po.getOrgrootid_link(), list_id);
				String packing_method = "";
				for (PackingType packing : list_packing) {
					if (packing_method != "") {
						packing_method += ", " + packing.getCode();
					} else {
						packing_method = packing.getCode();
					}
				}
				ship.setPacking_method(packing_method);
			}

			Long pcontract_poid_link = po.getId();
			List<POrder> porder_list = porder_line_Service.getporder_by_po(pcontract_poid_link);

			// SL Cắt
			// Tính theo sl vải chính đã cắt được cho bao nhiêu sản phẩm
			// Nếu sp dùng 2 vải chính trở lên thì lấy số lượng loại vải chính đã cắt cho sp
			// bé nhất
//			Integer totalamountcut = 0;
//			if (porder_list.size() > 0) {
//				for (POrder porder : porder_list) {
//					Long porderid_link = porder.getId();
//					// Lấy danh sách sku của Porder
//					List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(porderid_link);
//					for (POrder_Product_SKU porderProductSKU : porderProductSkus) {
//						// Tìm xem sku này đã cắt được bao nhiêu chiếc
//						Long product_skuid_link = porderProductSKU.getSkuid_link();
//
//						// Tìm danh sách các loại vải chính dùng cho sku này
//						List<Long> material_skuid_link_list = porderBOMSKU_Service
//								.getMaterialList_By_Porder_Sku(porderid_link, product_skuid_link, SkuType.SKU_TYPE_VAI);
//
//						// Mỗi loại vải dùng cho sku đã cắt được bao nhiêu chiếc,
//						// lưu vào amountList sau đố lấy số nhỏ nhất
//						// vd sp1 vải 1 cắt 10, vải 2 cắt 5 thì tính là cắt được 5 chiếc
//						List<Integer> amountList = new ArrayList<Integer>();
//						for (Long material_skuid_link : material_skuid_link_list) {
//							Integer amountcut = cutplanProcessingService.getSlCat_by_product_material_porder(
//									product_skuid_link, material_skuid_link, porderid_link);
//							amountList.add(amountcut);
//						}
//						if (amountList.size() > 0) {
//							Integer min = Collections.min(amountList);
//							totalamountcut += min;
//						}
//						if (porderid_link.equals(4020L)) {
////							System.out.println("pcontract po ser line 375");
////							System.out.println(amountList);
//						}
//					}
//				}
//			}
//			ship.setAmountcut(totalamountcut);
//
//			// SL Vào chuyền, Ra chuyền, Hoàn thiện ,Đóng gói,
//			Integer amountinputsum = 0;
//			Integer amountoutputsum = 0;
//			Integer amountpackstockedsum = 0;
//			Integer amountpackedsum = 0;
////			Integer amountstockedsum = 0;
//			if (porder_list.size() > 0) {
////				POrder porder = porder_list.get(0);
//				for (POrder porder : porder_list) {
//					Long porderid_link = porder.getId();
//					List<POrderGrant> porderGrant_list = porderGrantService.getByOrderId(porderid_link);
//					for (POrderGrant porderGrant : porderGrant_list) {
//						List<POrderProcessing> porderProcessing_list = pprocessRepository
//								.getByPOrderAndPOrderGrantAndMaxDate(porderid_link, porderGrant.getId());
//						if (porderProcessing_list.size() > 0) {
//							POrderProcessing porderProcessing = porderProcessing_list.get(0);
//							amountinputsum += porderProcessing.getAmountinputsum() == null ? 0
//									: porderProcessing.getAmountinputsum();
//							amountoutputsum += porderProcessing.getAmountoutputsum() == null ? 0
//									: porderProcessing.getAmountoutputsum();
//							amountpackstockedsum += porderProcessing.getAmountpackstockedsum() == null ? 0
//									: porderProcessing.getAmountpackstockedsum();
//							amountpackedsum += porderProcessing.getAmountpackedsum() == null ? 0
//									: porderProcessing.getAmountpackedsum();
////							amountstockedsum += porderProcessing.getAmountstockedsum() == null ? 0 : porderProcessing.getAmountstockedsum();
//						}
//					}
//				}
//			}
//			ship.setAmountinputsum(amountinputsum);
//			ship.setAmountoutputsum(amountoutputsum);
//			ship.setAmountpackstockedsum(amountpackstockedsum);
//			ship.setAmountpackedsum(amountpackedsum);
////			ship.setAmountstockedsum(amountstockedsum);
//
//			// Thành phẩm
//			Integer amountstockedsum = 0;
//			List<StockIn> stockin_list = stockInService.findByPO_Type_Status(po.getId(),
//					StockinType.STOCKIN_TYPE_TP_NEW, StockinStatus.STOCKIN_STATUS_APPROVED);
//			if (stockin_list.size() > 0) {
//				for (StockIn stockin : stockin_list) {
//					amountstockedsum += stockin.getTotalpackage();
//				}
//			}
//			ship.setAmountstockedsum(amountstockedsum);
//
//			// SL Giao hàng
//			Integer amountgiaohang = 0;
//			List<StockOut> stockOut_list = stockOutService.findByPO_Type_Status(po.getId(),
//					StockoutTypes.STOCKOUT_TYPE_TP_PO, StockoutStatus.STOCKOUT_STATUS_APPROVED);
//			for (StockOut stockout : stockOut_list) {
//				List<StockOutD> StockOutD_list = stockout.getStockoutd();
//				for (StockOutD stockOutD : StockOutD_list) {
//					List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
//					if (stockOutPklist_list != null) {
//						amountgiaohang += stockOutPklist_list.size();
//					}
//				}
//			}
//			ship.setAmountgiaohang(amountgiaohang);

			list_shipping.add(ship);
			list_po.remove(po);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			latch.countDown();
		}
	}

}
