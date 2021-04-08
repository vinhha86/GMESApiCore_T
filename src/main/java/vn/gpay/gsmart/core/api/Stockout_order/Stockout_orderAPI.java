package vn.gpay.gsmart.core.api.Stockout_order;

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

import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_bom_color.IPOrderBomColor_Service;
import vn.gpay.gsmart.core.porder_bom_color.PorderBomColor;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_coloramount_Service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_d_service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_pkl_Service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_service;
import vn.gpay.gsmart.core.stockout_order.Stockout_order;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_coloramount;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_d;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_pkl;
import vn.gpay.gsmart.core.utils.ResponseMessage;

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
			if(order.getId() == null) {
				order.setOrgrootid_link(orgrootid_link);
				order.setTimecreate(current_time);
				order.setUsercreateid_link(user.getId());
				order.setStatus(0);
			}
			
			order = stockout_order_Service.save(order);
			
			Long stockout_orderid_link = order.getId();
			
			for(Stockout_order_d detail : entity.detail) {
				detail.setStockoutorderid_link(stockout_orderid_link);
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
			List<Stockout_order_coloramount> list_amout_color = amount_color_Service.getby_stockout_Order(stockout_orderid_link);
			if(list_amout_color.size() == 0) {
				Long porderid_link = order.getPorderid_link();
				List<Long> list_colorid = porderskuService.getlist_colorid_byporder(porderid_link);
				for(Long colorid_link : list_colorid) {
					Stockout_order_coloramount color_amout = new Stockout_order_coloramount();
					color_amout.setAmount(0);
					color_amout.setColorid_link(colorid_link);
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
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getby_id_response>(response, HttpStatus.OK);
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
	
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public ResponseEntity<calculate_response> Calculate(HttpServletRequest request,
			@RequestBody calculate_request entity) {
		calculate_response response = new calculate_response();
		try {
			Stockout_order order = stockout_order_Service.findOne(entity.id);
			//lay nhung npl cua detail
			List<Stockout_order_d> list_detail = stockout_order_d_Service.getby_Stockout_order(entity.id);
			
			for(Stockout_order_d detail : list_detail) {
				
			}
			
			List<Stockout_order_coloramount> list_color_amount = amount_color_Service.getby_stockout_Order(entity.id);
			for(Stockout_order_coloramount color : list_color_amount) {
				List<PorderBomColor> list_bom_color = bomcolorService.getby_porder_and_color(order.getPorderid_link(), color.getColorid_link());
				for(PorderBomColor bom_color: list_bom_color) {
					
				}
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
			response.data = stockout_order_d_Service.getby_Stockout_order(entity.id);
			
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
	
	@RequestMapping(value = "/getStockoutorder", method = RequestMethod.POST)
	public ResponseEntity<Stockout_order_response> getStockoutorder(HttpServletRequest request,
			@RequestBody Stockout_order_getBySearch_request entity) {
		Stockout_order_response response = new Stockout_order_response();
		try {
			Date stockoutorderdate_from = entity.stockoutorderdate_from;
			Date stockoutorderdate_to = entity.stockoutorderdate_to;
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			List<Stockout_order> result = stockout_order_Service.findBySearch(stockoutorderdate_from, stockoutorderdate_to);
			
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
}
