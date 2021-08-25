package vn.gpay.gsmart.core.pcontract_po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.gsmart.core.packingtype.IPackingTypeRepository;
import vn.gpay.gsmart.core.packingtype.PackingType;
import vn.gpay.gsmart.core.pcontract_price.IPContract_Price_Repository;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.stockin.IStockInService;
import vn.gpay.gsmart.core.stockout.IStockOutService;

@Service
public class PContract_POService extends AbstractService<PContract_PO> implements IPContract_POService {
	@Autowired
	IPContract_PORepository repo;
	@Autowired
	IPContract_Price_Repository price_repo;
	@Autowired
	EntityManager em;
	@Autowired
	IPackingTypeRepository packing_repo;
	@Autowired
	IPOrder_POLine_Service porder_line_Service;
	@Autowired
	IPOrderGrant_Service porderGrantService;
	@Autowired
	IPOrderProcessing_Service pprocessRepository;
	@Autowired
	IStockOutService stockOutService;
	@Autowired
	IStockInService stockInService;
	@Autowired
	IPOrder_Product_SKU_Service porderskuService;
	@Autowired
	IPOrderBOMSKU_Service porderBOMSKU_Service;
	@Autowired
	ICutplanProcessingService cutplanProcessingService;
	@Autowired
	IProductService productService;
	@Autowired
	IProductPairingService pairService;

	@Override
	protected JpaRepository<PContract_PO, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<PContract_PO> getPOByContractProduct(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Long userid_link, Long orgid_link, Integer potype) {
		if (orgid_link == 1)
			userid_link = null;
		if (potype == 0)
			return repo.getPO_Chaogia(orgrootid_link, pcontractid_link, productid_link, userid_link);
		else
			return repo.getPO_Duyet(orgrootid_link, pcontractid_link, productid_link, userid_link);
	}

	@Override
	public List<PContract_PO> getPOByContractAndProduct(Long pcontractid_link, Long productid_link) {
		return repo.getPOByContractAndProduct(pcontractid_link, productid_link);
	}

//	@Override
//	//Chi lay cac PO o muc la
//	public List<PContract_PO> getPO_LeafOnly(Long orgrootid_link,
//			Long pcontractid_link,Long productid_link, Long userid_link, Long orgid_link){
//		try{
//			if(orgid_link == 1) userid_link = null;
//			List<PContract_PO> a = repo.getPOByContractProduct(orgrootid_link, pcontractid_link, productid_link, userid_link);
//			return a;
//		} catch(Exception ex){
//			ex.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public List<PContract_PO> getPOLeafOnlyByContract(Long pcontractid_link, Long productid_link) {
		try {
			productid_link = productid_link == 0 ? null : productid_link;
			return repo.getPOLeafOnlyByContract(pcontractid_link, productid_link);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public List<PContract_PO> getPOByContract(Long orgrootid_link, Long pcontractid_link) {
		return repo.getPOByContract(orgrootid_link, pcontractid_link);
	}

	@Override
	public List<PContract_PO> getPO_LaterShipdate(Long orgrootid_link, Long pcontractid_link, Long productid_link,
			Date shipdate) {
		return repo.getPO_LaterShipdate(orgrootid_link, pcontractid_link, productid_link, shipdate);
	}

	@Override
	public List<PContract_PO> getPO_Offer_Accept_ByPContract(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		try {
			return repo.getPO_Offer_Accept_ByPContract(pcontractid_link, productid_link == 0 ? null : productid_link);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PContract_PO> getPcontractPoByPContractAndPOBuyer(Long pcontractid_link, String po_buyer,
			String buyercode) {
		return repo.getPcontractPoByPContractAndPOBuyer(pcontractid_link, po_buyer, buyercode);
	}

	@Override
	public List<PContract_PO> getone_by_template_set(String PO_No, Date ShipDate, long productid_link,
			long shipmodeid_link, long pcontractid_link) {
		Long shipmode = shipmodeid_link == 0 ? null : shipmodeid_link;
		return repo.getone_by_template_set(PO_No, shipmode, productid_link, ShipDate, pcontractid_link);
	}

	@Override
	public List<PContract_PO> check_exist_po(Date ShipDate, long productid_link, long shipmodeid_link,
			long pcontractid_link, String po_buyer) {
		po_buyer = (po_buyer == "" || po_buyer.toUpperCase() == "TBD") ? null : po_buyer;
		List<PContract_PO> list_po = repo.getone_by_template(shipmodeid_link, productid_link, ShipDate,
				pcontractid_link, po_buyer);

		return list_po;
	}

	@Override
	public List<PContract_PO> get_by_parentid(Long pcontractpo_parentid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parentid_link(pcontractpo_parentid_link);
	}

	@Override
	public List<PContract_PO> check_exist_po_children(String PO_No, Date Shipdate, Long shipmodeid_link,
			Long pcontractid_link, Long parentid_link) {
		// TODO Auto-generated method stub
		return repo.getone_po_upload(PO_No, shipmodeid_link, Shipdate, pcontractid_link, parentid_link);
	}

	@Override
	public List<PContract_PO> check_exist_PONo(String PO_No, Long pcontractid_link) {
		return repo.getone_po_byPO_no(PO_No, pcontractid_link);
	}

	@Override
	public List<PContract_PO> getBySearch(String po_code, List<Long> orgs) {
		List<PContract_PO> lst = new ArrayList<PContract_PO>();
//		if (products.size() > 0)
//			if (orgs.size() > 0)
//				lst = repo.getBySearch(po_code,orgs);
//			else
//				lst = repo.getBySearch_ProductOnly(po_code);
//		else 
		po_code = po_code == null ? "" : po_code;
		if (orgs.size() > 0)
			lst = repo.getBySearch_OrgOnly(po_code, orgs);
		else
			lst = repo.getBySearch_CodeOnly(po_code);

		return lst;
	}

	@Override
	public List<PContract_POBinding> getForMarketTypeChart() {

		List<PContract_POBinding> data = new ArrayList<PContract_POBinding>();
		List<Object[]> objects = repo.getForMarketTypeChart();

		for (Object[] row : objects) {
			Long sum = (Long) row[0];
			String name = (String) row[1] == null ? "Khác" : (String) row[1];
			PContract_POBinding temp = new PContract_POBinding();
			temp.setSum(sum);
			temp.setMarketName(name);
			data.add(temp);
		}

		return data;
	}

	@Override
	public List<PContract_PO> getPO_Offer_Accept_ByPContract_AndOrg(Long pcontractid_link, Long productid_link,
			List<Long> list_orgid_link) {
		// TODO Auto-generated method stub
		productid_link = productid_link == 0 ? null : productid_link;
		if (list_orgid_link.size() == 0)
			return repo.getPO_Offer_Accept_ByPContract(pcontractid_link, productid_link);
		else
			return repo.getPO_Offer_Accept_ByPContract_AndOrg(pcontractid_link, productid_link, list_orgid_link);
	}

	@Override
	public List<PContract_PO> getby_porder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_porder(porderid_link);
	}

	@Override
	public List<PContract_PO> check_exist_line(Date ShipDate, long productid_link, long pcontractid_link,
			long parentid_link) {
		// TODO Auto-generated method stub
		return repo.getone_line_giaohang(productid_link, ShipDate, pcontractid_link, parentid_link);
	}

	@Override
	public List<PContract_PO> get_by_parent_and_type(Long pcontractpo_parentid_link, int po_typeid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parent_and_type(pcontractpo_parentid_link, po_typeid_link);
	}

	@Override
	public List<PContract_PO> getall_offers_by_org(List<Long> orgid_link) {
		// TODO Auto-generated method stub
		return repo.getOffers_byOrg(orgid_link);
	}

	@Override
	public List<Long> getpcontract_BySearch(String po_code, List<Long> orgs) {
		// TODO Auto-generated method stub
		po_code = po_code == null ? "" : po_code;
		orgs = orgs.size() == 0 ? null : orgs;

		return repo.getPContractBySearch_OrgOnly(po_code, orgs);
	}

	@Override
	public List<PContract_PO> getBySearch_andType(String po_code, List<Long> orgs, int po_type) {
		// TODO Auto-generated method stub
		orgs = orgs.size() == 0 ? null : orgs;
		return repo.getBySearch_OrgAndType(po_code, orgs, po_type);
	}

	@Override
	public List<PContract_PO> getby_pcontract_and_type(Long pcontractid_link, List<Integer> type) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_and_type(type, pcontractid_link);
	}

	@Override
	public List<PContractPO_Shipping> get_po_shipping(List<Long> orgs, int po_type, Date shipdate_from,
			Date shipdate_to, Long orgrootid_link, Boolean ismap) {
		// TODO Auto-generated method stub
		orgs = orgs.size() == 0 ? null : orgs;
		ismap = ismap == false ? null : true;

		List<PContract_PO> list_po = repo.getby_process_shipping(shipdate_from, shipdate_to, orgs, po_type, ismap);
		List<PContractPO_Shipping> list_shipping = new ArrayList<PContractPO_Shipping>();
		for (PContract_PO po : list_po) {
			PContractPO_Shipping ship = new PContractPO_Shipping();
//			ship.setActual_quantity(po.getActual_quantity());
//			ship.setActual_shipdate(po.getActual_shipdate());
			ship.setCode(po.getCode());
//			ship.setComment(po.getComment());
//			ship.setCurrencyid_link(po.getCurrencyid_link());
//			ship.setDate_importdata(po.getDate_importdata());
//			ship.setDatecreated(po.getDatecreated());
//			ship.setEtm_avr(po.getEtm_avr());
//			ship.setEtm_from(po.getEtm_from());
//			ship.setEtm_to(po.getEtm_to());
//			ship.setExchangerate(po.getExchangerate());
			ship.setId(po.getId());
//			ship.setIs_tbd(po.getIs_tbd());
//			ship.setIsauto_calculate(po.getIsauto_calculate());
//			ship.setMatdate(po.getMatdate());
//			ship.setMerchandiserid_link(po.getMerchandiserid_link());
//			ship.setOrgmerchandiseid_link(po.getOrgmerchandiseid_link());
//			ship.setOrgrootid_link(po.getOrgrootid_link());
//			ship.setPackingnotice(po.getPackingnotice());
//			ship.setParentpoid_link(po.getParentpoid_link());
			ship.setPcontractid_link(po.getPcontractid_link());
//			ship.setPlan_linerequired(po.getPlan_linerequired());
//			ship.setPlan_productivity(po.getPlan_productivity());
			ship.setPo_buyer(po.getPo_buyer());
			ship.setPo_quantity(po.getPo_quantity());
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
			ship.setProductid_link(po.getProductid_link());
//			ship.setProductiondate(po.getProductiondate());
//			ship.setProductiondays(po.getProductiondays());
//			ship.setProductiondays_ns(po.getProductiondays_ns());
//			ship.setQcorgid_link(po.getQcorgid_link()); 
//			ship.setQcorgname(po.getQcorgname());
//			ship.setSalaryfund(po.getSalaryfund());
//			ship.setSewtarget_percent(po.getSewtarget_percent());
			ship.setShipdate(po.getShipdate());
//			ship.setShipmodeid_link(po.getShipmodeid_link());
//			ship.setStatus(po.getStatus());
//			ship.setUnitid_link(po.getUnitid_link());
//			ship.setUsercreatedid_link(po.getUsercreatedid_link());
			ship.setProductbuyercode(po.getProductbuyercode());
			ship.setPortFrom(po.getPortFrom());
			ship.setShipmode_name(po.getShipMode());
			ship.setIsmap(po.getIsmap());
//			ship.setOrdercode(po.getOrdercode());

			List<ProductPairing> p = pairService.getproduct_pairing_detail_bycontract(orgrootid_link,
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
				List<PackingType> list_packing = packing_repo.getbylistid(orgrootid_link, list_id);
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

//			Long pcontract_poid_link = po.getId();
//			List<POrder> porder_list = porder_line_Service.getporder_by_po(pcontract_poid_link);

			// SL Cắt
			// Tính theo sl vải chính đã cắt được cho bao nhiêu sản phẩm
			// Nếu sp dùng 2 vải chính trở lên thì lấy số lượng loại vải chính đã cắt cho sp
			// bé nhất
//			Integer totalamountcut = 0;
//			if(porder_list.size() > 0) {
//				for(POrder porder : porder_list) {
//					Long porderid_link = porder.getId();
//					// Lấy danh sách sku của Porder
//					List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(porderid_link);
//					for(POrder_Product_SKU porderProductSKU : porderProductSkus) {
//						// Tìm xem sku này đã cắt được bao nhiêu chiếc
//						Long product_skuid_link = porderProductSKU.getSkuid_link();
//						
//						// Tìm danh sách các loại vải chính dùng cho sku này
//						List<Long> material_skuid_link_list = porderBOMSKU_Service.getMaterialList_By_Porder_Sku(
//								porderid_link, product_skuid_link, SkuType.SKU_TYPE_VAI
//								);
//						
//						// Mỗi loại vải dùng cho sku đã cắt được bao nhiêu chiếc, 
//						// lưu vào amountList sau đố lấy số nhỏ nhất
//						// vd sp1 vải 1 cắt 10, vải 2 cắt 5 thì tính là cắt được 5 chiếc
//						List<Integer> amountList = new ArrayList<Integer>();
//						for(Long material_skuid_link : material_skuid_link_list) {
//							Integer amountcut = cutplanProcessingService.getSlCat_by_product_material_porder(
//									product_skuid_link, material_skuid_link, porderid_link );
//							amountList.add(amountcut);
//						}
//						if(amountList.size() > 0) {
//							Integer min = Collections.min(amountList);
//							totalamountcut += min;
//						}
//						if(porderid_link.equals(4020L)) {
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
//			if(porder_list.size() > 0) {
////				POrder porder = porder_list.get(0);
//				for(POrder porder : porder_list) {
//					Long porderid_link = porder.getId();
//					List<POrderGrant> porderGrant_list = porderGrantService.getByOrderId(porderid_link);
//					for(POrderGrant porderGrant : porderGrant_list) {
//						List<POrderProcessing> porderProcessing_list = pprocessRepository.getByPOrderAndPOrderGrantAndMaxDate(porderid_link, porderGrant.getId());
//						if(porderProcessing_list.size() > 0) {
//							POrderProcessing porderProcessing = porderProcessing_list.get(0);
//							amountinputsum += porderProcessing.getAmountinputsum() == null ? 0 : porderProcessing.getAmountinputsum();
//							amountoutputsum += porderProcessing.getAmountoutputsum() == null ? 0 : porderProcessing.getAmountoutputsum();
//							amountpackstockedsum += porderProcessing.getAmountpackstockedsum() == null ? 0 : porderProcessing.getAmountpackstockedsum();
//							amountpackedsum += porderProcessing.getAmountpackedsum() == null ? 0 : porderProcessing.getAmountpackedsum();
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
//			List<StockIn> stockin_list = stockInService.findByPO_Type_Status(
//					po.getId(), StockinType.STOCKIN_TYPE_TP_NEW, StockinStatus.STOCKIN_STATUS_APPROVED
//					);
//			if(stockin_list.size() > 0) {
//				for(StockIn stockin : stockin_list) {
//					amountstockedsum += stockin.getTotalpackage();
//				}
//			}
//			ship.setAmountstockedsum(amountstockedsum);
//			
//			// SL Giao hàng
//			Integer amountgiaohang = 0;
//			List<StockOut> stockOut_list = stockOutService.findByPO_Type_Status(
//					po.getId(), StockoutTypes.STOCKOUT_TYPE_TP_PO, StockoutStatus.STOCKOUT_STATUS_APPROVED
//					);
//			for(StockOut stockout : stockOut_list) {
//				List<StockOutD> StockOutD_list = stockout.getStockoutd();
//				for(StockOutD stockOutD : StockOutD_list) {
//					List<StockOutPklist> stockOutPklist_list = stockOutD.getStockout_packinglist();
//					if(stockOutPklist_list != null) {
//						amountgiaohang += stockOutPklist_list.size();
//					}
//				}
//			}
//			ship.setAmountgiaohang(amountgiaohang);

			list_shipping.add(ship);
		}
		return list_shipping;
	}

	@Override
	public List<PContract_PO> getbycode_and_type_and_product(String po_no, int type, Long pcontractid_link,
			Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getbycode_and_type_and_product(pcontractid_link, po_no, type, productid_link);
	}

	@Override
	public List<PContract_PO> getpo_notin_list(List<String> list_po, int type, Long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getnotin_list_pono(pcontractid_link, list_po, type);
	}

	@Override
	public List<PContract_PO> getby_pcontract_and_type_andproduct(Long pcontractid_link, List<Integer> type,
			Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getby_pcontract_and_type_and_product(type, pcontractid_link, productid_link);
	}

	@Override
	public List<PContract_PO> getpo_byid(Long pcontractpoid_link) {
		// TODO Auto-generated method stub
		return repo.getbyId(pcontractpoid_link);
	}

	@Override
	public List<PContract_PO> get_by_parent_and_type_and_MauSP(Long pcontractpo_parentid_link, int po_typeid_link,
			Long mausanphamid_link) {
		// TODO Auto-generated method stub
		return repo.getby_parent_and_type_and_mausp(pcontractpo_parentid_link, mausanphamid_link, po_typeid_link);
	}

	@Override
	public Integer getTotalProductinPcontract(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getTotalProductinPcontract(pcontractid_link, productid_link);
	}

	@Override
	public Float getTotalPriceProductInPcontract(Long pcontractid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return null;
	}
}
