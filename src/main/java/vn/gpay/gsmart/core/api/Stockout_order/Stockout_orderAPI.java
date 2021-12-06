package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_bom_color.IPOrderBomColor_Service;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.gsmart.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_coloramount_Service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_d_service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_pkl_Service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_service;
import vn.gpay.gsmart.core.stockout_order.Stockout_order;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_coloramount;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_d;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_pkl;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderBomType;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.StockoutTypes;
import vn.gpay.gsmart.core.utils.commonUnit;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stockoutorder")
public class Stockout_orderAPI {
	@Autowired IStockout_order_service stockout_order_Service;
	@Autowired IStockout_order_d_service stockout_order_d_Service;
	@Autowired IStockout_order_pkl_Service stockout_pkl_Service;
	@Autowired IStockout_order_coloramount_Service amount_color_Service;
	@Autowired IPOrder_Product_SKU_Service porderskuService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPOrderBomColor_Service bomcolorService;
	@Autowired IPOrderBOMSKU_Service bomskuService;
	@Autowired IWarehouseService warehouseService;
	@Autowired Common commonService;
	@Autowired IStocking_UniqueCode_Service stockingService;
	
	@Autowired IPContract_POService pcontract_po_Service;
	@Autowired IPOrderGrant_Service porder_grantService;
	@Autowired IProductPairingService pairService;
	@Autowired IPContractProductSKUService pskuservice;
	@Autowired IOrgService orgService;;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<create_order_response> Create(HttpServletRequest request,
			@RequestBody create_order_request entity) {
		create_order_response response = new create_order_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Date current_time = new Date();
			Stockout_order order = entity.data;
			boolean isNew = false;
			
			if(order.getId() == null) {
				isNew = true;
				String stockout_order_code = commonService.getStockout_order_code();
				
				order.setOrgrootid_link(orgrootid_link);
				order.setTimecreate(current_time);
				order.setUsercreateid_link(user.getId());
				order.setStockout_order_code(stockout_order_code);
				order.setStatus(0);
				
				
			}
			
			order = stockout_order_Service.save(order);
			
			//kiem tra neu them moi thi cap nhat lai bang stocking_unique
			if(isNew) {
				Stocking_UniqueCode unique = stockingService.getby_type(3);
				Integer max = unique.getStocking_max();
				unique.setStocking_max(max+1);
				stockingService.save(unique);
			}
			
			Long stockout_orderid_link = order.getId();
			
			for(Stockout_order_d detail : entity.detail) {
				detail.setStockoutorderid_link(stockout_orderid_link);
				detail.setUnitid_link(order.getUnitid_link());
				if(detail.getId() == null) {
					detail.setTimecreate(current_time);
					detail.setUsercreateid_link(user.getId());
					detail.setOrgrootid_link(orgrootid_link);
				}
				else {
					detail.setLasttimeupdate(current_time);
					detail.setLastuserupdateid_link(user.getId());
				}
				
				Long stockout_order_did_link = detail.getId();
				
				for(Stockout_order_pkl pkl : detail.getStockout_order_pkl()) {
					pkl.setStockoutorderdid_link(stockout_order_did_link);
					pkl.setStockoutorderid_link(stockout_orderid_link);
					
					if(pkl.getId() == null) {
						pkl.setTimecreate(current_time);
						pkl.setUsercreateid_link(user.getId());
						pkl.setOrgid_link(orgrootid_link);
					}
					else {
						pkl.setLasttimeupdate(current_time);
						pkl.setLastuserupdateid_link(user.getId());
					}
				}
								
				detail = stockout_order_d_Service.save(detail);
			}
			
			//kiem tra bang color_amout co chua? chua co thi them so luong = 0 vao cho cac mau
			Long porderid_link = order.getPorderid_link();
			List<POrder_Product_SKU> list_porder_sku = porderskuService.getlist_sku_in_porder(orgrootid_link, porderid_link);
			
			for(POrder_Product_SKU porder_sku : list_porder_sku) {
				List<Stockout_order_coloramount> list_amout_sku = amount_color_Service.getby_stockoutorder_and_sku(stockout_orderid_link, porder_sku.getSkuid_link());
				if(list_amout_sku.size() == 0) {
					Stockout_order_coloramount color_amout = new Stockout_order_coloramount();
					color_amout.setAmount(0);
					color_amout.setSkuid_link(porder_sku.getSkuid_link());
					color_amout.setId(null);
					color_amout.setStockoutorderid_link(stockout_orderid_link);
					
					amount_color_Service.save(color_amout);
				}
			}
			
			response.id = order.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<create_order_response>(response, HttpStatus.OK);
	}
	

	
	@RequestMapping(value = "/getby_id", method = RequestMethod.POST)
	public ResponseEntity<getby_id_response> GetById(HttpServletRequest request,
			@RequestBody getby_id_request entity) {
		getby_id_response response = new getby_id_response();
		try {
			response.data = stockout_order_Service.findOne(entity.id);
			response.detail = stockout_order_d_Service.getby_Stockout_order(entity.id);
			response.color = amount_color_Service.getby_stockout_Order(entity.id);
			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			
//			//test
////			Database database = DatabaseBuilder.open(mdbFile)
//			Database db= DatabaseBuilder.open(new File("C:\\Users\\Phong\\Downloads\\ATSOFT\\data 20_03_2020.mdb"));  
//			System.out.println(db.getDatabasePassword() );
//			System.out.println();
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getby_id_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<delete_stockout_order_response> Delete(HttpServletRequest request,
			@RequestBody delete_stockout_order_request entity) {
		delete_stockout_order_response response = new delete_stockout_order_response();
		try {
			stockout_order_Service.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<delete_stockout_order_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_stockout_orderd", method = RequestMethod.POST)
	public ResponseEntity<update_stockout_orderd_response> StockoutOrderD(HttpServletRequest request,
			@RequestBody update_stockout_orderd_request entity) {
		update_stockout_orderd_response response = new update_stockout_orderd_response();
		try {
			stockout_order_d_Service.save(entity.data);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<update_stockout_orderd_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete_detail", method = RequestMethod.POST)
	public ResponseEntity<delete_stockout_order_d_response> DeleteDetail(HttpServletRequest request,
			@RequestBody delete_stockout_order_d_request entity) {
		delete_stockout_order_d_response response = new delete_stockout_order_d_response();
		try {
			stockout_order_d_Service.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<delete_stockout_order_d_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getpkl_bydetail", method = RequestMethod.POST)
	public ResponseEntity<get_pkl_by_detail_response> getPKLByDetail(HttpServletRequest request,
			@RequestBody get_pkl_by_detail_request entity) {
		get_pkl_by_detail_response response = new get_pkl_by_detail_response();
		try {
			response.data = stockout_pkl_Service.getby_detail(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<get_pkl_by_detail_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/add_pkl", method = RequestMethod.POST)
	public ResponseEntity<add_pkl_response> AddPKL(HttpServletRequest request,
			@RequestBody add_pkl_request entity) {
		add_pkl_response response = new add_pkl_response();
		GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgid_link = user.getOrgid_link();
		try {
//			Stockout_order order = stockout_order_Service.findOne(entity.stockoutorderid_link);
			for(Warehouse wh : entity.data) {
				Stockout_order_pkl pkl = new Stockout_order_pkl();
				pkl.setColorid_link(wh.getColorid_link());
				pkl.setEncryptdatetime(wh.getEncryptdatetime());
				pkl.setEpc(wh.getEpc());
				pkl.setId(null);
				pkl.setLotnumber(wh.getLotnumber());
				pkl.setMet(wh.getMet());
				pkl.setOrgid_link(orgid_link);
				pkl.setPackageid(wh.getPackageid());
				pkl.setSkuid_link(wh.getSkuid_link());
				pkl.setStockoutorderdid_link(entity.stockoutorderdid_link);
				pkl.setStockoutorderid_link(entity.stockoutorderid_link);
				pkl.setTimecreate(new Date());
				pkl.setUsercreateid_link(user.getId());
				pkl.setWidth(wh.getWidth());
				pkl.setYdsorigin((float)(wh.getMet()* commonUnit.metToyead));
				pkl.setStatus(wh.getStatus());
				
				stockout_pkl_Service.save(pkl);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<add_pkl_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public ResponseEntity<calculate_response> Calculate(HttpServletRequest request,
			@RequestBody calculate_request entity) {
		calculate_response response = new calculate_response();
		try {
			Stockout_order order = stockout_order_Service.findOne(entity.id);
			//Lay danh sach cac mau cua lenh
			List<Stockout_order_coloramount> list_color_amount = amount_color_Service.getby_stockout_Order(entity.id);
			list_color_amount.removeIf(c->c.getAmount().equals(0) || c.getAmount().equals(null));
			//lay nhung npl cua detail
			List<Stockout_order_d> list_detail = stockout_order_d_Service.getby_Stockout_order(entity.id);
			
			for(Stockout_order_d detail : list_detail) {
				float amount_req = 0;
				for(Stockout_order_coloramount color : list_color_amount) {
					int amount = color.getAmount() == null ? 0 : color.getAmount();
					List<POrderBOMSKU> list_bom_sku = bomskuService.getby_porder_and_material_and_sku_and_type(order.getPorderid_link(), 
							detail.getMaterial_skuid_link(), color.getSkuid_link(), POrderBomType.CanDoi);
					if(list_bom_sku.size() > 0) {
						float bom = list_bom_sku.get(0).getAmount() == null ? 0 : list_bom_sku.get(0).getAmount();
						amount_req += amount * bom;
					}	
				}
				detail.setTotalyds((float)(amount_req * commonUnit.metToyead));
				detail.setTotalmet(amount_req);
				stockout_order_d_Service.save(detail);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<calculate_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_color_amount", method = RequestMethod.POST)
	public ResponseEntity<update_color_amount_response> UpdateColorAmount(HttpServletRequest request,
			@RequestBody update_color_amount_request entity) {
		update_color_amount_response response = new update_color_amount_response();
		try {
			amount_color_Service.save(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<update_color_amount_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/unlock_pkl", method = RequestMethod.POST)
	public ResponseEntity<unlock_stockout_pkl_response> UnlockStockoutPKL(HttpServletRequest request,
			@RequestBody unlock_stockout_pkl_request entity) {
		unlock_stockout_pkl_response response = new unlock_stockout_pkl_response();
		try {
			stockout_pkl_Service.deleteById(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<unlock_stockout_pkl_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getdetail_byorder", method = RequestMethod.POST)
	public ResponseEntity<get_detail_by_order_response> getDetailbyOrder(HttpServletRequest request,
			@RequestBody get_detail_by_order_request entity) {
		get_detail_by_order_response response = new get_detail_by_order_response();
		try {
			Stockout_order stockout_order = stockout_order_Service.findOne(entity.id);
			
			response.data = stockout_order_d_Service.getby_Stockout_order(entity.id);
			for(Stockout_order_d item : response.data) {
				String data_spaces = warehouseService.getspaces_bysku(stockout_order.getOrgid_from_link(), item.getMaterial_skuid_link());
				item.setData_spaces(data_spaces);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<get_detail_by_order_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getby_porder", method = RequestMethod.POST)
	public ResponseEntity<getby_porder_response> GetByPorder(HttpServletRequest request,
			@RequestBody getby_porder_request entity) {
		getby_porder_response response = new getby_porder_response();
		try {
			response.data = stockout_order_Service.getby_porder(entity.porderid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getby_porder_npl", method = RequestMethod.POST)
	public ResponseEntity<getby_porder_response> GetByPorderNPL(HttpServletRequest request,
			@RequestBody getby_porder_npl_request entity) {
		getby_porder_response response = new getby_porder_response();
		try {
			Long porderid_link = entity.porderid_link;
			Long material_skuid_link = entity.material_skuid_link;
			
			response.data = stockout_order_Service.getby_porder_npl(porderid_link, material_skuid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getby_porder_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getStockoutorder", method = RequestMethod.POST)
	public ResponseEntity<Stockout_order_response> getStockoutorder(HttpServletRequest request,
			@RequestBody Stockout_order_getBySearch_request entity) {
		Stockout_order_response response = new Stockout_order_response();
		try {
			Date stockoutorderdate_from = entity.stockoutorderdate_from;
			Date stockoutorderdate_to = entity.stockoutorderdate_to;
			Integer stockouttypeid_link = entity.stockouttypeid_link;
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
//			List<Stockout_order> result = stockout_order_Service.findBySearch(stockoutorderdate_from, stockoutorderdate_to);
			List<Stockout_order> result = stockout_order_Service.findBySearch_type(stockoutorderdate_from, stockoutorderdate_to, stockouttypeid_link);
			for (Stockout_order po : result) {
				List<ProductPairing> p = pairService.getproduct_pairing_detail_bycontract(po.getOrgrootid_link(),
						po.getPcontractid_link(), po.getP_skuid_link());
				int total = 1;
				if (p.size() > 0) {
					total = 0;
					for (ProductPairing pair : p) {
						total += pair.getAmount();
					}
				}
				po.setTotalpair(total);
				po.setPo_quantity_sp(po.getPo_quantity() * total);
			}
			
			response.data = result;
			response.totalCount = result.size();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<Stockout_order_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create_YeuCauXuat", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<create_order_response> create_YeuCauXuat(HttpServletRequest request,
			@RequestBody create_stockout_order_request entity) {
		create_order_response response = new create_order_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			Date current_time = new Date();
			
			Long pordergrantid_link = entity.pordergrantid_link;
			POrderGrant porderGrant = porder_grantService.findOne(pordergrantid_link);
			Org toChuyen = orgService.findOne(porderGrant.getGranttoorgid_link());
			Org phanXuong = orgService.findOne(toChuyen.getParentid_link());
			
			// tim kho vai dau tien cua phan xuong
			List<Org> khoVai_list = orgService.findChildByType(orgrootid_link, phanXuong.getId(), OrgType.ORG_TYPE_KHONGUYENLIEU);
			Long orgid_from_link = null;
			if(khoVai_list.size() > 0) {
				orgid_from_link = khoVai_list.get(0).getId();
			}
			
			//
			Long porderid_link = porderGrant.getPorderid_link();
			POrder porder = porderService.findOne(porderid_link);
			Long pcontractid_link = porder.getPcontractid_link();
			
			List<Stockout_order> Stockout_order_list = entity.data;
			for(Stockout_order stockout_order : Stockout_order_list) {
				// voi moi stockout_order_d tao moi 1 stockout_order
				String stockout_order_code = commonService.getStockout_order_code();
				stockout_order.setId(null);
				stockout_order.setOrgrootid_link(orgrootid_link);
				stockout_order.setTimecreate(current_time);
				stockout_order.setUsercreateid_link(user.getId());
				stockout_order.setLasttimeupdate(current_time);
				stockout_order.setLastuserupdateid_link(user.getId());
				stockout_order.setStockout_order_code(stockout_order_code);
				stockout_order.setStockouttypeid_link(StockoutTypes.STOCKOUT_TYPE_NL_CUTHOUSE);
				stockout_order.setOrgid_from_link(orgid_from_link);
				stockout_order.setPcontractid_link(pcontractid_link);
				stockout_order.setPorderid_link(porderid_link);
				stockout_order.setPorder_grantid_link(pordergrantid_link);
				stockout_order.setStatus(0);
				
				List<Stockout_order_d> stockout_order_d_list = stockout_order.getStockout_order_d();
				for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
					stockout_order_d.setOrgrootid_link(orgrootid_link);
					stockout_order_d.setTimecreate(current_time);
					stockout_order_d.setUsercreateid_link(user.getId());
					stockout_order_d.setLasttimeupdate(current_time);
					stockout_order_d.setLastuserupdateid_link(user.getId());
					
					List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_d.getStockout_order_pkl();
					for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
						stockout_order_pkl.setTimecreate(current_time);
						stockout_order_pkl.setUsercreateid_link(user.getId());
						stockout_order_pkl.setLasttimeupdate(current_time);
						stockout_order_pkl.setLastuserupdateid_link(user.getId());
					}
				}

				stockout_order_Service.save(stockout_order);
					
				Stocking_UniqueCode unique = stockingService.getby_type(3);
				Integer max = unique.getStocking_max();
				unique.setStocking_max(max+1);
				stockingService.save(unique);
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<create_order_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/save_YeuCauXuat", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<create_order_response> save_YeuCauXuat(HttpServletRequest request,
			@RequestBody create_stockout_order_request entity) {
		create_order_response response = new create_order_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
//			Long orgrootid_link = user.getRootorgid_link();
			Date current_time = new Date();
			
			List<Stockout_order> Stockout_order_list = entity.data;
			for(Stockout_order stockout_order : Stockout_order_list) {
				stockout_order.setLasttimeupdate(current_time);
				stockout_order.setLastuserupdateid_link(user.getId());
				List<Stockout_order_d> stockout_order_d_list = stockout_order.getStockout_order_d();
				for(Stockout_order_d stockout_order_d : stockout_order_d_list) {
					if(stockout_order_d.getId() == null) {
						stockout_order_d.setTimecreate(current_time);
						stockout_order_d.setUsercreateid_link(user.getId());
					}
					stockout_order_d.setLasttimeupdate(current_time);
					stockout_order_d.setLastuserupdateid_link(user.getId());
					
					List<Stockout_order_pkl> stockout_order_pkl_list = stockout_order_d.getStockout_order_pkl();
					for(Stockout_order_pkl stockout_order_pkl : stockout_order_pkl_list) {
						if(stockout_order.getId() != null) {
							// check neu pkl da co trong db thi khong tao moi
							String epc = stockout_order_pkl.getEpc();
							List<Stockout_order_pkl> pkl_list = stockout_pkl_Service.getByEpc_stockout_order(stockout_order.getId(), epc);
							if(pkl_list.size() > 0) {
								stockout_order_pkl = pkl_list.get(0);
							}
						}
						if(stockout_order_pkl.getId() == null) {
							stockout_order_pkl.setTimecreate(current_time);
							stockout_order_pkl.setUsercreateid_link(user.getId());
						}
						stockout_order_pkl.setLasttimeupdate(current_time);
						stockout_order_pkl.setLastuserupdateid_link(user.getId());
					}
				}
				
				stockout_order_Service.save(stockout_order);
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<create_order_response>(response, HttpStatus.OK);
	}
	
	//Tao nhieu lenh xuat kho thanh pham 1 luc cho 1 hoac nhieu PO duoc chon
	@RequestMapping(value = "/create_from_po",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> Create_FromPO(@RequestBody StockoutP_Create_FromPO_Request entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Long> ls_po = new ArrayList<Long>();
			if (null != entity.list_po && entity.list_po.length() > 0) {
				String[] s_poid = entity.list_po.split(";");
				for (String sID : s_poid) {
					Long lID = Long.valueOf(sID);
					System.out.println(lID);
					ls_po.add(lID);
				}
			}
			
			if(ls_po.size()>0) {
				for (Long po_id: ls_po) {
					//Lay thong tin PO de tao Stockout Req tuong ung
					//Luu y: Khi tao yeu cau xuat kho, khong can quan tam den so ton kho tai thoi diem tao
					PContract_PO thePO = pcontract_po_Service.findOne(po_id);
				
					if (null!=thePO) {
						//Tim phan xuong chinh de tao Phieu xuat cho Vendor/Buyer tu phan xuong do theo PO chi tiet
						Long org_incharge = thePO.getOrg_inchargeid_link();
						
						if (null!= org_incharge) {
						
							//Tim danh sach cac porder_grant co chua po_id trong porder_grant_sku (co porder_grant nghia la da map)
							List<POrderGrant> lsPOrder_Grant = porder_grantService.getbypcontract_po(po_id);
							
							//Lap Yeu cau xuat kho cho cac phan xuong da map PO
							//Neu 1 phan xuong co >1 Porder_grant cho cung 1 PO --> Don vao 1 lenh xuat kho
							List<Stockout_order> lsStockout_orders = new ArrayList<Stockout_order>();
							for (POrderGrant thePorder_Grant: lsPOrder_Grant) {
								boolean isNew = true;
								Stockout_order stockout_order = new Stockout_order();
								
								for (Stockout_order theStockout_order: lsStockout_orders) {
									if (theStockout_order.getOrgid_from_link().equals(thePorder_Grant.getXuongSX_ID())){
										isNew = false;
										stockout_order = theStockout_order;
									}
								}
								
								//Neu la px chinh --> Tao phieu xuat toi Vendor tu PO chi tiet
								if (thePorder_Grant.getXuongSX_ID().equals(org_incharge)) {
									if (isNew) {
										String stockout_order_code = commonService.getStockout_order_code();
										
										stockout_order.setOrderdate(new Date());
										stockout_order.setOrdercode(stockout_order_code);
										
										//Noi xuat la px chinh - Noi nhan la Buyer 
										stockout_order.setOrgid_from_link(thePorder_Grant.getXuongSX_ID());
										stockout_order.setOrgid_to_link(thePorder_Grant.getOrgbuyerid_link());
										
										stockout_order.setP_skuid_link(thePO.getProductid_link());
										stockout_order.setStockoutdate(thePO.getShipdate());
										
										stockout_order.setOrgrootid_link(user.getRootorgid_link());
										stockout_order.setTimecreate(new Date());
										stockout_order.setUsercreateid_link(user.getId());
										stockout_order.setStockouttypeid_link(StockoutTypes.STOCKOUT_TYPE_TP_PO);
										
										stockout_order.setPcontractid_link(thePO.getPcontractid_link());
										stockout_order.setPcontract_poid_link(po_id);
										stockout_order.setStatus(0);
										
										//Danh sach hang la danh sach chi tiet mau co cua PO
										//Lenh xuat tu px chinh cho Buyer chi co 1 lenh duy nhat
//										long productid_link = thePO.getProductid_link();
//	
//										List<PContractProductSKU> lsPO_SKUs = pskuservice.getbypo_and_product(po_id, productid_link);
										for (PContractProductSKU p_sku : thePO.getPcontract_po_sku()) {
											Stockout_order_d stockout_order_d = new Stockout_order_d();
											
											stockout_order_d.setOrgrootid_link(user.getRootorgid_link());
											stockout_order_d.setUsercreateid_link(user.getId());
											stockout_order_d.setTimecreate(new Date());
											
											stockout_order_d.setP_skuid_link(p_sku.getSkuid_link());
											stockout_order_d.setTotalpackage(null == p_sku.getPquantity_porder()?0:p_sku.getPquantity_porder());
											
											stockout_order.getStockout_order_d().add(stockout_order_d);
										}
										
										
										lsStockout_orders.add(stockout_order);
										
										//Neu thanh cong Cap nhat bang unique code
										Stocking_UniqueCode unique = stockingService.getby_type(3);
										Integer max = unique.getStocking_max();
										unique.setStocking_max(max+1);
										stockingService.save(unique);
									}
									
								} else {
								//Neu khong la px chinh --> Tao phieu dieu chuyen ve px chinh theo danh sach trong grant_sku
									if (isNew) {
										String stockout_order_code = commonService.getStockout_order_code();
										
										stockout_order.setOrderdate(new Date());
										stockout_order.setOrdercode(stockout_order_code);
										
										//Noi xuat la px san xuat - Noi nhan la px chinh
										stockout_order.setOrgid_from_link(thePorder_Grant.getXuongSX_ID());
										stockout_order.setOrgid_to_link(org_incharge);
										
										stockout_order.setP_skuid_link(thePO.getProductid_link());
										//Ngay chuyen ve px chinh phai truoc ngay xuat cho khach 3 ngay
										stockout_order.setStockoutdate(DateUtils.addDays(thePO.getShipdate(), -3));
										
										stockout_order.setOrgrootid_link(user.getRootorgid_link());
										stockout_order.setTimecreate(new Date());
										stockout_order.setUsercreateid_link(user.getId());
										stockout_order.setStockouttypeid_link(StockoutTypes.STOCKOUT_TYPE_TP_MOVE);
										
										stockout_order.setPcontractid_link(thePO.getPcontractid_link());
										stockout_order.setPcontract_poid_link(po_id);
										stockout_order.setStatus(0);
									}
									//Chi dua vao danh sach Lenh xuat cac SKU cua po_id
									for (POrderGrant_SKU theSKU: thePorder_Grant.getPorder_grant_sku()) {
										if (theSKU.getPcontract_poid_link().equals(po_id)) {
											//Neu SKU da ton tai --> Cong them vao
											//Neu SKU chua co --> Them moi
											Stockout_order_d theStockout_order_d = stockout_order.getStockout_order_d().stream()
													.filter(prod -> prod.getP_skuid_link().equals(theSKU.getSkuid_link()))
													.findAny().orElse(null);
											if (null != theStockout_order_d) {
												theStockout_order_d.setTotalpackage(theStockout_order_d.getTotalpackage() + theSKU.getGrantamount());
											} else {
												Stockout_order_d stockout_order_d = new Stockout_order_d();
												
												stockout_order_d.setOrgrootid_link(user.getRootorgid_link());
												stockout_order_d.setUsercreateid_link(user.getId());
												stockout_order_d.setTimecreate(new Date());
												
												stockout_order_d.setP_skuid_link(theSKU.getSkuid_link());
												stockout_order_d.setTotalpackage(null == theSKU.getGrantamount()?0:theSKU.getGrantamount());
												
												stockout_order.getStockout_order_d().add(stockout_order_d);
											}
										}
									}
								}
								
								if (isNew) lsStockout_orders.add(stockout_order);
								
								//Neu thanh cong Cap nhat bang unique code
								Stocking_UniqueCode unique = stockingService.getby_type(3);
								Integer max = unique.getStocking_max();
								unique.setStocking_max(max+1);
								stockingService.save(unique);
							}
							//Update DB
							for (Stockout_order theStockout_order: lsStockout_orders) {
								stockout_order_Service.save(theStockout_order);
							}
							
							//Cap nhat lai trang thai PO ve da lap lenh xuat kho
							thePO.setStatus(POStatus.PO_STATUS_STOCKOUT);
							pcontract_po_Service.save(thePO);
						} else {
							//PO chua duoc thiet lap px chinh mac dinh
							response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
							response.setMessage("Chưa thiết lập phân xưởng chính cho PO. Báo lại cho quản lý đơn hàng để điều chỉnh");
							return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);		
						}
					}
				}
				
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);			
			} 
			else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);			
			}
			
		}catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	//Hung Dai Bang
	//Xem truoc danh sach cac phan xuong sx cho PO va SL tong sx cua tung px
	@RequestMapping(value = "/preview_bypo",method = RequestMethod.POST)
	public ResponseEntity<?> Preview_ByPO(@RequestBody StockoutP_Preview_ByPO_Request entity, HttpServletRequest request ) {
		getby_porder_response response = new getby_porder_response();
		try {
//			GpayUser user = (GpayUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long po_id = entity.po_id;
			//Lay thong tin PO de tao Stockout Req tuong ung
			//Luu y: Khi tao yeu cau xuat kho, khong can quan tam den so ton kho tai thoi diem tao
			PContract_PO thePO = pcontract_po_Service.findOne(po_id);
		
			if (null!=thePO) {
				//Tim phan xuong chinh de tao Phieu xuat cho Vendor/Buyer tu phan xuong do theo PO chi tiet
				Long org_incharge = thePO.getOrg_inchargeid_link();
				
				//Tim danh sach cac porder_grant co chua po_id trong porder_grant_sku (co porder_grant nghia la da map)
				List<POrderGrant> lsPOrder_Grant = porder_grantService.getbypcontract_po(po_id);
				
				//Lap Yeu cau xuat kho cho cac phan xuong da map PO
				//Neu 1 phan xuong co >1 Porder_grant cho cung 1 PO --> Don vao 1 lenh xuat kho
				List<Stockout_order> lsStockout_orders = new ArrayList<Stockout_order>();
				
				for (POrderGrant thePorder_Grant: lsPOrder_Grant) {
//					System.out.println(thePorder_Grant.getId());
					boolean isNew = true;
					Stockout_order stockout_order = new Stockout_order();
					
					for (Stockout_order theStockout_order: lsStockout_orders) {
						if (theStockout_order.getOrgid_from_link().equals(thePorder_Grant.getXuongSX_ID())){
							isNew = false;
							stockout_order = theStockout_order;
						}
					}
					
					if (isNew) {
						if (thePorder_Grant.getXuongSX_ID().equals(org_incharge))
							stockout_order.setExtrainfo("Phân xưởng chính");
						else
							stockout_order.setExtrainfo("Phân xưởng phụ");
						
						stockout_order.setOrgid_from_link(thePorder_Grant.getXuongSX_ID());
						stockout_order.setStockout_order_code(thePorder_Grant.getXuongSX());
						lsStockout_orders.add(stockout_order);
					}
					//Chi dua vao danh sach Lenh xuat cac SKU cua po_id
					for (POrderGrant_SKU theSKU: thePorder_Grant.getPorder_grant_sku()) {
						if (theSKU.getPcontract_poid_link().equals(po_id)) {
							stockout_order.setPo_quantity_sp(stockout_order.getPo_quantity_sp() + (null == theSKU.getGrantamount()?0:theSKU.getGrantamount()));
						}
					}					
				}
				response.data = lsStockout_orders;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<getby_porder_response>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Đơn hàng (PO) không tồn tại");
				return new ResponseEntity<getby_porder_response>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(null==e.getMessage()?"Lỗi hệ thống! Liên hệ IT để được hỗ trợ":e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
}
