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

import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_d_service;
import vn.gpay.gsmart.core.stockout_order.IStockout_order_service;
import vn.gpay.gsmart.core.stockout_order.Stockout_order;
import vn.gpay.gsmart.core.stockout_order.Stockout_order_d;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/stockoutorder")
public class Stockout_orderAPI {
	@Autowired IStockout_order_service stockout_order_Service;
	@Autowired IStockout_order_d_service stockout_order_d_Service;
	
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
				detail.setOrgrootid_link(orgrootid_link);
				if(detail.getId() == null) {
					detail.setTimecreate(current_time);
					detail.setUsercreateid_link(user.getId());
				}
				else {
					detail.setLasttimeupdate(current_time);
					detail.setLastuserupdateid_link(user.getId());
				}
				
				stockout_order_d_Service.save(detail);
			}
			
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
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getby_id_response>(response, HttpStatus.OK);
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
